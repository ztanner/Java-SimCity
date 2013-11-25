package restaurant;

import restaurant.gui.RestaurantGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Waiter;

public class NormalWaiterRole extends BaseWaiterRole implements Waiter{
	public NormalWaiterRole(RestaurantGui gui) {
		super();
		restGui = gui;
		waiterGui = new WaiterGui(this);
		restGui.getAnimationPanel().addGui(waiterGui);
		waiterGui.setPresent(false);
		
		currentCustomerNum = 0;
		menu.add(new FoodOnMenu("Steak", 15.99));
		menu.add(new FoodOnMenu("Chicken", 10.99));
		menu.add(new FoodOnMenu("Salad", 5.99));
		menu.add(new FoodOnMenu("Pizza", 8.99));
		state = agentState.WORKING;
	}
	
	public void goPlaceOrder(MyCustomer customer) {
		cook = host.getCook();
		print ("Here's an order for table " + customer.tableNumber);
		cook.msgHereIsAnOrder (customer.choice, this, customer.tableNumber);
		customer.state = customerState.waitingForFood;
	}
	
	public void done () {
		// gui needs to walk to exit
		waiterGui.setPresent(false);
		isActive = false;
		leaveWork = false;
		getPersonAgent().msgDone("RestaurantNormalWaiterRole");
	}
}
