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
package org.cote.beans;

import javax.xml.bind.annotation.XmlRootElement;

import org.cote.accountmanager.objects.AccountType;
import org.cote.accountmanager.objects.AddressType;
import org.cote.accountmanager.objects.AttributeType;
import org.cote.accountmanager.objects.AuthenticationRequestType;
import org.cote.accountmanager.objects.AuthenticationResponseType;
import org.cote.accountmanager.objects.AuthorizationPolicyType;
import org.cote.accountmanager.objects.BaseGroupType;
import org.cote.accountmanager.objects.BasePermissionType;
import org.cote.accountmanager.objects.BaseRoleType;
import org.cote.accountmanager.objects.BaseTagType;
import org.cote.accountmanager.objects.ContactInformationType;
import org.cote.accountmanager.objects.ContactType;
import org.cote.accountmanager.objects.CredentialType;
import org.cote.accountmanager.objects.DataTagSearchRequest;
import org.cote.accountmanager.objects.DataType;
import org.cote.accountmanager.objects.DirectoryGroupType;
import org.cote.accountmanager.objects.FactType;
import org.cote.accountmanager.objects.FunctionFactType;
import org.cote.accountmanager.objects.FunctionType;
import org.cote.accountmanager.objects.OperationType;
import org.cote.accountmanager.objects.OrganizationType;
import org.cote.accountmanager.objects.PatternType;
import org.cote.accountmanager.objects.PersonType;
import org.cote.accountmanager.objects.PolicyDefinitionType;
import org.cote.accountmanager.objects.PolicyRequestType;
import org.cote.accountmanager.objects.PolicyResponseType;
import org.cote.accountmanager.objects.PolicyType;
import org.cote.accountmanager.objects.RuleType;
import org.cote.accountmanager.objects.UserGroupType;
import org.cote.accountmanager.objects.UserType;


@XmlRootElement(name="EntitySchema")
public class EntitySchema {
	private String defaultPackage = "org.cote.beans";
	private BaseRoleType baseRoleType = null;
	private MessageBean messageBeanSchema = null;
	private DataType dataTypeSchema = null;
	//private DirectoryBean directoryBeanSchema = null;
	private CryptoBean cryptoBeanSchema = null;
	private SessionBean sessionBean = null;
	private SessionDataBean sessionDataBean = null;
	private UserType userType = null;
	private DirectoryGroupType directoryGroupType = null;
	private BaseGroupType baseGroupType = null;
	private UserGroupType userGroupType = null;
	private ContactInformationType contactInformationType = null;
	private PersonType personType = null;
	private ContactType contactType = null;
	private AddressType addressType = null;
	private OrganizationType organizationType = null;
	private AttributeType attributeType = null;
	private CredentialType credentialType = null;
	private AuthenticationRequestType authenticationRequestType = null;
	private AuthenticationResponseType authenticationResponseType = null;
	private PolicyDefinitionType policyDefinitionType = null;
	private PolicyRequestType policyRequestType = null;
	private PolicyResponseType policyResponseType = null;
	private FactType factType = null;
	private FunctionType functionType = null;
	private FunctionFactType functionFactType = null;
	private PatternType patternType = null;
	private PolicyType policyType = null;
	private OperationType operationType = null;
	private RuleType ruleType = null;
	private BasePermissionType basePermissionType = null;
	private AuthorizationPolicyType authorizationPolicyType = null;
	private AccountType accountType = null;
	private BaseTagType tagType = null;
	private DataTagSearchRequest dataTagSearchRequest = null;
	public EntitySchema(){
		credentialType = new CredentialType();
		authenticationRequestType = new AuthenticationRequestType();
		authenticationResponseType = new AuthenticationResponseType();
		dataTagSearchRequest = new DataTagSearchRequest();
		tagType = new BaseTagType();
		authorizationPolicyType = new AuthorizationPolicyType();
		addressType = new AddressType();
		basePermissionType = new BasePermissionType();
		policyDefinitionType = new PolicyDefinitionType();
		policyRequestType = new PolicyRequestType();
		policyResponseType = new PolicyResponseType();
		factType = new FactType();
		functionType = new FunctionType();
		functionFactType = new FunctionFactType();
		patternType = new PatternType();
		policyType = new PolicyType();
		operationType = new OperationType();
		ruleType = new RuleType();
		attributeType = new AttributeType();
		attributeType.getValues();
		userGroupType = new UserGroupType();
		baseGroupType = new BaseGroupType();
		contactType = new ContactType();
		personType = new PersonType();
		organizationType = new OrganizationType();
		baseRoleType = new BaseRoleType();
		directoryGroupType = new DirectoryGroupType();
		messageBeanSchema = new MessageBean();
		dataTypeSchema = new DataType();
		//directoryBeanSchema = new DirectoryBean();
		cryptoBeanSchema = new CryptoBean();
		sessionBean = new SessionBean();
		sessionDataBean = new SessionDataBean();
		userType = new UserType();
		contactInformationType = new ContactInformationType();
		accountType = new AccountType();
	}

	

	public CredentialType getCredentialType() {
		return credentialType;
	}



	public void setCredentialType(CredentialType credentialType) {
		this.credentialType = credentialType;
	}



	public AuthenticationRequestType getAuthenticationRequestType() {
		return authenticationRequestType;
	}



	public void setAuthenticationRequestType(
			AuthenticationRequestType authenticationRequestType) {
		this.authenticationRequestType = authenticationRequestType;
	}



	public AuthenticationResponseType getAuthenticationResponseType() {
		return authenticationResponseType;
	}



	public void setAuthenticationResponseType(
			AuthenticationResponseType authenticationResponseType) {
		this.authenticationResponseType = authenticationResponseType;
	}



	public DataTagSearchRequest getDataTagSearchRequest() {
		return dataTagSearchRequest;
	}



	public void setDataTagSearchRequest(DataTagSearchRequest dataTagSearchRequest) {
		this.dataTagSearchRequest = dataTagSearchRequest;
	}



	public BaseTagType getTagType() {
		return tagType;
	}



	public void setTagType(BaseTagType tagType) {
		this.tagType = tagType;
	}



	public AuthorizationPolicyType getAuthorizationPolicyType() {
		return authorizationPolicyType;
	}



	public void setAuthorizationPolicyType(
			AuthorizationPolicyType authorizationPolicyType) {
		this.authorizationPolicyType = authorizationPolicyType;
	}



	public AddressType getAddressType() {
		return addressType;
	}



	public void setAddressType(AddressType addressType) {
		this.addressType = addressType;
	}



	public BasePermissionType getBasePermissionType() {
		return basePermissionType;
	}


	public void setBasePermissionType(BasePermissionType basePermissionType) {
		this.basePermissionType = basePermissionType;
	}


	public AccountType getAccountType() {
		return accountType;
	}


	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}


	public PolicyDefinitionType getPolicyDefinitionType() {
		return policyDefinitionType;
	}


	public void setPolicyDefinitionType(PolicyDefinitionType policyDefinitionType) {
		this.policyDefinitionType = policyDefinitionType;
	}


	public PolicyRequestType getPolicyRequestType() {
		return policyRequestType;
	}


	public void setPolicyRequestType(PolicyRequestType policyRequestType) {
		this.policyRequestType = policyRequestType;
	}


	public PolicyResponseType getPolicyResponseType() {
		return policyResponseType;
	}


	public void setPolicyResponseType(PolicyResponseType policyResponseType) {
		this.policyResponseType = policyResponseType;
	}


	public FactType getFactType() {
		return factType;
	}


	public void setFactType(FactType factType) {
		this.factType = factType;
	}


	public FunctionType getFunctionType() {
		return functionType;
	}


	public void setFunctionType(FunctionType functionType) {
		this.functionType = functionType;
	}


	public FunctionFactType getFunctionFactType() {
		return functionFactType;
	}


	public void setFunctionFactType(FunctionFactType functionFactType) {
		this.functionFactType = functionFactType;
	}


	public PatternType getPatternType() {
		return patternType;
	}


	public void setPatternType(PatternType patternType) {
		this.patternType = patternType;
	}


	public PolicyType getPolicyType() {
		return policyType;
	}


	public void setPolicyType(PolicyType policyType) {
		this.policyType = policyType;
	}


	public OperationType getOperationType() {
		return operationType;
	}


	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}


	public RuleType getRuleType() {
		return ruleType;
	}


	public void setRuleType(RuleType ruleType) {
		this.ruleType = ruleType;
	}


	public AttributeType getAttributeType() {
		return attributeType;
	}


	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}


	public BaseGroupType getBaseGroupType() {
		return baseGroupType;
	}


	public void setBaseGroupType(BaseGroupType baseGroupType) {
		this.baseGroupType = baseGroupType;
	}


	public UserGroupType getUserGroupType() {
		return userGroupType;
	}


	public void setUserGroupType(UserGroupType userGroupType) {
		this.userGroupType = userGroupType;
	}


	public PersonType getPersonType() {
		return personType;
	}


	public void setPersonType(PersonType personType) {
		this.personType = personType;
	}


	public ContactType getContactType() {
		return contactType;
	}


	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}


	public OrganizationType getOrganizationType() {
		return organizationType;
	}


	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}


	public BaseRoleType getBaseRoleType() {
		return baseRoleType;
	}


	public void setBaseRoleType(BaseRoleType baseRoleType) {
		this.baseRoleType = baseRoleType;
	}


	public String getDefaultPackage() {
		return defaultPackage;
	}

	public void setDefaultPackage(String defaultPackage) {
		this.defaultPackage = defaultPackage;
	}



	public DirectoryGroupType getDirectoryGroupType() {
		return directoryGroupType;
	}


	public void setDirectoryGroupType(DirectoryGroupType directoryGroupType) {
		this.directoryGroupType = directoryGroupType;
	}


	public SessionDataBean getSessionDataBean() {
		return sessionDataBean;
	}



	public void setSessionDataBean(SessionDataBean sessionDataBean) {
		this.sessionDataBean = sessionDataBean;
	}



	public ContactInformationType getContactInformationType() {
		return contactInformationType;
	}



	public void setContactInformationType(
			ContactInformationType contactInformationType) {
		this.contactInformationType = contactInformationType;
	}



	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public CryptoBean getCryptoBeanSchema() {
		return cryptoBeanSchema;
	}

	public void setCryptoBeanSchema(CryptoBean cryptoBeanSchema) {
		this.cryptoBeanSchema = cryptoBeanSchema;
	}

	public MessageBean getMessageBeanSchema() {
		return messageBeanSchema;
	}

	public void setMessageBeanSchema(MessageBean messageBeanSchema) {
		this.messageBeanSchema = messageBeanSchema;
	}

	public DataType getDataTypeSchema() {
		return dataTypeSchema;
	}

	public void setDataTypeSchema(DataType dataTypeSchema) {
		this.dataTypeSchema = dataTypeSchema;
	}
/*
	public DirectoryBean getDirectoryBeanSchema() {
		return directoryBeanSchema;
	}

	public void setDirectoryBeanSchema(DirectoryBean directoryBeanSchema) {
		this.directoryBeanSchema = directoryBeanSchema;
	}
*/
	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
