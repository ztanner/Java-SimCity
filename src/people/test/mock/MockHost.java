package people.test.mock;

import java.util.List;

import people.People;
import people.Role;
import restaurant.BaseWaiterRole;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiter;

public class MockHost extends Role implements Host {

		public MockHost(String name) {
			super();	
		}

		@Override
		public void setCashier(Cashier cashierRole) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Cook getCook() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public People getPerson() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Cashier getCashier() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void msgTableIsFree(int tableNumber) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void IWantABreak(Waiter w) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<Waiter> getAvailableWaiters() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void IAmOffBreak(Waiter w) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void addWaiter(Waiter Waiter) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setCook(Cook cookRole) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public List<Waiter> getWaiters() {
			// TODO Auto-generated method stub
			return null;
		}
}
