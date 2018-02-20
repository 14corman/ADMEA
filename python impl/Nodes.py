class Nodes:
	"""The class that takes care of all the Node(s) in the system.
	lru is from (from functools import lru_cache)"""
	
	@lru_cache(maxsize = 1000)
	def getNode(item, type = 'id'):
		if(type is 'id')
			return 'get a node by id here'
		else if(type is 'state')
			return 'get node by state here'
	
	def saveNode(node):
		"""Go through and write this node out to memory, clear it from cache, and call its parents to update."""
		return node.id