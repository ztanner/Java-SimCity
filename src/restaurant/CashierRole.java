package restaurant;

import restaurant.gui.RestaurantCashierGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

import java.util.*;
import java.util.concurrent.Semaphore;

import bank.interfaces.Teller;
import market.interfaces.MarketCashier;
import people.Role;


// Cashier keeps track of the balance of every customer that once comes to the restaurant
// Cashier computes checks when he gets the request from waiter.
// waiter needs to go to cashier to take the bill
// customer needs to go to cashier to pay

public class CashierRole extends Role implements Cashier {

	public EventLog log = new EventLog();
	private Map<String, Double> price =Collections.synchronizedMap(new HashMap<String, Double>());

	private List<MarketBill> marketBills = Collections.synchronizedList(new ArrayList<MarketBill>());

	private Map<Customer, Double> balance = Collections.synchronizedMap(new HashMap<Customer, Double>());
	public enum checkState {COMPUTED, SENT_TO_WAITER, BEING_PAID};

	public enum bankActivityState {NONE, ASKED_FOR_HELP, ASKED_DEPOSIT, ASKED_WITHDRAW, DONE}
	public bankActivityState bankState;

	public enum bankActivityEvent {NONE, READY_TO_HELP, LOAN_GIVEN, DEPOSIT_SUCCESSFUL, WITHDRAW_SUCCESSFUL}
	public bankActivityEvent bankEvent;

	RestaurantGui restGui;
	RestaurantCashierGui cashierGui;
	private Semaphore atExit = new Semaphore(0,true);
	private Semaphore atPosition = new Semaphore(0,true);
	
	private List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());

	private Host host;
	private Teller teller;

	private Boolean leaveWork;
	private Boolean turnActive;
	private Boolean deposit = false;
	private Boolean withdraw = false;

	private double min_working_capital = 1000.0;
	private double working_capital = 100000.0;
	private double bank_balance = 0.0;
	private int waiter_salary = 100;
	private int cook_salary = 100;
	private int  host_salary = 100;
	private int cashier_salary = 100;


	public class Check {
		Waiter waiter;
		Customer customer;
		Double due;
		Double amountPaid;
		private checkState state;

		public Check (Customer c, Waiter w, double balance) {
			waiter = w;
			customer = c;
			due = balance;
			amountPaid = 0.00;
			setState(checkState.COMPUTED);
		}

		public checkState getState() {
			return state;
		}

		public void setState(checkState state) {
			this.state = state;
		}
	}

	public class MarketBill {
		public MarketCashier marketCashier;
		Double amount;
		boolean itemsReceived;
		Map<String, Integer> itemsOrdered = new HashMap<String, Integer>();

		public MarketBill (MarketCashier _marketCashier , double a, Map<String, Integer> items) {
			marketCashier = _marketCashier;
			amount = a;
			itemsReceived = false;
			itemsOrdered = items;
		}

		//public String toString () {
		//	return market.getName() + " " + amount;
		//}

	}


	public CashierRole(RestaurantGui gui) {
		cashierGui = new RestaurantCashierGui(this);
		restGui = gui;
		restGui.getAnimationPanel().addGui(cashierGui);
		cashierGui.setPresent(false);
		price.put("Steak", 15.99);
		price.put("Chicken", 10.99);
		price.put("Salad", 5.99);
		price.put("Pizza", 8.99);

		min_working_capital = 1000;
		working_capital = 100000;
		bank_balance = 0.0;
		waiter_salary = cook_salary = host_salary = cashier_salary = 100;
		leaveWork = false;
		isActive = false;
		turnActive = false;
	}

	// messages

	public void msgAtExit() {
		print ("received msgIsAtExit from gui");
		atExit.release();
		getPersonAgent().CallstateChanged();
	}
	
	public void msgAtPosition() {
		print ("received msgAtPosition from gui");
		atPosition.release();
		getPersonAgent().CallstateChanged();
	}
	
	public void msgIsActive() {
		print ("received msgIsActive from gui");
		turnActive = true;
		isActive = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgIsInActive() {
		print ("received msgIsInActive from gui");
		leaveWork = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgHereIsWhatIsDue(MarketCashier marketCashier, double price, Map<String, Integer> items) {
		print("Received msgHereIsWhatIsDue from MarketCashier " + marketCashier.getName());
		log.add(new LoggedEvent("Received msgHereIsWhatIsDue from MarketCashier " + marketCashier.getName() + " with price " + price));
		marketBills.add(new MarketBill(marketCashier, price, items));
		getPersonAgent().CallstateChanged();
	}


	public void msgHereIsChange(double change) {
		print ("received msgHereIsChange from market cashier");
		working_capital += change;
		getPersonAgent().CallstateChanged();

	}

	public void msgHereIsBill (Customer c, String food, Waiter w) {
		print("received msgHereIsBill " + c.getName());
		log.add(new LoggedEvent("Received msgHereIsBill from Waiter " + w.getName() + " with Customer " + c.getName() + " and food " + food));
		synchronized(balance){
			if (balance.containsKey(c)){
				balance.put(c, balance.get(c)+price.get(food));
			}
			else {
				getBalance().put(c, price.get(food));
			}
		}
		checks.add(new Check (c, w, getBalance().get(c)));
		getPersonAgent().CallstateChanged();
	}

	public void msgPayMyCheck (Customer c, Double amount) {
		print ("received msgPayMyCheck from customer " + c.getName());
		log.add(new LoggedEvent("Received msgPayMyCheck from Customer " + c.getName() + " and the amount is " + amount));
		synchronized (checks){
			for (Check check : checks) {
				if (check.customer == c) {
					check.setState(checkState.BEING_PAID);
					check.amountPaid = amount;
				}
			}
		}
		working_capital+=amount;

		getPersonAgent().CallstateChanged();
	}

	public void msgGotMarketOrder(Map<String, Integer> marketOrder) {
		print("received msgGotMarketOrder from cook");
		synchronized(marketBills){
		for (MarketBill mb : marketBills) {
			if (mb.itemsOrdered == marketOrder) {
				mb.itemsReceived = true;
			}
		}
		}
		getPersonAgent().CallstateChanged();

	}

	// from BankTellerRole
	public void msgReadyToHelp(Teller teller) {
		print("received msgReadyToHelp from teller");
		bankEvent = bankActivityEvent.READY_TO_HELP;
		System.out.println("got msgreadytohelp from teller");

		getPersonAgent().CallstateChanged();
	}

	public void msgGiveLoan(double balance, double amount) {
		print("received loan successful from bank");
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		bank_balance -= balance;
		getPersonAgent().CallstateChanged();
	}

	public void msgWithdrawSuccessful(double funds, double amount){
		print("received mssWithDrawSuccessful from teller");
		bankEvent = bankActivityEvent.WITHDRAW_SUCCESSFUL;
		working_capital += amount;
		bank_balance -= funds;
		//withdrawalSuccessful = true;
		getPersonAgent().CallstateChanged();
	}

	public void msgDepositSuccessful(double funds){
		print("received msgDepositSuccessful from teller");
		bankEvent = bankActivityEvent.DEPOSIT_SUCCESSFUL;
		working_capital -= (funds - bank_balance);
		bank_balance = funds;
		//depositSuccessful = true;
		getPersonAgent().CallstateChanged();
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {


		if (turnActive) {
			clockIn();
			return true;
		}

		synchronized (checks) {
			for (Check check : checks) {
				if (check.getState() == checkState.COMPUTED){
					sendCheckToWaiter(check);
					return true;
				}
			}
		}

		synchronized (checks) {
			for (Check check : checks) {
				if (check.getState() == checkState.BEING_PAID){
					giveChangeToCustomer(check);
					return true;
				}
			}
		}
		
		if (!marketBills.isEmpty()) {
			synchronized (marketBills) {
			for (int i = 0; i < marketBills.size(); i++) {
				if (marketBills.get(i).itemsReceived = true)
					payMarket(marketBills.get(i));
				}
			}
		}


		
		if (!marketBills.isEmpty()) {
			synchronized (marketBills) {
			for (MarketBill mb : marketBills) {
				if (mb.itemsReceived = true)
					payMarket(mb);
			}
		return true;
		}
		}

		/*if (loanRequested && loanGranted) {
			payWorkers();
			closeRestaurant();
			return true;
		}
		if (loanRequested && loanRefused) {
			depositExcessMoney();
			closeRestaurant();	
			return true;
		}*/
		if (bankState == bankActivityState.ASKED_FOR_HELP && bankEvent == bankActivityEvent.READY_TO_HELP) {
			if (deposit) {
				depositExcessMoney();
				bankState = bankActivityState.ASKED_DEPOSIT;
				return true;
			}
			if (withdraw) {
				withdrawMoney();
				bankState = bankActivityState.ASKED_WITHDRAW;
				return true;
			}
		}


		if (bankState == bankActivityState.ASKED_DEPOSIT && bankEvent == bankActivityEvent.DEPOSIT_SUCCESSFUL) {
			bankState = bankActivityState.NONE;
			closeRestaurant();
			return true;
		}
		if (bankState == bankActivityState.ASKED_WITHDRAW && bankEvent == bankActivityEvent.WITHDRAW_SUCCESSFUL) {
			bankState = bankActivityState.NONE;
			payWorkers();
			closeRestaurant();
			return true;
		} 
		if (leaveWork) 	{
			prepareToClose();
			return true;
		}
		
		return false;
		
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}





	// Actions
	private void clockIn() {
		print("in action clockIn");
		host = (Host) getPersonAgent().getHost(0);
		teller = (Teller) getPersonAgent().getTeller(0);
		host.setCashier(this);
		cashierGui.setPresent(true);
		cashierGui.DoGoToWorkingPosition();
		try {
			atPosition.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("fhio");
		turnActive = false;
		deposit = false;
		withdraw = false;
		bankState = bankActivityState.NONE;
	}

	private void sendCheckToWaiter (final Check c) {
		print ("in action sendCheckToWaiter");
		log.add(new LoggedEvent("In action sendCheckToWaiter, ready to send Check to " + c.waiter.getName() + 
				". Customer " + c.customer.getName() + " needs to pay " + c.due));
		/*new java.util.Timer().schedule(
				new java.util.TimerTask(){
					public void run(){
						//print ("Here is the bill for " + c.customer);
						c.waiter.msgHereIsCheck (c.customer, c.due);
					}
				},
			    1000);*/
		c.waiter.msgHereIsCheck (c.customer, c.due);
		c.setState(checkState.SENT_TO_WAITER);
	}

	private void giveChangeToCustomer (Check c) {
		print("in action giveChangeToCustomer");
		log.add(new LoggedEvent("In action giveChangeToCustomer, ready to give change to "+ c.customer.getName()));
		if (c.due > c.amountPaid) {
			c.customer.msgHereIsYourChange(0.00);
			balance.put(c.customer, getBalance().get(c.customer)-c.amountPaid);
		}
		else if (c.due < c.amountPaid){
			c.customer.msgHereIsYourChange(c.amountPaid-c.due);
			working_capital-=c.amountPaid-c.due;
			balance.put(c.customer, 0.00);
		}
		else if (c.due == c.amountPaid) {
			getBalance().put(c.customer, 0.00);
			c.customer.msgHereIsYourChange(0.00);
		}
		getChecks().remove(c);
	}

	private void payMarket(MarketBill bill){
		print ("in action payMarket");
		//log.add(new LoggedEvent("In action payMarket, paying market " + bill.market.getName()));
		if (working_capital > bill.amount) {
			//print ("Paying " + bill.market.getName() + " "+ String.format("%.2f",bill.amount));
			bill.marketCashier.msgHereIsPayment(working_capital, bill.itemsOrdered, this);
			setMyMoney(0);

		}
		/*else {
			print ("Paying " + bill.market.getName()+" , not able to pay full bill");
			bill.market.msgPayMarketBill(working_capital,this);
			setMyMoney(0);

		}*/

		marketBills.remove(bill);


	}

	private void prepareToClose() {
		print ("in action prepareToClose, will be messaging teller msgNeedHelp");

		log.add(new LoggedEvent("In action prepareToClose"));
		leaveWork = false;
		double total = getTotalSalary() + min_working_capital;
		if (working_capital >= total) {
			payWorkers(); 	
			System.out.println("Depositing Money");
			teller.msgNeedHelp(this, "blah");
			bankState = bankActivityState.ASKED_FOR_HELP;	
			deposit = true;
		}

		else {
			teller.msgNeedHelp(this, "blah");
			System.out.println("Withdrawing Money");
			bankState = bankActivityState.ASKED_FOR_HELP;
			withdraw = true;
		}
		

	}

	private void payWorkers() {
		print("in action payWorkers, paying everybody");
		working_capital -= getTotalSalary();
		for (int i=0; i < host.getWaiters().size(); i++) {
			host.getWaiters().get(i).getPerson().setMoney( waiter_salary);
		}
		host.getCook().getPerson().setMoney(cook_salary);
		host.getPerson().setMoney(host_salary);
		this.getPersonAgent().setMoney( cashier_salary);
	}

	private void closeRestaurant() {
		print ("in action closeRestaurant, goint to the exit");
		log.add(new LoggedEvent("In action closeRestaurant"));
		teller.msgDoneAndLeaving();
		deposit = withdraw = false;
		isActive = false;
		cashierGui.DoLeaveWork();
		try {
			atExit.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leaveWork = false;
		cashierGui.setPresent(false);
		getPersonAgent().msgDone("RestaurantCashierRole");
		//DoCloseRestaurant(); //gui stuff
	}

	private double getTotalSalary() {
		print ("in action getTotalSalary, calculating total salary for workeres");
		return (host.getWaiters().size()) * waiter_salary + cook_salary + host_salary + cashier_salary;
	}

	private void depositExcessMoney() {
		print ("in action depositExcessMoney, will message teller deposit");
		double amount = working_capital - min_working_capital;
		System.out.println("deposit monely " + amount + " to bank");
		teller.msgDeposit(getPersonAgent().getRestaurant(0).bankAccountID, working_capital - min_working_capital);
		bankState = bankActivityState.ASKED_DEPOSIT;
	}

	private void withdrawMoney() {
		print ("in action withdrawMoney, will message teller withdraw");
		double amount = getTotalSalary() + min_working_capital - working_capital;
		System.out.println("withdraw monely " + amount + " to bank");
		teller.msgWithdraw(getPersonAgent().getRestaurant(0).bankAccountID,getTotalSalary() + min_working_capital - working_capital);
		bankState = bankActivityState.ASKED_WITHDRAW;
	}


	//utilities

	public String getMaitreDName() {
		return getPersonAgent().getName();
	}

	public List<Check> getChecks() {
		return checks;
	}


	public void setChecks(List<Check> checks) {
		this.checks = checks;
	}

	public List<MarketBill> getMarketBills() {
		return marketBills;
	}

	public Map<Customer, Double> getBalance() {
		return balance;
	}

	public double getBankBalance() {
		return bank_balance;
	}

	public void setBalance(Map<Customer, Double> balance) {
		this.balance = balance;
	}


	public double getMyMoney() {
		return working_capital;
	}


	public void setMyMoney(double myMoney) {
		this.working_capital = myMoney;
	}

	public Boolean isActive(){
		return isActive;
	}

	public void setTeller (Teller t) {
		teller = t;
	}

	public void setHost (HostRole h) {
		host = h;
	}

	public String getName() {
		return getPersonAgent().getName();
	}


}