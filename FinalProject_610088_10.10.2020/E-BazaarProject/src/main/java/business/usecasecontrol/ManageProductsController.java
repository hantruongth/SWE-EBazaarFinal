
package business.usecasecontrol;

import java.util.List;
import java.util.logging.Logger;

import business.exceptions.BackendException;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import middleware.exceptions.DatabaseException;

public class ManageProductsController {

	private static final Logger LOG = Logger.getLogger(ManageProductsController.class.getName());

	public List<Product> getProductsList(Catalog catalog) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductList(catalog);
	}

	public List<Catalog> getCatalogList() throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getCatalogList();
	}

	public void deleteProduct() {
		// implement
	}

}
