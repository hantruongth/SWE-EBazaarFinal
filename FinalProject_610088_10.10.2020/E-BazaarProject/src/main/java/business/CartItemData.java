package business;

public class CartItemData {
	private String itemName;
	private int quantity;
	private double price;

	public CartItemData() {

	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getTotalPrice() {
		return quantity * price;

	}

	public String toString() {
		return itemName + ", " + quantity + ", " + price;
	}

}
