# SAAM

Dependencies :
 - Python 3.7.6 with numpy and matplotlib.pyplot
 - Java11 with Java.commons.math3-3.6, Java.commons.lang3-3.12
 
 To change the size of the flight set (complexity of the scenario)
 - run FlightSetSimulation.py to create a new flight set. The variable percentage changes the size of the flight set in comparison to the initial one.

 QL and SA are two optimisation algorithms to be run on scenarios. They call methods in the interface package and shouldn't directly call model's methods.

The package optimisation provides different algorithms to find the best combination parameters for the QL. EDA and GA are no longer in use.

