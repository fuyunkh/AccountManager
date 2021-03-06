package org.cote.accountmanager.console;

import java.io.UnsupportedEncodingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.data.factory.DataFactory;
import org.cote.accountmanager.data.security.ApiClientConfigurationBean;
import org.cote.accountmanager.data.security.ApiConnectionConfigurationService;
import org.cote.accountmanager.data.services.SessionSecurity;
import org.cote.accountmanager.objects.CredentialEnumType;
import org.cote.accountmanager.objects.DataType;
import org.cote.accountmanager.objects.OrganizationType;
import org.cote.accountmanager.objects.UserType;
import org.cote.accountmanager.objects.types.FactoryEnumType;
import org.cote.accountmanager.util.FileUtil;
import org.cote.accountmanager.util.JSONUtil;
import org.cote.accountmanager.data.factory.OrganizationFactory;

public class ApiConfigAction {
	public static final Logger logger = LogManager.getLogger(ApiConfigAction.class);
	public static void configureApi(String orgPath, String adminPassword, String apiFile, String identity, String credential){
		try{
		OrganizationType org = ((OrganizationFactory)Factories.getFactory(FactoryEnumType.ORGANIZATION)).findOrganization(orgPath);
			if(org == null){
				logger.error("Null organization");
				return;
			}
			UserType adminUser = SessionSecurity.login("Admin", CredentialEnumType.HASHED_PASSWORD,adminPassword, org.getId());
			if(adminUser == null){
				logger.error("Null admin");
				return;
			}
			String file = FileUtil.getFileAsString(apiFile);
			if(file == null || file.length() == 0){
				logger.error("Null file");
				return;
			}
			
			ApiClientConfigurationBean chkConfig = JSONUtil.importObject(file,ApiClientConfigurationBean.class);
			if(chkConfig == null){
				logger.error("Failed to import file");
				return;
			}
			
			byte[] idb = identity.getBytes("UTF-8");
			byte[] credb = credential.getBytes("UTF-8");
			
			ApiClientConfigurationBean apiConfig = ApiConnectionConfigurationService.getApiClientConfiguration(chkConfig.getServiceType(), chkConfig.getName(), org.getId());
			if(apiConfig == null){
				apiConfig = ApiConnectionConfigurationService.addApiClientConfiguration(chkConfig.getServiceType(), chkConfig.getName(), chkConfig.getServiceUrl(), idb, credb, chkConfig.getAttributes(),org.getId());
				if(apiConfig != null){
					logger.error("Added " + chkConfig.getName() + " configuration");
				}
				else{
					logger.error("Failed to add " + chkConfig.getName() + " configuration");
				}
			}
			else{
				DataType chkData = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getByUrn(apiConfig.getDataUrn());
				if(chkData == null){
					logger.error("Config data is null");
				}
				else{
					logger.warn("Update not implemented");
				}
			}
			
			SessionSecurity.logout(adminUser);
		}
		catch(FactoryException e){
			logger.error(e.getMessage());
		} catch (ArgumentException e) {
			
			logger.error(e.getMessage());
			logger.error("Error",e);
		} catch (UnsupportedEncodingException e) {
			
			logger.error("Error",e);
		}

	}

}
