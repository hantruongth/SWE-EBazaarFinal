package business.shoppingcartsubsystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import business.externalinterfaces.Address;
import business.externalinterfaces.CartItem;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.ShoppingCart;

/** Assumes 0 tax, 0 shipment costs */
class ShoppingCartImpl implements ShoppingCart {
	private List<CartItem> cartItems;
	private Address shipAddress;
	private Address billAddress;
	private CreditCard creditCard;
	@SuppressWarnings("unused")
	private String cartId;
	private String totalShipmentCost;
	private String totalTaxAmount;

	public String getTotalShipmentCost() {
		return totalShipmentCost;
	}

	public void setTotalShipmentCost(String totalShipmentCost) {
		this.totalShipmentCost = totalShipmentCost;
	}

	ShoppingCartImpl(List<CartItem> cartItems) {
		totalShipmentCost = "0";
		totalTaxAmount = "0";
		this.cartItems = cartItems;
	}

	public String getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(String totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	ShoppingCartImpl() {
		cartItems = new ArrayList<CartItem>();
	}

	void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public boolean isEmpty() {
		return cartItems == null || cartItems.isEmpty();
	}

	void addItem(CartItemImpl item) {
		if (cartItems == null) {
			cartItems = new LinkedList<CartItem>();
		}
		cartItems.add(item);
	}

	void insertItem(int pos, CartItemImpl item) {
		if (cartItems == null || pos >= cartItems.size()) {
			addItem(item);

		} else {
			cartItems.add(pos, item);
		}
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public void setShipAddress(Address addr) {
		shipAddress = addr;
	}

	public void setBillAddress(Address addr) {
		billAddress = addr;
	}

	public void setPaymentInfo(CreditCard cc) {
		creditCard = cc;
	}

	public Address getShippingAddress() {
		return shipAddress;
	}

	public Address getBillingAddress() {
		return billAddress;
	}

	public CreditCard getPaymentInfo() {
		return creditCard;
	}

	public boolean deleteCartItem(int pos) {
		Object ob = cartItems.remove(pos);
		return (ob != null);
	}

	public boolean deleteCartItem(String name) {
		CartItem itemSought = null;
		for (CartItem item : cartItems) {
			if (item.getProductName().equals(name))
				itemSought = item;
		}
		Object ob = cartItems.remove(itemSought);
		return (ob != null);
	}

	public void clearCart() {
		cartItems.clear();
	}

	public double getTotalPrice() {
		double sum = 0.00;

		Iterator<CartItem> itr = cartItems.iterator();
		while (itr.hasNext()) {
			CartItem item = itr.next();
			sum += Double.parseDouble(item.getTotalprice());
		}
		return sum;
	}

	@Override
	public String getTotalAmountCharged() {
		return Double
				.toString(getTotalPrice() + Double.parseDouble(totalShipmentCost) + Double.parseDouble(totalTaxAmount));
	}

	@Override
	public String getCartId() {
		return cartId;
	}

}
