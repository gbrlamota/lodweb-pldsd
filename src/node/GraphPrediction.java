package node;


public class GraphPrediction implements Comparable<GraphPrediction> {

	public String getSimilarityMethod() {
		return similarityMethod;
	}

	public void setSimilarityMethod(String similarityMethod) {
		this.similarityMethod = similarityMethod;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getNbr() {
		return nbr;
	}

	public void setNbr(int nbr) {
		this.nbr = nbr;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public int getNrpime() {
		return nrpime;
	}

	public void setNrpime(int nrpime) {
		this.nrpime = nrpime;
	}

	public GraphPrediction(String nodeUnderEvaluation, String testedNeighbour, String originalCandidate, String similarityMethod, int userId,
			int nbr, int n, int nrpime) {
		super();
		this.nodeUnderEvaluation = nodeUnderEvaluation;
		this.testedNeighbour = testedNeighbour;
		this.originalCandidate = originalCandidate;
		this.similarityMethod = similarityMethod;
		this.userId = userId;
		this.nbr = nbr;
		this.n = n;
		this.nrpime = nrpime;
	}

	public String getOriginalCandidate() {
		return originalCandidate;
	}

	public void setOriginalCandidate(String originalCandidate) {
		this.originalCandidate = originalCandidate;
	}

	public String getTestedNeighbour() {
		return testedNeighbour;
	}

	public void setTestedNeighbour(String testedNeighbour) {
		this.testedNeighbour = testedNeighbour;
	}

	public String getNodeUnderEvaluation() {
		return nodeUnderEvaluation;
	}

	public void setNodeUnderEvaluation(String nodeUnderEvaluation) {
		this.nodeUnderEvaluation = nodeUnderEvaluation;
	}

	//liked item of the user which has the LIKE class omitted.
	String originalCandidate;
	
	//one neighbour of the node under evaluation
	String testedNeighbour;

	//one of the 50 nodes under evaluation which will be compared with the original candidate.
	String nodeUnderEvaluation;
	
	String similarityMethod;
	
	int userId;
	
	int nbr;

	int n;
	
	int nrpime;
	
	@Override
	public int compareTo(GraphPrediction o) {
	   if (this.nbr > o.nbr) {
		  return -1;
	   } else if (this.nbr < o.nbr) {
		  return 1;
	   }else{
		  return 0;
	   }
	}
	

	
	
}
