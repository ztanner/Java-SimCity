package people.test.mock;

import people.Role;
import restaurant.interfaces.BankTeller;

public class MockTeller extends Role implements BankTeller{

	public MockTeller(String name) {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void msgDeposit(int id, double money) {
		// TODO Auto-generated method stub
		//log.add(new LoggedEvent("received message from customer id " + id + ". Customer wants to deposit " + money));
		
	}

	@Override
	public void msgWithdraw(int id, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void msgCreateAccount(String name, double money) {
		// TODO Auto-generated method stub
		
	}

}