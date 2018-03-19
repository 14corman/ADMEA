# -*- coding: utf-8 -*-
"""
Created on Sat Mar 10 20:39:17 2018

@author: Cory Edwards
"""
from math import log
import random
from NodeBuilder import Node

# beam search
def beamSearch(data, startIndex, k = 5, acceptStartPaths = False):
    paths = [[list(), 1.0, startIndex]]
    
    #Go until all paths are at a leaf in the graph
    while True:
        possiblePaths = list()
        hasPossiblePath = False
        
        # Go over each possible path
        for i in range(len(paths)):
            path, score, nextNodeId = paths[i]
            node = data[nextNodeId]
            if node.children and nextNodeId not in path[:-1]:
                
                #Go over each child of the current Node
                for child in node.children:
                    if child.id != startIndex:
                        hasPossiblePath = True
                        candidate = [path + [child.id], score * -log(child.score), child.id]
                        possiblePaths.append(candidate)
                        
                    #If allowing paths to return to the initial node is ok then this will get run. 
                    elif acceptStartPaths:
                        candidate = [path, score, nextNodeId]
                        possiblePaths.append(candidate)
            else:
                possiblePaths.append(paths[i])
                
        # order all possible paths by score
        ordered = sorted(possiblePaths, key=lambda tup:tup[1])
        
        # select k best
        paths = ordered[:k]
        if not hasPossiblePath:
            break
    return paths

def testBeamSearch(n = 0, k = 3, acceptStartPaths = False):
    #Initialize the list of Node's
    nodes = {}

    #Randomly initialize the Nodes
    for i in range(10):
        nodes[i] = Node(id = i, state = i, score = random.random() * 10)
    
    #Show the random initialization
    for i in range(len(nodes)):
        print(nodes[i])
    
    #Set up the graph
    nodes[0].addChild(nodes[4])
    nodes[0].addChild(nodes[5])
    nodes[0].addChild(nodes[7])
    nodes[0].addChild(nodes[1])

    nodes[1].addChild(nodes[8])

    nodes[2].addChild(nodes[1])

    nodes[4].addChild(nodes[2])
    nodes[4].addChild(nodes[5])

    nodes[5].addChild(nodes[7])

    nodes[6].addChild(nodes[4])

    nodes[7].addChild(nodes[9])

    #To see what happens if a loop is created, uncomment this line.
    nodes[9].addChild(nodes[0])

    print()

    '''
    Perform beam search with:
        The list of nodes as input
        Use the first n Node from the list
        k for the top k paths
        acceptStartPaths = False so no paths leading back to 0 are selected
    '''
    result = beamSearch(nodes, nodes[n].id, k, acceptStartPaths)

    #Print the k best paths
    for seq in result:
	    print(seq)
    
    #Determine if there is a next best path
    if result[0][0]:
        print(result[0][0][0])
    else:
        print('No child to go to.')