package restaurant;

import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

import people.Role;
/**
 * Restaurant Host Agent
 * @param <MyCustomer>
 */
// waiter gets the message from host and do all the rest job to serve the customer.

// waiter can go on break if the gui button is pushed (as long as there are more than one waiter available)
// that's why customer must negotiate with host and host might deny his request.
public abstract class BaseWaiterRole extends Role implements Waiter {

	protected List<MyCustomer> customers = new ArrayList<MyCustomer>();
	protected List<FoodOnMenu> menu = new ArrayList<FoodOnMenu>();

	protected String name;
	protected HostRole host;
	protected CookRole cook;
	private Cashier cashier;
	//private Boolean inProgress = false; // true if WaiterAgent is in the middle of animation
	protected Boolean onBreak = false; 
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atWaitingCustomer = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	protected int currentCustomerNum;

	private Boolean isActive = false;
	
	protected enum customerState {waiting, seated, readyToOrder, askedToOrder, ordered, 
		waitingForFood, outOfChoice, foodIsReady, checkIsReady, needsToPay, eating, doneLeaving};
	protected enum agentState {WORKING, ASKING_FOR_BREAK, ON_BREAK};
	protected agentState state;
	protected CookWaiterMonitor theMonitor;
	
	public class FoodOnMenu {
		String type;
		Double price;
		
		public FoodOnMenu (String t, Double p) {
			type = t;
			price = p;
		}
	}
	
	
	public WaiterGui waiterGui = null;

	/*public BaseWaiterRole(String name, CookWaiterMonitor monitor) {
		super();
		this.name = name;
		currentCustomerNum = 0;
		menu.add(new FoodOnMenu("Steak", 15.99));
		menu.add(new FoodOnMenu("Chicken", 10.99));
		menu.add(new FoodOnMenu("Salad", 5.99));
		menu.add(new FoodOnMenu("Pizza", 8.99));
		state = agentState.WORKING;
		theMonitor = monitor;
	}

	public BaseWaiterRole() {
		super();
	}*/

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List<MyCustomer> getCustomers() {
		return customers;
	}

	public int getCustomerNum() {
		return currentCustomerNum;
	}
	
	// messages
	
	//public void goOnBreak() { // from restPanel
	//	
	//}
	
	public void msgIsActive() {
		isActive = true;
	}
	
	public void msgIsInActive () {
		isActive = false;
	}
	
	public void msgAtTable() {//from animation
		atTable.release();// = true;
		getPersonAgent().CallstateChanged();

	}
	
	public void msgAtCook() {
		atCook.release();
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtCashier() {
		atCashier.release();
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtWaitingCustomer() {
		atWaitingCustomer.release();
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAskForBreak() { //from gui onBreak button
		state = agentState.ASKING_FOR_BREAK;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgOffBreak() { // from gui
		state = agentState.WORKING;
		onBreak = false;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgBreakApproved() { // from host Agent
		state = agentState.ON_BREAK;
		onBreak = true;
		getPersonAgent().CallstateChanged();
	}
	
	public void msgBreakDenied() { // from host agent
		state = agentState.WORKING;
		onBreak = false;
		getPersonAgent().CallstateChanged();
	}
	
	public void SitAtTable(Customer customer, int table) {
		currentCustomerNum++;
		customers.add(new MyCustomer(customer, table, "waiting"));
		getPersonAgent().CallstateChanged();
	}
	

	public void msgIAmReadyToOrder(Customer cust) {
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.readyToOrder;
			}
		}
		getPersonAgent().CallstateChanged();
	}

	public void msgHereIsMyOrder (Customer cust, String choice) {
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.ordered;
				customer.choice = choice;
			}
		}
		getPersonAgent().CallstateChanged();

	}
	
	public void msgOrderIsReady (String order, int t) {
		for (MyCustomer customer : customers) {
			if (customer.tableNumber == t){
				customer.state = customerState.foodIsReady;
			}
		}
		getPersonAgent().CallstateChanged();

	}
	
	public void msgOutOfFood (String order, int t) {
		for (MyCustomer customer : customers) {
			if (customer.tableNumber == t){
				customer.state = customerState.outOfChoice;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgHereIsCheck (Customer customer2, Double d) {
		for (MyCustomer customer : customers) {
			if (customer.c == customer2){
				customer.due = d;
				customer.state = customerState.checkIsReady;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	public void msgDoneEatingAndLeaving (Customer cust){
		for (MyCustomer customer : customers) {
			if (customer.c == cust){
				customer.state = customerState.doneLeaving;
			}
		}
		getPersonAgent().CallstateChanged();
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try{
	
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.waiting){
					seatCustomer(customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.readyToOrder){
					goTakeOrder(customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.outOfChoice){
					goTellCustomerToReorder (customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {	
				if (customer.state == customerState.foodIsReady){
					serveFoodToCustomer (customer);
					return true;
				}
			}

			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.ordered){
					goPlaceOrder(customer);
					return true;
				}
			}
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.checkIsReady){
					giveCheckToCustomer(customer);
					return true;
				}
			} 
			
			for (MyCustomer customer : customers) {
				if (customer.state == customerState.doneLeaving){
					UpdateTableInfo(customer.c);
					customers.remove(customer);
					return true;
				}
			}
		}catch (ConcurrentModificationException e) {return false;}
			
			
			//if (state == agentState.ON_BREAK) {
			//	updateGuiButton();
			//	return true;
			//}
	
			waiterGui.DoGoRest();	
			return false;
			
		
	}

	
	
	// Actions

	private void seatCustomer(MyCustomer customer) {
		print("Approaching waiting customer "+customer.c.getName());
		waiterGui.DoGoToWaitingCustomer();
		try {
			atWaitingCustomer.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.c.msgFollowMeToTable(this, customer.tableNumber, menu);
		waiterGui.DoSeatCustomer(customer.c, customer.tableNumber);
		print("Seating " + customer.c + " at table " + customer.tableNumber);
		try {
			atTable.acquire();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		customer.state = customerState.seated;


	}

	public void goTakeOrder (MyCustomer customer) {
		waiterGui.DoApproachCustomer(customer.c);
		print ("Approaching " + customer.c + " to take order.");
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		print ("What would you like?");
		customer.c.msgWhatWouldYouLike();
		//waiterGui.DoAskCustomer();
		customer.state = customerState.askedToOrder;	
	} 

	public abstract void goPlaceOrder(MyCustomer customer);
	
	public void goTellCustomerToReorder (MyCustomer customer) {
		waiterGui.DoApproachCustomer(customer.c);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		print ("Sorry we are running out of " + customer.choice + ". Please reorder");
		//generates a new menu for the customer
		List<FoodOnMenu> newMenu = new ArrayList<FoodOnMenu>();
		FoodOnMenu f = new FoodOnMenu(" ",0.00);
		newMenu.add(new FoodOnMenu("Steak", 15.99));
		newMenu.add(new FoodOnMenu("Chicken", 10.99));
		newMenu.add(new FoodOnMenu("Salad", 5.99));
		newMenu.add(new FoodOnMenu("Pizza", 8.99));
		for (FoodOnMenu temp : newMenu) {
			if (temp.type.equals(customer.choice)){
				f = temp;
			}
		}
		newMenu.remove(f);
		customer.c.msgReorder(newMenu);
		customer.state = customerState.askedToOrder;
	}
	
	public void serveFoodToCustomer(MyCustomer customer){
		atCook.drainPermits();
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		cook.getGui().foodPickedUp(customer.tableNumber);
		waiterGui.DoBringFoodToCustomer(customer.c);
		print ("Bringing food to table " + customer.tableNumber);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
		print ("Here's your food. Enjoy!");
		customer.c.msgHereIsYourFood();
		print ("Can you take care of the bill for table " + customer.tableNumber);
		cashier.msgHereIsBill(customer.c, customer.choice, this);
		customer.state = customerState.eating;
	}
	
	public void giveCheckToCustomer(MyCustomer customer) {
		
		waiterGui.DoGoToCashier();
		try {
			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		waiterGui.DoApproachCustomer(customer.c);
		try {
			atTable.drainPermits();
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		print ("Hi " + customer.c + " here is your check.");
		customer.c.msgHereIsCheck(customer.due, cashier);
		customer.state = customerState.needsToPay;
	}
	
	public void UpdateTableInfo(Customer c) {
		currentCustomerNum--;
		host.msgTableIsFree(((RestaurantCustomerRole) c).getTableNumber());
	}

	//utilities

	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	public void setHost (HostRole h) {
		host = h;
	}
	
	public void setCook (CookRole k) {
		cook = k;
	}
	
	public void setCashier (Cashier c) {
		cashier = c;
	}
	
	public List<MyCustomer> getMyCustomer(){
		return customers;
	}
	
	public HostRole getHost () {
		return host;
	}

	public Boolean isOnBreak() {
		return onBreak;
	}
	
	public class MyCustomer{
		protected Customer c;
		protected int tableNumber;
		protected String choice;
		customerState state;
		Double due;
		
		
		public MyCustomer(Customer customer, int tableNum, String s){

			c = customer;
			tableNumber = tableNum;
			state = customerState.waiting;
			stateChanged();
		}

		public Customer getCustomerAgent() {
			return this.c;
		}	
	}

	public Boolean isActive() {
		return isActive;
	}
	
}