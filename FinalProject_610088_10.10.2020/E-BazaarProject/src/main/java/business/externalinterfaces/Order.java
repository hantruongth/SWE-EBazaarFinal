
package business.externalinterfaces;

import java.time.LocalDate;
import java.util.List;

public interface Order {
	public List<OrderItem> getOrderItems();

	public LocalDate getOrderDate();

	public int getOrderId();

	public double getTotalPrice();

	public Address getShipAddress();

	public Address getBillAddress();

	public CreditCard getPaymentInfo();

	public void setOrderItems(List<OrderItem> orderItems);

	public void setOrderId(int orderId);

	public void setDate(LocalDate date);

	public void setShipAddress(Address add);

	public void setBillAddress(Address add);

	public void setPaymentInfo(CreditCard cc);

}
