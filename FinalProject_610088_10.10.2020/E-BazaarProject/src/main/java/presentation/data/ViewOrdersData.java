package presentation.data;

import java.util.ArrayList;
import java.util.List;

import business.BusinessConstants;
import business.SessionCache;
import business.Util;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderImpl;
import business.ordersubsystem.OrderSubsystemFacade;

public enum ViewOrdersData {
	INSTANCE;

	private OrderPres selectedOrder;

	public OrderPres getSelectedOrder() {
		return selectedOrder;
	}

	public void setSelectedOrder(OrderPres so) {
		selectedOrder = so;
	}

	public List<OrderPres> getOrders() {
		try {
			SessionCache cache = SessionCache.getInstance();
			CustomerSubsystem customerSub = (CustomerSubsystem) cache.get(BusinessConstants.CUSTOMER);
			OrderSubsystem orderSub = new OrderSubsystemFacade(customerSub.getCustomerProfile());
//			List<Order> orders = customerSub.getOrderHistory();
			List<Order> orders = orderSub.getOrderHistory();
			/*
			 * List<OrderPres> orderPresList = new ArrayList<OrderPres>(); for (int i = 0 ;
			 * i <orders.size(); i++) { OrderPres orderPres = new OrderPres();
			 * orderPres.setOrder((OrderImpl)orders.get(i)); orderPresList.add(orderPres); }
			 */
			return Util.orderListToOrderPresList(orders);
		} catch (BackendException e) {
			e.printStackTrace();
			return null;
		}
	}
}
