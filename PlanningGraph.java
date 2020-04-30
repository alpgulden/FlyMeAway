package PathFinding_Astar;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
 
public class PlanningGraph extends JComponent {
	
	//TODO : planningBase need to defined by the planning pyhton code here
 
	private Node[][] nodesMatrix;
	private Graph g;
	
	private int colNo;
	private int rowNo;
	
	private int blockSize;
	
	public PlanningGraph() {
		this.blockSize = 40;
		this.rowNo = planningBase .length;
		this.colNo = planningBase[0].length;
		this.g = new Graph();
		nodesMatrix = new Node[this.rowNo][this.colNo];
		
		int idx = 1;
		for (int i = 0; i < this.rowNo; i++) {
            for (int j = 0; j < this.colNo; j++) {
            	this.nodesMatrix[i][j] = new Node (idx);
            	idx++;
            }
        }
 
		createGraph();

	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
 
		
		// draw the planningBase 
        for (int i = 0; i < planningBase .length; i++) {
            for (int j = 0; j < planningBase [0].length; j++) {
                Color color;
                switch (planningBase [i][j]) {
                    case 1 : 
                    	color = Color.GRAY; 
                    	break;
                    case -1: // Start
                    	color = Color.RED;
                    	break;
                    case 9 : 
                    	color = Color.BLUE; 
                    	break;
                    default : 
                    	color = Color.WHITE;
                }
                g2.setColor(color);
                g2.fillRect(this.blockSize * j, this.blockSize * i, this.blockSize, this.blockSize);
                g2.setColor(Color.GREEN);
                g2.drawString(this.nodesMatrix[i][j].getElement()+ " ", this.blockSize * j + this.blockSize/2, this.blockSize * i + this.blockSize/2);
                g2.setColor(Color.BLACK);
                g2.drawRect(this.blockSize * j, this.blockSize * i, this.blockSize, this.blockSize);
            }
        } 
	}
	
	private void createGraph() {
		List<Node> allNodes = new ArrayList<>();
		/////////////////////////////////NEW NEW NEW //////////////////////////////////////////////// 
		int idx = getTargetPoint();
		int iTarget  = idx / (this.colNo);
		int jTarget  = idx % (this.colNo)-1;
		
		for (int i = 0; i < this.rowNo; i++) {
            for (int j = 0; j < this.colNo; j++) {
        /////////////////////////////////NEW NEW NEW ////////////////////////////////////////////////
            	double dx = i - iTarget;
            	double dy = j - jTarget;
            	this.nodesMatrix[i][j].sethValue(Math.sqrt(dx*dx + dy*dy)); 
        /////////////////////////////////NEW NEW NEW //////////////////////////////////////////////// 
            	if (this.planningBase [i][j] == 1) {
            		nodesMatrix[i][j].setVisited(true);
            	}
            	findAddNeighbours(i,j);
            	allNodes.add(nodesMatrix[i][j]);
            }
        }
			
		g.setAllNodes(allNodes);
	}
 

	private void findAddNeighbours(int  row, int col) {
		int colNum = col ;
	    int rowNum = row ;
        if(withinGrid (colNum+1, rowNum , this.rowNo, this.colNo)) {
        	nodesMatrix[row][col].addNeighbour(this.nodesMatrix[rowNum][colNum+1]); 
        }
        if(withinGrid (colNum, rowNum+1 , this.rowNo, this.colNo)) {
        	nodesMatrix[row][col].addNeighbour(this.nodesMatrix[rowNum+1][colNum]); 
        }
        if(withinGrid (colNum-1, rowNum , this.rowNo, this.colNo)) {
        	nodesMatrix[row][col].addNeighbour(this.nodesMatrix[rowNum][colNum-1]); 
        }
        if(withinGrid (colNum, rowNum-1 , this.rowNo, this.colNo)) {
        	nodesMatrix[row][col].addNeighbour(this.nodesMatrix[rowNum-1][colNum]); 
        }
	}
	                

	private boolean withinGrid(int colNum, int rowNum, int maxRow, int maxCol) {

	    if((colNum < 0) || (rowNum <0) ) {
	        return false;     
	    }
	    if((colNum >= maxCol) || (rowNum >= maxRow)) {
	        return false;     
	    }
	    return true;
	}

	public Graph getGraph() {
		return this.g;
	}
	
	public int getColNo() {
		return colNo;
	}

	public int getRowNo() {
		return rowNo;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public int[][] getPlanningGraph() {
		return planningBase ;
	}
	
	public int getStartingPoint() {
		int idx = 1;
		for (int i = 0; i < planningBase .length; i++) {
            for (int j = 0; j < planningBase [0].length; j++) {
            	idx++;
            	if (planningBase [i][j] == -1) {
            		return idx-1;
            	}
            }
		}
		return 1;
	}
	
	public int getTargetPoint() {
		int idx = 1;
		for (int i = 0; i < planningBase .length; i++) {
            for (int j = 0; j < planningBase [0].length; j++) {
            	idx++;
            	if (planningBase [i][j] == 9) {
            		return idx-1;
            	}
            }
		}
		return 1;
	}


	
	
/////////////////////////////////NEW NEW NEW //////////////////////////////////////////////// 
	public Node[][] getNodeMatrix() {
		return this.nodesMatrix;
	}	
/////////////////////////////////NEW NEW NEW //////////////////////////////////////////////// 
	
}

