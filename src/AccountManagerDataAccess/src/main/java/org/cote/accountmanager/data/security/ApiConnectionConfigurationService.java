package org.cote.accountmanager.data.security;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cote.accountmanager.beans.SecurityBean;
import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.data.factory.DataFactory;
import org.cote.accountmanager.data.factory.GroupFactory;
import org.cote.accountmanager.data.services.AuditService;
import org.cote.accountmanager.data.services.PersonService;
import org.cote.accountmanager.data.util.UrnUtil;
import org.cote.accountmanager.exceptions.DataException;
import org.cote.accountmanager.objects.ApiServiceEnumType;
import org.cote.accountmanager.objects.AttributeType;
import org.cote.accountmanager.objects.AuditType;
import org.cote.accountmanager.objects.CredentialEnumType;
import org.cote.accountmanager.objects.CredentialType;
import org.cote.accountmanager.objects.DataType;
import org.cote.accountmanager.objects.DirectoryGroupType;
import org.cote.accountmanager.objects.UserType;
import org.cote.accountmanager.objects.types.ActionEnumType;
import org.cote.accountmanager.objects.types.AuditEnumType;
import org.cote.accountmanager.objects.types.FactoryEnumType;
import org.cote.accountmanager.objects.types.UserEnumType;
import org.cote.accountmanager.objects.types.UserStatusEnumType;
import org.cote.accountmanager.util.DataUtil;
import org.cote.accountmanager.util.JAXBUtil;

public class ApiConnectionConfigurationService {
	
	public static final Logger logger = LogManager.getLogger(ApiConnectionConfigurationService.class);
	
	/// Api User holds the encrypted credentials and connection settings for making API calls
	/// 
	private static UserType apiUser = null;
	private static String apiEmailConfigName = "System Email";
	private static String apiRestConfigName = "System Rest";
	private static String apiUserName = "ApiUser";
	private static String apiDirectoryName = ".api";
	public static String getApiEmailConfigName() {
		return apiEmailConfigName;
	}

	public static void setApiEmailConfigName(String apiEmailConfigName) {
		ApiConnectionConfigurationService.apiEmailConfigName = apiEmailConfigName;
	}

	public static String getApiRestConfigName() {
		return apiRestConfigName;
	}

	public static void setApiRestConfigName(String apiRestConfigName) {
		ApiConnectionConfigurationService.apiRestConfigName = apiRestConfigName;
	}

	public static UserType getApiUser(long organizationId) throws FactoryException, ArgumentException{
		if(apiUser != null) return apiUser;
		UserType chkUser = Factories.getNameIdFactory(FactoryEnumType.USER).getByName(apiUserName, organizationId);
		if(chkUser == null){
			AuditType audit = AuditService.beginAudit(ActionEnumType.ADD, "ApiConnectionConfigurationService", AuditEnumType.USER, apiUserName);
			PersonService.createUserAsPerson(audit, apiUserName, UUID.randomUUID().toString(), "ApiUser@example.com", UserEnumType.SYSTEM,UserStatusEnumType.RESTRICTED , organizationId);
			chkUser = Factories.getNameIdFactory(FactoryEnumType.USER).getByName(apiUserName, organizationId);
		}
		apiUser = chkUser;
		return apiUser;
	}
	
	public static byte[] getApiClientCredential(ApiClientConfigurationBean apiConfig, CredentialEnumType credType){
		byte[] outBytes = new byte[0];
		DataType data = null;
		data = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getByUrn(apiConfig.getDataUrn());
		if(data != null){
			CredentialType cred = CredentialService.getPrimaryCredential(data,credType,true);
			if(cred != null){
				outBytes = CredentialService.decryptCredential(cred);
			}
			else{
				logger.error("Did not find credential for data " + data.getUrn());
			}
		}
		else{
			logger.error("Did not find data for " + apiConfig.getDataUrn());
		}
		//CredentialType cre
		return outBytes;
	}
	public static DirectoryGroupType getApiDirectory(UserType user){
		DirectoryGroupType dir = null;
		try {
			dir = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getCreateDirectory(user, apiDirectoryName, ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getUserDirectory(user),user.getOrganizationId());
			((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).denormalize(dir);
		} catch (FactoryException e) {
			logger.error(e.getMessage());
			logger.error("Error",e);
		} catch (ArgumentException e) {
			logger.error(e.getMessage());
			logger.error("Error",e);
		}
		
		return dir;
	}
	public static ApiClientConfigurationBean getApiClientConfiguration(ApiServiceEnumType serviceType, String name, long organizationId){
		ApiClientConfigurationBean outConfig = null;
		String dataName = serviceType.toString() + " " + name;
		DirectoryGroupType dir = null;

		try {
			UserType owner = getApiUser(organizationId);
			dir = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getCreateDirectory(owner, apiDirectoryName, ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getUserDirectory(owner),owner.getOrganizationId());
			DataType data = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getDataByName(dataName, false, dir);
			if(data == null){
				logger.error("API Config '" + dataName + "' doesn't exists");
				return null;
			}
			SecurityBean cipher = KeyService.getSymmetricKeyByObjectId(data.getKeyId(), data.getOrganizationId());
					//KeyService.getPrimarySymmetricKey(owner);
			DataUtil.setCipher(data,cipher);
			outConfig = JAXBUtil.importObject(ApiClientConfigurationBean.class, new String(DataUtil.getValue(data),"UTF-8"));
			
		} catch (FactoryException e) {
			
			logger.error(e.getMessage());
			logger.error("Error",e);
		} catch (ArgumentException e) {
			
			logger.error("Error",e);
		} catch (UnsupportedEncodingException e) {
			
			logger.error("Error",e);
		} catch (DataException e) {
			
			logger.error("Error",e);
		}
		return outConfig;
	}
	public static ApiClientConfigurationBean addApiClientConfiguration(ApiServiceEnumType serviceType, String name, String serviceUrl, byte[] identity, byte[] credential,List<AttributeType> attributes, long organizationId){
		DataType newData = null;
		ApiClientConfigurationBean apiConfig = new ApiClientConfigurationBean();
		ApiClientConfigurationBean outConfig = null;
		DirectoryGroupType dir = null;
		if(serviceType == ApiServiceEnumType.UNKNOWN){
			logger.error("Invalid service type");
			return null;
		}
		String dataName = serviceType.toString() + " " + name;
		try {
			UserType owner = getApiUser(organizationId);
			dir = ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getCreateDirectory(owner, apiDirectoryName, ((GroupFactory)Factories.getFactory(FactoryEnumType.GROUP)).getUserDirectory(owner),owner.getOrganizationId());
			if(((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getDataByName(dataName, true, dir) != null){
				logger.error("API Config already exists");
				return null;
			}

			
			SecurityBean cipher = KeyService.newPersonalSymmetricKey(owner,false);
			newData = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).newData(owner, dir.getId());
			newData.setName(dataName);
			newData.setKeyId(cipher.getObjectId());
			//newData.setSecurityType(cipher);
			DataUtil.setCipher(newData,KeyService.promote(cipher));
			newData.setEncipher(true);
			newData.setMimeType("application/accountManagerApiConfiguration");
			
			apiConfig.setName(name);
			apiConfig.setServiceType(serviceType);
			apiConfig.setServiceUrl(serviceUrl);
			apiConfig.setDataUrn(UrnUtil.getUrn(newData));
			apiConfig.getAttributes().addAll(attributes);
			byte[] conf = JAXBUtil.exportObject(ApiClientConfigurationBean.class, apiConfig).getBytes("UTF-8");
			
			DataUtil.setValue(newData, conf);
			if(((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).add(newData) == true){
				newData = ((DataFactory)Factories.getFactory(FactoryEnumType.DATA)).getDataByName(dataName, true, dir);
				if(CredentialService.newCredential(CredentialEnumType.ENCRYPTED_IDENTITY, null, owner, newData, identity, true, true, false) != null
					&&
					CredentialService.newCredential(CredentialEnumType.ENCRYPTED_PASSWORD, null, owner, newData, credential, true, true, false) != null
				){
					logger.info("Created API Configuration " + name);
					outConfig = apiConfig;
				}
				else{
					logger.error("Failed to complete creating API configuration " + name);

				}
			}
		} catch (FactoryException e) {
			
			logger.error(e.getMessage());
			logger.error("Error",e);
		} catch (ArgumentException e) {
			
			logger.error(e.getMessage());
			logger.error("Error",e);
		} catch (UnsupportedEncodingException e) {
			
			logger.error("Error",e);
		} catch (DataException e) {
			
			logger.error("Error",e);
		}
		return outConfig;
	}
	
}
