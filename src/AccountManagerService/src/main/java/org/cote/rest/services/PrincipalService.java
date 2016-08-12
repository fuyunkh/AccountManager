package org.cote.rest.services;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.objects.OrganizationType;
import org.cote.accountmanager.objects.UserType;
import org.cote.jaas.UserPrincipal;

@Path("/principal")
public class PrincipalService {
	private static final Logger logger = LogManager.getLogger(Principal.class);
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSelf(@Context HttpServletRequest request){
		Principal principal = request.getUserPrincipal();
		String outToken = null;
		if(principal != null && principal instanceof UserPrincipal){
			UserPrincipal userp = (UserPrincipal)principal;
			//userp.get
			logger.info("UserPrincipal: " + userp.toString());
			try {
				OrganizationType org = Factories.getOrganizationFactory().findOrganization(userp.getOrganizationPath());

				UserType user = Factories.getUserFactory().getById(userp.getId(), org.getId());
				if(user != null){
					outToken = user.getUrn();
				}
			} catch (FactoryException | ArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			logger.info("Don't know what");
		}
		boolean out_bool = false;

		return Response.status(200).entity(outToken).build();
	}
}
