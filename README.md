# SAAM

Dependencies :
 - Python 3.7.6 with numpy and matplotlib.pyplot
 - Java11

 QL and SA are two optimisation algorithms to be run on scenarios. They call methods in the interface package and shouldn't directly call model's methods.

The package optimisation provides different algorithms to find the best combination parameters for the QL. EDA and GA are no longer in use.

## Build
To change the size of the flight set (complexity of the scenario)
- run FlightSetSimulation.py <percentage> to create a new flight set. The variable percentage changes the size of the flight set in comparison to the initial one.
  percentage should be between 1 and 1.5

To build : 
- In the src folder run : javac SaamAlgo\Main.java
To Run :
  - In the src folder run : java SaamAlgo\Main.java <file>
  <file> is the file in which the output will be written
    
To see the result of a precise QL
- run results_analysis.py <file> with the same file name as with Main.java

To see all the results of all the previous QL runs :
- run total_analysis.py 
