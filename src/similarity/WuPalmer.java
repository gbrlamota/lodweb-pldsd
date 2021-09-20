package similarity;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ResourceImpl;

import node.SparqlWalk;

public class WuPalmer
{
	
	public static String resourceA = "http://dbpedia.org/resource/Finding_Nemo";

	public static String resourceB = "http://dbpedia.org/resource/Andrew_Stanton";
	public static String resourceB1 = "http://dbpedia.org/resource/Rockport,_Massachusetts";
	public static String resourceB2 = "http://dbpedia.org/resource/Film_director";

	public static String resourceC = "http://dbpedia.org/resource/Walt_Disney_Studios_Motion_Pictures";
	public static String resourceC1 = "http://dbpedia.org/resource/Division_(business)";
	public static String resourceC2 = "http://dbpedia.org/resource/Touchstone_Pictures";
	
	public static String resourceD = "http://dbpedia.org/resource/Willem_Dafoe";
	public static String resourceD2 = "http://dbpedia.org/resource/Appleton,_Wisconsin";
	public static String resourceD1 = "http://dbpedia.org/resource/Giada_Colagrande";

	public static String resourceE = "http://dbpedia.org/resource/Finding_Dory";
	public static String resourceF = "http://dbpedia.org/resource/Diane_Keaton";
	
	public static String resourceG = "http://dbpedia.org/resource/The_Matrix";
	public static String resourceH = "http://dbpedia.org/resource/Category:American_films";
	//public static String resourceH = "http://dbpedia.org/resource/Category:Drone_films";	
	public static String resourceI = "http://dbpedia.org/resource/Jurassic_World";
	public static String resourceJ = "http://dbpedia.org/resource/Albert_Brooks";
	
	
	
	
	
	//static String resourceC = "http://dbpedia.org/resource/25_Years_On";
	
	
/*
	public static String resourceE = "http://dbpedia.org/resource/Peru";
	public static String resourceF = "http://dbpedia.org/resource/Germany";
	public static String resourceG = "http://dbpedia.org/resource/France";
	public static String resourceH = "http://dbpedia.org/resource/Algeria";
	public static String resourceI = "http://dbpedia.org/resource/Crete";
	public static String resourceJ = "http://dbpedia.org/resource/Federalism";*/
	
	static String resourceJC = "http://dbpedia.org/resource/Johnny_Cash";
	static String resourceJCC = "http://dbpedia.org/resource/June_Carter_Cash";
	static String resourceKK = "http://dbpedia.org/resource/Al_Green";
	static String resourceEP = "http://dbpedia.org/resource/Elvis_Presley";
	
	
	public static void main(String[] args)
	{
		String resource01 = "http://dbpedia.org/resource/Back_to_the_Future";
		String resource02 = "http://dbpedia.org/resource/Ice_Age_(2002_film)";
		calculateSimilarity(resource02, resourceI);
	}
	
	public static double calculateSimilarity(String resourceURI1, String resourceURI2)
	{
		
		Resource mostSpecificSuperclass = SparqlWalk.getMostSpecificCommonSuperclassDbpedia(resourceURI1, resourceURI2);

		Resource root = SparqlWalk.getRootClass(mostSpecificSuperclass.getURI());

		return 0;
	}
}
