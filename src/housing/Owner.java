package housing;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import people.PeopleAgent;

public class Owner extends Resident {
	// Data

	private List<MyHouse> myHouses = new ArrayList<MyHouse>();
	private List<RentOrder> rents = new ArrayList<RentOrder>();
	private double money;
	private double PEN_INCREMENT = 50.0;

	public Owner() {
		super();
	}

	//-----------------------------------------------------------//

	// Actions

	public void applyPenalty(RentOrder ro) {
		ro.mh.penalty += PEN_INCREMENT;
		ro.mh.r.payPenalty(PEN_INCREMENT);
		ro.s = RentOrderState.AppliedPenalty;
	}

	public void applyPenaltyAndRemove(RentOrder ro) {
		ro.mh.penalty += PEN_INCREMENT;
		ro.mh.r.payPenalty(PEN_INCREMENT);
		rents.remove(ro);
	}

	//-----------------------------------------------------------//

	// Messages

	public void hereIsRent(House h, double money) {
		MyHouse m = find(h);
		this.money += money;
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.Due) {
				rents.remove(ro);
				return;
			}
		}
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.AppliedPenalty) {
				rents.remove(ro);
				return;
			}
		}
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.ApplyPenalty) {
				ro.s = RentOrderState.ApplyPenaltyAndRemove;
				return;
			}
		}
	}

	public void hereIsPenalty(House h, double money) {
		MyHouse mh = find(h);
		this.money += money;
		mh.penalty -= money;
	}

	public void generateRent(MyHouse m) {
		for (RentOrder ro : rents) {
			if (ro.mh == m && ro.s == RentOrderState.Due) {
				ro.s = RentOrderState.ApplyPenalty;
			}
		}
		rents.add(new RentOrder(m));
	}
	
	public void addHouse(House h, Renter r) {
		MyHouse mh = new MyHouse(h, r);
		myHouses.add(mh);
	}
	
	public void addRenterToHouse(House h, Renter r) {
		MyHouse mh = find(h);
		mh.setOccupant(r);
	}

	//-----------------------------------------------------------//

	// Scheduler

	public boolean pickAndExecuteAnAction() {
		RentOrder ro =  null;

		ro = findRentOrderByState(RentOrderState.ApplyPenaltyAndRemove);
		if (ro != null) {
			applyPenaltyAndRemove(ro);
			return true;
		}
		
		ro = findRentOrderByState(RentOrderState.ApplyPenalty);
		if (ro != null) {
			applyPenalty(ro);
			return true;
		}

		return super.pickAndExecuteAnAction();
	}

	//-----------------------------------------------------------//

	// Utilities

	private MyHouse find(House h1) {
		for (MyHouse mh : myHouses) {
			if (mh.h == h1)
				return mh;
		}
		return null;
	}

	private RentOrder findRentOrderByState(RentOrderState s1) {
		for (RentOrder ro : rents) {
			if (ro.s == s1)
				return ro;
		}
		return null;
	}
	
	public PeopleAgent getAgent() {
		return super.getAgent();
	}

	//-----------------------------------------------------------//

	// Helper Data Structures

	private class MyHouse {
		House h;
		Renter r = null;
		double penalty;
		double rent;
		Timer rentTimer;
		long period;

		public MyHouse(House h, Renter r) {
			this.h = h;
			this.period = 10000;
			this.r = r;
			this.penalty = 0.0;
			rent = 600.0;
			rentTimer = new Timer();
//			final MyHouse mh = this;
//			if (r != null) {
//				rentTimer.scheduleAtFixedRate(new TimerTask() {
//					public void run() {
//						generateRent(mh);
//					}
//				}, 0, period);
//			}
		}
		
		public void setOccupant(Renter r) {
			this.r = r;
//			final MyHouse mh = this;
//			rentTimer.scheduleAtFixedRate(new TimerTask() {
//				public void run() {
//					generateRent(mh);
//				}
//			}, 0, period);
		}
	}

	private class RentOrder {
		MyHouse mh;
		RentOrderState s;

		public RentOrder(MyHouse mh) {
			this.mh = mh;
			s = RentOrderState.Due;
		}
	}

	enum RentOrderState {Due, ApplyPenalty, ApplyPenaltyAndRemove, AppliedPenalty};
}

