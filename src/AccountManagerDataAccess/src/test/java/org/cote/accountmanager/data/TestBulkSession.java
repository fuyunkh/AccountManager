package org.cote.accountmanager.data;

import static org.junit.Assert.assertNotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cote.accountmanager.data.factory.RoleFactory;
import org.cote.accountmanager.objects.UserRoleType;
import org.cote.accountmanager.objects.types.FactoryEnumType;
import org.junit.Test;

public class TestBulkSession extends BaseDataAccessTest{
	public static final Logger logger = LogManager.getLogger(TestBulkSession.class);
	
	/*
	@Test
	public void TestBulkData(){
		try{
			Factories.getNameIdFactory(FactoryEnumType.USER).populate(testUser);
			DirectoryGroupType dir = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getCreateDirectory(testUser, "BulkData", testUser.getHomeDirectory(), testUser.getOrganization());
			String sessionId = BulkFactories.getBulkFactory().newBulkSession();
			long start = System.currentTimeMillis();
			for(int i = 0; i < 10;i++){
				DataType data = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).newData(testUser, dir);
				data.setName(UUID.randomUUID().toString());
				data.setMimeType("text/plain");
				DataUtil.setValueString(data, UUID.randomUUID().toString());
				((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).addData(data);
				data = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getDataByName(data.getName(),dir);
				AuthorizationService.switchData(testUser, testUser2,data,AuthorizationService.getViewDataPermission(testUser.getOrganization()), true);
			}
			long stop = System.currentTimeMillis();
			logger.info("Time to loop individual: " + (stop - start) + "ms");
			start = System.currentTimeMillis();
			Map<String,Long> bulkNameId = new HashMap<String,Long>();
			for(int i = 0; i < 10; i++){
				DataType data = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).newData(testUser, dir);
				data.setName(UUID.randomUUID().toString());
				data.setMimeType("text/plain");
				DataUtil.setValueString(data, UUID.randomUUID().toString());
				BulkFactories.getBulkFactory().createBulkEntry(sessionId, FactoryEnumType.DATA, data);
				assertTrue("Data id (" + data.getId() + " not set to bulk entry",data.getId() < 0);
				DataType checkData = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getDataByName(data.getName(),false,data.getGroup());
				assertNotNull("Failed cache check for bulk data object by name",checkData);
				checkData = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getDataById(data.getId(), data.getOrganization());
				assertNotNull("Failed cache check for bulk data object by id",checkData);
				logger.info("Data Bulk entry id=" + data.getId());
				
				AuthorizationService.switchData(testUser,testUser2,data,AuthorizationService.getViewDataPermission(testUser.getOrganization()),true);
				//bulkNameId.put(data.getName(), data.getId());
			}
			stop = System.currentTimeMillis();
			logger.info("Time to loop bulk: " + (stop - start) + "ms");
			BulkFactories.getBulkFactory().write(sessionId);
			logger.info("Time to write: " + (System.currentTimeMillis() - start) + "ms");
			BulkFactories.getBulkFactory().close(sessionId);
		}
		catch(FactoryException fe){
			logger.error(fe.getMessage());
			logger.error("Error",fe);
		} catch (ArgumentException e) {
			
			logger.error("Error",e);
		} catch (DataException e) {
			
			logger.error("Error",e);
		} catch (DataAccessException e) {
			
			logger.error("Error",e);
		}
	}
	*/
	
	@Test
	public void TestBulkRole(){
		try{
			String sessionId = BulkFactories.getBulkFactory().newBulkSession();
			Factories.getNameIdFactory(FactoryEnumType.USER).populate(testUser);
			UserRoleType baseRole = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).newUserRole(testUser, "BulkRoleParent");
			BulkFactories.getBulkFactory().createBulkEntry(sessionId, FactoryEnumType.ROLE, baseRole);
			UserRoleType childRole = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).newUserRole(testUser, "ChildRole",baseRole);
			BulkFactories.getBulkFactory().createBulkEntry(sessionId, FactoryEnumType.ROLE, childRole);
			
			logger.info("Retrieving Bulk Role Parent");
			UserRoleType check = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).getUserRoleByName("BulkRoleParent", testUser.getOrganizationId());
			assertNotNull("Failed role cache check",check);
			
			logger.info("Retrieving Child Role By Parent");
			check = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).getUserRoleByName("ChildRole", check, testUser.getOrganizationId());
			assertNotNull("Failed role+parent cache check",check);
			
			BulkFactories.getBulkFactory().close(sessionId);
		}
		catch(FactoryException fe){
			logger.error("Error",fe);
		}  catch (ArgumentException e) {
			
			logger.error("Error",e);
		}
	}
	/*
	public void TestBulkGroup(){
		try{
			Factories.getNameIdFactory(FactoryEnumType.USER).populate(testUser);
			UserRoleType baseRole = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).getCreateUserRole(testUser, "BulkRoleParent", null);
			DirectoryGroupType dir = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getCreateDirectory(testUser, "BulkGroup", testUser.getHomeDirectory(), testUser.getOrganization());
			String sessionId = BulkFactories.getBulkFactory().newBulkSession();
			
			List<String> debugRoles = new ArrayList<String>();
			for(int i = 0; i < 10;i++){
				String name = UUID.randomUUID().toString();
				UserRoleType role = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).newUserRole(testUser, name,baseRole);
				BulkFactories.getBulkFactory().createBulkEntry(sessionId, FactoryEnumType.ROLE, role);
				assertTrue("Role id (" + role.getId() + ") not set to bulk entry",role.getId() < 0);
				BaseRoleType checkRole = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).getUserRoleByName(role.getName(),baseRole,baseRole.getOrganization());
				assertNotNull("Failed cache check for bulk role object by name",checkRole);
				checkRole = ((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).getRoleById(role.getId(),baseRole.getOrganization());
				assertNotNull("Failed cache check for bulk role object by name",checkRole);
				debugRoles.add(name);
				
				RoleService.addUserToRole(testUser, role);
				RoleService.addUserToRole(testUser2, role);
			}
			
			long start = System.currentTimeMillis();
			Map<String,Long> bulkNameId = new HashMap<String,Long>();
			for(int i = 0; i < 10; i++){
				DirectoryGroupType group = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).newDirectoryGroup(testUser,UUID.randomUUID().toString(), dir, testUser.getOrganization());
				BulkFactories.getBulkFactory().createBulkEntry(sessionId, FactoryEnumType.GROUP, group);
				assertTrue("Group id (" + group.getId() + " not set to bulk entry",group.getId() < 0);
				DirectoryGroupType checkGroup = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getDirectoryByName(group.getName(),dir,group.getOrganization());
				assertNotNull("Failed cache check for bulk group object by name",checkGroup);
				checkGroup = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getDirectoryById(group.getId(), group.getOrganization());
				assertNotNull("Failed cache check for bulk group object by id",checkGroup);
				logger.info("Group Bulk entry id=" + group.getId());

				AuthorizationService.switchGroup(testUser,testUser2,group,AuthorizationService.getViewDataPermission(testUser.getOrganization()),true);
				for(int r = 0; r < debugRoles.size();r++){
					AuthorizationService.switchGroup(testUser,(UserRoleType)((RoleFactory)Factories.getFactory(FactoryEnumType.ROLE)).getUserRoleByName(debugRoles.get(r),baseRole,baseRole.getOrganization()),group,AuthorizationService.getViewDataPermission(testUser.getOrganization()),true);
				}
				//bulkNameId.put(data.getName(), data.getId());
			}
			long stop = System.currentTimeMillis();
			logger.info("Time to loop bulk: " + (stop - start) + "ms");
			BulkFactories.getBulkFactory().write(sessionId);
			logger.info("Time to write: " + (System.currentTimeMillis() - start) + "ms");
			BulkFactories.getBulkFactory().close(sessionId);
		}
		catch(FactoryException fe){
			logger.error(fe.getMessage());
			logger.error("Error",fe);
		} catch (ArgumentException e) {
			
			logger.error("Error",e);
		}catch (DataAccessException e) {
			
			logger.error("Error",e);
		}
	}
	*/
}