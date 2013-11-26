package bank.test;

import bank.BankCustomerRole;
import bank.BankCustomerRole.CustomerState;
import bank.test.mock.MockPeopleBank;
import bank.test.mock.MockTeller;
import junit.framework.*;

/**
 * 
 * This class is a JUnit test class to unit test the CashierAgent's basic interaction
 * with waiters, customers, and the host.
 * It is provided as an example to students in CS201 for their unit testing lab.
 *
 * @author Monroe Ekilah
 */
public class BankCustomerTest extends TestCase
{

	MockPeopleBank person;
	MockTeller teller;
	BankCustomerRole customer;
		
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		customer = new BankCustomerRole(null);		
		teller = new MockTeller("mockcustomer");
		person = new MockPeopleBank("teller");
		customer.setPerson(person);
	}	
	//This scenario tests one market order that the cashier has to pay.
	public void testFirstDepositScenario()
	{
		//setUp() runs first before this test!			
		
		//check preconditions
		assertEquals("BankCustomer should have an account id = -1. It doesn't",customer.accountID, -1);	
		
		assertEquals("Person should have 1000 dollars in their wallet. They don't",person.money, 1000.0);
		
		assertFalse("BankCustomer's scheduler should have returned false (no actions to do), but didn't.", 
				customer.pickAndExecuteAnAction());
		
		//check teller ready msg
		
		customer.msgIsActive();
		
		assertTrue("BankCustomer's role isActive variable should be true but is not.", 
				customer.isActive);
		
		assertEquals("BankCustomer state should be inline, it isn't",customer.state, CustomerState.inline);

		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: "
						+ market.log.toString(), 0, market.log.size());
		
		assertEquals("Cashier should have 1 market bill in it. It doesn't.", cashier.mBills.size(), 1);
		
		assertEquals("The bill should contain the amount given in the message", cashier.mBills.get(0).amount, 120.8);
		
		assertTrue("Cashier's scheduler should have returned true (needs to react to the new market bill), but didn't.", 
					cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier's wallet should have $1000 - 120.80. It doesn't.",cashier.wallet, 879.2);
	
		assertTrue("Market should have logged \"Received msgHereIsMoney\" but didn't. His log reads instead: "
                + market.log.getLastLoggedEvent().toString(), market.log.containsString("Received msgHereIsMoney from cashier. Total = 120.8"));
		
		assertEquals("Cashier should have 0 bills in it. It doesn't.",cashier.mBills.size(), 0);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
}
