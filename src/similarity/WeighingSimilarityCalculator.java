package similarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.DBFunctions;
/*import io.github.parklize.conf.RESIMConf.PropertyRestriction;
import io.github.parklize.measure.ResourceSimilarityMeasure;*/
import model.User;
/*import ufba.br.resim.similarity.CalculateResim;
import ufba.br.resim.test.Util;;*/

public class WeighingSimilarityCalculator implements Runnable
{
	private String name;

	public WeighingSimilarityCalculator(String name)
	{
		this.name = name;
		System.out.println("THREAD " + this.name + " HAS STARTED.");
	}
	
	@Override
	public void run()
	{
		String[] movies = Weighing.nextMoviePairing();
		User user = Weighing.getUser();
		
		// DBpedia list of additional no literal properties for film domain
		// Only needed for Resim
		/*List<String> additionalPropertyList_dbpedia = new ArrayList<String>();
		try {
			additionalPropertyList_dbpedia = Util.getAdditionalPropertyList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		while(!movies[0].equals("") && !movies[1].equals(""))
		{
			String movie1 = movies[0];
			String movie2 = movies[1];

			StringBuilder message = new StringBuilder();
			message.append("Thread " + name + " comparing " + movie1 + " and " + movie2 + "\n");

			try
			{
				if (!Weighing.isValidPairing(movie1, movie2))
				{
					movies = Weighing.nextMoviePairing();
					continue;
				}

				//boolean thereIsLiteral = false; //forcing this to not execute
				//boolean thereIsRESIM = true; //forcing this to not execute
				boolean thereIsPLDSD = true;
				boolean thereIsLDSD = true;
				boolean thereIsSPLDSD = true;
				boolean thereIsSLDSD = true;

				DBFunctions dbFunctions = Weighing.getDatabaseConnection();
				synchronized(dbFunctions)
				{
					//thereIsLiteral = dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LITERAL.toString());
					//thereIsRESIM = dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM.toString());
					//thereIsPLDSD = dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.PLDSD.toString(), user);
					//thereIsLDSD = dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.LDSD.toString(), user);
				}

				/*double teste1 = 0d;
				if (!thereIsLiteral)
				{
					teste1 = LiteralSim.calculateSimilarity(movie1, movie2);
					message.append("LITERAL: " + teste1 + "\n");

					synchronized(dbFunctions)
					{
						if (!dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.LITERAL.toString(), user))
							dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LITERAL.toString(), teste1, 0);
					}
				}
				else
				{
					teste1 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.LITERAL.toString());
				}*/
	
				/*double teste2 = 0d;
				if (thereIsPLDSD)
				{	
					
					//TODO checar se eh necessario mesmo subtrair de um, se o LDSD ja esta dando a resposta como distancia??
					//pq queremos a resposta como similaridade!
					teste2 = 1 - PLDSD.PLDSDweighted(movie1, movie2,user);
					message.append("PLDSD: " + teste2 + "\n");
					synchronized(dbFunctions)
					{
						if (!dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.PLDSD.toString(), user))
							dbFunctions.insertPersonalizedSemanticDistance(movie1, movie2, ESimilarity.PLDSD.toString(), teste2, user);
					}
				}
				else
				{
					teste2 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.PLDSD.toString(), user);
				}*/

				/*double teste5 = 0d;
				if (!thereIsSPLDSD)
				{

					//TODO checar se eh necessario mesmo subtrair de um, se o LDSD ja esta dando a resposta como distancia??
					//pq queremos a resposta como similaridade!
					teste5 = 1 - SummarizedPLDSD.PLDSDweighted(movie1, movie2,user);
					message.append("PLDSD: " + teste5 + "\n");
					synchronized(dbFunctions)
					{
						if (!dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.PLDSD_100.toString(), user))
							dbFunctions.insertPersonalizedSemanticDistance(movie1, movie2, ESimilarity.PLDSD_100.toString(), teste5, user);
					}
				}
				else
				{
					teste5 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.PLDSD_535.toString(), user);
				}*/
	
				/*double teste3 = 0d;
				if (thereIsLDSD)
				{	
						
					//TODO checar se eh necessario mesmo subtrair de um, se o LDSD ja esta dando a resposta como distancia??
					//pq queremos a resposta como similaridade!
					teste3 = 1 - LDSD.LDSDweighted(movie1, movie2);
					message.append("LDSD: " + teste3 + "\n");
					//TODO:tirar essa segunda verificacao com if?
					synchronized(dbFunctions)
					{
						if (!dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.LDSD.toString(), user))
					dbFunctions.insertPersonalizedSemanticDistance(movie1, movie2, ESimilarity.LDSD.toString(), teste3, user);
					}
				}
				else
				{
					teste3 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.LDSD.toString(), user);
				}*/

				double teste4 = 0d;
				if (thereIsSLDSD)
				{

					//TODO checar se eh necessario mesmo subtrair de um, se o LDSD ja esta dando a resposta como distancia??
					//pq queremos a resposta como similaridade!
					teste4 = 1 - SummarizedLDSD.LDSDweighted(movie1, movie2);
					message.append("LDSD: " + teste4 + "\n");
					//TODO:tirar essa segunda verificacao com if?
					synchronized(dbFunctions)
					{
						if (!dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.LDSD_500.toString(), user))
							dbFunctions.insertPersonalizedSemanticDistance(movie1, movie2, ESimilarity.LDSD_500.toString(), teste4, user);
					}
				}
				else
				{
					teste4 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.LDSD_500.toString(), user);
				}
				
				/*double teste4 = 0d;
				if (!thereIsRESIM)
				{
					ResourceSimilarityMeasure rsmForDBpedia = new ResourceSimilarityMeasure(PropertyRestriction.SamePropertyPath,
						     "http://dbpedia.org/sparql", null, "http://dbpedia.org/ontology/", 
						     null, null, null, null);

					
					//teste3 = rsmForDBpedia.getSimilarity("<"+movie1+">", "<"+movie2+">", 2);
					//teste3 = CalculateResim.getResimBetweenTwoResources("<"+movie1+">", "<"+movie2+">", additionalPropertyList_dbpedia);
					message.append("RESIM: " + teste4 + "\n");

					synchronized(dbFunctions)
					{
						if (!dbFunctions.checkPersonalizedSimilarity(movie1, movie2, ESimilarity.RESIM.toString(), user))
							dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM.toString(), teste4, 0);
					}
				}
				else
				{
					//teste2 = dbFunctions.getSimilarityByMethod(movie1, movie2, ESimilarity.RESIM.toString());
				}*/
				
				System.out.print(message.toString());
			}
			catch(Exception ex)
			{
				System.out.println("Exception thrown " + movie1 + " " + movie2);
			}

			movies = Weighing.nextMoviePairing();
		}

		System.out.println("END Thread "+name);//+": "+System.currentTimeMillis());
	}
}