package people;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

import bank.interfaces.Teller;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;
import transportation.CarPassengerRole;
import city.Bank;
import city.Market;
import city.Restaurant;
import city.gui.CityGui;
import city.gui.PersonGui;
import agent.Agent;

public class PeopleAgent2 extends Agent implements People{

	public List<MyRole> roles = Collections.synchronizedList(new ArrayList<MyRole>());
	public List<Restaurant> Restaurants = Collections.synchronizedList(new ArrayList<Restaurant>());
	public List<Market> Markets = Collections.synchronizedList(new ArrayList<Market>());
	public List<Bank> Banks = Collections.synchronizedList(new ArrayList<Bank>());
	public List<Job> jobs = Collections.synchronizedList(new ArrayList<Job>());
	public Double Money;
	public Double Balance;
	private int Hunger = 1200;
	public Boolean hasCar;
	public String name;
	public enum HungerState
	{NotHungry, Hungry, Eating};
	Random rand = new Random();
	PersonGui personGui;
	CityGui cityGui;
	private boolean testmode = false;
	public int HomeNum;
	private String type = "default";
	
	private Semaphore moving = new Semaphore(0,true);
	
	public EventLog log = new EventLog();

	public enum BuyState{GoingToBuy, NotBuying, NextDay}
	public enum AgentState 
	{Waiting, Sleeping, Working, EatingAtRestaurant, EatingAtHome, Idle, RestingAtHome, BuyingCar, atHome, GoingToBank, IdleAtHome}
	public enum AgentEvent 
	{Waiting, GoingToSleep, WakingUp, GoingToRestaurant, GoingToWork, LeavingWork, GoingToRetrieveMoney, 
		GoingToDepositMoney, GoingToBuyCar, Idle, GoingHome, RepairManMovingShop, RepairManArrivedShop, RepairManMoving, RepairManArrived, EatingAtHome}
	public enum AgentLocation
	{Home, Bank, Market, Restaurant, Road}
	
	public HungerState hunger = HungerState.NotHungry;
	public AgentState state = AgentState.Sleeping;
	public AgentEvent event = AgentEvent.GoingToSleep;
	public AgentLocation location = AgentLocation.Home;
	public BuyState buy = BuyState.NextDay;
	
	
	public void setTest()
	{
		testmode = true;
	}
	public void Arrived()
	{
		moving.release();
	}
	
	public double getMoney()
	{
		return Money;
	}
	
	public void setType(String t)
	{
		this.type = t;
	}
	
	public void setMoney(double Money)
	{
		this.Money = Money;
	}
	
	
	public List<Role> getRoles()
	{
		List<Role> temp = new ArrayList<Role>();
		for(MyRole a: roles)
		{
			temp.add(a.role);
		}
		return temp;
	}
	
	public String getAgentState()
	{
		return state.toString();
	} 
	
	public String getAgentEvent()
	{
		return event.toString();
	}
	
	public String getHunger()
	{
		return hunger.toString();
	}
	
	public Role getHost(int i)
	{
		return Restaurants.get(i).h;
	}
	
	public Role getTeller(int i)
	{
		return Banks.get(i).t;
	}
	
	public Bank getBank(int i)
	{
		return Banks.get(i);
	}
	
	public Market getMarket(int i)
	{
		return Markets.get(i);
	}
	
	public Restaurant getRestaurant(int i)
	{
		return Restaurants.get(i);
	}
	
	public Role getMarketEmployee(int i)
	{
		return Markets.get(i).mer;
	}
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public void setPersonGui(PersonGui gui)
	{
		personGui = gui;
	}
	
	public void setCityGui(CityGui gui)
	{
		cityGui = gui;
	}
	
	@Override
	public void addRole(Role r, String description)
	{
		roles.add(new MyRole(r, description));
		log.add(new LoggedEvent("Role added: " + description));
	}
	
	/* (non-Javadoc)
	 * @see people.People#CallPrint(java.lang.String)
	 */
	@Override
	public void CallPrint(String text)
	{
		print(text);
	}
	
	/* (non-Javadoc)
	 * @see people.People#CallDo(java.lang.String)
	 */
	@Override
	public void CallDo(String text)
	{
		Do(text);
	}
	
	/* (non-Javadoc)
	 * @see people.People#addJob(java.lang.String, int, int)
	 */
	@Override
	public void addJob(String job, int start, int end)
	{
		jobs.add(new Job(job, start, end));
		log.add(new LoggedEvent("Job added: " + job));
	}
	//messages

	/* (non-Javadoc)
	 * @see people.People#msgDone(people.Role)
	 */
	@Override
	public void msgDone(String role)
	{
		
	}
	
	public PeopleAgent2(String name, double Money, boolean hasCar)
	{
		super();
		this.name = name;
		this.Money = Money;
		this.hasCar = hasCar;
		this.Balance = this.Money;
	}
	
	
	/* (non-Javadoc)
	 * @see people.People#msgTimeIs(int)
	 */
	public int msgWhatIsTime()
	{
		return cityGui.time;
	}
	
	public void msgTimeIs(int Time)
	{		
	}

	//scheduler
	@Override
	public boolean pickAndExecuteAnAction() {
		boolean Roles = false, Person = false;
		return (Roles || Person);
	}

	





//Actions

	private void GoRepairShop() {
		// TODO Auto-generated method stub
		//gui message to ask for destination
		//Semaphore
		event = AgentEvent.RepairManArrivedShop;
		
	}

	private void GoRepair() {
		// TODO Auto-generated method stub
		//gui message to ask for destination
		//Semaphore
		event = AgentEvent.RepairManArrived;
		
	}

	public void GoToRestaurant()
	{
//		boolean temp = true;
		//roles.RestaurantCustomerAgent.msg(this);
//		if(hasCar)
//		{
//			hunger = HungerState.Eating;
//		for(MyRole r: roles)
//		{
//			if(r.description.equals("carPassenger"))
//			{
//				r.role.msgIsActive();
//			}
//			//Guifor Car Animation
//			
//			/*if(r.description.equals("CustomerAgent"))
//			{			
//				r.role.msgIsActive();
//			}*/
//		}
//		}
//		else
		{
			//TODO
			/*if(rand.nextInt(1) == 1)
			{
				for(MyRole r: roles)
				{
					if(r.description.equals("busPassenger"))
					{
						r.role.msgIsActive();
					}
				}
			}
			else*/
			for(MyRole r: roles)
			{
				if(r.description.equals("Resident"))
				{
					if(r.role.isActive)
					{
						r.role.msgIsInActive();
//						temp = false;
					}
				}
			}
		}
//		if(temp)
//		{
//			GoToRestaurantTwo();
//		}
//	}
//	public void GoToRestaurantTwo()
//	{
		//GUI WALK
		location = AgentLocation.Road;
		if(!testmode)
		{
			if(hasCar)
			{
				for(MyRole r: roles)
				{
					if(r.description == "CarPassenger")
					{
						((CarPassengerRole)r.role).setDestination("Restaurant 1");
						r.role.msgIsActive();
					}
				}
			}
			else
			{
				personGui.setDestination(840, 42);
				print("Do Not Have Car");
			}
		//personGui.GoToRestaurantOne();
		try {
			moving.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		location = AgentLocation.Restaurant;
		print("Walking to Restaurant");
		hunger = HungerState.Eating;
		//Semaphore
		for(MyRole r: roles)
		{
			if(r.description.equals("RestaurantCustomer"))
			{
				r.role.msgIsActive();
			}
		}
	}
		

	/* (non-Javadoc)
	 * @see people.People#GoToHouse()
	 */
	@Override
	public void GoToHouse()
	{
		print("Going Back Home");
		location = AgentLocation.Road;
		if(!testmode)
		{
		//personGui.GoToHouse(); TODO
			if(hasCar)
			{
				for(MyRole r: roles)
				{
					if(r.description == "CarPassenger")
					{
						((CarPassengerRole)r.role).setDestination("Home 1");
						r.role.msgIsActive();
					}
				}
			}
			else
			{
				personGui.setDestination(142, 42); //TODO this is guess
				print("Do Not Have Car");
			}
			
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Home;
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{	
				print("I am now a Resident");
				r.role.msgIsActive();
			}
		}
		if(state != AgentState.EatingAtHome && state != AgentState.IdleAtHome)
		{
		state = AgentState.Idle;
		}
	}

	/* (non-Javadoc)
	 * @see people.People#GoBuyCar()
	 */
	@Override
	public void GoBuyCar()
	{
//		boolean temp = true;
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{	
				if(r.role.isActive == true)
				{
				r.role.msgIsInActive();
//				temp = false;
				}
				//Stop
			}
		}
//		if(temp)
//		{
//			GoBuyCarTwo();
//		}
	}
	
	public void GoBuyCarTwo()
	{
		location = AgentLocation.Road;
		if(!testmode)
		{
			if(hasCar)
			{
				for(MyRole r: roles)
				{
					if(r.description == "CarPassenger")
					{
						((CarPassengerRole)r.role).setDestination("Market");
						r.role.msgIsActive();
					}
				}
			}
			else
			{
				personGui.setDestination(580, 322);
				print("Do Not Have Car");
			}
		//personGui.GoToMarket(); TODO
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Market;
		for(MyRole r: roles)
		{
			if(r.description.equals("MarketCustomer"))
			{	
				r.role.msgIsActive();
			}
		}
	}

	/* (non-Javadoc)
	 * @see people.People#LeaveWork()
	 */
	@Override
	public void LeaveWork()
	{
		for(MyRole r: roles)
		{
			if(r.description.equals(jobs.get(0).job))
			{
				print("I am leaving work");
				r.role.msgIsInActive();	
			}
		}
	}

	/* (non-Javadoc)
	 * @see people.People#GoToBank()
	 */
	@Override
	public void GoToBank()
	{		
//		boolean temp = true;
		for(MyRole r: roles)
		{
			if(r.description.equals("Resident"))
			{	
				
				if(r.role.isActive == true)
				{
					print("Resident Role turned off");
//					temp = false;
					r.role.msgIsInActive();
				}
				//Stop
			}
		}
//		if(temp)
//		{
//			GoToBankTwo();
//		}
//		//gui.GoToBank;
//		//roles.BankCustomerRole.msgIsActive();
//	}
//	
//	public void GoToBankTwo()
//	{
		location = AgentLocation.Road;
		if(!testmode)
		{
		//TODO personGui.goToBank();
			if(hasCar)
			{
				for(MyRole r: roles)
				{
					if(r.description == "CarPassenger")
					{
						((CarPassengerRole)r.role).setDestination("Bank");
						r.role.msgIsActive();
					}
				}
			}
			else
			{
				personGui.setDestination(580, 152);
				print("Do Not Have Car");
			}
		try {
			moving.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
		location = AgentLocation.Bank;
		
		for(MyRole r: roles)
		{
			if(r.description == "BankCustomer")
			{
				r.role.msgIsActive();
			}
		}
	}
		

	/* (non-Javadoc)
	 * @see people.People#GoToWork()
	 */
	@Override
	//Going to Work
	public void GoToWork()
	{
		for(int i = 0; i <jobs.size(); i++)
		{
			print("I am going to work now!");
			for(MyRole r: roles)
			{
				if(r.description.equals("Resident"))
				{	
					if(r.role.isActive == true)
					{
						print("Resident Role turned off");
						r.role.msgIsInActive();
					}
					//Stop
				}
			}
			//Pause the Gui
		}
//	}
//		
//	public void GoToWorkTwo(){
		//Release the Gui from msgDone
		for(int i = 0; i <jobs.size(); i++)
		{
		if(jobs.get(i).job.equals("RestaurantNormalWaiter"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantNormalWaiter"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination(840, 42);
							print("Do Not Have Car");
						}
						// TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantNormalWaiter");
					r.role.msgIsActive();
				}
			}
			//roles.WaiterRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RestaurantHost"))
		{
			for(MyRole r: roles)
			{
				
				if(r.description.equals("RestaurantHost"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination(840, 42);
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantHost");
					r.role.msgIsActive();
				}
			}
			//roles.HostRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RestaurantCook"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCook"))
				{			
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination(840, 42);
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantCook");
					r.role.msgIsActive();
				}
			}
			//roles.CookRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RestaurantCashier"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RestaurantCashier"))
				{	
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Restaurant");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination(840, 42);
							print("Do Not Have Car");
						}
					//TODO personGui.GoToRestaurantOne();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Restaurant;
					print("I am now a RestaurantCashier");
					r.role.msgIsActive();
					
				}
			}
			//roles.RepairRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("Teller"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("Teller"))
				{		
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Bank");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination(580, 152);
							print("Do Not Have Car");
						}
					//TOOD personGui.goToBank();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Bank;
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//personGui.setDestination(580, 322);
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("MarketCashier"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("MarketCashier"))
				{		
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Market");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination(580, 322);
							print("Do Not Have Car");
						}
					//TODO personGui.GoToMarket();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Market;
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("MarketEmployee"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("MarketEmployee"))
				{		
					location = AgentLocation.Road;
					if(!testmode)
					{
						if(hasCar)
						{
							for(MyRole ro: roles)
							{
								if(ro.description == "CarPassenger")
								{
									((CarPassengerRole)ro.role).setDestination("Market");
									ro.role.msgIsActive();
								}
							}
						}
						else
						{
							personGui.setDestination(580, 322);
							print("Do Not Have Car");
						}
					//TODO personGui.GoToMarket();
					try {
						moving.acquire();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
					location = AgentLocation.Market;
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.TellerRole.msgIsActive();
		}
		if(jobs.get(i).job.equals("RepairMan"))
		{
			for(MyRole r: roles)
			{
				if(r.description.equals("RepairMan"))
				{			
					print("I am now a " + r.description);
					r.role.msgIsActive();
				}
			}
			//roles.RepairRole.msgIsActive();
		}
		}
		//roles.ResidentRole.msgIsInActive();
	}

	
	/* (non-Javadoc)
	 * @see people.People#CallstateChanged()
	 */
	@Override
	public void CallstateChanged(){
		stateChanged();
	}
	
	class Job
	{
		String job;
		int start;
		int end;
		
		Job(String job, int start, int end)
		{
			this.job = job;
			this.start = start;
			this.end = end;
		}
	}

	class MyRole
	{
		String description;
		Role role;
		MyRole(Role r, String d)
		{
			role = r;
			description = d;
		}
	}

	@Override
	public Teller getTeller() {
		// TODO Auto-generated method stub
		return null;
	}
}

		