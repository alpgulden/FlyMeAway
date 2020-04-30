package PathFinding_Astar;


import java.util.*;

public class Node {
	
	// Attributes
	private Object element;
	private boolean visited;
	private List<Node> neighbours;
	
	
	//new
	private double hValue;
	private double gValue;
	private double fValue;
	
	// Methods 
 
	/////////////////////////////NEW NEW NEW //////////////////////////
	public Node(Object e) {
		this.element = e;
		this.visited = false;
		this.neighbours = new ArrayList<>();
		this.hValue = 0;
		this.gValue = 0;
		this.fValue = 0;
	}
	
	public void sethValue(double hValue) {
		this.hValue = hValue;
	}
	
	public double gethValue() {
		return this.hValue;
	}
	
	public double getgValue() {
		return gValue;
	}

	public void setgValue(double gValue) {
		this.gValue = gValue;
	}

	public double getfValue() {
		return fValue;
	}

	public void setfValue(double fValue) {
		this.fValue = fValue;
	}
	/////////////////////////////End new //////////////////////////
	
	


	public boolean isVisited() {
		return this.visited;
	}
	
	public void addNeighbour(Node n) {
		this.neighbours.add(n);
	}

	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public List<Node> getNeighbours() {
		return neighbours;
	}

	public void setNeighbours(List<Node> neighbours) {
		this.neighbours = neighbours;
	}
	
	public void setVisited( boolean status  ) {
		this.visited = status;
	}
	

}