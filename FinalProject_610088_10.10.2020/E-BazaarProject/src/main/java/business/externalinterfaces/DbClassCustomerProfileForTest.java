
package business.externalinterfaces;

import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassCustomerProfileForTest extends DbClass {

	public CustomerProfile getCustomerProfileForTest();

	public void readCustomerProfile(Integer custId) throws DatabaseException;

}
