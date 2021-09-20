package similarity;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.jena.datatypes.DatatypeFormatException;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.impl.ResourceImpl;
import org.apache.jena.sparql.engine.http.QueryExceptionHTTP;

import node.SparqlWalk;
import util.LiteralUtil;

public class LiteralSim
{
	static Resource resourceBRA = new ResourceImpl("http://dbpedia.org/resource/Brazil");
	static Resource resourceIRE = new ResourceImpl("http://dbpedia.org/resource/Ireland");
	static Resource resourceARG = new ResourceImpl("http://dbpedia.org/resource/Argentina");
	static Resource resourceA = new ResourceImpl("http://dbpedia.org/resource/Ocean's_Twelve");
	static Resource resourceB = new ResourceImpl("http://dbpedia.org/resource/Ocean's_Thirteen");
	static Resource resourceC = new ResourceImpl("http://dbpedia.org/resource/Pixar");
	static Resource resourceD = new ResourceImpl("http://dbpedia.org/resource/111_Murray_Street");
	static Resource resourceE = new ResourceImpl("http://dbpedia.org/resource/The_Extendables");
	static Resource resourceF = new ResourceImpl("http://dbpedia.org/page/Brian_Thompson");
	static Resource resourceG = new ResourceImpl("http://dbpedia.org/resource/A_Patriot's_Act");
	static Resource resourceH = new ResourceImpl("http://dbpedia.org/resource/U2");
	static Resource resourceI = new ResourceImpl("http://dbpedia.org/resource/11th_Ward,_Chicago");
	static Resource resourceTV1 = new ResourceImpl("http://dbpedia.org/resource/BoJack_Horseman");
	static Resource resourceTV2 = new ResourceImpl("http://dbpedia.org/resource/Adventure_Time");
	static Resource resourceTV3 = new ResourceImpl("http://dbpedia.org/resource/Steven_Universe");
	static Resource resourceA1 = new ResourceImpl("http://dbpedia.org/resource/Adele");
	static Resource resourceA2 = new ResourceImpl("http://dbpedia.org/resource/Amy_Winehouse");
	static Resource resourceA3 = new ResourceImpl("http://dbpedia.org/resource/Rihanna");
	static Resource resourceS1 = new ResourceImpl("http://dbpedia.org/resource/Bohemian_Rhapsody");
	static Resource resourceS2 = new ResourceImpl("http://dbpedia.org/resource/Total_Eclipse_of_the_Heart");
	static Resource resourceF1 = new ResourceImpl("http://dbpedia.org/resource/Brazil_(1985_film)");
	static Resource resourceF2 = new ResourceImpl("http://dbpedia.org/resource/Metropolis_(1927_film)");
	static Resource resourceF3 = new ResourceImpl("http://dbpedia.org/resource/Inception");
	

	static Resource p1 = new ResourceImpl("http://dbpedia.org/ontology/abstract");
	static Resource p2 = new ResourceImpl("http://www.w3.org/2000/01/rdf-schema#comment");

	static Resource[] resources = { resourceF1, resourceF2};
	static Resource[] properties = null;

	public static void main(String[] args)
	{
		simVSMbyProperty(Arrays.asList(resources));
		//simVSMbyProperty(resourceF3.getURI(), resourceF2.getURI());
		//simVSMbyProperty(resourceBRA.getURI(), resourceARG.getURI());
		//simVSMbyProperty(resourceA1.getURI(), resourceA2.getURI());

	}

	public static double calculateSimilarity(String resourceUri1, String resourceUri2)
	{
		Resource resource1 = new ResourceImpl(resourceUri1);
		Resource resource2 = new ResourceImpl(resourceUri2);
		return calculateSimilarity(resource1, resource2);
	}

	public static double calculateSimilarity(Resource resource1, Resource resource2)
	{
		List<Resource> propertyResources = SparqlWalk.getDataTypePropertiesSharedByResources(resource1.getURI(), resource2.getURI());
		VSMSimilarity vsmSimilarity = new VSMSimilarity(resource1, resource2);
		for (Resource propertyResource : propertyResources)
		{
			double similarityScore = LiteralSim.calculateSimilarity(resource1.getURI(), resource2.getURI(), propertyResource.getURI());
			if (similarityScore >= 0)
			{
				//System.out.println("Property score: " + similarityScore);
				vsmSimilarity.addPropertyScore(propertyResource.getURI(), similarityScore);
			}
		}
		String classe1 = SparqlWalk.getMostSpecificSubclasseOfDbpediaResource(resource1.getURI()).getURI();
		String classe2 = SparqlWalk.getMostSpecificSubclasseOfDbpediaResource(resource2.getURI()).getURI();
		vsmSimilarity.resource1Properties = SparqlWalk.getDbpediaOntologyDatatypeProperties(classe1).size();
		vsmSimilarity.resource2Properties = SparqlWalk.getDbpediaOntologyDatatypeProperties(classe2).size();

		//for (String keyProperty : vsmSimilarity.getPropertyScore().keySet())
		//{
		//	System.out.println("VSM:" + vsmSimilarity.getResource1().getURI() + " X "
		//			+ vsmSimilarity.getResource2().getURI() + " for property "
		//			+ new ResourceImpl(keyProperty).getLocalName() + " has score "
		//			+ vsmSimilarity.getPropertyScore().get(keyProperty) + " and total score == "
		//			+ String.format("%.2f", (vsmSimilarity.totalScore())));
		//}

		//System.out.println("Propriedades de " + vsmSimilarity.resource1.getURI() + ": " + vsmSimilarity.resource1Properties);
		//System.out.println("Propriedades de " + vsmSimilarity.resource2.getURI() + ": " + vsmSimilarity.resource2Properties);
		//System.out.println("Propriedades compartilhadas " + vsmSimilarity.getNumberOfProperties());
		//System.out.println("Score 1: " + vsmSimilarity.totalScore());
		//System.out.println("Score 2: " + vsmSimilarity.totalScore2());
		//System.out.println("Penalized score: " + vsmSimilarity.getPenalizedScore());

		return vsmSimilarity.getPenalizedScore();
	}

	public static double simVSMbyProperty(String uri1, String uri2)
	{
		List<Resource> sim = new ArrayList<Resource>();
		sim.add(new ResourceImpl(uri1));
		sim.add(new ResourceImpl(uri2));
		return simVSMbyProperty(sim).get(0).totalScore();
	}

	public static List<VSMSimilarity> simVSMbyProperty(List<Resource> uris)
	{
		List<VSMSimilarity> vSMSimilarities = new ArrayList<VSMSimilarity>();
		List<String> control = new ArrayList<String>();

		for (Resource r1 : uris)
		{
			for (Resource r2 : uris)
			{
				if (r1 != r2 && (!(control.contains(r2.getURI() + r1.getURI())
						|| control.contains(r1.getURI() + r2.getURI()))))
				{
					List<Resource> propertyResources = SparqlWalk.getDataTypePropertiesSharedByResources(r1.getURI(), r2.getURI());
					VSMSimilarity vsmSimilarity = new VSMSimilarity(r1, r2);
					for (Resource propertyResource : propertyResources)
					{
						double similarityScore = LiteralSim.calculateSimilarity(r1.getURI(), r2.getURI(), propertyResource.getURI());
						if (similarityScore >= 0)
						{
							System.out.println("Property score: " + similarityScore);
							vsmSimilarity.addPropertyScore(propertyResource.getURI(), similarityScore);
						}
					}
					String classe1 = SparqlWalk.getMostSpecificSubclasseOfDbpediaResource(r1.getURI()).getURI();
					String classe2 = SparqlWalk.getMostSpecificSubclasseOfDbpediaResource(r2.getURI()).getURI();
					vsmSimilarity.resource1Properties = SparqlWalk.getDbpediaOntologyDatatypeProperties(classe1).size();
					vsmSimilarity.resource2Properties = SparqlWalk.getDbpediaOntologyDatatypeProperties(classe2).size();

					vSMSimilarities.add(vsmSimilarity);

					control.add(r1.getURI() + r2.getURI());
					control.add(r2.getURI() + r1.getURI());
				}
			}
		}

		for (VSMSimilarity vsmSimilarity : vSMSimilarities)
		{
			for (String keyProperty : vsmSimilarity.getPropertyScore().keySet())
			{
				System.out.println("VSM:" + vsmSimilarity.getResource1().getURI() + " X "
						+ vsmSimilarity.getResource2().getURI() + " for property "
						+ new ResourceImpl(keyProperty).getLocalName() + " has score "
						+ vsmSimilarity.getPropertyScore().get(keyProperty) + " and total score == "
						+ String.format("%.2f", (vsmSimilarity.totalScore())));
			}

			//System.out.println("Propriedades de " + vsmSimilarity.resource1.getURI() + ": " + vsmSimilarity.resource1Properties);
			//System.out.println("Propriedades de " + vsmSimilarity.resource2.getURI() + ": " + vsmSimilarity.resource2Properties);
			//System.out.println("Propriedades compartilhadas " + vsmSimilarity.getNumberOfProperties());
			System.out.println("Score 1: " + vsmSimilarity.totalScore());
			System.out.println("Score 2: " + vsmSimilarity.totalScore2());
			System.out.println("Penalized score: " + vsmSimilarity.getPenalizedScore());
		}

		return vSMSimilarities;
	}

	public static double calculateSimilarity(String uri1, String uri2, String property)
	{
		double cbSim = 0;
		double count = 0;
		List<Literal> literals1 = SparqlWalk.getLiteralByResourceAndProperty(uri1, property);
		List<Literal> literals2 = SparqlWalk.getLiteralByResourceAndProperty(uri2, property);

		//System.out.println("COMPARING " + property);

		for (Literal literal1 : literals1)
		{
			for (Literal literal2 : literals2)
			{
				if (literal1 == null || literal2 == null)
				{
					continue;
				}

				//System.out.println("1: " + literal1);
				//System.out.println("2: " + literal2);

				/*if (literal1.getDatatype().getURI().equals("http://www.openlinksw.com/schemas/virtrdf#Geometry"))
				{
					//cbSim = cbSim + SparqlWalk.getDistanceBetweenTwoPoints(literal,literal2);
					continue;
				}*/

				try
				{
					Class<?> classLiteral1 = literal1.getValue().getClass();
					Class<?> classLiteral2 = literal2.getValue().getClass();

					if (classLiteral1 != null && classLiteral2 != null)
					{
						if (classLiteral1.equals(String.class) && classLiteral2.equals(String.class))
						{
							cbSim = cbSim + TextSimilarity.computeConsineSimilarity(
									TextSimilarity.filterStoppingsKeepDuplicates(literal1.getString()),
									TextSimilarity.filterStoppingsKeepDuplicates(literal2.getString()));
						}
						else if (literal1.getValue() instanceof Number && literal2.getValue() instanceof Number)
						{
							cbSim = cbSim + LiteralSim.calculateLiteralSimilarity(literal1.getDouble(), literal2.getDouble(), property);
						}
						else if (literal1.getValue() instanceof XSDDateTime && literal2.getValue() instanceof XSDDateTime)
						{
							cbSim = cbSim + LiteralSim.calculateLiteralSimilarity((XSDDateTime)literal1.getValue(), (XSDDateTime)literal2.getValue(), property);
						}
						else if(NumberUtils.isNumber(literal1.getLexicalForm()) && NumberUtils.isNumber(literal2.getLexicalForm()))
						{
							RDFDatatype datatype = literal1.getDatatype();
							cbSim = cbSim + LiteralSim.calculateLiteralSimilarity(Double.parseDouble(literal1.getLexicalForm()), Double.parseDouble(literal2.getLexicalForm()), property, datatype.getURI());	
						}
						else
						{
							//System.err.println("Propriedade não computada: " + property);
							continue;
						}
					}
					else
					{
						//System.err.println("Propriedade não computada: " + property);
						continue;
					}
					count = count + 1d;
				}
				catch(DatatypeFormatException ex)
				{
					System.err.println("Erro de formatação: "+literal1.getLexicalForm()+" "+literal2.getLexicalForm());
				}
				catch(QueryExceptionHTTP ex)
				{
					System.err.println("Query retornou erro.");
				}
			}
		}

		return count > 0 ? cbSim/count : -1d;
	}

	public static Object getJavaValue(final Literal literal)
	{
		final RDFDatatype dataType = literal.getDatatype();
		if (dataType == null)
		{
			return literal.toString();
		}
		return dataType.parse(literal.getLexicalForm());
	}

	public static List<String> getWords(String text)
	{
		List<String> words = new ArrayList<String>();
		BreakIterator breakIterator = BreakIterator.getWordInstance();
		breakIterator.setText(text);
		int lastIndex = breakIterator.first();

		while (BreakIterator.DONE != lastIndex)
		{
			int firstIndex = lastIndex;
			lastIndex = breakIterator.next();

			if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex)))
			{
				words.add(text.substring(firstIndex, lastIndex));
			}
		}

		return words;
	}

	public static Map<String, Double> getTF(String s)
	{
		Map<String, Double> m = new HashMap<String, Double>();
		String[] splitString = s.split(" ");

		int count = 1;
		for (String s1 : splitString)
		{
			count = m.containsKey(s1) ? count + 1 : 1;
			m.put(s1, (double) count);
		}
		return m;
	}

	public static double cosineSimilarity(String one, String two)
	{
		/*
		 * Map<String,double> tf = new HashMap<String, double>(); List<String>
		 * words1 = getWords(one); List<String> words2 = getWords(two);
		 * Set<String> terms = new HashSet<String>(words1);
		 * terms.addAll(words2);
		 * 
		 */
		return cosineSimilarity(new ArrayList<Double>(getTF(one).values()), new ArrayList<Double>(getTF(two).values()));
	}

	public static double cosineSimilarity(List<Double> vector1, List<Double> vector2)
	{

		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vector1.size(); i++)
		{
			dotProduct += vector1.get(i) * vector2.get(i);
			normA += Math.pow(vector1.get(i), 2);
			normB += Math.pow(vector2.get(i), 2);
		}

		return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

	public static double calculateLiteralSimilarity(double value1, double value2, String propertyUri)
	{
		return LiteralSim.calculateLiteralSimilarity(value1, value2, propertyUri, "");
	}
	
	public static double calculateLiteralSimilarity(double value1, double value2, String propertyUri, String type)
	{
		double difference = Math.abs(
				LiteralUtil.normalize(value1, propertyUri, type)
				- LiteralUtil.normalize(value2, propertyUri, type));
		return 1d - difference;
	}

	public static double calculateLiteralSimilarity(XSDDateTime value1, XSDDateTime value2, String propertyUri)
	{
		double difference = Math.abs(
				LiteralUtil.normalize(value1, propertyUri)
				- LiteralUtil.normalize(value2, propertyUri));
		return 1d - difference;
	}
}