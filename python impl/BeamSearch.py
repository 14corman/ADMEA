# -*- coding: utf-8 -*-
"""
Created on Sat Mar 10 20:39:17 2018

@author: Cory Edwards
"""

from math import log

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