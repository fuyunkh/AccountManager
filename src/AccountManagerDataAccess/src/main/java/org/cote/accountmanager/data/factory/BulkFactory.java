/*******************************************************************************
 * Copyright (C) 2002, 2015 Stephen Cote Enterprises, LLC. All rights reserved.
 * Redistribution without modification is permitted provided the following conditions are met:
 *
 *    1. Redistribution may not deviate from the original distribution,
 *        and must reproduce the above copyright notice, this list of conditions
 *        and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *    2. Products may be derived from this software.
 *    3. Redistributions of any form whatsoever must retain the following acknowledgment:
 *        "This product includes software developed by Stephen Cote Enterprises, LLC"
 *
 * THIS SOFTWARE IS PROVIDED BY STEPHEN COTE ENTERPRISES, LLC ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THIS PROJECT OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, 
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY 
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.cote.accountmanager.data.factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.DataAccessException;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.objects.BaseParticipantType;
import org.cote.accountmanager.objects.BulkEntryType;
import org.cote.accountmanager.objects.BulkSessionType;
import org.cote.accountmanager.objects.ContactInformationType;
import org.cote.accountmanager.objects.NameIdType;
import org.cote.accountmanager.objects.UserType;
import org.cote.accountmanager.objects.types.FactoryEnumType;
import org.cote.accountmanager.objects.types.NameEnumType;
import org.cote.accountmanager.util.CalendarUtil;

public class BulkFactory {
	public static final Logger logger = LogManager.getLogger(BulkFactory.class);
	//private Map<String,Map<NameEnumType,Integer>> idScanMap = null;
	protected static Map<String,BulkSessionType> sessions = null;
	protected static Map<Long,String> sessionIdMap = null;
	protected static Map<Long,Long> idMap = null;
	/// Dirty write should be moved to the session object
	///
	protected static Set<FactoryEnumType> dirtyWrite = null;
	protected static int maximumWritePasses = 3;
	
	/// globalSessionId is used to track ad-hoc writes that, for whatever reason, could not trace back to a particular session
	/// EG: Bulk adds routed through update requests where no bulk id is available to retrieve the sessionId
	///
	protected static String globalSessionId = null;
	protected static Object globalLock = null;
	/// Note - update cache being stored against the session opposed to the data level as an initial proof
	/// eventually ,this should be migrated to the data level
	///
	protected static Map<String,Map<FactoryEnumType,List<NameIdType>>> updateCache = null;
	protected static Map<String,Map<FactoryEnumType,List<NameIdType>>> deleteCache = null;
	protected static Map<String,Map<FactoryEnumType,Set<String>>> updateSet = null;
	protected static Map<String,Map<FactoryEnumType,Set<String>>> deleteSet = null;

	private Random rand = null;
	public BulkFactory(){
		rand = new Random();
		if(dirtyWrite == null) dirtyWrite = new HashSet<>();
		if(sessions == null) sessions = new HashMap<>();
		if(sessionIdMap == null) sessionIdMap = new HashMap<>();
		if(idMap == null) idMap = new HashMap<>();
		if(updateCache == null) updateCache = new HashMap<>();
		if(updateSet == null) updateSet = new HashMap<>();
		if(deleteSet == null) deleteSet = new HashMap<>();
		if(deleteCache == null) deleteCache = new HashMap<>();
		if(globalLock == null) globalLock = new Object();
	}
	/*
	public String getSessionForPersistentId(FactoryEnumType factoryType, long id){
		String out_sess = null;
		String key = factoryType.toString() + "-" + id;
		if(persistentIdSessionMap.containsKey(key)) out_sess = persistentIdSessionMap.get(key);
		return out_sess;
	}
	*/
	public String getGlobalSessionId(){
		return globalSessionId;
	}
	public void setDirty(FactoryEnumType factoryType){
		//logger.debug("Marking " + factoryType + " for a dirty write");
		dirtyWrite.add(factoryType);
	}
	public String getSessionForBulkId(long id){
		String out_sess = null;
		if(sessionIdMap.containsKey(id)) out_sess = sessionIdMap.get(id);
		return out_sess;
	}
	public void close(String sessionId) throws ArgumentException{
		logger.debug("Closing bulk session " + sessionId);
		if(sessionId == null || sessionId.length() == 0){
			logger.error("Session id is null");
			throw new ArgumentException("Session id is null");
		}
		BulkSessionType session = sessions.get(sessionId);
		if(session == null){
			logger.error("Invalid session id '" + sessionId + "'");
			throw new ArgumentException("Invalid session id '" + sessionId + "'");
		}
		sessions.remove(sessionId);
		updateCache.remove(sessionId);
		updateSet.remove(sessionId);
		deleteSet.remove(sessionId);
		deleteCache.remove(sessionId);
		//synchronized(sessionIdMap){
			//while (persistentIdSessionMap.values().remove(sessionId));
			Iterator<Long> keys = sessionIdMap.keySet().iterator();
			while(keys.hasNext()){
				long val = keys.next();
				if(sessionIdMap.get(val).equals(sessionId)){
					//sessionIdMap.remove(val);
					keys.remove();
				}
				if(idMap.containsKey(val)) idMap.remove(val);
			}
		//}
	}
	public void write(String sessionId) throws ArgumentException, FactoryException, DataAccessException{
		write(sessionId, 1, 0);
	}
	protected void write(String sessionId, int pass, int offset) throws ArgumentException, FactoryException, DataAccessException{
		long start = System.currentTimeMillis();
		if(sessionId == null || sessionId.length() == 0){
			logger.error("Session id is null");
			throw new ArgumentException("Session id is null");
		}
		BulkSessionType session = sessions.get(sessionId);
		if(session == null){
			logger.error("Invalid session id '" + sessionId + "'");
			throw new ArgumentException("Invalid session id '" + sessionId + "'");
		}
		if(session.getPersisted() == true){
			logger.error("Session id '" + sessionId + "' is already persisted");
			throw new ArgumentException("Session id '" + sessionId + "' is already persisted");			
		}
		
		Map<FactoryEnumType,List<Long>> factoryIds = new HashMap<FactoryEnumType,List<Long>>();
		int eLen = 0;

		synchronized(session){
			eLen = session.getBulkEntries().size();
			logger.info("Writing Bulk Session " + sessionId + " with " + (eLen - offset) + " objects");
			long startPass = System.currentTimeMillis();
			for(int i = offset; i < eLen; i++){
				BulkEntryType entry = session.getBulkEntries().get(i);
				if(entry.getPersisted()){
					logger.warn("Skipping persisted entry " + entry.getObject().getName());
					continue;
				}
				if(factoryIds.containsKey(entry.getFactoryType())==false){
					long startFact = System.currentTimeMillis();
					factoryIds.put(entry.getFactoryType(), getFactoryIds(sessionId,entry.getFactoryType()));
					logger.debug("Retrieved factory ids for " + entry.getFactoryType().toString() + " in " + (System.currentTimeMillis() - startFact) + "ms");
				}
				List<Long> ids = factoryIds.get(entry.getFactoryType());
				long id = ids.remove(ids.size()-1);

				entry.setPersistentId(id);
				entry.setPersisted(true);
				/// don't set id until the cache is cleared for this object
				/// entry.getObject().setId(id);

				/// 2013/06/26 - moved up from writeObject
				/// Set the ids before invoking writeObject
				/// While this will cause a double-pass, it also
				/// allows for object with participant dependencies
				/// to be written out of any particular order
				///
				NameIdFactory factory = getFactory(entry.getFactoryType());
				factory.removeFromCache(entry.getObject(),factory.getCacheKeyName(entry.getObject()));
				entry.getObject().setId(entry.getPersistentId());
				idMap.put(entry.getTemporaryId(), entry.getPersistentId());
				//persistentIdSessionMap.put(entry.getFactoryType().toString() + "-" + entry.getPersistentId(), sessionId);
				
				//writeObject(session, entry);
				//eLen = session.getBulkEntries().size();
			}
			logger.debug("Pass #1 in " + (System.currentTimeMillis() - startPass) + "ms");
			startPass = System.currentTimeMillis();
			/// 2013/06/26 - Second pass, map ids
			///
			for(int i = offset; i < eLen;i++){
				BulkEntryType entry = session.getBulkEntries().get(i);
				/// Only map ids if the 'persisted' bit is set to true (see previous iteration)
				/// The bit indicates that the final DB id has been assigned
				/// 
				if(entry.getPersisted() == false){
					logger.warn("Skipping unpersisted entry " + entry.getObject().getName());
					continue;
				}
				mapObjectIds(entry);
			}
			//logger.info("Pass #2 in " + (System.currentTimeMillis() - startPass) + "ms");
			//startPass = System.currentTimeMillis();

			/// 2013/06/26 - Third pass, write objects into the bulk table queues
			/// 2014/08/15 - Add attribute dump
			List<NameIdType> attrDump = new ArrayList<NameIdType>();
			long totalAttrs = 0;
			for(int i = offset; i < eLen; i++){
				BulkEntryType entry = session.getBulkEntries().get(i);
				if(entry.getObject().getAttributes().size() > 0){
					totalAttrs += entry.getObject().getAttributes().size();
					attrDump.add(entry.getObject());
				}
				/// 2013/06/26 - Second pass, map ids
				///
				if(entry.getPersisted() == false){
					logger.warn("Skipping unpersisted entry " + entry.getObject().getName());
					continue;
				}
				writeObject(session, entry);
				/// 2014/01/11  - need to update attributes, but in one bulk pass
			}
			//logger.info("Pass #3 in " + (System.currentTimeMillis() - startPass) + "ms");
			//startPass = System.currentTimeMillis();

			logger.debug("Writing " + totalAttrs + " attributes for " + attrDump.size() + " objects");
			Factories.getAttributeFactory().addAttributes(attrDump.toArray(new NameIdType[0]));
			
			//logger.info("Pass #4 in " + (System.currentTimeMillis() - startPass) + "ms");
			//startPass = System.currentTimeMillis();

			synchronized(dirtyWrite){
				Iterator<FactoryEnumType> keys = factoryIds.keySet().iterator();
				while(keys.hasNext()){
					FactoryEnumType factoryType = keys.next();
					dirtyWrite.remove(factoryType);
					writeSpool(factoryType);
				}
				
				/// A dirty write is when a factory adds an object to be bulk written
				/// But that object was not created as a bulk entry.  Participations are examples of dirty writes.
				///
				if(dirtyWrite.size() > 0){
					FactoryEnumType[] fTypes = dirtyWrite.toArray(new FactoryEnumType[0]);
					for(int i = 0; i < fTypes.length;i++){
						logger.debug("Writing dirty bulk spool for " + fTypes[i]);
						dirtyWrite.remove(fTypes[i]);
						writeSpool(fTypes[i]);
					}
				}
			}
			//logger.info("Pass #5 in " + (System.currentTimeMillis() - startPass) + "ms");
			//startPass = System.currentTimeMillis();

			/// 2013/09/14 - Don't clear the dirtyWrite queue - it should be cleaned up in the previous pass
			///dirtyWrite.clear();

			/// 2016/07/22 - Add bulk delete
			/// 
			if(deleteCache.containsKey(sessionId)){
				//Iterator<FactoryEnumType> keys = deleteCache.get(sessionId).keySet().iterator();
				int count = 0;
				//while(keys.hasNext()){
				for (Entry<FactoryEnumType,List<NameIdType>> entry : deleteCache.get(sessionId).entrySet()) {
					FactoryEnumType factoryType = entry.getKey();
					List<NameIdType> objs = entry.getValue();
					logger.debug("Processing delete cache for " + factoryType.toString() + " having " + objs.size() + " objects");
					deleteSpool(factoryType,objs);
					NameIdFactory factory = getBulkFactory(factoryType);
					logger.debug("Processing bulk modifications for " + factoryType.toString());
					factory.deleteBulk(objs,null);
					logger.debug("Processing bulk attribute modifications cache for " + factoryType.toString());
					Factories.getAttributeFactory().deleteAttributesForObjects(objs.toArray(new NameIdType[0]));
					count += objs.size();
					
				}
				logger.debug("Deleted " + count + " objects");
				deleteCache.get(sessionId).clear();
				deleteSet.get(sessionId).clear();
			}
			else{
				logger.debug("Delete cache is empty");
			}
			
			/// 2014/12/23 - Add bulk update hook
			if(updateCache.containsKey(sessionId)){
				//Iterator<FactoryEnumType> keys = updateCache.get(sessionId).keySet().iterator();
				int count = 0;
				//while(keys.hasNext()){
				/// 2016/07/27 - bug with the modify method where the updateCache gets multiple entries, even though the check catches it
				///

				for (Entry<FactoryEnumType,List<NameIdType>> entry : updateCache.get(sessionId).entrySet()) {
					FactoryEnumType factoryType = entry.getKey();
					List<NameIdType> objs = entry.getValue();
					logger.debug("Processing modification cache for " + factoryType.toString() + " having " + objs.size() + " objects");
					//if(factoryType == FactoryEnumType.LOCATION) for(NameIdType obj : objs) logger.info(obj.getId() + " " + obj.getName());
					updateSpool(factoryType,objs);
					NameIdFactory factory = getBulkFactory(factoryType);
					logger.debug("Processing bulk modifications for " + factoryType.toString());
					factory.updateBulk(objs,null);
					logger.debug("Processing bulk attribute modifications cache for " + factoryType.toString());
					Factories.getAttributeFactory().updateAttributes(objs.toArray(new NameIdType[0]));
					count += objs.size();
					
				}
				logger.debug("Modified " + count + " objects");
				updateCache.get(sessionId).clear();
				updateSet.get(sessionId).clear();
			}
			else{
				logger.debug("Modification cache is empty");
			}
			//logger.info("Pass #6 in " + (System.currentTimeMillis() - startPass) + "ms");
			//startPass = System.currentTimeMillis();

		}
		synchronized(globalLock){
			if(globalSessionId != null && globalSessionId.equals(sessionId) == false){
				logger.info("Writing global session");
				write(globalSessionId);
				globalSessionId = null;
			}
		}
		
		/// 2013/09/13 - A session obtains dirty entries when the write operation results in additional bulk entries being created (as opposed to a bulk object being written directly to the table queue)
		/// Those objects cannot just be written because they need to obtain database id, and then have that id cross mapped (if needed) for any foreign keys
		///
		if(session.getBulkEntries().size() != eLen){
			if(pass < maximumWritePasses){
				logger.debug("Bulk Session is dirty with " + (session.getBulkEntries().size() - eLen) + " entries.  Attempting pass #" + (pass + 1));
				write(sessionId, pass+1,eLen);
			}
			else{
				logger.error("Bulk Session is still dirty with " + (session.getBulkEntries().size() - eLen) + " entries.  Halting attempts after " + pass + " passes");
			}
		}
		long stop = System.currentTimeMillis();
		if(pass == 1){
			session.setPersisted(true);
			logger.info("Wrote Bulk Session " + sessionId + " in " + (stop - start) + "ms");
		}
		
		
	}
	
	
	
	protected void deleteSpool(FactoryEnumType factoryType,List<NameIdType> objects) throws FactoryException, DataAccessException, ArgumentException{
		/// The initial bulk update logic, until this is refactored into a pure data update (which is more work than doing it this initial way) is:
		/// 1) Invoke the updateType for each object in the list
		///		This will address any factory specific tweaks that are needed, and queue up dependencies
		/// 2) Pass the entire list to the factory's updateBulk.

		INameIdFactory iFact = (INameIdFactory)Factories.getBulkFactory(factoryType);
		for(int i = 0; i < objects.size();i++){
			
			NameIdType object = objects.get(i);
			iFact.delete(object);

		}
	}
	
	
	protected void updateSpool(FactoryEnumType factoryType,List<NameIdType> objects) throws FactoryException, DataAccessException, ArgumentException{
		/// The initial bulk update logic, until this is refactored into a pure data update (which is more work than doing it this initial way) is:
		/// 1) Invoke the updateType for each object in the list
		///		This will address any factory specific tweaks that are needed, and queue up dependencies
		/// 2) Pass the entire list to the factory's updateBulk.
		
		INameIdFactory iFact = (INameIdFactory)Factories.getBulkFactory(factoryType);

		for(int i = 0; i < objects.size();i++){
			NameIdType object = objects.get(i);
			iFact.update(object);
			
		} // end for
		
	}
	
	protected void writeSpool(FactoryEnumType factoryType) throws FactoryException{
		INameIdFactory iFact = (INameIdFactory)Factories.getBulkFactory(factoryType);
		iFact.writeSpool();
		
	}
	protected void writeObject(BulkSessionType session, BulkEntryType entry) throws FactoryException, DataAccessException, ArgumentException{
		writePreparedObject(session,entry);
	}
	protected void mapObjectIds(BulkEntryType entry){
		/// TODO - Why is this not just looking up the factory type and invoking the method instead of the big switch here?
		/// Note: Not all types are supported, and operations should gracefully fall through 
		///
		INameIdFactory iFact = (INameIdFactory)Factories.getBulkFactory(entry.getFactoryType());
		iFact.mapBulkIds(entry.getObject());
		
	}
	protected void writePreparedObject(BulkSessionType session,BulkEntryType entry) throws FactoryException, ArgumentException, DataAccessException{
		BaseParticipantType part = null;
		INameIdFactory iFact = (INameIdFactory)Factories.getBulkFactory(entry.getFactoryType());
		if(iFact.isParticipation()){
			updateParticipantIds((BaseParticipantType)entry.getObject());
		}
		iFact.add(entry.getObject());
		if(entry.getFactoryType().equals(FactoryEnumType.USER)){
			UserType user = (UserType)entry.getObject();
			if(user.getContactInformation() == null){
				ContactInformationType cit = ((ContactInformationFactory)Factories.getFactory(FactoryEnumType.CONTACTINFORMATION)).newContactInformation((UserType)entry.getObject());
				createBulkEntry(session.getSessionId(), FactoryEnumType.CONTACTINFORMATION, cit);
			}
		}
		
	}
	public long getMappedId(long temporaryId){
		long out_id = 0;
		if(idMap.containsKey(temporaryId)) out_id = idMap.get(temporaryId);
		return out_id;
	}
	protected void updateParticipantIds(BaseParticipantType part) throws ArgumentException{
		if(part.getParticipantId() < 0L){
			if(idMap.containsKey(part.getParticipantId())){
				//logger.debug("Remapping Participant Id " + part.getParticipantId() + " to " + idMap.get(part.getParticipantId()));
				part.setParticipantId(idMap.get(part.getParticipantId()));
			}
			else{
				throw new ArgumentException("Unable to correct participant id");
			}
		}
		if(part.getParticipationId() < 0L){
			if(idMap.containsKey(part.getParticipationId())){
				//logger.debug("Remapping Participation Id " + part.getParticipationId() + " to " + idMap.get(part.getParticipationId()));
				part.setParticipationId(idMap.get(part.getParticipationId()));
			}
			else{
				throw new ArgumentException("Unable to correct participation id");
			}
		}
		if(part.getAffectId() < 0L){
			if(idMap.containsKey(part.getAffectId())){
				part.setAffectId(idMap.get(part.getAffectId()));
			}
			else{
				throw new ArgumentException("Unable to correct affect id");
			}
		}
	}
	protected List<Long> getFactoryIds(String sessionId, FactoryEnumType factoryType) throws ArgumentException, FactoryException{
		List<Long> ids = new ArrayList<Long>();
		NameIdFactory factory = getBulkFactory(factoryType);
		if(factory == null){
			logger.error("Invalid factory type " + factoryType);
			throw new ArgumentException("Invalid factory type " + factoryType);
		}
		if(factory.getBulkMap().containsKey(sessionId) == false){
			logger.error("Bulk ID Map is out of sync for type " + factoryType);
			throw new ArgumentException("Bulk ID Map is out of sync for type " + factoryType);
			
		}
		List<Long> tmpIds = factory.getBulkMap().get(sessionId);
		factory.getBulkMap().remove(sessionId);
		ids = factory.getNextIds(tmpIds.size());
		return ids;
	}
	protected NameIdFactory getBulkFactory(FactoryEnumType factoryType){
		return Factories.getBulkFactory(factoryType);
	}
	protected NameIdFactory getFactory(FactoryEnumType factoryType){
		return Factories.getFactory(factoryType);
	}
	public void deleteBulkEntry(String sessionId, FactoryEnumType factoryType, NameIdType object) throws ArgumentException{
		if(object == null){
			logger.error("Object is null");
			throw new ArgumentException("Object is null");
		}
		if(object.getNameType() == NameEnumType.UNKNOWN){
			logger.error("Object cannot be of an unknown type");
			throw new ArgumentException("Object cannot be of an unknown type");
		}

		if(object.getId() <= 0L) throw new ArgumentException("Object " + factoryType.toString() + " " + object.getName() + " #" + object.getId() + " does not have a valid id for a delete operation");

		if(deleteCache.containsKey(sessionId) == false) deleteCache.put(sessionId, new HashMap<FactoryEnumType,List<NameIdType>>());
		if(deleteCache.get(sessionId).containsKey(factoryType) == false) deleteCache.get(sessionId).put(factoryType, new ArrayList<>());
		if(deleteSet.containsKey(sessionId) == false) deleteSet.put(sessionId, new HashMap<FactoryEnumType,Set<String>>());
		if(deleteSet.get(sessionId).containsKey(factoryType) == false) deleteSet.get(sessionId).put(factoryType, new HashSet<>());
		
		
		NameIdFactory factory = getFactory(factoryType);
		NameIdFactory bulkFactory = getBulkFactory(factoryType);
		
		if(factory == null || bulkFactory == null){
			logger.error("Factory or BulkFactory is null for type " + factoryType);
			throw new ArgumentException("Factory or BulkFactory is null for type " + factoryType);
		}
		
		BulkSessionType session = sessions.get(sessionId);
		if(session == null){
			logger.error("Invalid session id '" + sessionId + "'");
			throw new ArgumentException("Invalid session id '" + sessionId + "'");
		}
		
		/// drop from factory cache
		///
		String key = factory.getCacheKeyName(object);
		factory.removeFromCache(object,key);
		if(deleteSet.get(sessionId).get(factoryType).contains(key)){
			//logger.debug("Object '" + object.getName() + "' is already marked for deletion");
			return;
		}
		deleteCache.get(sessionId).get(factoryType).add(object);
		deleteSet.get(sessionId).get(factoryType).add(key);
	}
	public void modifyBulkEntry(String sessionId, FactoryEnumType factoryType, NameIdType object) throws ArgumentException{
		if(object == null){
			logger.error("Object is null");
			throw new ArgumentException("Object is null");
		}
		if(object.getNameType() == NameEnumType.UNKNOWN){
			logger.error("Object cannot be of an unknown type");
			throw new ArgumentException("Object cannot be of an unknown type");
		}

		if(object.getId() <= 0L) throw new ArgumentException("Object " + factoryType.toString() + " " + object.getName() + " #" + object.getId() + " does not have a valid id for an update operation");

		if(updateCache.containsKey(sessionId) == false) updateCache.put(sessionId, new HashMap<FactoryEnumType,List<NameIdType>>());
		if(updateSet.containsKey(sessionId)==false) updateSet.put(sessionId, new HashMap<FactoryEnumType,Set<String>>());
		if(updateCache.get(sessionId).containsKey(factoryType) == false) updateCache.get(sessionId).put(factoryType, new ArrayList<NameIdType>());
		if(updateSet.get(sessionId).containsKey(factoryType) == false) updateSet.get(sessionId).put(factoryType, new HashSet<String>());
		
		
		NameIdFactory factory = getFactory(factoryType);
		NameIdFactory bulkFactory = getBulkFactory(factoryType);
		
		if(factory == null || bulkFactory == null){
			logger.error("Factory or BulkFactory is null for type " + factoryType);
			throw new ArgumentException("Factory or BulkFactory is null for type " + factoryType);
		}
		
		BulkSessionType session = sessions.get(sessionId);
		if(session == null){
			logger.error("Invalid session id '" + sessionId + "'");
			throw new ArgumentException("Invalid session id '" + sessionId + "'");
		}
		
		/// rewrite factory cache in case the name was changed on the update
		///
		String key = factory.getCacheKeyName(object);
		if(factory.updateToCache(object,key)==false){
			logger.warn("Failed to add object '" + object.getName() + "' to factory cache with key name " + factory.getCacheKeyName(object));
		}
		
		//if(updateCache.get(sessionId).get(factoryType).contains(object)){
		if(updateSet.get(sessionId).get(factoryType).contains(key)){
			
			//updateCache.get(sessionId).get(factoryType).remove(object);
			//logger.debug("Object " + factoryType.toString() + " '" + object.getName() + "' is already marked for modification");
			return;
		}
		//logger.info("Adding " + object.getName() + " as " + factoryType + " to modification cache");
		/// Add the object to the updateCache
		updateCache.get(sessionId).get(factoryType).add(object);
		updateSet.get(sessionId).get(factoryType).add(key);
	}
	public void createBulkEntry(String sessionId, FactoryEnumType factoryType, NameIdType object) throws ArgumentException{
		long bulkId = (long)(rand.nextDouble()*1000000000L) * -1;
				///rand.nextLong() * -1;
		
		if(sessionId == null){
			synchronized(globalLock){
				if(globalSessionId != null){
					//logger.info("Pushing write into global session.");
					sessionId = globalSessionId;
				}
				else{
					logger.info("Creating new global session.");
					globalSessionId = newBulkSession();
					sessionId = globalSessionId;
				}
			}
		}
		
		/// With large datasets, the random quickly starts to repeat.  Very bad Java.  Very bad.
		while(sessionIdMap.containsKey(bulkId) == true){
			bulkId = (long)(rand.nextDouble()*1000000000L) * -1;
		}
		
		if(object == null){
			logger.error("Object is null");
			throw new ArgumentException("Object is null");
		}

		
		if(sessionIdMap.containsKey(bulkId)){
			logger.error("Random id " + bulkId + " assigned to " + object.getName() + " already consumed");
			throw new ArgumentException("Random id " + bulkId + " assigned to " + object.getName() + " already consumed");			
		}
		
		if(object.getId() != 0L){
			logger.error("Object id is already set to " + object.getObjectId());
			throw new ArgumentException("Object id is already set");
		}
		if(object.getNameType() == NameEnumType.UNKNOWN){
			logger.error("Object cannot be of an unknown type");
			throw new ArgumentException("Object cannot be of an unknown type");
		}
		
		NameIdFactory factory = getFactory(factoryType);
		NameIdFactory bulkFactory = getBulkFactory(factoryType);
		
		if(factory == null || bulkFactory == null){
			logger.error("Factory or BulkFactory is null for type " + factoryType);
			throw new ArgumentException("Factory or BulkFactory is null for type " + factoryType);
		}
		
		BulkSessionType session = sessions.get(sessionId);
		if(session == null){
			logger.error("Invalid session id '" + sessionId + "'");
			throw new ArgumentException("Invalid session id '" + sessionId + "'");
		}
		

		sessionIdMap.put(bulkId,sessionId);
		/*
		if(idScanMap.containsKey(sessionId) == false){
			logger.error("Scan map is unavailable");
			throw new ArgumentException("Scan map is unavailable");			
		}
		int currentCount = 0;
		Map<NameEnumType,Integer> scanMap = idScanMap.get(sessionId);
		if(scanMap.containsKey(object.getNameType()) == true){
			currentCount = scanMap.get(object.getNameType());
		}
		scanMap.put(object.getNameType(), currentCount + 1);
		*/
		/// logger.debug("Creating Bulk Entry #" + bulkId + " for " + object.getNameType().toString() + " " + object.getName());
		
		object.setId(bulkId);
		/// 2015/06/25 - Assign object id for factories with the bit set to true
		/// This is otherwise done last minute, but thwarts foreign key references
		/// primarily between credentials, controls, and keys
		///
		if(factory.hasObjectId) object.setObjectId(UUID.randomUUID().toString());
		
		BulkEntryType entry = new BulkEntryType();
		entry.setFactoryType(factoryType);
		entry.setPersistentId(0L);
		entry.setTemporaryId(bulkId);
		entry.setObject(object);
		session.getBulkEntries().add(entry);
		
		bulkFactory.addBulkId(sessionId, bulkId);
		
		/// 2016/07/22 - Some objects do not have a name, such as participations
		///
		if(object.getName() != null && factory.updateToCache(object,factory.getCacheKeyName(object))==false){
			logger.warn("Failed to add object '" + object.getName() + "' to factory cache with key name " + factory.getCacheKeyName(object));
		}
		
		//sessionIdMap.put(bulkId, sessionId);
	}
	public String newBulkSession(){
		String sessionId = UUID.randomUUID().toString();
		BulkSessionType sess = new BulkSessionType();
		sess.setSessionId(sessionId);
		Calendar now = Calendar.getInstance();
		sess.setSessionCreated(CalendarUtil.getXmlGregorianCalendar(now.getTime()));
		now.add(Calendar.MINUTE, 5);
		sess.setSessionExpires(CalendarUtil.getXmlGregorianCalendar(now.getTime()));
		sessions.put(sessionId, sess);
		//idScanMap.put(sessionId, new HashMap<NameEnumType,Integer>());
		logger.debug("Created Bulk Session '" + sessionId + "'.  Expires: " + sess.getSessionExpires().toString());
		return sessionId;
	}
	
}
