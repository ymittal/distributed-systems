#!/usr/bin/env python

from random import choice, random


def complete_graph(num_nodes):
    """Runs PUSH protocol on a complete graph
    :param  num_nodes   number of nodes in graph
    :return number of rounds to spread rumor
    """
    graph = dict()
    for i in range(num_nodes):
        graph[i] = range(i) + range(i + 1, num_nodes)

    gossip_nodes = set()
    gossip_nodes.add(0)

    num_rounds = 0
    while len(gossip_nodes) < num_nodes:
        round_gossip_nodes = set()
        for i in range(num_nodes):
            if i in gossip_nodes:
                round_gossip_nodes.add(choice(graph[i]))
        gossip_nodes |= round_gossip_nodes
        num_rounds += 1

    return num_rounds


def evolving_graph(num_nodes, p=0.3):
    """Runs PUSH protocol on an evolving graph
    :param  num_nodes   number of nodes in graph
    :param  p           prob of an edge being present
    :return number of rounds to spread rumor
    """
    graph = dict()
    for i in range(num_nodes):
        graph[i] = range(i) + range(i + 1, num_nodes)

    gossip_nodes = set()
    gossip_nodes.add(0)

    num_rounds = 0
    while len(gossip_nodes) < num_nodes:
        round_gossip_nodes = set()
        for i in range(num_nodes):
            if i in gossip_nodes:
                edges = filter(lambda _: random() < p, graph[i])
                if edges:
                    round_gossip_nodes.add(choice(edges))
        gossip_nodes |= round_gossip_nodes
        num_rounds += 1

    return num_rounds


def main():
    num_iters = 1000
    for num_nodes in range(10, 110, 10):
        evolving_rounds = 0
        for i in range(num_iters):
            evolving_rounds += complete_graph(num_nodes)
        evolving_rounds = float(evolving_rounds) / num_iters
        print("nodes={}\tavg rounds={}".format(num_nodes, evolving_rounds))


if __name__ == '__main__':
    main()
