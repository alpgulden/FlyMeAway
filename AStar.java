package PathFinding_Astar;

import java.util.*;

public class AStar {

	private int startNodeIndex;
	private int targetNodeIndex;
	
	private PriorityQueue<Node> openSet;
	private boolean[] closedSet;
	
	public AStar(){
		openSet = new PriorityQueue<Node>( (Node n1, Node n2)    -> {
			if ( n1.getfValue() < n2.getfValue() ) {
				return -1;   
			}
			
			else if(n1.getfValue() > n2.getfValue()) {
				return 1; 
			}
			else {
				return 0; 
			}
		});
		
	}
	
	public List<Node> search(Graph myGraph , 
	                 int startNodeIndex , int targetNodeIndex) {
		List<Node> nodeVisited = new ArrayList<>();
		this.startNodeIndex = startNodeIndex;
		this.targetNodeIndex = targetNodeIndex;
 
		List<Node> nodeList = myGraph.getAllNodes();
		
		closedSet = new boolean[nodeList.size()];
		
		Node root = nodeList.get(this.startNodeIndex - 1);
		
		
		List<Node> neighbours = null;
		root.setgValue(0);
		root.setVisited(true);
		openSet.add(root);
		Node current;
		
		while(true) {
			current = openSet.poll();
			nodeVisited.add(current);
			
			if (current == null) {
				break;
			}
			
			this.closedSet[(int) current.getElement()-1] = true;
			
			if((int) current.getElement() == this.targetNodeIndex ) {
				return nodeVisited;
			}

			neighbours = current.getNeighbours();
			
			for (Node n : neighbours) {
				updateOpenSetIfNeeded(current, n);
			}
		}

		return nodeVisited;
	}
	
	
	public void updateOpenSetIfNeeded(Node current, Node n) {
		
		double Ver_Hor_COST = 1;
 
		if (n == null || closedSet[(int) n.getElement() - 1] 
		              || n.isVisited())
			return;
		
		boolean isOpen = openSet.contains(n);
		
		if(!isOpen ) {
			n.setgValue(current.getgValue() + Ver_Hor_COST);
			n.setfValue(n.getgValue() + n.gethValue()); 
			openSet.add(n);
		}
		
	}
	
}


 
