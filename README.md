team30
======

SimCity201 Project Repository for CS 201 students

Vikrant Singhal:
My contribution:
Entire Housing Scenario:
+ Creation and implementation of 4 roles: HousingResidentRole, HousingRepairManRole, HousingRenterRole and HousingOwnerRole.
+ GUIs and interfaces for each of the above roles.
+ Objects in the background, which include, Item, House (which can be of two types: Villa and Apartment), Apartments (the apartment complex), and GUIs for each one of them.
+ Unit tests for all the roles mentioned in 1.
+ Manual construction of house layout.
+ Over 4200 lines of code (excluding repeating or empty part of interfaces and mock agents).

Partially/unimplemented parts:
+ The RepairShop has been created, and is supposed to be used by the RepairMan, which it does. But details of repair shop (for example: layout of the shop) yet to be covered.
+ The resident(HousingResidentRole) does not purchase things to cook from the market.
+ A resident can run over objects.
+ This implementation allows only one resident per house.
+ Not modified my restaurant for V1, hence not integrated it in V1.

Parts not integrated in the project:
+ The repair man exists, but doesn't actually do his job, to reduce complexity of PeopleAgent's scheduler for v1. The repair man's functionality, however, has been fully tested, and it works properly.
+ Renter and owner interactions, although fully tested and implemented, haven't been put inside V1 for the sake of simplicity.
+ Apartment complex(Apartments) has been implemented, and the GUIs of roles have been fully adapted and tested for the concept. But for the sake of simplicity, it has been left out.

Improvements for v2:
+ Implementing A* algorithm to prevent residents from colliding with objects or going through walls.
+ Resident's need for markets, to be created.
+ Integration of the parts that had been already implemented (for example: the concept of apartment complex(Apartments), the job of repair man(HousingRepairManRole)), but excluded from v1.
+ My own restaurant has to be modified and integrated in V2.
