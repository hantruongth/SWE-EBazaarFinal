package performancetests.rulesstubs;

import java.util.ArrayList;
import java.util.List;

import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.Address;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesSubsystem;
import business.rulesbeans.AddressBean;
import business.rulesubsystem.RulesSubsystemFacade;
import middleware.exceptions.MiddlewareException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

public class RulesAddress implements Rules {
	private final String MODULE_NAME = "rules-address";
	private final String RULES_FILE = "address-rules.clp";
	private final String DEFTEMPLATE_NAME = "address-template";
	private HashMap<String,DynamicBean> table;
	private DynamicBean bean;
	private AddressImpl updatedAddress;
	
	public RulesAddress(Address address){
		bean = new AddressBean(address);
		
		//don't call prepare data here -- we don't know 
		//what else might need to be done first
		//let the RulesSubsystem make that call
	}	
	
	
	///////////////implementation of interface
	public String getModuleName(){
		return MODULE_NAME;
	}
	public String getRulesFile() {
		return RULES_FILE;
	}
	public void prepareData() {
		table = new HashMap<String,DynamicBean>();		
		table.put(DEFTEMPLATE_NAME, bean);
		
	}
	public HashMap<String,DynamicBean> getTable(){
		return table;
	}
	
	public void runRules() throws BusinessException, RuleException {
		RulesSubsystem rules = new RulesSubsystemFacade();
		rules.runRules(this);		
	}
	/* expect a list of address values, in order
	 * street, city, state ,zip
	 */
	public void populateEntities(List<String> updates){
		updatedAddress = new AddressImpl(updates.get(0),
				updates.get(1),
				updates.get(2),
				updates.get(3), true, true);
		
	}
	
	public List<AddressImpl> getUpdates() {
		List<AddressImpl> retVal = new ArrayList<AddressImpl>();
		retVal.add(updatedAddress);
		return retVal;
	}
	


}
