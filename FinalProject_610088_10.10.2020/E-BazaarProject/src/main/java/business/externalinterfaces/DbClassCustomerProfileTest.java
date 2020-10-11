package business.externalinterfaces;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassCustomerProfileTest extends DbClass {

	public void readCustomerProfile(Integer custId) throws DatabaseException;

	public CustomerProfile getProfileForTest();

}