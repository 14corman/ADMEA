# -*- coding: utf-8 -*-
"""
Created on Mon Mar 26 18:12:07 2018

@author: Cory
"""
import ASystem
import NodesBuilder as Nodes

class SmartAI(ASystem):
    def isSuccess(self, lastNode, newState):
        winningChild = lastNode.getMaxQChild()
        currentChild = Nodes.Nodes.getNode(newState, 'state').getNodeId()
        if winningChild == None:
            return False
        else:
            return winningChild == currentChild
        
    def calculateScore(self, state):
        self.calculdateDifference(state)
        
    def areStatesEqual(self, stateA, stateB):
        return stateA == stateB
        
    def setChildren(self, state, parent):
        self.calculateOptions(state, self.setGeneration(state), parent)
        
    def calculdateOptions(state, computerCells, parent):
        