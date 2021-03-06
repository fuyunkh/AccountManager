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


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.data.services.SessionSecurity;
import org.cote.accountmanager.objects.OrganizationType;
import org.cote.accountmanager.objects.UserSessionType;
import org.cote.accountmanager.service.rest.SchemaBean;
import org.cote.accountmanager.service.rest.ServiceSchemaBuilder;
import org.cote.accountmanager.service.util.ServiceUtil;
import org.cote.beans.SessionBean;
import org.cote.util.BeanUtil;

@Path("/session")
public class SessionService{

	public static final Logger logger = LogManager.getLogger(SessionService.class);
	private static SchemaBean schemaBean = null;	
	public SessionService(){
		//JSONConfiguration.mapped().rootUnwrapping(false).build();

	}
	
	@GET @Path("/getSafeSession/{id : [a-zA-Z_0-9\\-]+}") @Produces(MediaType.APPLICATION_JSON) @Consumes(MediaType.APPLICATION_JSON)
	public SessionBean getSafeSession(@PathParam("id") String id,@Context HttpServletRequest request, @Context HttpServletResponse response){
		return getCreateSession(request,response);
	}
	
	@GET @Path("/getSession") @Produces(MediaType.APPLICATION_JSON)
	public SessionBean getSession(@Context HttpServletRequest request, @Context HttpServletResponse response){
		return getCreateSession(request,response);
	}
	private SessionBean getCreateSession(HttpServletRequest request,HttpServletResponse response){
		String sessionId = ServiceUtil.getSessionId(request,response,true);
		UserSessionType session = null;
		
		try {
			OrganizationType org = ServiceUtil.getOrganizationFromRequest(request);
			if(org != null) session = SessionSecurity.getUserSession(sessionId, org.getId());

		} catch (FactoryException e) {
			
			logger.error("Error",e);
			System.out.println(e.getMessage());
		}
		

		return BeanUtil.getSessionBean(session, sessionId);
	}
	
	 @GET @Path("/smd") @Produces(MediaType.APPLICATION_JSON)
	 public SchemaBean getSmdSchema(@Context UriInfo uri){
		 if(schemaBean != null) return schemaBean;
		 schemaBean = ServiceSchemaBuilder.modelRESTService(this.getClass(),uri.getAbsolutePath().getRawPath().replaceAll("/smd$", ""));
		 return schemaBean;
	 }
}