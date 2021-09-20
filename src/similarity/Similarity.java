package similarity;

public class Similarity
{
	public static double calculateSimilarity(String resourceUri1, String resourceUri2, ESimilarity strategy) throws UnsupportedOperationException
	{
		if (strategy.equals(ESimilarity.LITERAL))
			return LiteralSim.calculateSimilarity(resourceUri1, resourceUri2);
		else if (strategy.equals(ESimilarity.LDSD))
			return 1 - LDSD.LDSDweighted(resourceUri1, resourceUri2);
		else
			throw new UnsupportedOperationException();
	}
}
