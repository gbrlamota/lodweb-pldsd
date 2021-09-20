package similarity;

public class LDSD_LIT
{
	public static double calculateSimilarity(double similarityLITERAL, double similarityLDSD, double alpha)
	{
		return (alpha * similarityLITERAL) + ((1d - alpha) * similarityLDSD);
	}
}
