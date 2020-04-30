package PathFinding_Astar;

import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.regex.*;


public class WebCrawler {
    
    public static void main(String[] args)        
	{
			// Draw the planningBase
			JFrame f = new JFrame("PlanningGraph");
			f.setSize(1000, 1000);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			PlanningGraph planningBase = new PlanningGraph();
			f.add(planningBase);
			f.setVisible(true);

			Graph g = planningBase.getGraph();

			List<Node> nodeVisited = new ArrayList<>();

			// New 
			JFrame f3 = new JFrame("Heuristic heatmap");
			f3.setSize(1000, 1000);
			f3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			DrawHeuristic d = new DrawHeuristic(planningBase);
			f3.add(d);
			f3.setVisible(true);


			AStar aStatSearchEngine = new AStar();
			nodeVisited = aStatSearchEngine.search(g,
							 planningBase.getStartingPoint(), planningBase.getTargetPoint());

			//for (Node n : nodeVisited) {
			//	System.out.println("Node" +n.getElement().toString());
			//}

			System.out.println("A* Search is done!");

			FinalPath finalPath = new FinalPath(planningBase, nodeVisited);

			JFrame f4 = new JFrame("A*");
			f4.setSize(1000, 1000);
			f4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f4.add(finalPath);
			f4.setVisible(true);
		}
	}

}