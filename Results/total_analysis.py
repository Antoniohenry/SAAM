import matplotlib.pyplot as plt

with open('total.txt') as file:

    x = [{} for i in range(8)]

    numberOfQlRun = 0
    for line in file:
        splited = line.split()

        if len(splited) != 0:
            numberOfQlRun += 1

            splited = [float(splited[i].replace(',', '.')) for i in range(len(splited))]
            for i in range(0, 8):
                para = splited[i]
                try:
                    x[i].update({para: x[i][para] + [splited[11]]})
                except KeyError:
                    x[i].update({splited[i]: [splited[11]]})


xlabels = ["Initial Temperature", "Final Temperature", "Decreasing factor", "Number of iterations", "Threshold", "Q initial", "Alpha", "Gamma"]

for i in range(0, 8):

    plt.figure()
    plt.xlabel(xlabels[i])
    plt.ylabel("Flightset Reward")

    mini = min(x[i].keys())
    maxi = max(x[i].keys())
    scale = maxi - mini
    if scale == 0:
        margin = 0.03
        width = 0.005
    else:
        margin = scale / 10
        width = scale / 10

    plt.boxplot(list(x[i].values()), positions=list(x[i].keys()), widths=width, whis=1.5)

    #plt.xscale("log")

    plt.xlim(mini - margin, maxi + margin)
    plt.show()

print("Number of Ql run : " + str(numberOfQlRun))