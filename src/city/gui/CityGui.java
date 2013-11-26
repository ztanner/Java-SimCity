package city.gui;
import housing.House;
import housing.HouseType;
import housing.HousingRepairManRole;
import housing.HousingResidentRole;
import housing.gui.HouseAnimationPanel;

import javax.swing.*;
import javax.swing.Timer;

import bank.BankCustomerRole;
import bank.TellerRole;
import bank.gui.BankGui;
import market.MarketEmployeeRole;
import market.gui.MarketGui;
import city.Bank;
import city.Market;
import city.Restaurant;
import people.PeopleAgent;
import restaurant.*;
import restaurant.gui.RestaurantGui;
import restaurant.gui.RestaurantPanel.CookWaiterMonitor;
import restaurant.gui.RestaurantPanel;
import restaurant.gui.WaiterGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class CityGui extends JFrame implements ActionListener {
	BankGui bankGui = new BankGui();
	CityPanel cityPanel;
	JPanel buildingPanels;
	CardLayout cardLayout;
	CityControls cityControls;
	List<String> configParams = Collections
			.synchronizedList(new ArrayList<String>());
	RestaurantGui restaurantGui = new RestaurantGui();
	MarketGui marketGui = new MarketGui();
	HouseAnimationPanel houseAnimationPanel = new HouseAnimationPanel();
	
	ArrayList<PeopleAgent> people = new ArrayList<PeopleAgent>();
	HostRole RestaurantHostRole = new HostRole();
	MarketEmployeeRole MarketEmployeeRole = new MarketEmployeeRole(marketGui);
	Restaurant restaurant = new Restaurant(RestaurantHostRole, new Dimension(100, 100), "Restaurant 1");
	Market market = new Market(MarketEmployeeRole, new Dimension(100,100),"Market 1"); 
	TellerRole BankTellerRole = new TellerRole(bankGui);
	Bank bank = new Bank(BankTellerRole, new Dimension(100, 100), "Bank 1");
	HousingRepairManRole repairManRole = new HousingRepairManRole();
	
	
	


	public int time;

	public CityGui() {
		//this.setResizable(false);
		cityPanel = new CityPanel(this);
		cityPanel.setPreferredSize(new Dimension(1024, 500));		
		cityPanel.setMaximumSize(new Dimension(1024, 500));
		cityPanel.setMinimumSize(new Dimension(1024, 500));
		
		Timer timer = new Timer(10, this);
		RestaurantPanel restPanel = new RestaurantPanel(restaurantGui);
		restPanel.setHost(RestaurantHostRole);
		CookWaiterMonitor RestaurantCookWaiterMonitor = restPanel.theMonitor;
		MarketEmployeeRole RestaurantMarketRole = new MarketEmployeeRole(marketGui);

		FileReader input;
		try {
			input = new FileReader("src/config.txt");
			BufferedReader bufRead = new BufferedReader(input);
			String line = null;
			while ((line = bufRead.readLine()) != null) {
				configParams.addAll(Arrays.asList(line.split("\\s*,\\s*")));
			}
			Iterator<String> configIteration = configParams.iterator();
			while (configIteration.hasNext()) {
				String amount = configIteration.next();
				String job = configIteration.next();
				String name = configIteration.next();
				int start = Integer.parseInt(configIteration.next());
				int end = Integer.parseInt(configIteration.next());
				if (isInteger(amount)) {
					PeopleAgent person = new PeopleAgent(name, 1000.0, false);
					person.setCityGui(this);
					PersonGui personGui = new PersonGui( 5, 5, 5, 5, cityPanel.sidewalks.get(29),cityPanel.sidewalks,cityPanel,person,cityPanel.buildings.get(0));
					person.setPersonGui(personGui);
					person.Restaurants.add(restaurant);
					person.Banks.add(bank);
					person.Markets.add(market);
					cityPanel.people.add(personGui);
					RestaurantCustomerRole RestaurantCustomerRole = new RestaurantCustomerRole(restaurantGui);
					person.addRole(RestaurantCustomerRole,"RestaurantCustomer");
					RestaurantCustomerRole.setPerson(person);
					BankCustomerRole bankCustomerRole = new BankCustomerRole(bankGui);
					person.addRole(bankCustomerRole,"BankCustomer");
					bankCustomerRole.setPerson(person);
					House house = new House("House", 1, HouseType.Villa);
					HousingResidentRole residentRole = new HousingResidentRole();
					residentRole.testModeOn();
					residentRole.setPerson(person);
					residentRole.isActive = true;
					residentRole.setRepairMan(repairManRole);
					residentRole.setHouse(house);
					person.addRole(residentRole, "Resident");
					
					
					person.startThread();
					person.setTest();
					
					if (job.equals("RestaurantNormalWaiter")) {
						NormalWaiterRole RestaurantNormalWaiterRole = new NormalWaiterRole(restaurantGui);
						//WaiterGui g = new WaiterGui(RestaurantNormalWaiterRole);
						//RestaurantNormalWaiterRole.setGui(g);
						person.addJob("RestaurantNormalWaiter", start, end);
						person.addRole(RestaurantNormalWaiterRole,"RestaurantNormalWaiter");
						RestaurantNormalWaiterRole.setPerson(person);
					}
					if (job.equals("RestaurantCook")) {
						CookRole RestaurantCookRole = new CookRole(RestaurantCookWaiterMonitor, restaurantGui);
						person.addJob("RestaurantCook", start, end);
						person.addRole(RestaurantCookRole, "RestaurantCook");
						RestaurantCookRole.setPerson(person);
					}
					if (job.equals("RestaurantHost")) {
						person.addJob("RestaurantHost", start, end);
						person.addRole(RestaurantHostRole, "RestaurantHost");
						RestaurantHostRole.setPerson(person);
					}
					if (job.equals("RestaurantCashier")) {
						CashierRole RestaurantCashierRole = new CashierRole();
						person.addJob("RestaurantCashier", start, end);
						person.addRole(RestaurantCashierRole,"RestaurantCashier");
						RestaurantCashierRole.setPerson(person);
					}
					if (job.equals("RestaurantCustomer"))
					{
						
					}
					if (job.equals("Teller")) {
						person.addJob("Teller", start, end);
						person.addRole(BankTellerRole, "Teller");
						BankTellerRole.setPerson(person);			
					}
					if(job.equals("Nobody")) {
						person.addJob("MarketEmployee", start, end);
						person.addRole(MarketEmployeeRole,"MarketEmployee");
						MarketEmployeeRole.setPerson(person);
						person.setMoney(1000000);
						person.hasCar = true;
					}
					if(job.equals("RepairMan"))
					{
						person.addJob("RepairMan", start, end);
						person.addRole(repairManRole,"RepairMan");
						repairManRole.setPerson(person);
						person.setMoney(1000000);
						person.hasCar = true;
					}
					people.add(person);
				}
			}
			bufRead.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (PeopleAgent p : people) {
			p.Restaurants.add(restaurant);
			p.Markets.add(market);
			p.Banks.add(bank);
		}
		setVisible(true);
		setSize(1024, 768);

		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		cityControls = new CityControls();
		cityControls.setPreferredSize(new Dimension(500, 268));
		cityControls.setMaximumSize(new Dimension(500, 268));
		cityControls.setMinimumSize(new Dimension(500, 268));
		
		cardLayout = new CardLayout();

		buildingPanels = new JPanel();
		buildingPanels.setLayout(cardLayout);
		buildingPanels.setPreferredSize(new Dimension(500, 268));
		buildingPanels.setMaximumSize(new Dimension(500, 268));
		buildingPanels.setMinimumSize(new Dimension(500, 268));
		buildingPanels.setBackground(Color.yellow);


		// Create the BuildingPanel for each Building object
		ArrayList<Building> buildings = cityPanel.getBuildings();
		for (int i = 0; i < buildings.size(); i++) {
            Building b = buildings.get(i);
            BuildingPanel bp = new BuildingPanel(b, i, this);
            b.setBuildingPanel(bp);
		}
		
		JScrollPane marketContainer = new JScrollPane(marketGui);
		marketContainer.setOpaque(true);
		JScrollPane restaurantContainer = new JScrollPane(restaurantGui);
		restaurantContainer.setOpaque(true);
		JScrollPane bankContainer = new JScrollPane(bankGui);
		bankContainer.setOpaque(true);
		JScrollPane houseContainer = new JScrollPane(houseAnimationPanel);
		houseContainer.setOpaque(true);
        buildingPanels.add(restaurantContainer, "" + 0);
        buildingPanels.add(bankContainer, "" + 1);
        buildingPanels.add(houseContainer, "" + 2);
        buildingPanels.add(marketContainer,"" + 3);


		//getContentPane().add(BorderLayout.WEST, cityControls);
		//getContentPane().add(BorderLayout.NORTH, cityPanel);
		//getContentPane().add(BorderLayout.SOUTH, buildingPanels);
        
        buildingPanels.setOpaque(true);
        cityControls.setOpaque(true);
        cityPanel.setOpaque(true);
		
	    c.gridx = 0; c.gridy = 0;
	    c.gridwidth = 2; c.gridheight = 1;
	    this.add(cityPanel, c);

	    c.gridx = 0; c.gridy = 1;
	    c.gridwidth = 1; c.gridheight = 1;
	    this.add(buildingPanels, c);

	    c.gridx = 1; c.gridy = 1;
	    c.gridwidth = 1; c.gridheight = 1;
	    this.add(cityControls, c);
	    
	    this.pack();
		
		timer.start();
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	}

	public void displayBuildingPanel(BuildingPanel bp) {
		cardLayout.show(buildingPanels, bp.getName());
	}

	public static void main(String[] args) {
		CityGui sc = new CityGui();

	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		bankGui.updatePosition();
		time++;
		if(time % 1 == 0)
		{
			if(time%60 == 0)
			{
				System.out.println(time/1);
			}
			for (PeopleAgent p : people) {
				p.msgTimeIs(time/1);
			}
		}
		if(time == 2400*1) {
			time=0;
		}
		repaint();

	}
}
