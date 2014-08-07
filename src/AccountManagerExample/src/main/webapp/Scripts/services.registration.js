/*
 * Define who is providing the service layer
 */
if(!window.uwmServices){
	window.uwmServices = Hemi.json.rpc.service;
	window.uwmServiceCache = Hemi.json.rpc.cache.service;
}

/*
 * Register the available services
 * NOTE: The 3rd and 4th parameters of the service registration may be used to define static service and object models so they are not queried everytime
 */
(function(){
	/// Jersey JSON+REST w/ Basic AM SMD Support
	///
	/// The following scripts are generated using the service pack script.
	///

	uwmServices.addService(
			"AccountManager",
			"/AccountManager/rest/accountmanager/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/accountmanager","methods":[{"name":"flushCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"clearAuthorizationCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}}]},
			false
		);

		uwmServices.addService(
			"Organization",
			"/AccountManager/rest/organization/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/organization","methods":[{"name":"find","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OrganizationType"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"java.lang.String"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OrganizationType"}},{"name":"getRoot","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OrganizationType"}},{"name":"getDevelopment","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OrganizationType"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OrganizationType"}},{"name":"getPublic","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OrganizationType"}}]},
			false
		);

		uwmServices.addService(
			"Blog",
			"/AccountManager/rest/blog/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/blog","methods":[{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"java.lang.String"},{"name":"p2","type":"int"},{"name":"p3","type":"int"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"java.lang.String"},{"name":"p2","type":"java.lang.String"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DataType"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"getAuthorRole","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.UserRoleType"}},{"name":"listFull","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"java.lang.String"},{"name":"p2","type":"int"},{"name":"p3","type":"int"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Message",
			"/AccountManager/rest/message/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/message","methods":[{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"postMessage","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.beans.MessageBean"}],"returnValue":{"name":"retVal","type":"org.cote.beans.MessageBean"}},{"name":"postDataMessage","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.beans.DataBean"}],"returnValue":{"name":"retVal","type":"org.cote.beans.MessageBean"}},{"name":"getDataMessage","httpMethod":"GET","parameters":[],"returnValue":{"name":"retVal","type":"org.cote.beans.DataBean"}}]},
			false
		);

		uwmServices.addService(
			"Crypto",
			"/AccountManager/rest/crypto/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/crypto","methods":[{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"getKeyRing","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"[Lorg.cote.beans.CryptoBean;"}}]},
			false
		);

		uwmServices.addService(
			"Session",
			"/AccountManager/rest/session/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/session","methods":[{"name":"getSession","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SessionBean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"getSafeSession","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"},{"name":"p2","type":"javax.servlet.http.HttpServletResponse"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SessionBean"}}]},
			false
		);

		uwmServices.addService(
			"User",
			"/AccountManager/rest/user/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/user","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.UserType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.UserType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.UserType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.UserType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"logout","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"},{"name":"p1","type":"javax.servlet.http.HttpServletResponse"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SessionBean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"getSelf","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.UserType"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.UserType"}},{"name":"readByOrganizationId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.UserType"}},{"name":"listInOrganization","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"getPublicUser","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.UserType"}},{"name":"postLogin","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.UserType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"},{"name":"p2","type":"javax.servlet.http.HttpServletResponse"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.UserType"}},{"name":"safeLogout","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"},{"name":"p2","type":"javax.servlet.http.HttpServletResponse"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SessionBean"}},{"name":"confirm","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"java.lang.String"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.lang.String"}},{"name":"testConfirmRegistration","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"testRegistration","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.lang.String"}},{"name":"postRegistration","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.UserType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SessionBean"}}]},
			false
		);

		uwmServices.addService(
			"Data",
			"/AccountManager/rest/data/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/data","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.DataType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.DataType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DataType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.DataType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"getProfile","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DataType"}},{"name":"authorizeUser","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"boolean"},{"name":"p5","type":"boolean"},{"name":"p6","type":"boolean"},{"name":"p7","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DataType"}},{"name":"updateProfile","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.DataType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"listAuthorizedRoles","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"authorizeRole","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"boolean"},{"name":"p5","type":"boolean"},{"name":"p6","type":"boolean"},{"name":"p7","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"addFeedback","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.DataType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DataType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Group",
			"/AccountManager/rest/group/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/group","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BaseGroupType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"find","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DirectoryGroupType"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BaseGroupType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.DirectoryGroupType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"cd","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DirectoryGroupType"}},{"name":"home","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DirectoryGroupType"}},{"name":"dir","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"[Lorg.cote.accountmanager.objects.DirectoryGroupType;"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"authorizeUser","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"boolean"},{"name":"p5","type":"boolean"},{"name":"p6","type":"boolean"},{"name":"p7","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"countInParent","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"listAuthorizedRoles","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"authorizeRole","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"boolean"},{"name":"p5","type":"boolean"},{"name":"p6","type":"boolean"},{"name":"p7","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"listInDataGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"listInUserGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"getCreatePath","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.DirectoryGroupType"}},{"name":"deleteDirectory","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.DirectoryGroupType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}}]},
			false
		);

		uwmServices.addService(
			"Person",
			"/AccountManager/rest/person/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/person","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PersonType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PersonType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PersonType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PersonType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PersonType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PersonType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Account",
			"/AccountManager/rest/account/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/account","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.AccountType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.AccountType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.AccountType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.AccountType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.AccountType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.AccountType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Contact",
			"/AccountManager/rest/contact/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/contact","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.ContactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.ContactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.ContactType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.ContactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.ContactType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.ContactType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Address",
			"/AccountManager/rest/address/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/address","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.AddressType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.AddressType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.AddressType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.AddressType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.AddressType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.AddressType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Fact",
			"/AccountManager/rest/fact/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/fact","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FactType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FactType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FactType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Function",
			"/AccountManager/rest/function/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/function","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FunctionType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FunctionType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FunctionType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FunctionType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FunctionType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FunctionType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"FunctionFact",
			"/AccountManager/rest/functionfact/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/functionfact","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FunctionFactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FunctionFactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FunctionFactType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.FunctionFactType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FunctionFactType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.FunctionFactType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Pattern",
			"/AccountManager/rest/pattern/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/pattern","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PatternType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PatternType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PatternType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PatternType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PatternType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PatternType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Policy",
			"/AccountManager/rest/policy/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/policy","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PolicyType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PolicyType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PolicyType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PolicyType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"evaluate","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.PolicyRequestType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PolicyResponseType"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"define","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PolicyDefinitionType"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PolicyType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.PolicyType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Operation",
			"/AccountManager/rest/operation/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/operation","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.OperationType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.OperationType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OperationType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.OperationType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OperationType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.OperationType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Rule",
			"/AccountManager/rest/rule/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/rule","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.RuleType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"list","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.RuleType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"read","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.RuleType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.RuleType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.RuleType"}},{"name":"readByGroupId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.RuleType"}},{"name":"listInGroup","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"int"},{"name":"p2","type":"int"},{"name":"p3","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);

		uwmServices.addService(
			"Permission",
			"/AccountManager/rest/permission/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/permission","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BasePermissionType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BasePermissionType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BasePermissionType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.BasePermissionType"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"countInParent","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"readByParentId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"java.lang.String"},{"name":"p2","type":"long"},{"name":"p3","type":"long"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.BasePermissionType"}},{"name":"listInParent","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"int"},{"name":"p4","type":"int"},{"name":"p5","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"getUserPermission","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.BasePermissionType"}},{"name":"setPermissionOnGroupForUser","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"setPermissionOnGroupForAccount","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"setPermissionOnGroupForPerson","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"setPermissionOnGroupForRole","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}}]},
			false
		);

		uwmServices.addService(
			"AMSchema",
			"/AccountManager/rest/schema/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/schema","methods":[{"name":"get","httpMethod":"GET","parameters":[],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"entity","httpMethod":"GET","parameters":[],"returnValue":{"name":"retVal","type":"org.cote.beans.EntitySchema"}}]},
			{"defaultPackage":"org.cote.beans","baseRoleType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"roleType":null,"referenceId":null},"messageBeanSchema":{"javaClass":"org.cote.beans.MessageBean"},"dataTypeSchema":{"dataBytesStore":null,"cipherKey":null,"passKey":null,"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"detailsOnly":null,"publicId":null,"description":null,"dimensions":null,"mimeType":null,"size":null,"createdDate":null,"modifiedDate":null,"expiryDate":null,"blob":null,"compressed":null,"shortData":null,"passwordProtected":null,"passwordProtect":null,"enciphered":null,"encipher":null,"vaulted":null,"vaultId":null,"keyId":null,"readDataBytes":null,"wasDataBlob":null,"rating":null,"pointer":null,"dataHash":null,"compressionType":null,"volatile":null},"cryptoBeanSchema":{"publicKeyBytes":null,"privateKeyBytes":null,"cipherIV":null,"cipherKey":null,"encryptedCipherIV":null,"cipherProvider":null,"symetricCipherKeySpec":null,"asymetricCipherKeySpec":null,"randomSeedLength":null,"encryptCipherKey":null,"reverseEncrypt":null,"hashProvider":null,"cipherKeySpec":null,"keySize":null,"javaClass":"org.cote.beans.CryptoBean","spoolId":null},"sessionBean":{"dataSize":null,"sessionId":null,"securityId":null,"userId":null,"sessionCreated":null,"sessionAccessed":null,"sessionExpires":null,"sessionStatus":null,"expired":null,"organizationId":null,"javaClass":"org.cote.beans.SessionBean"},"sessionDataBean":{"name":null,"value":null,"javaClass":"org.cote.beans.SessionDataBean"},"userType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"password":null,"sessionStatus":null,"userStatus":null,"userType":null,"databaseRecord":null,"userId":null,"accountId":null},"directoryGroupType":{"subDirectories":null,"data":null,"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"groupType":null,"referenceId":null,"readData":null,"path":null},"baseGroupType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"groupType":null,"referenceId":null},"userGroupType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"groupType":null,"referenceId":null},"contactInformationType":{"contacts":null,"addresses":null,"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"contactInformationType":null,"description":null,"referenceId":null},"personType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null},"contactType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"contactType":null,"locationType":null,"preferred":null},"organizationType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"logicalId":null,"referenceId":null,"organizationType":null},"attributeType":{"values":[],"dataType":null,"index":null,"referenceType":null,"referenceId":null,"organizationId":null},"policyDefinitionType":{"parameters":null,"urn":null,"expires":null,"decisionAge":null,"enabled":null,"modified":null,"created":null},"policyRequestType":{"facts":null,"urn":null,"subject":null,"credential":null,"requestType":null,"asyncRequest":null,"organizationPath":null},"policyResponseType":{"patternChain":null,"response":null,"urn":null,"expires":null,"score":null},"factType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"urn":null,"logicalOrder":null,"score":null,"sourceUrn":null,"sourceDataType":null,"sourceUrl":null,"factType":null,"factoryType":null,"parameter":null,"sourceType":null},"functionType":{"facts":null,"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"urn":null,"logicalOrder":null,"score":null,"functionType":null,"sourceUrl":null,"sourceUrn":null},"functionFactType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"urn":null,"logicalOrder":null,"score":null,"functionUrn":null,"factUrn":null},"patternType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"urn":null,"logicalOrder":null,"score":null,"factUrn":null,"comparator":null,"patternType":null,"matchUrn":null,"operationUrn":null},"policyType":{"rules":null,"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"urn":null,"logicalOrder":null,"score":null,"enabled":null,"expires":null,"decisionAge":null,"modified":null,"created":null},"operationType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"urn":null,"logicalOrder":null,"score":null,"operationType":null},"ruleType":{"rules":null,"patterns":null,"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"urn":null,"logicalOrder":null,"score":null,"ruleType":null,"condition":null},"basePermissionType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"permissionType":null,"referenceId":null},"accountType":{"attributes":null,"nameType":null,"parentId":null,"name":null,"id":null,"ownerId":null,"populated":null,"objectId":null,"attributesPopulated":null,"accountType":null,"accountStatus":null,"referenceId":null,"accountId":null,"databaseRecord":null}}
		);

		uwmServices.addService(
			"Role",
			"/AccountManager/rest/role/smd",
			{"serviceType":"JSON-REST","serviceURL":"/AccountManager/rest/role","methods":[{"name":"add","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BaseRoleType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"count","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"delete","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BaseRoleType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"getUserRole","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.BaseRoleType"}},{"name":"update","httpMethod":"POST","parameters":[{"name":"p0","type":"org.cote.accountmanager.objects.BaseRoleType"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"clearCache","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"authorizeUser","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"long"},{"name":"p3","type":"boolean"},{"name":"p4","type":"boolean"},{"name":"p5","type":"boolean"},{"name":"p6","type":"boolean"},{"name":"p7","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"boolean"}},{"name":"readById","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.BaseRoleType"}},{"name":"smd","httpMethod":"GET","parameters":[{"name":"p0","type":"javax.ws.rs.core.UriInfo"}],"returnValue":{"name":"retVal","type":"org.cote.beans.SchemaBean"}},{"name":"countInParent","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"int"}},{"name":"readByParentId","httpMethod":"GET","parameters":[{"name":"p0","type":"java.lang.String"},{"name":"p1","type":"java.lang.String"},{"name":"p2","type":"long"},{"name":"p3","type":"long"},{"name":"p4","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"org.cote.accountmanager.objects.BaseRoleType"}},{"name":"listGroups","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"listUsers","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"listForUser","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}},{"name":"listInParent","httpMethod":"GET","parameters":[{"name":"p0","type":"long"},{"name":"p1","type":"long"},{"name":"p2","type":"java.lang.String"},{"name":"p3","type":"int"},{"name":"p4","type":"int"},{"name":"p5","type":"javax.servlet.http.HttpServletRequest"}],"returnValue":{"name":"retVal","type":"java.util.List"}}]},
			false
		);



	/*
	 * Register the AccountManager core services
	 */
		/*
		uwmServices.addService(
				"Permission",
				"/AccountManager/rest/permission/smd",
				true,
				true,
				true
		);
		uwmServices.addService(
				"AMSchema",
				"/AccountManager/rest/schema/smd"
				, true
				, true
		);
		

		uwmServices.addService(
			"Role",
			"/AccountManager/rest/role/smd",
			true,
			true,
			true
		);


	uwmServices.addService(
			"Organization",
			"/AccountManager/rest/organization/smd",
			true,
			true
	);
	uwmServices.addService(
			"Blog",
			"/AccountManager/rest/blog/smd",
			true,
			true
	);
	uwmServices.addService(
			"Message",
			"/AccountManager/rest/message/smd"
			, true
			, true
	);	
	uwmServices.addService(
			"Crypto",
			"/AccountManager/rest/crypto/smd"
			, true
			, true
	);
	uwmServices.addService(
			"Session",
			"/AccountManager/rest/session/smd"
			, true
			, true
	);
	uwmServices.addService(
			"User",
			"/AccountManager/rest/user/smd"
			, true
			, true
	);
	uwmServices.addService(
			"Data",
			"/AccountManager/rest/data/smd"
			, true
			, true
			, true
	);
	uwmServices.addService(
			"Role",
			"/AccountManager/rest/role/smd"
			, true
			, true
	);
	uwmServices.addService(
			"Group",
			"/AccountManager/rest/group/smd"
			, true
			, true
			, true
	);
	
uwmServices.addService(
				"Person",
				"/AccountManager/rest/person/smd",
				true,
				false,
				true
		);
		uwmServices.addService(
				"Account",
				"/AccountManager/rest/account/smd",
				true,
				false,
				true
		);
		uwmServices.addService(
				"Contact",
				"/AccountManager/rest/contact/smd",
				true,
				false,
				true
		);
		uwmServices.addService(
				"Address",
				"/AccountManager/rest/address/smd",
				true,
				false,
				true
		);
		
		uwmServices.addService(
				"Fact",
				"/AccountManager/rest/fact/smd",
				true,
				false,
				true
		);
		
		uwmServices.addService(
				"Function",
				"/AccountManager/rest/function/smd",
				true,
				false,
				true
		);
		
		uwmServices.addService(
				"FunctionFact",
				"/AccountManager/rest/functionfact/smd",
				true,
				false,
				true
		);
		
		uwmServices.addService(
				"Pattern",
				"/AccountManager/rest/pattern/smd",
				true,
				false,
				true
				
		);
		
		uwmServices.addService(
				"Policy",
				"/AccountManager/rest/policy/smd",
				true,
				false,
				true
		);
		
		uwmServices.addService(
				"Operation",
				"/AccountManager/rest/operation/smd",
				true,
				false,
				true
		);
		
		uwmServices.addService(
				"Rule",
				"/AccountManager/rest/rule/smd",
				true,
				false,
				true
		);
	*/
})();
