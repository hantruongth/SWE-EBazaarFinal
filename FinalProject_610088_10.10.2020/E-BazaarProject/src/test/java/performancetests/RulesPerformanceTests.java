package performancetests;



import java.util.logging.Logger;

import junit.framework.TestCase;
import performancetests.rulesstubs.AddressImpl;
import performancetests.rulesstubs.RulesAddress;
import alltests.AllTests;
import business.externalinterfaces.Address;
import business.externalinterfaces.DynamicBean;
import business.externalinterfaces.Rules;
import business.externalinterfaces.RulesSubsystem;
import business.rulesubsystem.RulesSubsystemFacade;


public class RulesPerformanceTests extends TestCase {

	static String name = "business.RulesSubsystemFacade";
	static Logger log = Logger.getLogger(RulesPerformanceTests.class.getName());
	
	static {
		AllTests.initializeProperties();
	}

	
	public RulesPerformanceTests(String arg0) {
		super(arg0);
	}
	
	public void setUp() {
		
	}
	RulesSubsystem rules; 
	Rules rulesAddress;
	DynamicBean bean;
	Address addr;
	int i = 0;
	public void testAddressRulesRepeatedly(){
		final int NUM_TRIALS = 10;
		final int EXPECTED_RUNNING_TIME = 140;
		long[] results = new long[NUM_TRIALS];
		long start = 0L;
		long finish = 0L;
		for(i = 1; i < NUM_TRIALS; ++i){
			addressRulesSetup(i);
			start = System.currentTimeMillis();
			try {
				rules.runRules(rulesAddress);
			}
			catch(Exception e) {
				log.info(e.getMessage());
				//fail("Rules engine encountered an exception.");
			}
			
			finish = System.currentTimeMillis();
			if(i>0){
				results[i]= finish-start;
			}
		}
		long accum = 0L;
		//count time elapsed starting at j=1 
		//because the 0th trial measures startup time too
		String output = "[";
		for(int j = 0; j < NUM_TRIALS; ++j){
			accum += results[j];
			output += (results[j]+", ");
		}
		output = output.substring(0,output.length()-2)+"]";
		
		long average = accum / (NUM_TRIALS - 1);
		log.info(output);
		log.info("average: "+average);
		assertTrue(average < EXPECTED_RUNNING_TIME);
		
	}

	

	public void addressRulesSetup(int i){		
		String[] addrFields = {"10"+i+" N. 6th","Fairfield","IA","5255"+i};
		addr = new AddressImpl(addrFields);
		
		rulesAddress = new RulesAddress(addr);
		rules = new RulesSubsystemFacade();
		
		
	}
	
	
}
