# -*- coding: utf-8 -*-
"""
Created on Sat Mar 10 20:39:17 2018

@author: Cory
"""
from scipy.stats import binom

class Node:
   """A Node in the system"""
	
   def __init__(self, p = 1.0, id = 0, state = '', score = None):
	    """Create a new Node with p, id, state, and score."""
	    self.p = p
	    self.n = 0
	    self.k = 0
	    self.id = id
	    self.children = []
	    self.policy = {}
	    self.state = state
	    self.parents = []
	    self.score = score
	    self.isNew = True
        
   def __str__(self):
        return "id = {0} & score = {1}".format(self.id, self.score)
		
   def addParent(self, parent):
	   """Add a parent for this Node"""
	   self.parents.append(parent)
       
   def addChild(self, child):
       """Add a child to this Node"""
       self.children.append(child)
		
   def givePolicy(self, policy):
       """Give a newly obtained policy from a Q learning model."""
       self.policy = policy
		
   def getPolicy(self):
       return self.policy
		
   def getN(self):
	   return self.n
		
   def getK(self):
	   return self.k
		
   def getMaxQChild(self):
	   return max(self.policy, key = self.policy.get)
		
   def getMaxQScore(self):
	   return self.policy[self.getMaxQChild()]
		
   def getState(self):
	   return self.state
		
   def isNew(self):
       temp = self.isNew
       self.isNew = temp if temp == False else False
       return self.isNew
		
   def addOccurance(self):
       self.n += 1
		
   def addSuccess(self):
       self.k += 1
		
   def recalProb(self):
       self.p = self.k / self.n
		
   def giveProb(self, prob = 1.0):
       self.p = prob
		
   def getScore(self):
       return self.score
		
   def calculateProbability(self):
       """Calculate the binomial probability given n attempts, k successes, and probability of success p.
       from scipy.stats import binom
       https://docs.scipy.org/doc/scipy/reference/generated/scipy.stats.binom.html"""
       return binom.cdf(self.k, self.n, self.p)
		
   def getNodeId(self):
       return self.id
		
   def update(self):
       """Perform an iteration of Q learning on this Node then call its parents to update.
       (As a test later try and create a heap of parents then perform Q learning all at once and administer policies.)"""
       print("update")
		