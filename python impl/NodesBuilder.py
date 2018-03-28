# -*- coding: utf-8 -*-
"""
Created on Sat Mar 10 20:39:17 2018

@author: Cory
"""
from functools import lru_cache
from Nodes import getNode

	
class Nodes:
    """The class that takes care of all the Node(s) in the system.
    lru is from (from functools import lru_cache)"""
    
    def __init__(self):
        self.nodes = {}
        
    @lru_cache(maxsize = 1000)
    def getNode(self, item, type = 'id'):
	    if type is 'id':
		    return 'get a node by id here'
	    elif type is 'state':
		    return 'get node by state here'
	
    def saveNode(self, node):
        """Go through and write this node out to memory, clear it from cache, and call its parents to update."""
        getNode.cache_clear()
        for parent in node.parents:
            parent.update()
        return node.id

    def getNodes(self):
        return self.nodes