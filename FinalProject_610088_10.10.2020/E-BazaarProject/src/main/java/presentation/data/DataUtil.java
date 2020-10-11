package presentation.data;

import business.BusinessConstants;
import business.SessionCache;
import business.externalinterfaces.CustomerSubsystem;

public class DataUtil {

	public static boolean custIsAdmin() {
		CustomerSubsystem cust = readCustFromCache();
		return (cust != null && cust.isAdmin());
	}

	public static boolean isLoggedIn() {
		return (Boolean) SessionCache.getInstance().get(BusinessConstants.LOGGED_IN);
	}

	public static CustomerSubsystem readCustFromCache() {
		return (CustomerSubsystem) SessionCache.getInstance().get(BusinessConstants.CUSTOMER);
	}
}
