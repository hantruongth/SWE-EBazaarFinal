
package business.usecasecontrol;

import java.util.List;

import presentation.data.OrderPres;
import presentation.data.ViewOrdersData;

public enum ViewOrdersController {
	INSTANCE;

	public List<OrderPres> getOrders() {
		return ViewOrdersData.INSTANCE.getOrders();
	}
}
