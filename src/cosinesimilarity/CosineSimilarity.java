package cosinesimilarity;

import org.apache.commons.math3.linear.RealVector;

public class CosineSimilarity {
	
	public static double CosineSimilarity(DocVector d1, DocVector d2) {
		double cosinesimilarity;
		try {
			cosinesimilarity = (((RealVector) d1.vector).dotProduct(d2.vector)) / (d1.vector.getNorm() * d2.vector.getNorm());
		} catch (Exception e) {
			return 0.0;
		}
		return cosinesimilarity;
	}
}
