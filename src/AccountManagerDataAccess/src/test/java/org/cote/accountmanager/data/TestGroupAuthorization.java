package org.cote.accountmanager.data;

public class TestGroupAuthorization extends BaseDataAccessTest {
	/*
	 * This test largely depends on a pre-populated dataset that isn't being setup in the test
	@Test
	public void testEncryptedData(){
		OrganizationType org = null;
		UserType user = null;
		UserType user2 = null;
		try {
			org = ((OrganizationFactory)Factories.getFactory(FactoryEnumType.ORGANIZATION)).findOrganization("/Accelerant/Rocket");
			assertNotNull("Organization is null");
			user = Factories.getNameIdFactory(FactoryEnumType.USER).getByName("TestUser1", org.getId());
			user2 = Factories.getNameIdFactory(FactoryEnumType.USER).getByName("TestUser2", org.getId());
			assertNotNull("User 1 is null",user);
			assertNotNull("User 2 is null",user2);
			
			List<BasePermissionType> permissions = new ArrayList<BasePermissionType>();
			permissions.add(AuthorizationService.getViewGroupPermission(org.getId()));
			List<Long> ids = AuthorizationService.getAuthorizedGroups(user, permissions.toArray(new BasePermissionType[0]), org.getId());
			assertTrue("Invalid group count",ids.size() > 0);
			List<Long> ids2 = AuthorizationService.getAuthorizedGroups(user2, permissions.toArray(new BasePermissionType[0]), org.getId());
			assertTrue("Invalid group count",ids2.size() > 0);
			assertTrue("Unexpected group count.  User 1 has more rights than User 2, so should have access to more groups",ids.size() != ids2.size());
			
			logger.info("Id count #1 = " + ids.size());
			logger.info("Id count #2 = " + ids2.size());
		} catch (FactoryException | ArgumentException e) {
			
			logger.error("Error",e);
		}
		
		
		
		
		
	}
	*/
}
