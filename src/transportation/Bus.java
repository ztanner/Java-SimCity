package transportation;
import java.util.*;

import agent.Agent;
import transportation.BusPassengerRole;
public class Bus extends Agent{
	public enum PassState {waiting,waitingAndNotified};
class Passenger {
	PassState st;
    BusPassengerRole bpr;
    Passenger(BusPassengerRole p)
    {
            bpr = p;
            st = PassState.waiting;
    }
}
List<Passenger> myBusPassengers;
BusGui busGui;
public enum BusState {driving, newStop, newStopAndPassengersNotified, waitingForNewPassengers, readyToLeave, off};
BusState busState;
BusStop currentStop;
List<BusStop> myBusStops;


public void msgImBoarding(BusPassengerRole p){ //remove place
myBusPassengers.add(new Passenger(p));
}

public void msgImLeaving(BusPassengerRole p){
Passenger toRemove = findPassenger(p);
myBusPassengers.remove(toRemove);
}



public void AnimationFinishedArrivedAtStop(BusStop S){
currentStop = S;
busState = BusState.newStop;
stateChanged();
}

public void msgAllBusStopPassengersNotified(){
busState = BusState.readyToLeave;
stateChanged();
}



protected boolean pickAndExecuteAnAction() {
for (Passenger passenger : myBusPassengers)
{
    if (busState == BusState.newStop && passenger.st == PassState.waiting)
    {
            passenger.st = PassState.waitingAndNotified;
            NotifyPassengerAboutCurrentStop(passenger);
            return true;
    }
}


if(busState == BusState.newStopAndPassengersNotified)
{
    busState = BusState.waitingForNewPassengers;
    WaitForNewPassengers();
    return true;
}

if(busState == BusState.readyToLeave) {
    busState = BusState.driving;
    GoToNextStop();
    return true;
}

return false;
}

private void WaitForNewPassengers(){
currentStop.msgBusArrived(this);
}

private void NotifyPassengerAboutCurrentStop(Passenger p){
p.bpr.msgArrivedAtStop(currentStop);
if(areAllBusPassengersNotified()) //function that loops through passengers, checking if each state says notified
	busState = BusState.newStopAndPassengersNotified;
    

}

private void GoToNextStop(){
busGui.msgGoToNextStop(this,myBusStops);
}

private Passenger findPassenger(BusPassengerRole target) {
	// TODO Auto-generated method stub
	for(Passenger p : myBusPassengers)
	{
		if(p.bpr == target)
			return p;
	}
	return null;
}
private boolean areAllBusPassengersNotified(){
	for(Passenger p : myBusPassengers)
	{
		if(p.st != PassState.waitingAndNotified)
			return false;
	}
	return true;
}
}

