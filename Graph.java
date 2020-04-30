package PathFinding_Astar;

import java.util.*;

public class Graph {
	// Attributes 
	private List<Node> allNodes;
	
	
	// Methods 
	public Graph() {
		this.allNodes = new ArrayList<>();
	}
	
	public void setAllNodes(List<Node> allNodes) {
		this.allNodes = allNodes;
	}
	
	public List<Node> getAllNodes(){
		return this.allNodes;
	}

}
