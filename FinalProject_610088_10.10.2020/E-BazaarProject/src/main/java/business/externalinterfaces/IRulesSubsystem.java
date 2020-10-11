package business.externalinterfaces;

import business.exceptions.*;
import middleware.EBazaarException;

public interface IRulesSubsystem {
	public void runRules(IRules rulesIface) throws EBazaarException, RuleException;

}
