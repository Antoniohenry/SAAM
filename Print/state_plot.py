import matplotlib.pyplot as plt
import numpy as np


def get_data(filename):

    data = {}

    with open(filename) as file:
        node_name = None
        aircrafts = []
        for line in file:
            splited = line.split()
            if(splited[0] == "Node"):
                if node_name is not None and len(aircrafts) != 0:
                    data.update({node_name: aircrafts})

                node_name = splited[2]
                aircrafts = []
            if(splited[0] == "Aircraft"):
                aircraft = []
                aircraft.append(float(splited[2]))
                aircraft.append(splited[3])
                aircraft.append(int(splited[4]))
                aircraft.append(float(splited[5]))
                aircraft.append(float(splited[6]))
                node_conflict = splited[7][1:-2].split(',')
                conflict = []
                for item in node_conflict:
                    if(len(item) > 0):
                        conflict.append(item)
                aircraft.append(conflict)
                aircrafts.append(aircraft)
        if node_name is not None and len(aircrafts) != 0:
            data.update({node_name: aircrafts})
    return data


def plot_node(node, data, offset):
    aircrafts = data[node]
    plt.figure(figsize=(8, 4), dpi=700)
    plt.title(node)
    for aircraft in aircrafts:
        if aircraft[0] > offset and aircraft[0] < offset + 2 *3600:
            color = 'blue' if (len(aircraft[5]) == 0) else 'red'
            plt.plot([aircraft[0], aircraft[0]], [0.1, aircraft[4]], color=color, linewidth=1)

    x_ticks = np.arange(offset, offset + 2 * 3600, 15 * 60)
    x_tick_labels = [round(tick / 3600, 2) for tick in x_ticks]
    print(x_tick_labels)
    plt.xticks(x_ticks)
    plt.gca().set_xticklabels(x_tick_labels)
    plt.xlabel("time")
    plt.ylabel("reward")
    plt.show()


def subplot(ax, node, data, offset, window_length):
    aircrafts = data[node]
    ax.set_title(node)
    for aircraft in aircrafts:
        if aircraft[0] > offset and aircraft[0] < offset + window_length:
            color = 'blue' if (len(aircraft[5]) == 0) else 'red'
            ax.plot([aircraft[0], aircraft[0]], [0.1, aircraft[4]], color=color, linewidth=1)

    x_ticks = np.arange(offset, offset + window_length + 1, 1 * 60 * 60)
    x_tick_labels = [round(tick / 3600, 2) for tick in x_ticks]
    ax.set_xticks(x_ticks)
    ax.set_xticklabels(x_tick_labels)
    ax.set(xlabel='time', ylabel='reward')


def get_plot(data, offset, window_length):
    fig, axs = plt.subplots(4, 2, figsize=(11, 9))
    subplot(axs[0, 0], 'LORNI', data, offset, window_length)
    subplot(axs[1, 0], 'PG562', data, offset, window_length)
    subplot(axs[2, 0], 'IF_27R', data, offset, window_length)
    subplot(axs[3, 0], 'RWY_27R', data, offset, window_length)
    subplot(axs[0, 1], 'OKIPA', data, offset, window_length)
    subplot(axs[1, 1], 'PG564', data, offset, window_length)
    subplot(axs[2, 1], 'IF_26L', data, offset, window_length)
    subplot(axs[3, 1], 'RWY_26L', data, offset, window_length)
    fig.tight_layout()
    plt.savefig('lastState.png')
    #plt.show()


data = get_data("laststate")
get_plot(data, 8 * 3600, 4 * 3600)
#plot_node('OKIPA', data, 6 * 3600)

