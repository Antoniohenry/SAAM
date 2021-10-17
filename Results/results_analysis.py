import numpy as np
import matplotlib.pyplot as plt
import sys

data = []

file = sys.argv[1]

with open(file) as file:
    for line in file:
        try:
            splited = line.split()
            data.append( [ (int(splited[1]) / 3600) -1 ] + [int(splited[i]) for i in range(2, len(splited))])
        except Exception:
            pass

data = np.array(data)

xlabels = ["Time In [Hours]", "Delay [Seconds]", "Time in Merge Point [Seconds]", "Delay entering the TMA [Seconds]", "Runway change", "Speed [kts]", "Conflict"]
ylabels = ["Number of aircraft"] * 7


for i in [0, 1, 2, 3, 5]:
    mini = min(data[:, i])
    maxi = max(data[:, i])
    xvalues = np.arange(mini - 1, maxi + 1, 20)
    data_to_print = data[:, i]

    if(i == 0):
        xvalues = np.arange(mini, maxi + 1, 1)

    if(i == 5):
        xvalues = np.arange(mini, maxi, 10)

    plt.figure()
    plt.hist(data_to_print, xvalues, align='mid')
    plt.xlabel(xlabels[i])
    plt.ylabel(ylabels[i])
    print('mean ' + xlabels[i] + ' ' + str(np.mean(data_to_print)))
    plt.show()
    #plt.savefig( 'figures/' + xlabels[i] + ".png")

print("number of runway change : ", sum(data[:, 4]))