/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package business.externalinterfaces;

import business.exceptions.BackendException;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DbClass;

public interface DbClassShoppingCartForTest extends DbClass {
	public ShoppingCart retrieveSavedCart(CustomerProfile custProfile) throws BackendException, DatabaseException;
}
