package similarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.DBFunctions;
import io.github.parklize.conf.RESIMConf.PropertyRestriction;
import io.github.parklize.measure.ResourceSimilarityMeasure;
import ufba.br.resim.similarity.CalculateResim;
import ufba.br.resim.test.Util;;

public class TestingSimilarityCalculator implements Runnable
{
	private String name;

	public TestingSimilarityCalculator(String name)
	{
		this.name = name;
		System.out.println("THREAD " + this.name + " HAS STARTED.");
	}
	
	@Override
	public void run()
	{
		String[] movies = Testing.nextMoviePairing();
		
		// DBpedia list of additional no literal properties for film domain
		// Only needed for Resim
		List<String> additionalPropertyList_dbpedia = new ArrayList<String>();
		try {
			additionalPropertyList_dbpedia = Util.getAdditionalPropertyList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(!movies[0].equals("") && !movies[1].equals(""))
		{
			String movie1 = movies[0];
			String movie2 = movies[1];

			StringBuilder message = new StringBuilder();
			message.append("Thread " + name + " comparing " + movie1 + " and " + movie2 + "\n");

			try
			{
				if (!Testing.isValidPairing(movie1, movie2))
				{
					movies = Testing.nextMoviePairing();
					continue;
				}

				boolean thereIsLiteral = true;
				boolean thereIsLDSD = true;
				boolean thereIsRESIM = false;

				DBFunctions dbFunctions = Testing.getDatabaseConnection();
				synchronized(dbFunctions)
				{
					//thereIsLiteral = dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LITERAL.toString());
					//thereIsLDSD = dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD.toString());
					thereIsRESIM = dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM.toString());
				}

				double teste1 = 0d;
				if (!thereIsLiteral)
				{
					teste1 = LiteralSim.calculateSimilarity(movie1, movie2);
					message.append("LITERAL: " + teste1 + "\n");

					synchronized(dbFunctions)
					{
						//if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LITERAL.toString()))
							dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LITERAL.toString(), teste1, 0);
					}
				}
				else
				{
					teste1 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.LITERAL.toString());
				}
	
				double teste2 = 0d;
				if (!thereIsLDSD)
				{
					teste2 = 1 - LDSD.LDSDweighted(movie1, movie2);
					message.append("LDSD: " + teste2 + "\n");

					synchronized(dbFunctions)
					{
						//if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD.toString()))
							dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD.toString(), teste2, 0);
					}
				}
				else
				{
					teste2 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.LDSD.toString());
				}
	
				double teste3 = 0d;
				if (!thereIsRESIM)
				{
					/*ResourceSimilarityMeasure rsmForDBpedia = new ResourceSimilarityMeasure(PropertyRestriction.SamePropertyPath,
						     "http://dbpedia.org/sparql", null, "http://dbpedia.org/ontology/", 
						     null, null, null, null);*/

					
					//teste3 = rsmForDBpedia.getSimilarity("<"+movie1+">", "<"+movie2+">", 2);
					teste3 = CalculateResim.getResimBetweenTwoResources("<"+movie1+">", "<"+movie2+">", additionalPropertyList_dbpedia);
					message.append("RESIM: " + teste3 + "\n");

					synchronized(dbFunctions)
					{
						//if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM.toString()))
							dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM.toString(), teste3, 0);
					}
				}
				else
				{
					teste3 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.RESIM.toString());
				}
				
				System.out.print(message.toString());
			}
			catch(Exception ex)
			{
				System.out.println("Exception thrown " + movie1 + " " + movie2);
			}

			movies = Testing.nextMoviePairing();
		}

		System.out.println("END Thread "+name);//+": "+System.currentTimeMillis());
	}
}