package daotests;

import alltests.AllTests;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassShoppingCartForTest;
import business.externalinterfaces.ShoppingCart;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import customersubsystem.CustomerSubsystemFacade;
import dbsetup.DbQueries;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;

public class DbClassShoppingCartTest extends TestCase {

    static String name = "DbClassShoppingCart Test";

    static {
        AllTests.initializeProperties();
    }

    /**
     * Test of retrieveSavedCart method, of class DbClassShoppingCart.
     */
    public void testRetrieveSavedCart() throws Exception {
        //Insert one customer to Database
        String[] customer = DbQueries.insertCustomerRow();
        Integer custId = Integer.parseInt(customer[1]);

        // Add one row of cart to database for testing
        String[] row = DbQueries.insertCartRow(custId, 50);

        String expectedRowId = row[1];
        double expectedRowTotal = Double.parseDouble(row[3]);

        // Perform test
        ShoppingCart cart = null;
        try {
            CustomerSubsystem css = new CustomerSubsystemFacade();
            css.initializeCustomer(custId, 0);

            DbClassShoppingCartForTest dbClass =ShoppingCartSubsystemFacade.INSTANCE.getGenericDbClassShoppingCart();
            cart = dbClass.retrieveSavedCart(css.getCustomerProfile());
        } catch (BackendException ex) {
            fail("DatabaseException: " + ex.getMessage());
        } finally {
            assertTrue(cart != null);
            boolean cartRowFound = false;
            if (cart != null) {
                if (cart.getCartId().equals(expectedRowId)) {
                    cartRowFound = true;
                }
            }
            System.out.println(expectedRowId+"::"+expectedRowTotal);
            assertTrue(cartRowFound);
        }

        // Delete Customer and Cart from Database after testing
        DbQueries.deleteCartRow(row[1]);
        DbQueries.deleteCustomerRow(custId);
    }

}
