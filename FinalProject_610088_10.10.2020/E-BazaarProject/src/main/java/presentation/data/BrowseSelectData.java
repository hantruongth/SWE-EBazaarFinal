package presentation.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import business.BusinessConstants;
import business.CartItemData;
import business.SessionCache;
import business.Util;
import business.exceptions.BackendException;
import business.externalinterfaces.*;
import business.ordersubsystem.OrderImpl;
import business.ordersubsystem.OrderSubsystemFacade;
import business.productsubsystem.ProductSubsystemFacade;
import business.rulesubsystem.RulesSubsystemFacade;
import business.usecasecontrol.BrowseAndSelectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import presentation.control.BrowseSelectUIControl;
import presentation.gui.GuiConstants;

public enum BrowseSelectData {
	INSTANCE;

	// Fields that are maintained as user interacts with UI
	private CatalogPres selectedCatalog;
	private ProductPres selectedProduct;
	private CartItemPres selectedCartItem;

	public CatalogPres getSelectedCatalog() {
		return selectedCatalog;
	}

	public void setSelectedCatalog(CatalogPres selectedCatalog) {
		this.selectedCatalog = selectedCatalog;
	}

	public ProductPres getSelectedProduct() {
		return selectedProduct;
	}

	public Product getProductForProductName(String name) throws BackendException {
		return BrowseAndSelectController.INSTANCE.getProductForProductName(name);
	}

	public void setSelectedProduct(ProductPres selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public CartItemPres getSelectedCartItem() {
		return selectedCartItem;
	}

	public void setSelectedCartItem(CartItemPres selectedCartItem) {
		this.selectedCartItem = selectedCartItem;
	}

	// ShoppingCart model
	private ObservableList<CartItemPres> cartData;

	public ObservableList<CartItemPres> getCartData() {
		return cartData;
	}

	public CartItemPres cartItemPresFromData(String name, double unitPrice, int quantAvail) {
		CartItemData item = new CartItemData();
		item.setItemName(name);
		item.setPrice(unitPrice);
		item.setQuantity(quantAvail);
		CartItemPres cartPres = new CartItemPres();
		cartPres.setCartItem(item);
		return cartPres;
	}

	public void addToCart(CartItemPres cartPres) {
		ObservableList<CartItemPres> newCartItems = FXCollections.observableArrayList(cartPres);
		// Place the new item at the top of the list
		if (cartData != null) {
			newCartItems.addAll(cartData);
		}
		cartData = newCartItems;
		updateShoppingCart();
	}

	public boolean removeFromCart(ObservableList<CartItemPres> toBeRemoved) {
		if (cartData != null && toBeRemoved != null && !toBeRemoved.isEmpty()) {
			cartData.remove(toBeRemoved.get(0));
			updateShoppingCart();
			return true;
		}
		return false;
	}

	/** Sets the latest version of cartData to the ShoppingCartSubsystem */
	public void updateShoppingCart() {
		List<CartItem> theCartItems = Util.cartItemPresToCartItemList(cartData);
		BrowseAndSelectController.INSTANCE.updateShoppingCartItems(theCartItems);
	}

	/**
	 * Used to update cartData (in this class) when shopping cart subsystem is
	 * changed
	 */
	public void updateCartData() {
		List<CartItem> cartItems = new ArrayList<CartItem>();
		List<CartItem> newlist = BrowseAndSelectController.INSTANCE.getCartItems();
		if (newlist != null)
			cartItems = newlist;
		cartData = FXCollections.observableList(Util.cartItemsToCartItemPres(cartItems));
		BrowseSelectUIControl.INSTANCE.updateCartItems(cartData);
	}

	public int quantityAvailable(Product product) {
		return product.getQuantityAvail();
	}

	public List<CatalogPres> getCatalogList() throws BackendException {

		try {
			ProductSubsystem productSub = new ProductSubsystemFacade();
			List<Catalog> catalogs = productSub.getCatalogList();
			return Util.catalogListToCatalogPresList(catalogs);
		} catch (BackendException e) {
			e.printStackTrace();
			return null;
		}

	}

	public List<ProductPres> getProductList(CatalogPres selectedCatalog) throws BackendException {
		return BrowseAndSelectController.INSTANCE.getProducts(selectedCatalog.getCatalog()).stream()
				.map(prod -> Util.productToProductPres(prod)).collect(Collectors.toList());
	}

	public List<String> getProductDisplayValues(ProductPres productPres) {
		return Arrays.asList(productPres.nameProperty().get(), productPres.unitPriceProperty().get(),
				productPres.quantityAvailProperty().get(), productPres.descriptionProperty().get());
	}

	public List<String> getProductFieldNamesForDisplay() {
		return GuiConstants.DISPLAY_PRODUCT_FIELDS;
	}

	// Synchronizers
	private class ShoppingCartSynchronizer implements Synchronizer {
		@SuppressWarnings("rawtypes")
		@Override
		public void refresh(ObservableList list) {
			cartData = list;
		}
	}

	public ShoppingCartSynchronizer getShoppingCartSynchronizer() {
		return new ShoppingCartSynchronizer();
	}
}
