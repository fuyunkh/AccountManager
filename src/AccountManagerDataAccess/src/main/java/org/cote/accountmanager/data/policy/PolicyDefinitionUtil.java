package org.cote.accountmanager.data.policy;

import java.util.List;

import org.apache.log4j.Logger;
import org.cote.accountmanager.data.ArgumentException;
import org.cote.accountmanager.data.Factories;
import org.cote.accountmanager.data.FactoryException;
import org.cote.accountmanager.objects.FactEnumType;
import org.cote.accountmanager.objects.FactType;
import org.cote.accountmanager.objects.OperationType;
import org.cote.accountmanager.objects.PatternType;
import org.cote.accountmanager.objects.PolicyDefinitionType;
import org.cote.accountmanager.objects.PolicyRequestEnumType;
import org.cote.accountmanager.objects.PolicyRequestType;
import org.cote.accountmanager.objects.PolicyType;
import org.cote.accountmanager.objects.RuleType;
import org.cote.accountmanager.objects.types.FactoryEnumType;


public class PolicyDefinitionUtil {
	public static final Logger logger = Logger.getLogger(PolicyDefinitionUtil.class.getName());
	
	public static PolicyRequestType generatePolicyRequest(PolicyDefinitionType pdt){
		PolicyRequestType prt = new PolicyRequestType();
		prt.setRequestType(PolicyRequestEnumType.DECIDE);
		prt.setOrganizationPath(pdt.getOrganizationPath());
		prt.setUrn(pdt.getUrn());
		for(int i = 0; i < pdt.getParameters().size();i++){
			FactType parm = pdt.getParameters().get(i);
			FactType fact = new FactType();
			fact.setFactoryType(parm.getFactoryType());
			fact.setUrn(parm.getUrn());
			fact.setName(parm.getName());
			fact.setFactType(parm.getFactType());
			fact.setFactData(parm.getFactData());
			//fact.setGroup(parm.getGroup());
			//fact.s
			fact.setNameType(parm.getNameType());
			fact.setObjectId(parm.getObjectId());
			fact.setLogicalOrder(parm.getLogicalOrder());
			fact.setParameter(parm.getParameter());
			fact.setSourceDataType(parm.getSourceDataType());
			fact.setSourceType(parm.getSourceType());
			fact.setSourceUrl(parm.getSourceUrl());
			fact.setSourceUrn(parm.getSourceUrn());
			prt.getFacts().add(fact);
		}
		return prt;
	/*	
	
		createPolicyRequest : function(d, s){
		if(!d){
			Hemi.logError("Missing policy definition");
			return;
		}
		var r = new org.cote.beans.policyRequestType();
		r.urn = d.urn;
		r.requestType = (s ? s : "DECIDE");
		r.organizationPath = accountManager.getOrganizationPath();
		r.facts = [];
		if(d.parameters && d.parameters.length){
			for(var i = 0; i < d.parameters.length;i++){
				var f = new org.cote.beans.factType();
				for(var v in d.parameters[i]) f[v] = d.parameters[i][v];
				r.facts.push(f);
			}
		}
		return r;
	*/
	
	}
	public static PolicyDefinitionType generatePolicyDefinition(PolicyType pol) throws FactoryException, ArgumentException{
		PolicyDefinitionType pdt = new PolicyDefinitionType();
		pdt.setCreated(pol.getCreated());
		pdt.setDecisionAge(pol.getDecisionAge());
		pdt.setEnabled(pol.getEnabled());
		pdt.setExpires(pol.getExpires());
		pdt.setModified(pol.getModified());
		pdt.setUrn(pol.getUrn());
		pdt.setOrganizationPath(Factories.getOrganizationFactory().getOrganizationPath(pol.getOrganization()));
		copyParameters(pdt,pol);
		return pdt;
	}
	private static void copyParameters(PolicyDefinitionType pdt, PolicyType pol) throws FactoryException, ArgumentException{
		Factories.getPolicyFactory().populate(pol);
		logger.info("Processing " + pol.getRules().size() + " rules");
		for(int i = 0;i < pol.getRules().size();i++){
			copyParameters(pdt,pol.getRules().get(i));
		}
		
	}
	private static void copyParameters(PolicyDefinitionType pdt, RuleType rule) throws FactoryException, ArgumentException{
		Factories.getRuleFactory().populate(rule);
		logger.info("Processing " + rule.getPatterns().size() + " patterns");
		for(int i = 0; i < rule.getPatterns().size();i++){
			copyParameters(pdt,rule.getPatterns().get(i));
		}
		logger.info("Processing " + rule.getRules().size() + " child rules");
		for(int i = 0; i < rule.getRules().size();i++){
			copyParameters(pdt,rule.getRules().get(i));
		}

	}
	private static boolean haveParameter(PolicyDefinitionType pdt, FactType fact){
		boolean out_bool = false;
		for(int i = 0; i < pdt.getParameters().size();i++){
			if(pdt.getParameters().get(i).getUrn().equals(fact.getUrn())){
				out_bool = true;
				break;
			}
		}
		return out_bool;
	}
	private static void copyParameters(PolicyDefinitionType pdt, PatternType pattern) throws FactoryException, ArgumentException{
		Factories.getPatternFactory().populate(pattern);
		if(pattern.getFact() != null && pattern.getFact().getFactType() == FactEnumType.PARAMETER){
			logger.info("Processing Parameter");
			if(haveParameter(pdt,pattern.getFact())){
				logger.info("Skipping duplicate parameter");
				return;
			}
			logger.info(pdt.getUrn() + " Parameter " + pattern.getFactUrn());
			FactType parmFact = new FactType();
			parmFact.setUrn(pattern.getFactUrn());
			parmFact.setFactoryType(pattern.getFact().getFactoryType());
			parmFact.setFactType(pattern.getFact().getFactType());
			parmFact.setSourceDataType(pattern.getFact().getSourceDataType());
			parmFact.setSourceUrn(pattern.getFact().getSourceUrn());
			parmFact.setSourceUrl(pattern.getFact().getSourceUrl());
			logger.info("Defining Parameter " + parmFact.getUrn());
			pdt.getParameters().add(parmFact);
		}
		else{
			logger.info("SKIP " + pdt.getUrn() + " Fact " + pattern.getFactUrn());
		}
		
	}
	public static String printPolicy(PolicyType pol) throws FactoryException, ArgumentException{
		StringBuffer buff = new StringBuffer();
		Factories.getPolicyFactory().populate(pol);
		buff.append("\nPOLICY " + pol.getName()+ "\n");
		buff.append("\turn\t" + pol.getUrn()+ "\n");
		buff.append("\tenabled\t" + pol.getEnabled()+ "\n");
		buff.append("\tcreated\t" + pol.getCreated().toString()+ "\n");
		buff.append("\texpires\t" + pol.getExpires().toString()+ "\n");
		List<RuleType> rules = pol.getRules();
		for(int i = 0; i < rules.size();i++){
			RuleType rule = rules.get(i);
			Factories.getRuleFactory().populate(rule);
			buff.append("\tRULE " + rule.getName()+ "\n");
			buff.append("\t\turn\t" + rule.getUrn()+ "\n");
			buff.append("\t\ttype\t" + rule.getRuleType()+ "\n");
			buff.append("\t\tcondition\t" + rule.getCondition()+ "\n");
			buff.append("\t\torder\t" + rule.getLogicalOrder()+ "\n");
			List<PatternType> patterns = rule.getPatterns();
			for(int p = 0; p < patterns.size();p++){
				PatternType pattern = patterns.get(p);
				Factories.getPatternFactory().populate(pattern);
				buff.append("\t\tPATTERN " + pattern.getName()+ "\n");
				buff.append("\t\t\turn\t" + pattern.getUrn()+ "\n");
				buff.append("\t\t\ttype\t" + pattern.getPatternType()+ "\n");
				buff.append("\t\t\torder\t" + pattern.getLogicalOrder()+ "\n");
				if(pattern.getOperationUrn() != null) buff.append("\t\t\toperation\t" + pattern.getOperationUrn()+ "\n");
				FactType srcFact = pattern.getFact();
				FactType mFact = pattern.getMatch();
				buff.append("\t\t\tSOURCE FACT " + (srcFact != null ? srcFact.getName() : "IS NULL")+ "\n");
				if(srcFact != null){
					buff.append("\t\t\t\turn\t" + srcFact.getUrn()+ "\n");
					buff.append("\t\t\t\ttype\t" + srcFact.getFactType()+ "\n");
					buff.append("\t\t\t\tfactoryType\t" + srcFact.getFactoryType()+ "\n");
					buff.append("\t\t\t\tsourceUrl\t" + srcFact.getSourceUrl()+ "\n");
					buff.append("\t\t\t\tsourceUrn\t" + srcFact.getSourceUrn()+ "\n");
					buff.append("\t\t\t\tsourceType\t" + srcFact.getSourceType()+ "\n");
					buff.append("\t\t\t\tsourceDataType\t" + srcFact.getSourceDataType().toString()+ "\n");
					buff.append("\t\t\t\tfactData\t" + srcFact.getFactData()+ "\n");
				}
				buff.append("\t\t\tCOMPARATOR " + pattern.getComparator()+ "\n");
				buff.append("\t\t\tMATCH FACT " + (mFact != null ? mFact.getName() : "IS NULL")+ "\n");
				if(mFact != null){
					buff.append("\t\t\t\turn\t" + mFact.getUrn()+ "\n");
					buff.append("\t\t\t\ttype\t" + mFact.getFactType()+ "\n");
					buff.append("\t\t\t\tfactoryType\t" + mFact.getFactoryType()+ "\n");
					buff.append("\t\t\t\tsourceUrl\t" + mFact.getSourceUrl()+ "\n");
					buff.append("\t\t\t\tsourceUrn\t" + mFact.getSourceUrn()+ "\n");
					buff.append("\t\t\t\tsourceType\t" + mFact.getSourceType()+ "\n");
					buff.append("\t\t\t\tsourceDataType\t" + mFact.getSourceDataType().toString()+ "\n");
					buff.append("\t\t\t\tfactData\t" + mFact.getFactData()+ "\n");
					if(mFact.getFactType() == FactEnumType.OPERATION){
						buff.append("\t\t\t\tOPERATION\t" + (mFact.getSourceUrl() != null ? mFact.getSourceUrl() : "IS NULL")+ "\n");
						if(mFact.getSourceUrl() != null){
							OperationType op = Factories.getOperationFactory().getByUrn(mFact.getSourceUrl(), mFact.getOrganization());
							buff.append("\t\t\t\turn\t" + op.getUrn()+ "\n");
							buff.append("\t\t\t\toperationType\t" + op.getOperationType()+ "\n");
							buff.append("\t\t\t\toperation\t" + op.getOperation()+ "\n");
						}
						
					}
				}

			}
			
		}
		return buff.toString();
	}
}