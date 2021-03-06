import random as rd
import copy
from numpy.random import choice
import sys

# files containing the initial set
files = ["20170711_26L_ARRIVEES.flights", "20170711_27R_ARRIVEES.flights"]

# Percentage of aircraft to add
# In %, need to be greater than 1
try:
    percentage = float(sys.argv[1])
except:
    percentage = 1.

# new file to write the new flight set into
result = "simulation.flights"


def from_string_to_dict(str):
    splited = str.split()
    arrive = float(splited[7])
    return {arrive: splited}


def from_dict_to_string(dict):
    splited = dict.values()[0]
    return [item + ' ' for item in splited]


def add_offset(aircraft, offset):
    new = copy.deepcopy(aircraft)
    if offset != 0:
        new[0] += "sim"
        new[1] += "sim"
    new[7] = str(float(aircraft[7]) + offset)
    new[10] = str(float(aircraft[10]) + offset)
    return new


def add_new_aircraft(actual_set, aircraft, time):
    offset = time - float(aircraft[7])
    actual_set.update({time: add_offset(aircraft, offset)})
    return actual_set


def getSet():
    actualSet = {}
    for file in files:
        with open(file) as f:
            for line in f:
                splited = line.split()
                if int(splited[8]) == 2 or int(splited[8]) == 3:
                    actualSet.update(from_string_to_dict(line))
    return actualSet


def get_possibility(set):
    possibilities = []
    weight = []
    previous_key = 0
    sum = 0
    for key in sorted(set.keys()):
        if previous_key + 120 < key:
            possibilities.append((previous_key + 60, key - 60))
            weight.append(120/(key - previous_key))
            sum += 120/(key - previous_key)
        previous_key = key

    for i in range(len(weight)):
        weight[i] = weight[i]/sum

    return possibilities, weight


def get_new_set(actualSet_, augmentation):
    initial_number = len(actualSet_.keys())
    possibilities, weight = get_possibility(actualSet_)
    new_aircrafts = rd.sample(list(actualSet_.keys()), k=(int(initial_number * (augmentation - 1))))

    while (len(new_aircrafts) != 0) and (possibilities != 0):

        possibilities, weight = get_possibility(actualSet_)
        index_possibility = choice([i for i in range(len(possibilities))], 1, p=weight)
        possibility = possibilities[index_possibility[0]]

        time = rd.uniform(possibility[0], possibility[1])
        new_aircraft = new_aircrafts.pop()
        actualSet_ = add_new_aircraft(actualSet_, actualSet_[new_aircraft], time)

    return actualSet_


with open(result, 'w') as f:
    actualSet_ = getSet()
    initial_number = len(actualSet_.keys())

    new_set = get_new_set(actualSet_, percentage)
    print('initial number of aircraft : ', initial_number, '\n number of aircraft in the new set : ',  len(new_set.keys()))
    for value in new_set.values():
        str = ' '.join([item for item in value])
        f.write(str + '\n')
        f.flush()
