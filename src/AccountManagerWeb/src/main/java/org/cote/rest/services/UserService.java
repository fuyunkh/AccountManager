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
package org.cote.rest.services;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.data.security.CredentialService;
import org.cote.accountmanager.data.security.OrganizationSecurity;
import org.cote.accountmanager.data.services.AuditService;
import org.cote.accountmanager.data.services.AuthorizationService;
import org.cote.accountmanager.data.services.SessionSecurity;
import org.cote.accountmanager.objects.AuditType;
import org.cote.accountmanager.objects.AuthenticationRequestType;
import org.cote.accountmanager.objects.AuthenticationResponseEnumType;
import org.cote.accountmanager.objects.AuthenticationResponseType;
import org.cote.accountmanager.objects.BaseSpoolType;
import org.cote.accountmanager.objects.ContactInformationType;
import org.cote.accountmanager.objects.ContactType;
import org.cote.accountmanager.objects.CredentialType;
import org.cote.accountmanager.objects.DataType;
import org.cote.accountmanager.objects.NameIdType;
import org.cote.accountmanager.objects.OrganizationType;
import org.cote.accountmanager.objects.UserSessionType;
import org.cote.accountmanager.objects.UserType;
import org.cote.accountmanager.objects.types.ActionEnumType;
import org.cote.accountmanager.objects.types.AuditEnumType;
import org.cote.accountmanager.objects.types.ContactEnumType;
import org.cote.accountmanager.objects.types.LocationEnumType;
import org.cote.accountmanager.services.DataServiceImpl;
import org.cote.accountmanager.services.UserServiceImpl;
import org.cote.accountmanager.util.CalendarUtil;
import org.cote.accountmanager.util.SecurityUtil;
import org.cote.accountmanager.util.ServiceUtil;
import org.cote.beans.MessageBean;
import org.cote.beans.SessionBean;
import org.cote.beans.SchemaBean;
//import org.cote.beans.UserBean;
import org.cote.rest.schema.ServiceSchemaBuilder;
import org.cote.util.BeanUtil;
import org.cote.util.RegistrationUtil;

@Path("/user")
public class UserService{

	public static final Logger logger = Logger.getLogger(UserService.class.getName());
	private static SchemaBean schemaBean = null;	
	public UserService(){
		//JSONConfiguration.mapped().rootUnwrapping(false).build();

	}
	@GET @Path("/clearCache") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public boolean flushCache(@Context HttpServletRequest request){
		AuditType audit = AuditService.beginAudit(ActionEnumType.MODIFY, "clearCache",AuditEnumType.SESSION, request.getSession(true).getId());
		AuditService.targetAudit(audit, AuditEnumType.INFO, "Request clear factory cache");
		UserType user = ServiceUtil.getUserFromSession(audit,request);
		if(user==null){
			AuditService.denyResult(audit, "Deny for anonymous user");
			return false;
		}
		AuditService.targetAudit(audit, AuditEnumType.USER, "User Factory");
		Factories.getUserFactory().clearCache();
		AuditService.permitResult(audit,user.getName() + " flushed User Factory cache");
		return true;
	}
	@GET @Path("/getPublicUser") @Produces(MediaType.APPLICATION_JSON)
	public UserType getPublicUser(@Context HttpServletRequest request){
		 return Factories.getDocumentControl(ServiceUtil.getOrganizationFromRequest(request));
	}
	@GET @Path("/getSelf") @Produces(MediaType.APPLICATION_JSON)
	public UserType getSelf(@Context HttpServletRequest request){
		//UserType bean = null;
		String sessionId = request.getSession(true).getId();
		UserType user = null;
		try{
			user = SessionSecurity.getUserBySession(sessionId,ServiceUtil.getOrganizationFromRequest(request));
			if(user != null){
				/*
				if(user.getContactInformation() == null){
					user.setContactInformation(new ContactInformationType());
					user.getContactInformation().setEmail("none");
				}
				*/
				//bean = BeanUtil.getUserType(user);
			}
		}
		catch(FactoryException fe){
			logger.error(fe.getMessage());
			fe.printStackTrace();
		} catch (ArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return user;
	}
	@POST @Path("/authenticate") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public AuthenticationResponseType authenticate(AuthenticationRequestType authRequest, @Context HttpServletRequest request, @Context HttpServletResponse response){
		
		String sessionId = request.getSession(true).getId();
		AuditType audit = AuditService.beginAudit(ActionEnumType.AUTHENTICATE, "authenticate", AuditEnumType.SESSION, sessionId);
		AuthenticationResponseType authResponse = new AuthenticationResponseType();
		authResponse.setSessionId(sessionId);
		authResponse.setResponse(AuthenticationResponseEnumType.NOT_AUTHENTICATED);
		AuditService.targetAudit(audit, AuditEnumType.USER, authRequest.getSubject());
		if(authRequest == null || authRequest.getSubject() == null || authRequest.getCredential() == null || authRequest.getCredential().length == 0){
			logger.error("Invalid authentication request");
			AuditService.denyResult(audit, "Invalid authentication request");
			authResponse.setMessage("Invalid arguments");
			return authResponse;
		}
		if(authRequest.getOrganizationPath() == null) authRequest.setOrganizationPath("/Public");
		AuditService.targetAudit(audit, AuditEnumType.USER, authRequest.getSubject() + " in " + authRequest.getOrganizationPath());
		boolean requireSSL = Boolean.parseBoolean(request.getServletContext().getInitParameter("ssl.login.required"));
		if(requireSSL && request.isSecure() == false){
			AuditService.denyResult(audit, "Authentication requires a secure connection");
			authResponse.setMessage("Secure connection required");
			return authResponse;

		}
		UserType user = null;

		try{
			OrganizationType org = Factories.getOrganizationFactory().findOrganization(authRequest.getOrganizationPath());
			if(org == null){
				AuditService.denyResult(audit, "Invalid organization");
				authResponse.setMessage("Authentication failed.");
				return authResponse;
			}
			user = SessionSecurity.login(sessionId,authRequest.getSubject(), authRequest.getCredentialType(), new String(authRequest.getCredential(),"UTF-8"), org);
			if(user != null){
				//Factories.getUserFactory().populate(user);
				authResponse.setResponse(AuthenticationResponseEnumType.AUTHENTICATED);
				authResponse.setUser(user);
				AuditService.permitResult(audit,"#" + user.getId());
			}
			else{
				AuditService.denyResult(audit, "User not found");
				authResponse.setMessage("Authentication failed.");
			}
		}
		catch(FactoryException fe){
			logger.error(fe.getMessage());
			fe.printStackTrace();
			user = null;
			AuditService.denyResult(audit, "Error: " + fe.getMessage());
			authResponse.setMessage("An error occured.");
		} catch (ArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
			user = null;
			AuditService.denyResult(audit, "Error: " + e.getMessage());
			authResponse.setMessage("An error occured.");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(user != null) ServiceUtil.addCookie(response,"OrganizationId",Long.toString(user.getOrganization().getId()));
		
		return authResponse;
	}
	

	@GET @Path("/safeLogout/{id : [a-zA-Z_0-9\\-]+}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public SessionBean safeLogout(@PathParam("id") String id,@Context HttpServletRequest request, @Context HttpServletResponse response){
		return doLogout(request, response);
	}

	@GET @Path("/logout/") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public SessionBean logout(@Context HttpServletRequest request, @Context HttpServletResponse response){
		return doLogout(request, response);
	}
	public SessionBean doLogout(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(true);
		String sessionId = session.getId();
		AuditType audit = AuditService.beginAudit(ActionEnumType.MODIFY, "getLogout",AuditEnumType.SESSION, sessionId);
		OrganizationType org = null;
		
		UserType user = null;
		UserSessionType userSession = null;
		try{
			user = SessionSecurity.getUserBySession(sessionId, ServiceUtil.getOrganizationFromRequest(request));
			if(user != null){
				AuditService.targetAudit(audit, AuditEnumType.USER, user.getName());
				SessionSecurity.logout(user.getSession());
				userSession = user.getSession();
				AuditService.permitResult(audit, "User session logged out");
			}
			else{
				AuditService.denyResult(audit, "User not found");
				userSession = SessionSecurity.getUserSession(sessionId, ServiceUtil.getOrganizationFromRequest(request));
			}
			logger.info("Not invalidating the session while JEE LoginModule is not being used.");
			//session.invalidate();

		}
		catch(FactoryException fe){
			logger.error(fe.getMessage());
			fe.printStackTrace();
			AuditService.denyResult(audit, "Error: " + fe.getMessage());
		} catch (ArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			AuditService.denyResult(audit, "Error: " + e.getMessage());
			e.printStackTrace();
		}
		ServiceUtil.clearCookie(response, "OrganizationId");
		//ServiceUtil.clearCookie(response, "JSESSIONID");
		return BeanUtil.getSessionBean(userSession, sessionId);
	}

	//@POST @Path("/postConfirmation") @Produces(MediaType.TEXT_PLAIN) @Consumes(MediaType.APPLICATION_JSON)
	//public boolean postRegistration(String id, String regId, @Context HttpServletRequest request){
	@GET @Path("/confirm/{id : [a-zA-Z_0-9\\-]+}/{reg : [a-zA-Z_0-9\\-]+}") @Produces(MediaType.APPLICATION_XHTML_XML) @Consumes(MediaType.APPLICATION_JSON)
	public String confirmRegistration(@PathParam("id") String id,@PathParam("reg") String regId, @Context HttpServletRequest request) {
		StringBuffer buff = new StringBuffer();
		
		String sessionId = request.getSession(true).getId();
		
		boolean registered = RegistrationUtil.confirmUserRegistration(id,  regId, request.getRemoteAddr(), sessionId);
		
		MessageBean bean = new MessageBean();
		bean.setId(UUID.randomUUID().toString());
		bean.setId(id);
		bean.setName("Registration");
		bean.setData(regId);
		logger.error("id=" + id + " / reg=" + regId);
		buff.append("<html><head></head><body>[ ... " + registered + " ...]</body></html>");
		return buff.toString();
		//return bean;
		
	}
	
	
	
	@POST @Path("/postRegistration") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public SessionBean postRegistration(UserType user, @Context HttpServletRequest request){
		//logger.error(request.getRemoteAddr());
		String sessionId = request.getSession(true).getId();
		SessionBean ctxSession = null;

		if(user.getOrganization() == null) user.setOrganization(Factories.getPublicOrganization());
		
		logger.error("Configuring registration for organization " + user.getOrganization().getName());
		
		boolean regEnabled = Boolean.parseBoolean(request.getServletContext().getInitParameter("registration.enabled"));
		if(regEnabled == false){
			AuditType audit = AuditService.beginAudit(ActionEnumType.ADD, "Registration", AuditEnumType.USER, user.getName() + " in " + user.getOrganization().getName());
			AuditService.denyResult(audit, "Web Registration is disabled");
			return null;
		}

				
		UserSessionType session1 = RegistrationUtil.createUserRegistration(user, request.getRemoteAddr());
		if(session1 != null){
			try{
				ctxSession = BeanUtil.getSessionBean(SessionSecurity.getUserSession(sessionId, user.getOrganization()),sessionId);
				ctxSession.setValue("registration-session-id", session1.getSessionId());
				ctxSession.setValue("registration-organization-id", Long.toString(user.getOrganization().getId()));
				Factories.getSessionFactory().updateData(ctxSession);
			}
			catch(FactoryException fe){
				logger.error(fe.getMessage());
				fe.printStackTrace();
			}
		}
		return BeanUtil.getSessionBean(session1, (session1 == null ? null : session1.getSessionId()));
	}
	
	
	@GET @Path("/count/{organizationId:[0-9]+}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public int count(@PathParam("organizationId") long organizationId,@Context HttpServletRequest request){
		return UserServiceImpl.count(organizationId, request);
	}
	@POST @Path("/delete") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public boolean delete(UserType bean,@Context HttpServletRequest request){
		return UserServiceImpl.delete(bean, request);
	}
	
	@POST @Path("/add") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public boolean add(UserType bean,@Context HttpServletRequest request){
		
		return UserServiceImpl.add(bean, request);
	}
	
	@POST @Path("/update") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public boolean update(UserType bean,@Context HttpServletRequest request){
		return UserServiceImpl.update(bean, request);
	}
	@GET @Path("/read/{name: [%\\sa-zA-Z_0-9\\-]+}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public UserType read(@PathParam("name") String name,@Context HttpServletRequest request){
		
		return UserServiceImpl.readByOrganizationId(ServiceUtil.getOrganizationFromRequest(request).getId(),name, request);
	}
	@GET @Path("/readByOrganizationId/{orgId:[0-9]+}/{name: [%\\sa-zA-Z_0-9\\-]+}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public UserType readByOrganizationId(@PathParam("name") String name,@PathParam("orgId") long orgId,@Context HttpServletRequest request){
		return UserServiceImpl.readByOrganizationId(orgId, name, request);
	}	
	@GET @Path("/readById/{id: [0-9]+}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public UserType readById(@PathParam("id") long id,@Context HttpServletRequest request){
		return UserServiceImpl.readById(id, request);
	}
	
	@GET @Path("/list") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public List<UserType> list(@Context HttpServletRequest request){
		UserType user = ServiceUtil.getUserFromSession(request);
		return UserServiceImpl.getList(user, user.getOrganization(),0,0);

	}
	@GET @Path("/listInOrganization/{orgId : [\\d]+}/{startIndex: [\\d]+}/{recordCount: [\\d]+}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public List<UserType> listInOrganization(@PathParam("orgId") long orgId,@PathParam("startIndex") long startIndex,@PathParam("recordCount") int recordCount,@Context HttpServletRequest request){
		UserType user = ServiceUtil.getUserFromSession(request);
		OrganizationType org = null;
		try {
			org =Factories.getOrganizationFactory().getOrganizationById(orgId);
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (ArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		if(org == null){
			return new ArrayList<UserType>();
		}
		return UserServiceImpl.getList(user, org, startIndex, recordCount );

	}
	
	@POST @Path("/newPrimaryCredential") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public boolean newPrimaryCredential(AuthenticationRequestType authReq,@Context HttpServletRequest request){
		AuditType audit = AuditService.beginAudit(ActionEnumType.MODIFY, "newPrimaryCredential", AuditEnumType.SESSION, request.getSession().getId());
		UserType user = ServiceUtil.getUserFromSession(audit,request);
		boolean out_bool = false;
		if(user == null) return out_bool;
		CredentialType newCred = null;
		try{
			UserType updateUser = Factories.getUserFactory().getUserByName(authReq.getSubject(), user.getOrganization());
			if(updateUser == null){
				AuditService.denyResult(audit, "Target user " + authReq.getSubject() + " does not exist");
				return out_bool;
			}
			/// If not account admin, then validate the current password
			if(AuthorizationService.isAccountAdministratorInOrganization(user, user.getOrganization()) == false){
				CredentialType currentCred = CredentialService.getPrimaryCredential(updateUser);
				if(CredentialService.validatePasswordCredential(updateUser, currentCred, new String(authReq.getCheckCredential(),"UTF-8"))==false){
					AuditService.denyResult(audit, "Failed to validate current credential");
					return out_bool;
				}
			}
			/// Create a new primary credential for target user
			newCred = CredentialService.newCredential(authReq.getCredentialType(), null, updateUser, updateUser, authReq.getCredential(), true, true);
		}
		catch(FactoryException fe){
			logger.error(fe.getMessage());
		} catch (ArgumentException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(newCred != null){
			AuditService.permitResult(audit, "Created a new primary credential");
			out_bool = true;
		}
		else{
			AuditService.denyResult(audit, "Failed to create new primary credential");
		}
		return out_bool;

	}
	
	 @GET @Path("/smd") @Produces(MediaType.APPLICATION_JSON)
	 public SchemaBean getSmdSchema(@Context UriInfo uri){
		 if(schemaBean != null) return schemaBean;
		 schemaBean = ServiceSchemaBuilder.modelRESTService(this.getClass(),uri.getAbsolutePath().getRawPath().replaceAll("/smd$", ""));
		 return schemaBean;
	 }
}