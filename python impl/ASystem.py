# -*- coding: utf-8 -*-
"""
Created on Sat Mar 10 20:39:17 2018

@author: Cory Edwards
"""
import BeamSearch as bs
from NodesBuilder import Nodes
from NodeBuilder import Node
import QLearning as ql

class ASystem(object):
    
    def __init__(self):
        self.nodes = Nodes()
        self.lastNodeId = None
        self.lastStepId = None
        
    def getWinningPath(self, startingNode):
        """Uses Beam Search to get approximate best child."""
        result = bs.beamSearch(self.nodes.getNodes(), startingNode, 15)
        if result[0][0]:
            return result[0][0][0]
        else:
            return None
    
    def performAction(self, state):
        if self.lastNodeId:
            if self.isSuccess(self.nodes.getNode(self.lastStepId), state):
                self.nodes.getNode(self.lastNodeId).addSuccess()
                self.nodes.getNode(self.lastStepId).addSuccess()
        root = self.nodes.getNode(state)
        if root.isNew():
            root.children = self.setChildren(state, root)
            root.setScore(self.calculateScore(state))
            ql.run(root)
        nextState = self.getWinningPath(root.id)
        nextNode = self.nodes.getNode(nextState)
        self.lastStepId = nextNode.id
        self.updateProbabilities()
        self.nodes.saveNode(nextNode)
        self.nodes.saveNode(root)
        
    def updateProbabilities(self):
        self.nodes.getNode(self.lastNodeId).recalcProb()
        self.nodes.getNode(self.lastStepId).recalcProb()
        
    def isSuccess(self, lastNode, newState):
        raise NotImplementedError("Please Implement this method")
        
    def calculateScore(self, state):
        raise NotImplementedError("Please Implement this method")
        
    def areStatesEqual(self, stateA, stateB):
        raise NotImplementedError("Please Implement this method")
        
    def setChildren(self, state, parent):
        raise NotImplementedError("Please Implement this method")
    