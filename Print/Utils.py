import matplotlib.pyplot as plt


def my_plot(list, title, xlabel, ylabel):
    plt.figure(figsize=(6, 4), dpi=700)
    plt.title(title)
    plt.plot(list)
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    plt.show()