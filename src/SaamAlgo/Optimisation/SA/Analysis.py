import numpy as np
import matplotlib.pyplot as plt

filename = "C:\\Users\\antoi\\IdeaProjects\\SAAMAlgo\\src\\SaamAlgo\\Optimisation\\SA\\SA30"

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



abscisse = { 0: "Initial Temperature",
             1: "Final Temperature",
             2: "Decreasing",
             3: "Itertion",
             #4: "Threshold",
             5: "Q Initial",
             6: "Alpha",
             7: "Gamma",
             #8: "Linear",
             #9: "Reward RTA",
             #10: "Reward Conflict"
             }

ordonnee = {11: "Total Reward",
            #12: "Worst Reward",
            #13: "Average Delay",
            #14: "Node Conflict",
            #15: "Link Conflict",
            #16: "Computational Time"
            }




for a in abscisse.keys():
    for o in ordonnee.keys():
        plot = {}
        for line in tab:

            #if(line[14] == 0 and line[15] == 0):

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

        plt.figure()
        plt.xlabel(abscisse[a])
        plt.ylabel(ordonnee[o])
        plt.boxplot(list(plot.values()), positions=list(plot.keys()), widths=largeur/10, whis=1.5)

        #plt.xscale("log")



        plt.xlim( mini - 0.1 * largeur, maxi + 0.1 * largeur)

        plt.show()