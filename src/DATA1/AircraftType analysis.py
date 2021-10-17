import matplotlib.pyplot as plt


def get_flights_from_data(filename):
    flights = []
    with open(filename) as file:
        for line in file:
            splited = line.split()
            flight = (splited[2],  splited[3])
            flights.append(flight)
    return flights


flights = (get_flights_from_data("20170711_26L_ARRIVEES.flights")
           + get_flights_from_data("20170711_27R_ARRIVEES.flights"))

light = [f for f in flights if f[1] == '0']
medium = [f for f in flights if f[1] == '1']
heavy = [f for f in flights if f[1] == '2']


def count(category):
    frequency = {}
    for f in category:
        if f[0] not in frequency.keys():
            frequency.update({f[0]: 1})
        else:
            frequency.update({f[0]: frequency[f[0]] + 1})
    return frequency


for frequency in [count(light), count(medium), count(heavy)]:
    plt.bar(frequency.keys(), frequency.values())
    plt.xticks([i for i in range(len(frequency.keys()))], frequency.keys(), rotation='vertical')
    plt.show()


