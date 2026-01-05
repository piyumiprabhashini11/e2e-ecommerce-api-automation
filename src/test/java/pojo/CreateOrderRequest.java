package pojo;

import java.util.List;

public class CreateOrderRequest {
	
	private List<OrdersPojo> orders;

	public List<OrdersPojo> getOrders() {
		return orders;
	}

	public void setOrders(List<OrdersPojo> orders) {
		this.orders = orders;
	}

}
