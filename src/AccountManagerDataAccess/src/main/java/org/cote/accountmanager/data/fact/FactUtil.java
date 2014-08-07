package org.cote.accountmanager.data.fact;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.DataAccessException;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.data.factory.DataFactory;
import org.cote.accountmanager.data.factory.GroupFactory;
import org.cote.accountmanager.data.factory.NameIdFactory;
import org.cote.accountmanager.data.factory.NameIdGroupFactory;
import org.cote.accountmanager.data.factory.RoleFactory;
import org.cote.accountmanager.data.factory.UserFactory;
import org.cote.accountmanager.objects.BaseRoleType;
import org.cote.accountmanager.objects.DirectoryGroupType;
import org.cote.accountmanager.objects.FactEnumType;
import org.cote.accountmanager.objects.FactType;
import org.cote.accountmanager.objects.NameIdType;
import org.cote.accountmanager.objects.OperationResponseEnumType;
import org.cote.accountmanager.objects.types.FactoryEnumType;
import org.cote.accountmanager.objects.types.GroupEnumType;
import org.cote.accountmanager.objects.types.RoleEnumType;

public class FactUtil {
	public static final Logger logger = Logger.getLogger(FactUtil.class.getName());
	private static final Pattern idPattern = Pattern.compile("^\\d+$");
	
	public static void setFactReference(FactType sourceFact, FactType matchFact){
		if(sourceFact.getFactReference() != null) return;
		
		NameIdType obj = factoryRead(sourceFact,matchFact);
		if(obj == null){
			logger.error("Failed to find object " + sourceFact.getSourceUrn() + " (" + sourceFact.getFactoryType().toString() + ") in organization " + matchFact.getOrganization().getName());
			return;
		}
		logger.info("Found object " + sourceFact.getSourceUrn() + " (" + sourceFact.getFactoryType().toString() + ") in organization " + matchFact.getOrganization().getName() + " having id " + obj.getId());
		sourceFact.setFactReference(obj);
	}
	public static String getFactAttributeValue(FactType sourceFact, FactType matchFact){
		setFactReference(sourceFact, matchFact);
		if(sourceFact.getFactReference() == null) return null;
		Factories.getAttributeFactory().populateAttributes(sourceFact.getFactReference());
		return Factories.getAttributeFactory().getAttributeValueByName(sourceFact.getFactReference(), matchFact.getSourceUrn());
	}
	/*
	public static <T> T getFactPropertyValue(FactType sourceFact, FactType matchFact){
		setFactReference(sourceFact, matchFact);
		if(sourceFact.getFactReference() == null) return null;
	
        Field field = Other.class.getDeclaredField(matchFact.getSourceUrn());
        field.setAccessible(true);
        T value = (T)field.get(t);
	    
		
		return Factories.getAttributeFactory().getAttributeValueByName(sourceFact.getFactReference(), matchFact.getSourceUrn());
	}
	*/
	public static String getFactValue(FactType sourceFact, FactType matchFact){
		String out_val = null;
		/// Fact value is driven by a combination of what the source fact has and what  the matchFact expects
		/// The source fact provides context, and the match fact provides specificity
		///
		switch(matchFact.getFactType()){
			case STATIC:
				out_val = sourceFact.getFactData();
				break;
			case ATTRIBUTE:
				out_val = getFactAttributeValue(sourceFact, matchFact);
				break;
			/*
			case PROPERTY:
				out_val = getFactPropertyValue(sourceFact, matchFact);
				break;
			*/
			default:
				logger.error("Unhandled fact type: " + matchFact.getFactType());
				break;
		}
		return out_val;
	}
	public static String getMatchFactValue(FactType sourceFact, FactType matchFact){
		String out_val = null;
		switch(matchFact.getFactType()){
			/// Note: The match of an attribute fact is presently the static value
			/// This is because the source type got cross-purposed to parameter
			case ATTRIBUTE:
			case STATIC:
				out_val = matchFact.getFactData();
				break;
			default:
				logger.error("Unhandled fact type: " + matchFact.getFactType());
				break;
		}
		return out_val;
	}
	
	private static DirectoryGroupType getDirectoryFromFact(FactType sourceFact, FactType referenceFact) throws FactoryException, ArgumentException{
		if(sourceFact.getSourceUrl() == null){
			logger.error("Source URL is null");
			return null;
		}
		DirectoryGroupType dir = (DirectoryGroupType)Factories.getGroupFactory().findGroup(null, GroupEnumType.DATA, sourceFact.getSourceUrl(), referenceFact.getOrganization());
		if(dir == null) throw new ArgumentException("Invalid group path " + sourceFact.getSourceUrl());
		return dir;
	}
	private static BaseRoleType getRoleFromFact(FactType sourceFact, FactType referenceFact) throws FactoryException, ArgumentException, DataAccessException{
		if(sourceFact.getSourceUrl() == null){
			logger.error("Source URL is null");
			return null;
		}
		BaseRoleType role = (BaseRoleType)Factories.getRoleFactory().findRole(RoleEnumType.fromValue(sourceFact.getSourceType()), sourceFact.getSourceUrl(), referenceFact.getOrganization());
		if(role == null) throw new ArgumentException("Invalid role path " + sourceFact.getSourceUrl());
		return role;
	}
	/* 
	 * NOTE: Authorization factories intentionally not included in the lookup by name for rules
	 */

	public static <T> T factoryRead(FactType sourceFact,final FactType referenceFact){
		T out_obj = null;
		boolean lookupRef = false;
		FactType useRef = (lookupRef ? referenceFact : sourceFact);
		if(sourceFact.getFactoryType() == FactoryEnumType.UNKNOWN || referenceFact.getFactoryType() == FactoryEnumType.UNKNOWN){
			logger.error("Source fact (" + sourceFact.getFactoryType() + ") or reference fact (" + referenceFact.getFactoryType() + ") is not configured for a factory read operation");
			return null;
		}
		if(sourceFact.getSourceUrn() == null){
			logger.error("Source URN is null");
			return out_obj;
		}
		try {
			//out_obj = (T)Factories.getUserFactory().getUserByName(sourceFact.getFactData(), referenceFact.getOrganization());
			NameIdFactory fact = Factories.getFactory(useRef.getFactoryType());
			DirectoryGroupType dir = null;
			if(idPattern.matcher(sourceFact.getSourceUrn()).matches()){
				out_obj = fact.getById(Long.parseLong(sourceFact.getSourceUrn()), referenceFact.getOrganization());
			}
			else{
				switch(useRef.getFactoryType()){
					/// User is one of the only organization level schemas with a unique constraint on just the name
					///
					case USER:
						out_obj = (T)((UserFactory)fact).getUserByName(sourceFact.getSourceUrn(), referenceFact.getOrganization());
						break;
						
					/// NameIdGroupFactory types
					case ACCOUNT:
					case CONTACT:
					case PERSON:
					case ADDRESS:
						dir =  getDirectoryFromFact(sourceFact,referenceFact);
						
						out_obj = (T)((NameIdGroupFactory)fact).getByName(sourceFact.getSourceUrn(), dir);
						logger.debug("Looking for " + useRef.getFactoryType() + " " + sourceFact.getSourceUrn() + " in " + (dir != null ? dir.getPath() : "Null Dir") + " - Result is " + (out_obj == null ? "Null":"Found"));
						break;
					/// Data is a predecessor to the NameIdGroupFactory type, but it doesn't inherity from that base class
					case DATA:
						out_obj = (T)((DataFactory)fact).getDataByName(sourceFact.getSourceUrn(), getDirectoryFromFact(sourceFact,referenceFact));
						break;
					case GROUP:
						dir =  getDirectoryFromFact(sourceFact,referenceFact);
						out_obj = (T)((GroupFactory)fact).getDirectoryByName(sourceFact.getSourceUrn(), dir,referenceFact.getOrganization());
						logger.debug("Looking for " + useRef.getFactoryType() + " " + sourceFact.getSourceUrn() + " in " + (dir != null ? dir.getPath() : "Null Dir") + " - Result is " + (out_obj == null ? "Null":"Found"));
						break;
					case ROLE:
						BaseRoleType parent = getRoleFromFact(sourceFact, referenceFact);
						out_obj = (T)((RoleFactory)fact).getRoleByName(sourceFact.getSourceUrn(), parent, referenceFact.getOrganization());
						logger.debug("Looking for " + useRef.getFactoryType() + " " + sourceFact.getSourceUrn() + " in " + (parent != null ? sourceFact.getSourceUrl() : "Null Role") + " - Result is " + (out_obj == null ? "Null":"Found"));
						break;
					default:
						throw new ArgumentException("Unhandled factory type " + useRef.getFactoryType());
				}
			}
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (ArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return out_obj;
	}

}