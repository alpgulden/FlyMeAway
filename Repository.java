package PathFinding_Astar;

import java.util.*;

public class Repository {
	
	private Queue<String> bfsQueue = new LinkedList<>();
	private Set<String> urlVisitedSet = new HashSet<>();
	private int urlNo;
	
	public Repository() {
		Queue<String> bfsQueue = new LinkedList<>();
		Set<String> urlVisitedSet = new HashSet<>();
		this.urlNo = 0;
	}
	
	public void addURLtoQueue(String url) {
		bfsQueue.add(url);
	}
	
	public String removeURLfromQueue() {
		return bfsQueue.poll();
	}
	
	public void addURLtoSet(String url) {
		urlVisitedSet.add(url);
		this.urlNo++;
	}
	
	public int urlNo() {
		return this.urlNo;
	}
	
	public boolean isQueueEmpty() {
		return bfsQueue.isEmpty();
	}
	
	public boolean setHasURL(String url) {
		return urlVisitedSet.contains(url);
	}

	public Set<String> getUrlVisited() {
		return urlVisitedSet;
	}

}
