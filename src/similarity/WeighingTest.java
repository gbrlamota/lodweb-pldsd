package similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import database.DBFunctions;
import model.MovieRating;
import model.User;
import node.SimpleTriple;
import node.SparqlWalk;
import util.StringUtilsNode;

public class WeighingTest
{

	private static List<String> movies = new ArrayList<String>();
	private static Set<String> properties = new HashSet<String>();
	
	private static Map<String, Integer> movieRate = new HashMap<String, Integer>();
	private static Map<String, Integer> objectFrequency = new HashMap<String, Integer>();
	private static Map<String, Double> propertyWeight = new HashMap<String, Double>();


	public static void main(String[] args)
	{
		loadMoviesManually();
		runWeigh();
		//runWeighTwoLevels();
		
		
		for (String key : propertyWeight.keySet()) {
            
            Double value = propertyWeight.get(key);
            System.out.println(key + " = " + value);
		}	
		
		normalizeWeigh();
		
		for (String key : propertyWeight.keySet()) {
            
            Double value = propertyWeight.get(key);
            System.out.println(key + " = " + value);
		}	
		

	}
	
	public static int generateRandomInt(int min, int max) {
	    Random r = new Random();
	    return r.nextInt((max - min) + 1) + min;
	}
	
	public static Integer generateRandomRating() {
	    return new Integer(generateRandomInt(1,5));
	}

	
	
	
	private static void loadMoviesManually()
	{
		movies.add("http://dbpedia.org/resource/The_Avengers_(2012_film)");
		movies.add("http://dbpedia.org/resource/Toy_Story)");
		movies.add("http://dbpedia.org/resource/The_Amazing_Spider-Man_(2012_film)");
		movies.add("http://dbpedia.org/resource/Memoirs_of_a_Geisha_(film)");
		/*movies.add("http://dbpedia.org/resource/The_Godfather");
		movies.add("http://dbpedia.org/resource/P.S._I_Love_You_(film)");
		movies.add("http://dbpedia.org/resource/Donnie_Darko");
		movies.add("http://dbpedia.org/resource/Tron");
		movies.add("http://dbpedia.org/resource/Grown_Ups_(film)");
		movies.add("http://dbpedia.org/resource/V_for_Vendetta_(film)");
		movies.add("http://dbpedia.org/resource/2012_(film)");
		movies.add("http://dbpedia.org/resource/Scarface_(1983_film)");
		movies.add("http://dbpedia.org/resource/The_Hobbit:_An_Unexpected_Journey");
		movies.add("http://dbpedia.org/resource/The_Notebook");
		movies.add("http://dbpedia.org/resource/Jackass:_The_Movie");
		movies.add("http://dbpedia.org/resource/Pirates_of_the_Caribbean:_The_Curse_of_the_Black_Pearl");
		movies.add("http://dbpedia.org/resource/Dirty_Dancing");
		movies.add("http://dbpedia.org/resource/Forrest_Gump");
		movies.add("http://dbpedia.org/resource/Titanic_(1997_film)");
		movies.add("http://dbpedia.org/resource/300_(film)");
		movies.add("http://dbpedia.org/resource/Pineapple_Express_(film)");*/
		/*movies.add("http://dbpedia.org/resource/Where_the_Wild_Things_Are_(film)");
		movies.add("http://dbpedia.org/resource/Scott_Pilgrim_vs._the_World");
		movies.add("http://dbpedia.org/resource/Dear_John_(2010_film)");
		movies.add("http://dbpedia.org/resource/The_Princess_Bride_(film)");
		movies.add("http://dbpedia.org/resource/Twilight_(2008_film)");
		movies.add("http://dbpedia.org/resource/The_Last_Song_(film)");
		movies.add("http://dbpedia.org/resource/Vampires_Suck");
		movies.add("http://dbpedia.org/resource/The_Karate_Kid");
		movies.add("http://dbpedia.org/resource/Gladiator_(2000_film)");
		movies.add("http://dbpedia.org/resource/The_Shawshank_Redemption");
		movies.add("http://dbpedia.org/resource/The_Nightmare_Before_Christmas");
		movies.add("http://dbpedia.org/resource/Edward_Scissorhands");
		movies.add("http://dbpedia.org/resource/Slumdog_Millionaire");
		movies.add("http://dbpedia.org/resource/Goodfellas");
		movies.add("http://dbpedia.org/resource/The_Proposal_(film)");
		movies.add("http://dbpedia.org/resource/The_Black_Swan_(film)");
		movies.add("http://dbpedia.org/resource/Eat_Pray_Love");
		movies.add("http://dbpedia.org/resource/3_Idiots");
		movies.add("http://dbpedia.org/resource/Across_the_Universe_(film)");
		movies.add("http://dbpedia.org/resource/A_Clockwork_Orange_(film)");
		movies.add("http://dbpedia.org/resource/Fear_and_Loathing_in_Las_Vegas_(film)");
		movies.add("http://dbpedia.org/resource/The_Last_Airbender");
		movies.add("http://dbpedia.org/resource/Wanted_(2009_film)");
		movies.add("http://dbpedia.org/resource/The_Lovely_Bones_(film)");
		movies.add("http://dbpedia.org/resource/Requiem_for_a_Dream");
		movies.add("http://dbpedia.org/resource/Troy_(film)");
		movies.add("http://dbpedia.org/resource/Labyrinth_(film)");
		movies.add("http://dbpedia.org/resource/Happy_Gilmore");
		movies.add("http://dbpedia.org/resource/Remember_the_Titans");
		movies.add("http://dbpedia.org/resource/District_9");
		movies.add("http://dbpedia.org/resource/50_First_Dates");
		movies.add("http://dbpedia.org/resource/The_Other_Guys");
		movies.add("http://dbpedia.org/resource/Pan's_Labyrinth");
		movies.add("http://dbpedia.org/resource/Knocked_Up");
		movies.add("http://dbpedia.org/resource/Home_Alone");
		movies.add("http://dbpedia.org/resource/Letters_to_Juliet");
		movies.add("http://dbpedia.org/resource/The_Pursuit_of_Happyness");
		movies.add("http://dbpedia.org/resource/Next_Friday");
		movies.add("http://dbpedia.org/resource/Dr._Horrible's_Sing-Along_Blog");
		movies.add("http://dbpedia.org/resource/Takers");
		movies.add("http://dbpedia.org/resource/Taken_(film)");
		movies.add("http://dbpedia.org/resource/Love_Actually");
		movies.add("http://dbpedia.org/resource/The_Goonies");
		movies.add("http://dbpedia.org/resource/Coraline_(film)");
		movies.add("http://dbpedia.org/resource/Hancock_(film)");
		movies.add("http://dbpedia.org/resource/10_Things_I_Hate_About_You");
		movies.add("http://dbpedia.org/resource/Hot_Fuzz");
		movies.add("http://dbpedia.org/resource/The_American_Beauty");
		movies.add("http://dbpedia.org/resource/A_Beautiful_Mind_(film)");
		movies.add("http://dbpedia.org/resource/White_Chicks");
		movies.add("http://dbpedia.org/resource/Rocky");
		movies.add("http://dbpedia.org/resource/Interview_with_the_Vampire_(film)");
		movies.add("http://dbpedia.org/resource/Jurassic_Park");
		movies.add("http://dbpedia.org/resource/The_Curious_Case_of_Benjamin_Button_(film)");
		movies.add("http://dbpedia.org/resource/Megamind");
		movies.add("http://dbpedia.org/resource/The_Big_Lebowski");
		movies.add("http://dbpedia.org/resource/Friday_After_Next");
		movies.add("http://dbpedia.org/resource/The_Butterfly_Effect");
		movies.add("http://dbpedia.org/resource/The_Ugly_Truth");
		movies.add("http://dbpedia.org/resource/Alien_(film)");
		movies.add("http://dbpedia.org/resource/Transformers:_Revenge_of_the_Fallen");
		movies.add("http://dbpedia.org/resource/Ace_Ventura:_Pet_Detective");
		movies.add("http://dbpedia.org/resource/The_Color_Purple_(film)");
		movies.add("http://dbpedia.org/resource/The_Yes_Man");
		movies.add("http://dbpedia.org/resource/Clash_of_the_Titans_(2010_film)");
		movies.add("http://dbpedia.org/resource/Full_Metal_Jacket");
		movies.add("http://dbpedia.org/resource/Trainspotting_(film)");
		movies.add("http://dbpedia.org/resource/Gran_Torino");
		movies.add("http://dbpedia.org/resource/Big_Fish");
		movies.add("http://dbpedia.org/resource/Fireproof_(film)");
		movies.add("http://dbpedia.org/resource/Forgetting_Sarah_Marshall");
		movies.add("http://dbpedia.org/resource/Law_Abiding_Citizen");
		movies.add("http://dbpedia.org/resource/Billy_Madison");
		movies.add("http://dbpedia.org/resource/Dinner_for_Schmucks");
		movies.add("http://dbpedia.org/resource/The_Book_of_Eli");*/
		
		for(String movie : movies)
			//assign random rating to movies
			movieRate.put(movie, generateRandomRating());
		
	}

	
	public static void runWeigh()
	{
		for(String movie : movies) {
		
			List<SimpleTriple> triples = SparqlWalk.getDBpediaObjectsTripletBySubjectURI(movie);
			
			for(SimpleTriple triple : triples) {
				
				//Adiciona a proprerty no Set
				properties.add(triple.getPredicate());
				//conta a frenquência do object e adiciona no Hash 
				if (objectFrequency.containsKey(triple.getObject()))
					objectFrequency.put(triple.getObject(), objectFrequency.get(triple.getObject())+1); //incrementa a entrada no Hash
				else
					objectFrequency.put(triple.getObject(), 1); //cria nova entrada no Hash
			}
			
		}
		
		for(String property : properties) {
			
			/* TODO melhorar essa forma de resgatar as triplas pelas propriedades, 
			 		pegar apenas as triplas em que o subject é um dos filmes da lista */
			List<SimpleTriple> triples = 
					SparqlWalk.getDBpediaObjectTripletByPropertyURIBySubjectList(property, StringUtilsNode.formatURIList(movies));
			
			int  rating = 0;
			int frequencySum = 0;
			int count = 0;
			
			for(SimpleTriple triple : triples) {
				
				if (movieRate.containsKey(triple.getSubject())) {
					rating = movieRate.get(triple.getSubject());
					frequencySum +=  objectFrequency.get(triple.getObject())*rating;
					count++;	
				}
			}
			
			if (count != 0) {
				Double d = new Double(frequencySum/count);
				propertyWeight.put(property, d);
			}	
			
		}
	}
	
	/**
	 * Calculates a value between 0 and 1, given the precondition that value
	 * is between min and max. 
	 */
	public static double normalize(double value, double min, double max) {
	    return ((value - min) / (max - min));
	}
	
	
	public static void normalizeWeigh() {
		
		Double min = Collections.min(propertyWeight.entrySet(), 
				Comparator.comparingDouble(Map.Entry::getValue)).getValue();
		Double max = Collections.max(propertyWeight.entrySet(), 
				Comparator.comparingDouble(Map.Entry::getValue)).getValue();
		
		for (Map.Entry<String, Double> entry : propertyWeight.entrySet()) {
			Double normalizedWeight = normalize(entry.getValue(), min, max);
			propertyWeight.put(entry.getKey(), normalizedWeight);
		}
		
	}
}
