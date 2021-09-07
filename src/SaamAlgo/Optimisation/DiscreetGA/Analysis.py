import numpy as np
import matplotlib.pyplot as plt
import numpy.ma as ma

## to be use only on file produced by DiscreetGA.java


# the file to plot
filename = "DiscreetGA120"

# read file
tab = []
with open(filename) as file:
    for line in file:
        try :
            splited = line.split()
            l = [0] * len(splited)
            for flottant in range(len(splited)):
                l[flottant] = float(splited[flottant].replace(',', '.'))

            for entier in [0, 3, 5, 9, 10, 14, 15]:
                l[entier] = int(l[entier])

            tab.append(l)
        except:
            pass


# uncomment parameters to plot them

# x axis
abscissa = { #0: "Initial Temperature",
             #1: "Final Temperature",
             #2: "Decreasing",
             #3: "Iteration",
             #4: "Threshold",
             #5: "Q Initial",
             #6: "Alpha",
             7: "Gamma",
             #8: "Linear",
             #9: "Reward RTA",
             #10: "Reward Conflict"
             }

# y axis
ordonnee = {11: "Total Reward",
            #12: "Worst Reward",
            #13: "Average Delay",
            #14: "Node Conflict",
            #15: "Link Conflict",
            #16: "Computational Time"
            }


for a in abscissa.keys():
    for o in ordonnee.keys():
        plot = {}
        for line in tab:
            try:
                parameter = line[a]
                result = line[o]
                plot.update({parameter: plot[parameter] + [result]})
            except KeyError:
                parameter = line[a]
                result = line[o]
                plot.update({parameter: [result]})
            except ValueError:
                pass


        mini = min(plot.keys())
        maxi = max(plot.keys())

        largeur = maxi - mini

        plt.figure(figsize=(6.5, 5), dpi=200)
        plt.xlabel(abscissa[a])
        plt.ylabel(ordonnee[o])
        plt.boxplot(list(plot.values()), positions=list(plot.keys()), widths=largeur/30, whis=1.5)
        #plt.xscale("log")
        plt.xlim(mini - 0.1 * largeur, maxi + 0.1 * largeur)
        plt.show()
        #plt.savefig("C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\Print\\" + abscisse[a] +".png")



## this part print the covariance matrix ##

matrix = []
masked = []
for line in tab:
    if(line[14] == 0 and line[15] == 0): # if no conflict
        # initial Temperature, final Temperature, decreasing, number of iterations, threshold, Q0, alpha, gamma, Conflict reward offset, RTA Reward, coflict Reward
        masked.append([True, True, False, True, True, False, False, False, True, True, True]) #True will hide the corresponding parameter
        matrix.append(line[:11])

data = ma.MaskedArray(matrix, masked)

matrix = np.array(matrix)
masked = np.array(masked)

comp = np.reshape(data.compressed(), (-1, len(masked[0]) - sum(masked[0])))

for line in np.cov(comp.T):
    print(np.around(line))
