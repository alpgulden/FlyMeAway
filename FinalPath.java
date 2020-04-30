package PathFinding_Astar;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class FinalPath extends JComponent{
	
	private List<Node> nodeVisited;
	private PlanningGraph planningBase;
	
	public FinalPath(PlanningGraph planningBase , List<Node> nodeVisited) {
		this.planningBase = planningBase;
		this.nodeVisited = nodeVisited; 
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int planningBaseColNo = planningBase.getColNo();
		int planningBaseRowNo = planningBase.getRowNo();
		
		
		int blockSize = planningBase.getBlockSize();
		
		for (int i = 0; i < planningBaseRowNo; i++) {
            for (int j = 0; j < planningBaseColNo; j++) {
                Color color;
                switch (planningBase.getPlanningGraph()[i][j]) {
                    case 1 : // Walls
                    	color = Color.GRAY; 
                    	break;
                    case -1: // Start
                    	color = Color.RED;
                    	break;
                    case 9 :  // End
                    	color = Color.BLUE; 
                    	break;
                    default : // Others
                    	color = Color.WHITE;
                }
                g2.setColor(color);
                g2.fillRect(blockSize * j, blockSize * i, blockSize, blockSize);
                
                g2.setColor(Color.BLACK);
                g2.drawRect(blockSize * j, blockSize * i, blockSize, blockSize);
            }
        }
		
		
		int idx, i , j;
		int nodeOrder = 0;
		
		for (Node n : nodeVisited) {
			
			idx =  (int) n.getElement();
			i = idx / (planningBaseColNo);
			j = idx % (planningBaseColNo)-1;
			g2.setColor(Color.ORANGE);
            g2.fillRect(blockSize * j, blockSize * i, blockSize, blockSize);
            	
            g2.setColor(Color.BLACK);
            g2.drawString( nodeOrder+ "", blockSize * j + blockSize/2, blockSize * i + blockSize/2);
            nodeOrder++;
		}
		
		Node n = nodeVisited.get(0);
		idx =  (int) n.getElement();
		i = idx / (planningBaseColNo);
		j = idx % (planningBaseColNo)-1;
		g2.setColor(Color.RED);
        g2.fillRect(blockSize * j, blockSize * i, blockSize, blockSize);
        
		// NEW
		g2.setColor(Color.BLACK);
        g2.drawString(0+ "", blockSize * j + blockSize/2, 
		               blockSize * i + blockSize/2);
        
		// Last block blue
        n = nodeVisited.get(nodeVisited.size()-1);
		idx =  (int) n.getElement();
		i = idx / (planningBaseColNo);
		j = idx % (planningBaseColNo)-1;
		g2.setColor(Color.BLUE);
		g2.fillRect(blockSize * j, blockSize * i, blockSize, blockSize);

	}
	
	
 
	

}
