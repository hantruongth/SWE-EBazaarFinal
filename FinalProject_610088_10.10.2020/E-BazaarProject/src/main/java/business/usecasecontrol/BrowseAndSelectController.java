package business.usecasecontrol;

import java.util.List;

import business.RulesQuantity;
import business.exceptions.BackendException;
import business.exceptions.BusinessException;
import business.exceptions.RuleException;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.externalinterfaces.Rules;
import business.externalinterfaces.ShoppingCartSubsystem;
import business.productsubsystem.ProductSubsystemFacade;
import business.shoppingcartsubsystem.ShoppingCartSubsystemFacade;
import presentation.data.DataUtil;

public enum BrowseAndSelectController {
	INSTANCE;

	public void updateShoppingCartItems(List<CartItem> cartitems) {
		ShoppingCartSubsystemFacade.INSTANCE.updateShoppingCartItems(cartitems);
	}

	public List<CartItem> getCartItems() {
		return ShoppingCartSubsystemFacade.INSTANCE.getCartItems();
	}

	/**
	 * Makes saved cart live in subsystem and then returns the new list of cartitems
	 */
	public void retrieveSavedCart() {
		ShoppingCartSubsystem shopCartSS = ShoppingCartSubsystemFacade.INSTANCE;

		// Saved cart was retrieved during login
		shopCartSS.makeSavedCartLive();
	}

	public void runQuantityRules(Product product, String quantityRequested) throws RuleException, BusinessException {

		ProductSubsystem prodSS = new ProductSubsystemFacade();

		// find current quant avail since quantity may have changed
		// since product was first loaded into UI
		int currentQuantityAvail = prodSS.readQuantityAvailable(product);
		Rules transferObject = new RulesQuantity(currentQuantityAvail, quantityRequested);//
		transferObject.runRules();

	}

	public List<Catalog> getCatalogs() throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getCatalogList();
	}

	public List<Product> getProducts(Catalog catalog) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductList(catalog);
	}

	public Product getProductForProductName(String name) throws BackendException {
		ProductSubsystem pss = new ProductSubsystemFacade();
		return pss.getProductFromName(name);
	}

	/** Assume customer is logged in */
	public CustomerProfile getCustomerProfile() {
		CustomerSubsystem cust = DataUtil.readCustFromCache();
		return cust.getCustomerProfile();
	}
}
