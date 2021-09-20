package similarity;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ResourceImpl;

import database.DBFunctions;
import model.User;
import util.StringUtilsNode;
import node.NodeUtil;
import node.SparqlWalk;

public class PLDSD {
	
	private static DBFunctions dbFunctions = null;

	public static synchronized DBFunctions getDatabaseConnection()
	{
		if(dbFunctions==null)
		{
			dbFunctions = new DBFunctions();
		}
		return dbFunctions;
	}
	
	
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
	public static String resourceI = "http://dbpedia.org/resource/Jurassic_Shark";
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
	
	
	public static void main(String[] args) {	
		
		
		//System.out.println(LDSD.LDSDweighted("http://dbpedia.org/resource/California","http://dbpedia.org/resource/Nevada"));
		
		//String resources[] = {resourceA,resourceB,resourceC,resourceD,resourceE};
		//String resources[] = {resourceJC,resourceEP,resourceKK};
		//simVSMbyProperty(resources);
		
		//System.out.println(LDSDweighted(resourceJC,resourceJC));
		//System.out.println(LDSDweighted("http://dbpedia.org/resource/Johnny_Cash","http://dbpedia.org/resource/June_Carter_Cash"));
		//System.out.println(LDSDweighted("http://dbpedia.org/resource/Johnny_Cash","http://dbpedia.org/resource/Bob_Dylan"));
		//System.out.println(LDSDweighted("http://dbpedia.org/resource/Johnny_Cash","http://dbpedia.org/resource/Al_Green"));
		//System.out.println(LDSDweighted("http://dbpedia.org/resource/Johnny_Cash","http://dbpedia.org/resource/Elvis_Presley"));
		//System.out.println(LDSDweighted("http://dbpedia.org/resource/Diary","http://dbpedia.org/resource/Fight_Club"));
		//System.out.println(LDSDweighted(resourceJC,resourceJCC));
		//System.out.println(LDSDweighted(resourceKK,resourceJC));
		//System.out.println(LDSDweighted(resourceJC,resourceEP));
		
		
		
		//System.out.println("LDSDdirect	"+LDSDweighted("http://dbpedia.org/resource/Argentina","http://dbpedia.org/resource/Frank_Sinatra"));
		long init = System.currentTimeMillis();
		System.out.println("LDSDweighted	"+LDSDweighted("http://dbpedia.org/resource/Brazil","http://dbpedia.org/resource/Argentina"));
		//System.out.println("LDSDweighted	"+LDSDweighted("http://dbpedia.org/resource/Argentina","http://dbpedia.org/resource/Brazil"));
		NodeUtil.print("time: "+(StringUtilsNode.getDuration(System.currentTimeMillis() - init)));
		
/*		System.out.println("LDSDdirect	"+LDSDdirect("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));
		System.out.println("LDSDIndirect	"+LDSDIndirect("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));
		
		System.out.println("LDSDdirect	"+LDSDdirect("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));
		System.out.println("LDSDIndirect	"+LDSDIndirect("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));		*/
		
		
		//System.out.println("LDSDDirectweighted "+LDSDDirectweighted("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));
		
/*		System.out.println("LDSDIndirect	"+LDSDIndirect("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));
		System.out.println("LDSDIndirectweighted "+LDSDIndirectweighted("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));
		
		System.out.println("LDSDweighted	"+LDSDweighted("http://learningsparql.com/ns/expenses#r1","http://learningsparql.com/ns/expenses#r2"));	*/
		
		
		
		
		
	}
	public static List<VSMSimilarity> simVSMbyProperty(String[] resources){
		
		List<VSMSimilarity> vSMSimilaritys = new ArrayList<VSMSimilarity>();
		
		List<String> control = new ArrayList<String>();

		for (String r1 : resources) {
			for (String r2 : resources) {
				if (r1!=r2  && (!(control.contains(r2+r1)||control.contains(r1+r2)))) {
					VSMSimilarity vsmSimilarity = new VSMSimilarity(new ResourceImpl(r1), new ResourceImpl(r2));
					vsmSimilarity.setSimScore(LDSDweighted(r1,r2));
					vSMSimilaritys.add(vsmSimilarity);
				}
				control.add(r1+r2);
				control.add(r2+r1);
			}
		}
		
		for (VSMSimilarity vsmSimilarity : vSMSimilaritys) {
			System.out.println("VSM:"+vsmSimilarity.getResource1().getLocalName()+ " X " + vsmSimilarity.getResource2().getLocalName()+ " has score == "+String.format( "%.2f", (vsmSimilarity.getSimScore())));				
		}

		return vSMSimilaritys;
	}	

	/**
	 * @author gbrlamota
	 * @param resourceA
	 * @param resourceB
	 * @param user
	 * @return
	 */
/*	public static double PersonalizedLDSD(String resourceA, String resourceB, User user){
		if (resourceA.equals(resourceB)) {
			return 0d;
		}
		double ldsd = 1d / ((double)(1d + (double)nDDLR(resourceA,resourceB, user) + (double)nDDLR(resourceB,resourceA, user) + (double)nIDLI(resourceA,resourceB) + (double)nIDLO(resourceB,resourceA)));
		//System.out.println("LDSDweighted:"+ldsd);
		return ldsd;
	}*/
	
	/**
	 * @author gbrlamota
	 * @param resourceA
	 * @param resourceB
	 * @param user
	 * @return
	 */
	public static double nDDLRP(String resourceA, String resourceB, User user){
		double sum = 0d;
		List<Resource> resources = new ArrayList<Resource>();
		resources = SparqlWalk.getDirectLinksBetween2Resources(resourceA,resourceB);
		for (Resource resource : resources) {
			
			//TODO: substituir o peso que aqui eh calculado pela contagem de links do mesmo tipo pelo peso personalizado da propriedade
			sum = sum + ((resources.size()/ (1 + Math.log(SparqlWalk.countTotalDirectLinksFromResourceAndProperty(resourceA,resource.getURI())))));
		}
		return sum; 
	}
	
	public static double PLDSDweighted(String resourceA, String resourceB, User user){
		if (resourceA.equals(resourceB)) {
			return 0d;
		}
		double ldsd = 1d / ((double)(1d + (double)nDDLR(resourceA,resourceB,user) + (double)nDDLR(resourceB,resourceA,user) + (double)nIDLI(resourceA,resourceB,user) + (double)nIDLO(resourceB,resourceA,user)));
		//System.out.println("LDSDweighted:"+ldsd);
		return ldsd;
	}

	
	public static double LDSDweighted(String resourceA, String resourceB){
		if (resourceA.equals(resourceB)) {
			return 0d;
		}
		double ldsd = 1d / ((double)(1d + (double)nDDLR(resourceA,resourceB) + (double)nDDLR(resourceB,resourceA) + (double)nIDLI(resourceA,resourceB) + (double)nIDLO(resourceB,resourceA)));
		//System.out.println("LDSDweighted:"+ldsd);
		return ldsd;
	}

	public static double LDSDdirect(String resourceA, String resourceB){
		double ldsd = 1d / ((double)(1d + (double)SparqlWalk.countDirectLinksBetween2Resources(resourceA, resourceB)+(double)SparqlWalk.countDirectLinksBetween2Resources(resourceB, resourceA)));
		//System.out.println("LDSDdirect:"+ldsd);
		return ldsd;
	}
	
	public static double LDSDIndirect(String resourceA, String resourceB){
		double ldsd = 1d / ((double)(1d + (double)SparqlWalk.countTotalNumberOfIndirectInconmingLinksBetween2Resources(resourceA, resourceB)+ (double)SparqlWalk.countTotalNumberOfIndirectOutgoingLinksBetween2Resources(resourceA, resourceB)));
		//System.out.println("LDSDindirect:"+ldsd);
		return ldsd;
	}
	
	public static double LDSDDirectweighted(String resourceA, String resourceB){
		double ldsd = 1d / ((double)(1d + (double)nDDLR(resourceA,resourceB) + (double)nDDLR(resourceB,resourceA)));
		//System.out.println("LDSDweighted:"+ldsd);
		return ldsd;
	}	
	
	public static double LDSDIndirectweighted(String resourceA, String resourceB){
		double ldsd = 1d / ((double)(1d + (double)nIDLI(resourceA,resourceB) + (double)nIDLO(resourceB,resourceA)));
		//System.out.println("LDSDweighted:"+ldsd);
		return ldsd;
	}
	
	/**
	 * @author gbrlamota
	 * @param resourceA
	 * @param resourceB
	 * @param user
	 * @return
	 */
	public static double nDDLR(String resourceA, String resourceB, User user){
		double sum = 0d;
		double weight = 0d;
		int nLinks = 0;
		
		List<Resource> resources = new ArrayList<Resource>();
		//computes how many links exist from resourceA to recourceB (returns all)
		resources = SparqlWalk.getDirectLinksBetween2Resources(resourceA,resourceB);
		
		DBFunctions dbFunctions = getDatabaseConnection();
		
		for (Resource resource : resources) {
			synchronized (dbFunctions)
			{
				//if weight = 0 the link is not being computed
				//changed for: if the weight is 0.5 for instance, then this property has 1.5x chances to influence tha calculation 
				weight = dbFunctions.getPropertyWeightByUser(user, resource.getURI()) + 1;
			}
			nLinks = SparqlWalk.getCountDirectLinkFrom2ResourcesAndLink(resourceA,resourceB,resource.getURI());
			if (nLinks != resources.size())
				System.out.println("ENCONTREI O ERRO!");
				
			 //TODO: debugar para ver se a quantidade de links (resources.size()) é sempre a mesma ou se muda de acordo com o link 
			sum = sum + (((nLinks * weight)/ (1 + Math.log(SparqlWalk.countTotalDirectLinksFromResourceAndProperty(resourceA,resource.getURI())))));
			//sum = sum + ( nLinks / (1 + Math.log(SparqlWalk.countTotalDirectLinksFromResourceAndProperty(resourceA,resource.getURI()))));
		}
		return sum; 
	}
	
	/**
	 * DONE
	 * @param resourceA
	 * @param resourceB
	 * @param linkInstance
	 * @return
	 * erro!!!
	 */
	public static double nDDLR(String resourceA, String resourceB){
		double sum = 0d;
		
		List<Resource> resources = new ArrayList<Resource>();
		//computes how many links exist from resourceA to recourceB (returns all)
		resources = SparqlWalk.getDirectLinksBetween2Resources(resourceA,resourceB);
		
		for (Resource resource : resources) {
			
			 //TODO: debugar para ver se a quantidade de links (resources.size()) é sempre a mesma ou se muda de acordo com o link 
			sum = sum + ((resources.size()/ (1 + Math.log(SparqlWalk.countTotalDirectLinksFromResourceAndProperty(resourceA,resource.getURI())))));
		}
		return sum; 
	}
	
	/**
	 * @author gbrlamota
	 * @param resourceA
	 * @param resourceB
	 * @param user
	 * @return
	 */
	public static double nIDLO(String resourceA, String resourceB, User user){
		double sum = 0d;
		double weight = 0d;
		List<Resource> resources = new ArrayList<Resource>();
		resources = SparqlWalk.getIndirectDistinctOutgoingLinksBetween2Resources(resourceA,resourceB);
		
		DBFunctions dbFunctions = getDatabaseConnection();
		
		for (Resource resource : resources) {
			synchronized (dbFunctions)
			{
				//if weight = 0 the link is not being computed
				//changed to: if the weight is 0.5 for instance, then this property has 1.5x chances to influence tha calculation 
				weight = dbFunctions.getPropertyWeightByUser(user, resource.getURI()) + 1;
			}
		
			sum = sum + ((SparqlWalk.getCountIndirectOutgoingLinkFrom2ResourcesAndLink(resourceA, resourceB,resource.getURI()) * weight) 
					/ (1 + Math.log(SparqlWalk.countIndirectOutgoingLinksFromResourceAndLink(resourceA,resource.getURI()))));
		}
		return sum; 
	}
	
	/**
	 * DONE
	 * Function that compute the number of Indirect and Distinct Outcome Links
	 * @param resourceA
	 * @param resourceB
	 * @param linkInstance
	 * @return
	 */
	public static double nIDLO(String resourceA, String resourceB){
		double sum = 0d;
		List<Resource> resources = new ArrayList<Resource>();
		resources = SparqlWalk.getIndirectDistinctOutgoingLinksBetween2Resources(resourceA,resourceB);
		for (Resource resource : resources) {		
			sum = sum + (SparqlWalk.getCountIndirectOutgoingLinkFrom2ResourcesAndLink(resourceA, resourceB,resource.getURI()) / (1 + Math.log(SparqlWalk.countIndirectOutgoingLinksFromResourceAndLink(resourceA,resource.getURI()))));
		}
		return sum; 
	}
	
	/**
	 * @author gbrlamota
	 * @param resourceA
	 * @param resourceB
	 * @param user
	 * @return
	 */
	public static double nIDLI(String resourceA, String resourceB, User user){
		double sum = 0d;
		double weight = 0d;
		
		List<Resource> resources = new ArrayList<Resource>();
		resources = SparqlWalk.getIndirectDistinctInconmingLinksBetween2Resources(resourceA,resourceB);
		
		DBFunctions dbFunctions = getDatabaseConnection();
		
		for (Resource resource : resources) {
			synchronized (dbFunctions)
			{
				//if weight = 0 the link is not being computed
				//changed to: if the weight is 0.5 for instance, then this property has 1.5x chances to influence tha calculation 
				weight = dbFunctions.getPropertyWeightByUser(user, resource.getURI()) + 1; 
			}
	
			sum = sum + ((SparqlWalk.getCountIndirectIncomingLinkFrom2ResourcesAndLink(resourceA,resourceB,resource.getURI()) * weight )
					/ (1 + Math.log(SparqlWalk.countIndirectIncomingLinksFromResourceAndLink(resourceA,resource.getURI()))));			
		}
		
		//System.out.println("sumFinal"+sum);
		return sum; 
	}	
	
	/**
	 * DONE
	 * Function that compute the number of Indirect and Distinct Income Links
	 * @param resourceA
	 * @param resourceB
	 * @param linkInstance
	 * @return
	 */
	public static double nIDLI(String resourceA, String resourceB){
		double sum = 0d;
		
		List<Resource> resources = new ArrayList<Resource>();
		resources = SparqlWalk.getIndirectDistinctInconmingLinksBetween2Resources(resourceA,resourceB);
		for (Resource resource : resources) {
			sum = sum + (SparqlWalk.getCountIndirectIncomingLinkFrom2ResourcesAndLink(resourceA,resourceB,resource.getURI()) / (1 + Math.log(SparqlWalk.countIndirectIncomingLinksFromResourceAndLink(resourceA,resource.getURI()))));			
		}
		
		//System.out.println("sumFinal"+sum);
		return sum; 
	}	

}
