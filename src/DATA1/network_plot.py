import matplotlib.pyplot as plt

nodes_filename = "nodes.txt"
links_filename = "links.txt"


def read_nodes(filename):
    nodes = {}

    x_min = 1000
    x_max = -1000
    y_min = 1000
    y_max = -1000

    with open(filename, 'r') as f:
        for line in f:
            l = line.split()
            x = float(l[0])
            y = float(l[1])
            if x < x_min: x_min = x
            if x > x_max: x_max = x
            if y < y_min: y_min = y
            if y > y_max: y_max = y

            nodes[l[2]] = (x, y)

    return nodes


def read_links(filename, nodes):
    links = []

    with open(filename, 'r') as f:
        for line in f:
            l = line.split()
            links.append([nodes[l[0]], nodes[l[1]], l[0], l[1]])
    return links


def plot(nodes, links):

    plt.figure(figsize=(6, 6), dpi=500)
    for i in range(len(links)):
        color = 'red'
        if links[i][2] in ["OKIPA", "PG564", "IF_26L"]:
            color = 'green'
        plt.plot([links[i][0][0], links[i][1][0]], [links[i][0][1], links[i][1][1]], color=color)

    for key in nodes.keys():
        if key in ["RWY_27R", "IF_27R", "PG562", "OKIPA"]:
            y = 3
        else:
            y = -3
        x = 0
        if key in ["PG562", "PG564", "OKIPA", "IF_27R", "IF_26L"]:
            x = -2
        plt.annotate(key, (nodes[key][0] + x, nodes[key][1] + y), color='black', fontsize='10')
        plt.plot(nodes[key][0], nodes[key][1], color='black', marker='o')

    plt.xlabel("[nm]")
    plt.ylabel("[nm]")
    plt.savefig("network1.png")
    #plt.show()


nodes = read_nodes(nodes_filename)
links = read_links(links_filename, nodes)

plot(nodes, links)
