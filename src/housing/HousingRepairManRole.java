package housing;

import housing.gui.RepairManGui;
import housing.interfaces.RepairMan;
import housing.interfaces.Resident;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import people.People;
import people.PeopleAgent;
import people.Role;

public class HousingRepairManRole extends Role implements RepairMan {
	// Data

	private List<MyHouse> houses = new ArrayList<MyHouse>();
	private double salary;
	private double money;
	private double homeMoney;
	private MyHouse currentHouse = null;
	public Location location = Location.Shop;
	public RepairManGui gui;
	public Semaphore activity = new Semaphore(0, true);
	public MyHouse currentLocationHouse;

	public HousingRepairManRole() {
		super();
	}

	//-----------------------------------------------------------//

	// Actions

	public void goToHouse(final MyHouse mh) {
		// Animation implementation.
		mh.r.ImHere();
		mh.s = HouseState.Reached;
	}

	public void repairItems(MyHouse mh) {
		List<Item> brokenItems = mh.h.getBrokenItems();
		for (Item i : brokenItems) {
			i.repair();
		}
		mh.r.repairDone();
		mh.s = HouseState.None;
		currentLocationHouse = currentHouse;
		currentHouse = null;
		// Leaving.
	}
	
	public void leaveShop() {
		gui.DoLeaveShop();
		try {
			activity.acquire();
		} catch (InterruptedException e) {}
		location = Location.OutsideFixing;
		myPerson.msgDone("RepairManFixing");
	}
	
	public void returnToShop() {
		location = Location.OutsideReturning;
	}
	
	public void enterShop() {
		location = Location.Shop;
	}

	//-----------------------------------------------------------//

	// Messages

	public void needHelp(House h, double money) {
		MyHouse mh = find(h);
		mh.s = HouseState.NeedsRepair;
		this.money += money;
		stateChanged();
	}
	
	public void activityDone() {
		location = Location.OutsideReturning;
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		if (location == Location.OutsideReturning) {
			enterShop();
			return true;
		}
		if (currentHouse == null) {
			currentHouse = findMyHouseByState(HouseState.NeedsRepair);
		}
		if (currentHouse != null) {
			if (location == Location.Shop) {
				leaveShop();
				return true;
			}
			else if (location == Location.Resident) {
				if (currentHouse)
			}
			if (currentHouse.s == HouseState.NeedsRepair) {
				goToHouse(currentHouse);
				return true;
			}
			if (currentHouse.s == HouseState.Reached) {
				repairItems(currentHouse);
				return true;
			}
		}
		else {
			if (location == Location.Resident) {
				returnToShop();
				return true;
			}
		}
		return false;
	}

	//-----------------------------------------------------------//

	// Utilities

	// Ensure thread safety for homeMoney.
	public void pickUpSalaryMoney() {
		money += homeMoney;
		homeMoney = 0;
	}

	public MyHouse find(House h) {
		for (MyHouse mh : houses) {
			if (mh.h == h)
				return mh;
		}
		return null;
	}

	public MyHouse findMyHouseByState(HouseState s) {
		for (MyHouse mh : houses) {
			if (mh.s == s)
				return mh;
		}
		return null;
	}

	public void addHouse(House h, Resident r) {
		houses.add(new MyHouse(h, r));
	}
	
	public People getAgent() {
		return myPerson;
	}
	
	public boolean doesItNeedRepair(House h) {
		MyHouse mh = find(h);
		if (mh.s == HouseState.NeedsRepair)
			return true;
		return false;
	}
	
	public boolean anyCurrentHouse() {
		if (currentHouse != null)
			return true;
		return false;
	}
	
	@Override
	public void setPerson(PeopleAgent p) {
		myPerson = p;
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	private class MyHouse {
		House h;
		Resident r;
		HouseState s;

		public MyHouse(House h, Resident r) {
			this.h = h;
			this.r = r;
			s = HouseState.None;
		}
	}
	
	public enum HouseState {None, NeedsRepair, Reached}
	public enum Location {Shop, OutsideFixing, OutsideReturning, Resident};
}