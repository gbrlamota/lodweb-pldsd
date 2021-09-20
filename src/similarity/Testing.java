package similarity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import database.DBFunctions;
import metric.NDCGString;
import metric.PrecisionAndRecall;
import metric.PrecisionAndRecallString;
import node.IConstants;
import node.SparqlWalk;

public class Testing
{
	private static DBFunctions dbFunctions = null;

	public static synchronized DBFunctions getDatabaseConnection()
	{
		if(dbFunctions==null)
		{
			dbFunctions = new DBFunctions();
		}
		return dbFunctions;
	}
	
	private static Map<String, Set<String>> relatedMovies = new HashMap<String, Set<String>>();
	private static List<String> allMovies = new LinkedList<String>();
	private static List<String> baseMovies = new LinkedList<String>();
	private static List<String> candidateMovies = new LinkedList<String>();

	private static String currentMovie1 = "";
	private static List<String> synchronizedControl = new ArrayList<String>();
	
	private static int totalMovies1;
	private static int totalMovies2;
	private static int currentPosition1;
	private static int currentPosition2;

	public static void main(String[] args)
	{
		//System.out.println(SparqlWalk.getMostSpecificSubclasseOfDbpediaResource("http://dbpedia.org/resource/Madonna_(entertainer)"));
		
		
		//System.out.println(SparqlWalk.getCountDBpediaObjecstBySubject("http://dbpedia.org/resource/Spider-Man_(2002_film)"));
		//System.out.println(SparqlWalk.getCountLiteralByUri("http://dbpedia.org/resource/Spider-Man_(2002_film)"));
		
		//System.out.println(SparqlWalk.getLiteralByUri("http://dbpedia.org/resource/Spider-Man_(2002_film)"));
		//System.out.println(SparqlWalk.getDbpediaOntologyDatatypeProperties("http://dbpedia.org/ontology/Film"));
		
		
		loadMovies();
		//calculateObjectAndDatatype();
		//loadMusicalArtists();				
		//runSimilarities();
		//runHybrids(baseMovies, getAllMovies());
		runTests();
	}
	
	public static void calculateObjectAndDatatype() {
		allMovies = getAllMovies();
		int countObjects = 0;
		int countLiterals = 0;
		
		for (String movie : allMovies) {
			countObjects += SparqlWalk.getCountDBpediaObjecstBySubject(movie);
			//countLiterals += SparqlWalk.getCountLiteralByUri(movie);
		}
		
		System.out.println("O numero medio de links por recurso nesse banco eh de "+countObjects/allMovies.size());
		//System.out.println("O numero medio de literais por recurso nesse banco eh de "+countLiterals/allMovies.size());
	}
	
	public static synchronized String[] nextMoviePairing()
	{
		String[] moviePairing = new String[2];

		if (currentPosition2 < totalMovies2)
		{
			moviePairing[1] = allMovies.get(currentPosition2);
			currentPosition2++;
			if (currentMovie1.equals("") && currentPosition1 < totalMovies1)
			{
				currentMovie1 = baseMovies.get(currentPosition1);
				currentPosition1++;
			}
			moviePairing[0] = currentMovie1;
		}
		else
		{
			currentPosition2 = 0;
			moviePairing[1] = allMovies.get(currentPosition2);
			currentPosition2++;

			if (currentPosition1 < totalMovies1)
			{
				currentMovie1 = baseMovies.get(currentPosition1);
				currentPosition1++;
			}
			else
				currentMovie1 = "";

			moviePairing[0] = currentMovie1;
		}
		System.out.println("PROCESSING "+currentPosition1+"/"+totalMovies1+" || "+currentPosition2+"/"+totalMovies2);
		return moviePairing;
	}
	
	public static synchronized boolean isValidPairing(String movie1, String movie2)
	{
		synchronized(synchronizedControl)
		{
			boolean alreadyProcessed = synchronizedControl.contains(movie1 + movie2)
				|| synchronizedControl.contains(movie2 + movie1);

			if (!alreadyProcessed)
			{
				synchronizedControl.add(movie1 + movie2);
				synchronizedControl.add(movie2 + movie1);	
			}
			
			return !(movie1.equals(movie2) || alreadyProcessed);
		}
	}
	
	private static List<String> getAllMovies()
	{
		Set<String> allMovies = new HashSet<String>();

		allMovies.addAll(candidateMovies);
		allMovies.addAll(baseMovies);
		for(String baseMovie : baseMovies)
		{
			allMovies.addAll(relatedMovies.get(baseMovie));	
		}
		
		return new LinkedList<>(allMovies);
	}
	
	private static void loadMusicalArtists()
	{
		candidateMovies.add("http://dbpedia.org/resource/AC/DC");
		candidateMovies.add("http://dbpedia.org/resource/Queen_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Avril_Lavigne");
		candidateMovies.add("http://dbpedia.org/resource/Red_Hot_Chili_Peppers");
		candidateMovies.add("http://dbpedia.org/resource/Shakira");
		candidateMovies.add("http://dbpedia.org/resource/Alicia_Keys");
		candidateMovies.add("http://dbpedia.org/resource/Nickelback");
		candidateMovies.add("http://dbpedia.org/resource/Akon");
		candidateMovies.add("http://dbpedia.org/resource/The_Black_Eyed_Peas");
		candidateMovies.add("http://dbpedia.org/resource/Slipknot_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Jason_Mraz");
		candidateMovies.add("http://dbpedia.org/resource/Guns_N'_Roses");
		candidateMovies.add("http://dbpedia.org/resource/Bon_Jovi");
		candidateMovies.add("http://dbpedia.org/resource/Jay-Z");
		candidateMovies.add("http://dbpedia.org/resource/The_Killers");
		candidateMovies.add("http://dbpedia.org/resource/Foo_Fighters");
		candidateMovies.add("http://dbpedia.org/resource/Bruno_Mars");
		candidateMovies.add("http://dbpedia.org/resource/Miley_Cyrus");
		candidateMovies.add("http://dbpedia.org/resource/Daft_Punk");
		candidateMovies.add("http://dbpedia.org/resource/Maroon_5");
		candidateMovies.add("http://dbpedia.org/resource/Britney_Spears");
		candidateMovies.add("http://dbpedia.org/resource/U2");
		candidateMovies.add("http://dbpedia.org/resource/Led_Zeppelin");
		candidateMovies.add("http://dbpedia.org/resource/The_Rolling_Stones");
		candidateMovies.add("http://dbpedia.org/resource/Slash_(musician)");
		candidateMovies.add("http://dbpedia.org/resource/Aerosmith");
		candidateMovies.add("http://dbpedia.org/resource/Jonas_Brothers");
		candidateMovies.add("http://dbpedia.org/resource/David_Bowie");
		candidateMovies.add("http://dbpedia.org/resource/Pearl_Jam");
		candidateMovies.add("http://dbpedia.org/resource/The_Cure");
		candidateMovies.add("http://dbpedia.org/resource/Megadeth");
		candidateMovies.add("http://dbpedia.org/resource/Oasis_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Marilyn_Manson");
		candidateMovies.add("http://dbpedia.org/resource/Elvis_Presley");
		candidateMovies.add("http://dbpedia.org/resource/Kiss_(band)");
		candidateMovies.add("http://dbpedia.org/resource/The_Smashing_Pumpkins");
		candidateMovies.add("http://dbpedia.org/resource/Ramones");
		candidateMovies.add("http://dbpedia.org/resource/Sade_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Frank_Sinatra");
		candidateMovies.add("http://dbpedia.org/resource/Vampire_Weekend");
		candidateMovies.add("http://dbpedia.org/resource/Fergie_(singer)");
		candidateMovies.add("http://dbpedia.org/resource/Amy_Winehouse");
		candidateMovies.add("http://dbpedia.org/resource/Van_Halen");
		candidateMovies.add("http://dbpedia.org/resource/Celine_Dion");
		candidateMovies.add("http://dbpedia.org/resource/Dream_Theater");
		candidateMovies.add("http://dbpedia.org/resource/The_Strokes");
		candidateMovies.add("http://dbpedia.org/resource/Lenny_Kravitz");
		candidateMovies.add("http://dbpedia.org/resource/The_Eagles_(UK_band)");
		candidateMovies.add("http://dbpedia.org/resource/Grateful_Dead");
		candidateMovies.add("http://dbpedia.org/resource/Queens_of_the_Stone_Age");
		candidateMovies.add("http://dbpedia.org/resource/John_Legend");
		candidateMovies.add("http://dbpedia.org/resource/Billy_Joel");
		candidateMovies.add("http://dbpedia.org/resource/Phoenix_(band)");
		candidateMovies.add("http://dbpedia.org/resource/R.E.M.");
		candidateMovies.add("http://dbpedia.org/resource/The_Pussycat_Dolls");
		candidateMovies.add("http://dbpedia.org/resource/Stevie_Wonder");
		candidateMovies.add("http://dbpedia.org/resource/Ellie_Goulding");
		candidateMovies.add("http://dbpedia.org/resource/Simon_&_Garfunkel");
		candidateMovies.add("http://dbpedia.org/resource/Robbie_Williams");
		candidateMovies.add("http://dbpedia.org/resource/Enya");
		candidateMovies.add("http://dbpedia.org/resource/Neil_Young");
		candidateMovies.add("http://dbpedia.org/resource/DragonForce");
		candidateMovies.add("http://dbpedia.org/resource/Eminem");
		candidateMovies.add("http://dbpedia.org/resource/Lady_Gaga");
		candidateMovies.add("http://dbpedia.org/resource/Linkin_Park");
		candidateMovies.add("http://dbpedia.org/resource/Michael_Jackson");
		candidateMovies.add("http://dbpedia.org/resource/Pink_Floyd");
		candidateMovies.add("http://dbpedia.org/resource/Green_Day");
		candidateMovies.add("http://dbpedia.org/resource/Beyoncé");
		candidateMovies.add("http://dbpedia.org/resource/Coldplay");
		candidateMovies.add("http://dbpedia.org/resource/System_of_a_Down");
		candidateMovies.add("http://dbpedia.org/resource/Tool_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Adam_Lambert");
		candidateMovies.add("http://dbpedia.org/resource/Mumford_&_Sons");
		candidateMovies.add("http://dbpedia.org/resource/Norah_Jones");
		candidateMovies.add("http://dbpedia.org/resource/The_Black_Keys");
		candidateMovies.add("http://dbpedia.org/resource/Josh_Groban");
		candidateMovies.add("http://dbpedia.org/resource/Tom_Waits");
		candidateMovies.add("http://dbpedia.org/resource/Iron_&_Wine");
		candidateMovies.add("http://dbpedia.org/resource/Bruce_Springsteen");
		candidateMovies.add("http://dbpedia.org/resource/Marvin_Gaye");
		candidateMovies.add("http://dbpedia.org/resource/Bon_Iver");
		candidateMovies.add("http://dbpedia.org/resource/Jamiroquai");
		candidateMovies.add("http://dbpedia.org/resource/Paolo_Nutini");
		candidateMovies.add("http://dbpedia.org/resource/Ray_Charles");
		candidateMovies.add("http://dbpedia.org/resource/Leonard_Cohen");
		candidateMovies.add("http://dbpedia.org/resource/Ella_Fitzgerald");
		candidateMovies.add("http://dbpedia.org/resource/Lauryn_Hill");
		candidateMovies.add("http://dbpedia.org/resource/Boyce_Avenue");
		candidateMovies.add("http://dbpedia.org/resource/Kate_Nash");
		candidateMovies.add("http://dbpedia.org/resource/Sepultura");
		candidateMovies.add("http://dbpedia.org/resource/Nicole_Scherzinger");
		candidateMovies.add("http://dbpedia.org/resource/Ricky_Martin");
		candidateMovies.add("http://dbpedia.org/resource/Dimmu_Borgir");
		candidateMovies.add("http://dbpedia.org/resource/Jeff_Buckley");
		candidateMovies.add("http://dbpedia.org/resource/Nina_Simone");
		candidateMovies.add("http://dbpedia.org/resource/Stevie_Ray_Vaughan");
		candidateMovies.add("http://dbpedia.org/resource/Nick_Cave_and_the_Bad_Seeds");
		candidateMovies.add("http://dbpedia.org/resource/Amon_Amarth");
		candidateMovies.add("http://dbpedia.org/resource/Creedence_Clearwater_Revival");
		candidateMovies.add("http://dbpedia.org/resource/Jefferson_Airplane");
		candidateMovies.add("http://dbpedia.org/resource/Sonata_Arctica");
		candidateMovies.add("http://dbpedia.org/resource/Belle_and_Sebastian");
		candidateMovies.add("http://dbpedia.org/resource/Annie_Lennox");
		candidateMovies.add("http://dbpedia.org/resource/Kamelot");
		candidateMovies.add("http://dbpedia.org/resource/Joss_Stone");
		candidateMovies.add("http://dbpedia.org/resource/Aretha_Franklin");
		candidateMovies.add("http://dbpedia.org/resource/Jimmy_Buffett");
		candidateMovies.add("http://dbpedia.org/resource/Jim_Morrison");
		candidateMovies.add("http://dbpedia.org/resource/Barbra_Streisand");
		candidateMovies.add("http://dbpedia.org/resource/Meshuggah");
		candidateMovies.add("http://dbpedia.org/resource/TLC_(group)");
		candidateMovies.add("http://dbpedia.org/resource/Kate_Bush");
		candidateMovies.add("http://dbpedia.org/resource/Mike_Portnoy");
		candidateMovies.add("http://dbpedia.org/resource/R._Kelly");
		candidateMovies.add("http://dbpedia.org/resource/Al_Green");
		candidateMovies.add("http://dbpedia.org/resource/Cat_Stevens");
		candidateMovies.add("http://dbpedia.org/resource/John_Butler_Trio");
		candidateMovies.add("http://dbpedia.org/resource/Elliott_Smith");
		candidateMovies.add("http://dbpedia.org/resource/Lou_Reed");
		candidateMovies.add("http://dbpedia.org/resource/Nat_King_Cole");
		candidateMovies.add("http://dbpedia.org/resource/Rod_Stewart");
		candidateMovies.add("http://dbpedia.org/resource/White_Zombie_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Girl_Talk_(musician)");
		candidateMovies.add("http://dbpedia.org/resource/The_Temptations");
		candidateMovies.add("http://dbpedia.org/resource/Janelle_Monáe");
		candidateMovies.add("http://dbpedia.org/resource/George_Michael");
		candidateMovies.add("http://dbpedia.org/resource/Andrew_Bird");
		candidateMovies.add("http://dbpedia.org/resource/Loreena_McKennitt");
		candidateMovies.add("http://dbpedia.org/resource/Frank_Zappa");
		candidateMovies.add("http://dbpedia.org/resource/Tracy_Chapman");
		candidateMovies.add("http://dbpedia.org/resource/Rhianna_(singer)");
		candidateMovies.add("http://dbpedia.org/resource/Queensrÿche");
		candidateMovies.add("http://dbpedia.org/resource/Etta_James");
		candidateMovies.add("http://dbpedia.org/resource/Rufus_Wainwright");
		candidateMovies.add("http://dbpedia.org/resource/Wyclef_Jean");
		candidateMovies.add("http://dbpedia.org/resource/Vanessa_Carlton");
		candidateMovies.add("http://dbpedia.org/resource/Édith_Piaf");
		candidateMovies.add("http://dbpedia.org/resource/Herbie_Hancock");
		candidateMovies.add("http://dbpedia.org/resource/Norman_Cook");
		candidateMovies.add("http://dbpedia.org/resource/Dio_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Jessica_Simpson");
		candidateMovies.add("http://dbpedia.org/resource/Sammy_Adams");
		candidateMovies.add("http://dbpedia.org/resource/Pet_Shop_Boys");
		candidateMovies.add("http://dbpedia.org/resource/Nick_Drake");
		candidateMovies.add("http://dbpedia.org/resource/JoJo_(singer)");
		candidateMovies.add("http://dbpedia.org/resource/Nick_Cave");
		candidateMovies.add("http://dbpedia.org/resource/Peter_Bjorn_and_John");
		candidateMovies.add("http://dbpedia.org/resource/Otis_Redding");
		candidateMovies.add("http://dbpedia.org/resource/Van_Morrison");
		candidateMovies.add("http://dbpedia.org/resource/The_Specials");
		candidateMovies.add("http://dbpedia.org/resource/Electric_Light_Orchestra");
		candidateMovies.add("http://dbpedia.org/resource/Neil_Diamond");
		candidateMovies.add("http://dbpedia.org/resource/Pat_Benatar");
		candidateMovies.add("http://dbpedia.org/resource/Muddy_Waters");
		candidateMovies.add("http://dbpedia.org/resource/Chaka_Khan");
		candidateMovies.add("http://dbpedia.org/resource/The_Jackson_5");
		candidateMovies.add("http://dbpedia.org/resource/Aqua_(band)");
		candidateMovies.add("http://dbpedia.org/resource/Fear_Factory");
		candidateMovies.add("http://dbpedia.org/resource/Jamie_Cullum");
		candidateMovies.add("http://dbpedia.org/resource/Buddy_Holly");
		candidateMovies.add("http://dbpedia.org/resource/Billy_Idol");
		candidateMovies.add("http://dbpedia.org/resource/The_Black_Crowes");
		candidateMovies.add("http://dbpedia.org/resource/John_Denver");
		candidateMovies.add("http://dbpedia.org/resource/Rick_Astley");
		candidateMovies.add("http://dbpedia.org/resource/Stratovarius");
		candidateMovies.add("http://dbpedia.org/resource/Zakk_Wylde");
		candidateMovies.add("http://dbpedia.org/resource/Manowar");
		candidateMovies.add("http://dbpedia.org/resource/Soulfly");
		candidateMovies.add("http://dbpedia.org/resource/Mark_Ronson");
		candidateMovies.add("http://dbpedia.org/resource/Sam_Cooke");
		candidateMovies.add("http://dbpedia.org/resource/Roy_Orbison");
		candidateMovies.add("http://dbpedia.org/resource/Tom_Petty");
		candidateMovies.add("http://dbpedia.org/resource/Ryan_Adams");
		candidateMovies.add("http://dbpedia.org/resource/Amos_Lee");
		candidateMovies.add("http://dbpedia.org/resource/Charles_Mingus");
		candidateMovies.add("http://dbpedia.org/resource/Yo-Yo_Ma");
		candidateMovies.add("http://dbpedia.org/resource/Barry_White");
		candidateMovies.add("http://dbpedia.org/resource/Wynton_Marsalis");
		candidateMovies.add("http://dbpedia.org/resource/SOJA");
		candidateMovies.add("http://dbpedia.org/resource/Helloween");
		candidateMovies.add("http://dbpedia.org/resource/Headhunterz");
		candidateMovies.add("http://dbpedia.org/resource/Carole_King");
		candidateMovies.add("http://dbpedia.org/resource/Bob_Weir");
		candidateMovies.add("http://dbpedia.org/resource/The_Clutch");
		candidateMovies.add("http://dbpedia.org/resource/Jeff_Beck");
		candidateMovies.add("http://dbpedia.org/resource/Gov't_Mule");
		candidateMovies.add("http://dbpedia.org/resource/Edguy");
		candidateMovies.add("http://dbpedia.org/resource/Sublime_with_Rome");
		candidateMovies.add("http://dbpedia.org/resource/Marcus_Miller");
		candidateMovies.add("http://dbpedia.org/resource/Fela_Kuti");
		candidateMovies.add("http://dbpedia.org/resource/Darren_Hayes");
		candidateMovies.add("http://dbpedia.org/resource/A._Quincy_Jones");
		candidateMovies.add("http://dbpedia.org/resource/Joe_Cocker");
		candidateMovies.add("http://dbpedia.org/resource/Dave_Brubeck");
		candidateMovies.add("http://dbpedia.org/resource/Warren_Haynes");
		candidateMovies.add("http://dbpedia.org/resource/Gary_Moore");
		candidateMovies.add("http://dbpedia.org/resource/Toots_and_the_Maytals");
		candidateMovies.add("http://dbpedia.org/resource/Buffalo_Springfield");
		candidateMovies.add("http://dbpedia.org/resource/Joe_Bonamassa");
		candidateMovies.add("http://dbpedia.org/resource/George_Benson");
		
		//base MusicalArtist #1
		//?r1 dct:subject dbc:American_female_pop_singers . 
		String baseMovie = "http://dbpedia.org/resource/Madonna_(entertainer)";
		Set<String> related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Cher");
		related.add("http://dbpedia.org/resource/Mariah_Carey");
		related.add("http://dbpedia.org/resource/Janet_Jackson");
		related.add("http://dbpedia.org/resource/Cyndi_Lauper");
		related.add("http://dbpedia.org/resource/Donna_Summer");
		related.add("http://dbpedia.org/resource/Gloria_Estefan");
		related.add("http://dbpedia.org/resource/Jennifer_Lopez");
		related.add("http://dbpedia.org/resource/Paula_Abdul");
		related.add("http://dbpedia.org/resource/Whitney_Houston");
		related.add("http://dbpedia.org/resource/Tina_Turner");
		related.add("http://dbpedia.org/resource/Toni_Braxton");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);

		//base Band #2
		//?r1 dct:subject dbc:Rock_and_Roll_Hall_of_Fame_inductees . 
		baseMovie = "http://dbpedia.org/resource/The_Beatles";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/The_Rolling_Stones");
		related.add("http://dbpedia.org/resource/The_Who");
		related.add("http://dbpedia.org/resource/The_Kinks");
		related.add("http://dbpedia.org/resource/The_Hollies");
		related.add("http://dbpedia.org/resource/The_Doors");
		related.add("http://dbpedia.org/resource/The_Beach_Boys");
		related.add("http://dbpedia.org/resource/The_Animals");
		related.add("http://dbpedia.org/resource/The_Byrds");
		related.add("http://dbpedia.org/resource/George_Harrison");
		related.add("http://dbpedia.org/resource/Paul_McCartney");
		related.add("http://dbpedia.org/resource/John_Lennon");
		related.add("http://dbpedia.org/resource/Ringo_Starr");
		related.add("http://dbpedia.org/resource/Elton_John");
		related.add("http://dbpedia.org/resource/Bee_Gees");
		//...
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist #3
		//?r1 dct:subject dbc:Blues_rock_musicians .
		baseMovie = "http://dbpedia.org/resource/Janis_Joplin";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Alvin_Lee");
		related.add("http://dbpedia.org/resource/Captain_Beefheart");
		related.add("http://dbpedia.org/resource/Carlos_Santana");
		related.add("http://dbpedia.org/resource/Eric_Burdon");
		related.add("http://dbpedia.org/resource/Eric_Clapton");
		related.add("http://dbpedia.org/resource/Jimi_Hendrix");
		related.add("http://dbpedia.org/resource/John_Mayall");
		related.add("http://dbpedia.org/resource/Johnny_Winter");
		related.add("http://dbpedia.org/resource/Rory_Gallagher");
		related.add("http://dbpedia.org/resource/Susan_Tedeschi");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist #4
		//?r1 dct:subject dbc:African-American_jazz_musicians . 
		baseMovie = "http://dbpedia.org/resource/Miles_Davis";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/John_Coltrane");
		related.add("http://dbpedia.org/resource/Duke_Ellington");
		related.add("http://dbpedia.org/resource/Thelonious_Monk");
		related.add("http://dbpedia.org/resource/Cannonball_Adderley");
		related.add("http://dbpedia.org/resource/Art_Blakey");
		related.add("http://dbpedia.org/resource/Sonny_Rollins");
		related.add("http://dbpedia.org/resource/Dexter_Gordon");
		related.add("http://dbpedia.org/resource/Count_Basie");
		related.add("http://dbpedia.org/resource/Charlie_Parker");
		related.add("http://dbpedia.org/resource/Louis_Armstrong");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist and Band #5
		//?r1 dct:subject dbc:American_funk_singers .
		//?r1 dct:subject dbc:American_funk_musical_groups .
		baseMovie = "http://dbpedia.org/resource/James_Brown";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Parliament_(band)");
		related.add("http://dbpedia.org/resource/Sly_Stone");
		related.add("http://dbpedia.org/resource/Earth,_Wind_&_Fire");
		related.add("http://dbpedia.org/resource/Kool_&_the_Gang");
		related.add("http://dbpedia.org/resource/Ohio_Players");
		related.add("http://dbpedia.org/resource/The_Meters");
		related.add("http://dbpedia.org/resource/Funkadelic");
		related.add("http://dbpedia.org/resource/Isaac_Hayes");
		related.add("http://dbpedia.org/resource/The_Bar-Kays");
		related.add("http://dbpedia.org/resource/George_Clinton_(musician)");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist #6
		//?r1 dct:subject dbc:American_folk_rock_musicians .
		//?r1 dct:subject dbc:American_folk_singers .
		baseMovie = "http://dbpedia.org/resource/Bob_Dylan";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Joni_Mitchell");
		related.add("http://dbpedia.org/resource/Joan_Baez");
		related.add("http://dbpedia.org/resource/Johnny_Cash");
		related.add("http://dbpedia.org/resource/David_Crosby");
		related.add("http://dbpedia.org/resource/Paul_Simon");
		related.add("http://dbpedia.org/resource/Arlo_Guthrie");
		related.add("http://dbpedia.org/resource/Art_Garfunkel");
		related.add("http://dbpedia.org/resource/Béla_Fleck");
		related.add("http://dbpedia.org/resource/Harry_Belafonte");
		related.add("http://dbpedia.org/resource/Tim_Buckley");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist #7
		//?r1 dct:subject dbc:Jamaican_reggae_singers  . 
		baseMovie = "http://dbpedia.org/resource/Bob_Marley";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Gregory_Isaacs");
		related.add("http://dbpedia.org/resource/Jimmy_Cliff");
		related.add("http://dbpedia.org/resource/Peter_Tosh");
		related.add("http://dbpedia.org/resource/Dennis_Brown");
		related.add("http://dbpedia.org/resource/Max_Romeo");
		related.add("http://dbpedia.org/resource/Shaggy_(musician)");
		related.add("http://dbpedia.org/resource/Ziggy_Marley");
		related.add("http://dbpedia.org/resource/Bunny_Wailer");
		related.add("http://dbpedia.org/resource/Burning_Spear");
		related.add("http://dbpedia.org/resource/Damian_Marley");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist #8
		//?r1 dct:subject dbc:English_heavy_metal_musical_groups .
		//?r1 dct:subject dbc:American_rock_music_groups .
		baseMovie = "http://dbpedia.org/resource/Iron_Maiden";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Judas_Priest");
		related.add("http://dbpedia.org/resource/Black_Sabbath");
		related.add("http://dbpedia.org/resource/Led_Zeppelin");
		related.add("http://dbpedia.org/resource/Rainbow_(rock_band)");
		related.add("http://dbpedia.org/resource/Deep_Purple");
		related.add("http://dbpedia.org/resource/Def_Leppard");
		related.add("http://dbpedia.org/resource/Heaven_&_Hell_(band)");
		related.add("http://dbpedia.org/resource/Ozzy_Osbourne");
		related.add("http://dbpedia.org/resource/Metallica");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist #9
		//?r1 dct:subject dbc:American_blues_singers .
		//?r1 dct:subject dbc:Soul-blues_musicians .
		baseMovie = "http://dbpedia.org/resource/B.B._King";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/John_Lee_Hooker");
		related.add("http://dbpedia.org/resource/Buddy_Guy");
		related.add("http://dbpedia.org/resource/Howlin'_Wolf");
		related.add("http://dbpedia.org/resource/Otis_Rush");
		related.add("http://dbpedia.org/resource/Albert_King");
		related.add("http://dbpedia.org/resource/Freddie_King");
		related.add("http://dbpedia.org/resource/Jimmy_Reed");
		related.add("http://dbpedia.org/resource/Little_Walter");
		related.add("http://dbpedia.org/resource/Magic_Sam");
		related.add("http://dbpedia.org/resource/Lowell_Fulson");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		//base MusicalArtist #10
		//?r1 dct:subject dbc:American_boy_bands .
		//?r1 dct:subject dbc:English_boy_bands .
		baseMovie = "http://dbpedia.org/resource/Backstreet_Boys";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/'N_Sync");
		related.add("http://dbpedia.org/resource/New_Kids_on_the_Block");
		related.add("http://dbpedia.org/resource/Hanson_(band)");
		related.add("http://dbpedia.org/resource/Westlife");
		related.add("http://dbpedia.org/resource/Take_That");
		related.add("http://dbpedia.org/resource/Five_(band)");
		related.add("http://dbpedia.org/resource/Menudo_(band)");
		related.add("http://dbpedia.org/resource/Spice_Girls");
		related.add("http://dbpedia.org/resource/Destiny's_Child");
		related.add("http://dbpedia.org/resource/Savage_Garden");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
				
	}

	
	private static void loadMovies()
	{
		candidateMovies.add("http://dbpedia.org/resource/The_Godfather");
		candidateMovies.add("http://dbpedia.org/resource/P.S._I_Love_You_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Donnie_Darko");
		candidateMovies.add("http://dbpedia.org/resource/Tron");
		candidateMovies.add("http://dbpedia.org/resource/Grown_Ups_(film)");
		candidateMovies.add("http://dbpedia.org/resource/V_for_Vendetta_(film)");
		candidateMovies.add("http://dbpedia.org/resource/2012_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Scarface_(1983_film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Hobbit:_An_Unexpected_Journey");
		candidateMovies.add("http://dbpedia.org/resource/The_Notebook");
		candidateMovies.add("http://dbpedia.org/resource/Jackass:_The_Movie");
		candidateMovies.add("http://dbpedia.org/resource/Pirates_of_the_Caribbean:_The_Curse_of_the_Black_Pearl");
		candidateMovies.add("http://dbpedia.org/resource/Dirty_Dancing");
		candidateMovies.add("http://dbpedia.org/resource/Forrest_Gump");
		candidateMovies.add("http://dbpedia.org/resource/Titanic_(1997_film)");
		candidateMovies.add("http://dbpedia.org/resource/300_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Pineapple_Express_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Where_the_Wild_Things_Are_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Scott_Pilgrim_vs._the_World");
		candidateMovies.add("http://dbpedia.org/resource/Dear_John_(2010_film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Princess_Bride_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Twilight_(2008_film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Last_Song_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Vampires_Suck");
		candidateMovies.add("http://dbpedia.org/resource/The_Karate_Kid");
		candidateMovies.add("http://dbpedia.org/resource/Gladiator_(2000_film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Shawshank_Redemption");
		candidateMovies.add("http://dbpedia.org/resource/The_Nightmare_Before_Christmas");
		candidateMovies.add("http://dbpedia.org/resource/Edward_Scissorhands");
		candidateMovies.add("http://dbpedia.org/resource/Slumdog_Millionaire");
		candidateMovies.add("http://dbpedia.org/resource/Goodfellas");
		candidateMovies.add("http://dbpedia.org/resource/The_Proposal_(film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Black_Swan_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Eat_Pray_Love");
		candidateMovies.add("http://dbpedia.org/resource/3_Idiots");
		candidateMovies.add("http://dbpedia.org/resource/Across_the_Universe_(film)");
		candidateMovies.add("http://dbpedia.org/resource/A_Clockwork_Orange_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Fear_and_Loathing_in_Las_Vegas_(film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Last_Airbender");
		candidateMovies.add("http://dbpedia.org/resource/Wanted_(2009_film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Lovely_Bones_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Requiem_for_a_Dream");
		candidateMovies.add("http://dbpedia.org/resource/Troy_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Labyrinth_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Happy_Gilmore");
		candidateMovies.add("http://dbpedia.org/resource/Remember_the_Titans");
		candidateMovies.add("http://dbpedia.org/resource/District_9");
		candidateMovies.add("http://dbpedia.org/resource/50_First_Dates");
		candidateMovies.add("http://dbpedia.org/resource/The_Other_Guys");
		candidateMovies.add("http://dbpedia.org/resource/Pan's_Labyrinth");
		candidateMovies.add("http://dbpedia.org/resource/Knocked_Up");
		candidateMovies.add("http://dbpedia.org/resource/Home_Alone");
		candidateMovies.add("http://dbpedia.org/resource/Letters_to_Juliet");
		candidateMovies.add("http://dbpedia.org/resource/The_Pursuit_of_Happyness");
		candidateMovies.add("http://dbpedia.org/resource/Next_Friday");
		candidateMovies.add("http://dbpedia.org/resource/Dr._Horrible's_Sing-Along_Blog");
		candidateMovies.add("http://dbpedia.org/resource/Takers");
		candidateMovies.add("http://dbpedia.org/resource/Taken_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Love_Actually");
		candidateMovies.add("http://dbpedia.org/resource/The_Goonies");
		candidateMovies.add("http://dbpedia.org/resource/Coraline_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Hancock_(film)");
		candidateMovies.add("http://dbpedia.org/resource/10_Things_I_Hate_About_You");
		candidateMovies.add("http://dbpedia.org/resource/Hot_Fuzz");
		candidateMovies.add("http://dbpedia.org/resource/The_American_Beauty");
		candidateMovies.add("http://dbpedia.org/resource/A_Beautiful_Mind_(film)");
		candidateMovies.add("http://dbpedia.org/resource/White_Chicks");
		candidateMovies.add("http://dbpedia.org/resource/Rocky");
		candidateMovies.add("http://dbpedia.org/resource/Interview_with_the_Vampire_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Jurassic_Park");
		candidateMovies.add("http://dbpedia.org/resource/The_Curious_Case_of_Benjamin_Button_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Megamind");
		candidateMovies.add("http://dbpedia.org/resource/The_Big_Lebowski");
		candidateMovies.add("http://dbpedia.org/resource/Friday_After_Next");
		candidateMovies.add("http://dbpedia.org/resource/The_Butterfly_Effect");
		candidateMovies.add("http://dbpedia.org/resource/The_Ugly_Truth");
		candidateMovies.add("http://dbpedia.org/resource/Alien_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Transformers:_Revenge_of_the_Fallen");
		candidateMovies.add("http://dbpedia.org/resource/Ace_Ventura:_Pet_Detective");
		candidateMovies.add("http://dbpedia.org/resource/The_Color_Purple_(film)");
		candidateMovies.add("http://dbpedia.org/resource/The_Yes_Man");
		candidateMovies.add("http://dbpedia.org/resource/Clash_of_the_Titans_(2010_film)");
		candidateMovies.add("http://dbpedia.org/resource/Full_Metal_Jacket");
		candidateMovies.add("http://dbpedia.org/resource/Trainspotting_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Gran_Torino");
		candidateMovies.add("http://dbpedia.org/resource/Big_Fish");
		candidateMovies.add("http://dbpedia.org/resource/Fireproof_(film)");
		candidateMovies.add("http://dbpedia.org/resource/Forgetting_Sarah_Marshall");
		candidateMovies.add("http://dbpedia.org/resource/Law_Abiding_Citizen");
		candidateMovies.add("http://dbpedia.org/resource/Billy_Madison");
		candidateMovies.add("http://dbpedia.org/resource/Dinner_for_Schmucks");
		candidateMovies.add("http://dbpedia.org/resource/The_Book_of_Eli");
		
		String baseMovie = "http://dbpedia.org/resource/Aladdin_(1992_Disney_film)";
		Set<String> related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Mulan_(1998_film)");
		related.add("http://dbpedia.org/resource/The_Little_Mermaid_(1989_film)");
		related.add("http://dbpedia.org/resource/Pocahontas_(1995_film)");
		related.add("http://dbpedia.org/resource/Beauty_and_the_Beast_(1991_film)");
		related.add("http://dbpedia.org/resource/The_Lion_King");
		related.add("http://dbpedia.org/resource/Hercules_(1997_film)");
		related.add("http://dbpedia.org/resource/The_Princess_and_the_Frog");
		related.add("http://dbpedia.org/resource/Alice_in_Wonderland_(1951_film)");
		related.add("http://dbpedia.org/resource/Frozen_(2013_film)");
		related.add("http://dbpedia.org/resource/Pinocchio_(1940_film)");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);

		baseMovie = "http://dbpedia.org/resource/Star_Wars_(film)";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Return_of_the_Jedi");
		related.add("http://dbpedia.org/resource/Star_Wars:_The_Clone_Wars_(film)");
		related.add("http://dbpedia.org/resource/Star_Trek_(film)");
		related.add("http://dbpedia.org/resource/Star_Wars:_Episode_II_–_Attack_of_the_Clones");
		related.add("http://dbpedia.org/resource/Star_Wars:_Episode_I_–_The_Phantom_Menace");
		related.add("http://dbpedia.org/resource/Star_Wars:_Episode_III_–_Revenge_of_the_Sith");
		related.add("http://dbpedia.org/resource/The_Empire_Strikes_Back");
		related.add("http://dbpedia.org/resource/Star_Wars:_The_Force_Awakens");
		related.add("http://dbpedia.org/resource/Star_Trek_III:_The_Search_for_Spock");
		related.add("http://dbpedia.org/resource/Star_Trek_V:_The_Final_Frontier");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);

		baseMovie = "http://dbpedia.org/resource/The_Amazing_Spider-Man_(2012_film)";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Deadpool_(film)");
		related.add("http://dbpedia.org/resource/Wonder_Woman_(2017_film)");
		related.add("http://dbpedia.org/resource/The_Avengers_(2012_film)");
		related.add("http://dbpedia.org/resource/Justice_League_(film)");
		related.add("http://dbpedia.org/resource/Daredevil_(film)");
		related.add("http://dbpedia.org/resource/Iron_Man_(2008_film)");
		related.add("http://dbpedia.org/resource/Thor_(film)");
		related.add("http://dbpedia.org/resource/Captain_America:_Civil_War");
		related.add("http://dbpedia.org/resource/Avengers:_Age_of_Ultron");
		related.add("http://dbpedia.org/resource/Ant-Man_(film)");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);

		baseMovie = "http://dbpedia.org/resource/Pitch_Perfect";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/High_School_Musical");
		related.add("http://dbpedia.org/resource/Legally_Blonde");
		related.add("http://dbpedia.org/resource/Glitter_(film)");
		related.add("http://dbpedia.org/resource/La_La_Land_(film)");
		related.add("http://dbpedia.org/resource/Grease_(film)");
		related.add("http://dbpedia.org/resource/The_Sisterhood_of_the_Traveling_Pants_(film)");
		related.add("http://dbpedia.org/resource/High_School_Musical_2");
		related.add("http://dbpedia.org/resource/High_School_Musical_3:_Senior_Year");
		related.add("http://dbpedia.org/resource/Hannah_Montana:_The_Movie");
		related.add("http://dbpedia.org/resource/A_Cinderella_Story");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);

		baseMovie = "http://dbpedia.org/resource/Gravity_(film)";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/The_Martian_(film)");
		related.add("http://dbpedia.org/resource/Interstellar_(film)");
		related.add("http://dbpedia.org/resource/Alien_(film)");
		related.add("http://dbpedia.org/resource/Prometheus_(2012_film)");
		related.add("http://dbpedia.org/resource/Avatar_(2009_film)");
		related.add("http://dbpedia.org/resource/Armageddon_(1998_film)");
		related.add("http://dbpedia.org/resource/Destination_Moon_(film)");
		related.add("http://dbpedia.org/resource/The_Martian_(film)");
		related.add("http://dbpedia.org/resource/Europa_Report");
		related.add("http://dbpedia.org/resource/Apollo_13_(film)");
		related.add("http://dbpedia.org/resource/The_Terminator");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		baseMovie = "http://dbpedia.org/resource/Spirited_Away";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/My_Neighbor_Totoro");
		related.add("http://dbpedia.org/resource/Princess_Mononoke");
		related.add("http://dbpedia.org/resource/Kiki's_Delivery_Service");
		related.add("http://dbpedia.org/resource/The_Wind_Rises");
		related.add("http://dbpedia.org/resource/Nausicaä_of_the_Valley_of_the_Wind_(film)");
		related.add("http://dbpedia.org/resource/Howl's_Moving_Castle_(film)");
		related.add("http://dbpedia.org/resource/Castle_in_the_Sky");
		related.add("http://dbpedia.org/resource/The_Castle_of_Cagliostro");
		related.add("http://dbpedia.org/resource/Porco_Rosso");
		related.add("http://dbpedia.org/resource/Ponyo");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);

		baseMovie = "http://dbpedia.org/resource/The_Evil_Dead";
		related = new LinkedHashSet<String>();
		related.add("http://dbpedia.org/resource/Paranormal_Activity");
		related.add("http://dbpedia.org/resource/Paranormal_Activity_2");
		related.add("http://dbpedia.org/resource/Paranormal_Activity_4");
		related.add("http://dbpedia.org/resource/Dawn_of_the_Dead_(1978_film)");
		related.add("http://dbpedia.org/resource/The_Innocents_(1961_film)");
		related.add("http://dbpedia.org/resource/The_Blair_Witch_Project");
		related.add("http://dbpedia.org/resource/Land_of_the_Dead");
		related.add("http://dbpedia.org/resource/Diary_of_the_Dead");
		related.add("http://dbpedia.org/resource/Survival_of_the_Dead");
		related.add("http://dbpedia.org/resource/Two_Evil_Eyes");
		related.add("http://dbpedia.org/resource/Shaun_of_the_Dead");
		
		baseMovies.add(baseMovie);
		relatedMovies.put(baseMovie, related);
		
		baseMovie = "http://dbpedia.org/resource/Mean_Girls";
        related = new LinkedHashSet<String>();
        related.add("http://dbpedia.org/resource/Easy_A");
        related.add("http://dbpedia.org/resource/Heathers");
        related.add("http://dbpedia.org/resource/Jawbreaker_(film)");
        related.add("http://dbpedia.org/resource/Mean_Girls_2");
        related.add("http://dbpedia.org/resource/The_Duff");
        related.add("http://dbpedia.org/resource/Three_O'Clock_High");
        related.add("http://dbpedia.org/resource/Can't_Hardly_Wait");
        related.add("http://dbpedia.org/resource/Clueless_(film)");
        related.add("http://dbpedia.org/resource/Fast_Times_at_Ridgemont_High");
        related.add("http://dbpedia.org/resource/American_Pie_(film)");

        baseMovies.add(baseMovie);
        relatedMovies.put(baseMovie, related);

        baseMovie = "http://dbpedia.org/resource/Into_the_Woods_(film)";
        related = new LinkedHashSet<String>();
        related.add("http://dbpedia.org/resource/Evita_(1996_film)");
        related.add("http://dbpedia.org/resource/West_Side_Story_(film)");
        related.add("http://dbpedia.org/resource/Fiddler_on_the_Roof_(film)");
        related.add("http://dbpedia.org/resource/Chicago_(2002_film)");
        related.add("http://dbpedia.org/resource/On_the_Town_(film)");
        related.add("http://dbpedia.org/resource/Rock_of_Ages_(2012_film)");
        related.add("http://dbpedia.org/resource/Bye_Bye_Birdie_(film)");
        related.add("http://dbpedia.org/resource/The_Sound_of_Music_(film)");
        related.add("http://dbpedia.org/resource/Leathernecking");
        related.add("http://dbpedia.org/resource/The_Rocky_Horror_Picture_Show");
        related.add("http://dbpedia.org/resource/Mary_Poppins_(film)");

        baseMovies.add(baseMovie);
        relatedMovies.put(baseMovie, related);

        baseMovie = "http://dbpedia.org/resource/Kill_Bill";
        related = new LinkedHashSet<String>();
        related.add("http://dbpedia.org/resource/Kill_Bill:_Volume_2");
        related.add("http://dbpedia.org/resource/Pulp_Fiction");
        related.add("http://dbpedia.org/resource/Reservoir_Dogs");
        related.add("http://dbpedia.org/resource/Django_Unchained");
        related.add("http://dbpedia.org/resource/Inglourious_Basterds");
        related.add("http://dbpedia.org/resource/Jackie_Brown");
        related.add("http://dbpedia.org/resource/The_Hateful_Eight");
        
        baseMovies.add(baseMovie);
        relatedMovies.put(baseMovie, related);
	}

	
	public static void runSimilarities()
	{
		allMovies = getAllMovies();
		currentPosition1 = 0;
		currentPosition2 = 0;
		totalMovies1 = baseMovies.size();
		totalMovies2 = allMovies.size();
		
		int nProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println(nProcessors + " PROCESSORS AVAILABLE");
		List<Thread> threads = new ArrayList<Thread>();
		
		for (int i=0; i < nProcessors; i++)
		{
			TestingSimilarityCalculator calc = new TestingSimilarityCalculator(String.valueOf(i));
			Thread t = new Thread(calc);
			threads.add(t);
			t.start();
		}
		
		for (Thread t : threads)
		{
			try
			{
				t.join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void runHybrids(List<String> movieUris1, List<String> movieUris2)
	{
		List<String> control = new ArrayList<String>();

		for(String movie1 : movieUris1)
		{
			for(String movie2 : movieUris2)
			{
				if (!movie1.equals(movie2) && (!(control.contains(movie1 + movie2)
						|| control.contains(movie2 + movie1))))
				{
					DBFunctions dbFunctions = getDatabaseConnection();
					synchronized (dbFunctions)
					{

						double score_LIT = dbFunctions.getDistanceFrom2Resources(movie1, movie2, ESimilarity.LITERAL.toString());
						double score_LDSD = dbFunctions.getDistanceFrom2Resources(movie1, movie2, ESimilarity.LDSD.toString());
						double score_RESIM = dbFunctions.getDistanceFrom2Resources(movie1, movie2, ESimilarity.RESIM.toString());

						try {
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_10.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_10.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.1), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_20.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_20.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.2), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_30.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_30.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.3), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_40.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_40.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.4), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_50.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_50.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.5), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_60.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_60.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.6), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_70.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_70.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.7), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_80.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_80.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.8), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.LDSD_LIT_90.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.LDSD_LIT_90.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_LDSD, 0.9), 0);
						} catch (NullPointerException e) {
							System.out.println("Unable to find LDSD score for "+movie1+" and "+movie2);
							e.printStackTrace();
						}

						try {
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_10.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_10.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.1), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_20.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_20.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.2), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_30.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_30.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.3), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_40.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_40.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.4), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_50.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_50.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.5), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_60.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_60.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.6), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_70.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_70.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.7), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_80.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_80.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.8), 0);
							if (!dbFunctions.checkSimilarity(movie1, movie2, ESimilarity.RESIM_LIT_90.toString()))
								dbFunctions.insertSemanticDistance(movie1, movie2, ESimilarity.RESIM_LIT_90.toString(), LDSD_LIT.calculateSimilarity(score_LIT, score_RESIM, 0.9), 0);
						} catch (NullPointerException e) {
							System.out.println("Unable to find RESIM score for "+movie1+" and "+movie2);
							e.printStackTrace();
						}
					}

					control.add(movie1 + movie2);
					control.add(movie2 + movie1);
				}
			}
		}
	}
	
	public static void runTests()
	{
		calculateEvaluationMeasure(ESimilarity.LDSD);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_10);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_20);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_30);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_40);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_50);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_60);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_70);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_80);
		calculateEvaluationMeasure(ESimilarity.LDSD_LIT_90);
		calculateEvaluationMeasure(ESimilarity.LITERAL);

		calculateEvaluationMeasure(ESimilarity.RESIM);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_10);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_20);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_30);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_40);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_50);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_60);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_70);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_80);
		calculateEvaluationMeasure(ESimilarity.RESIM_LIT_90);
	}
	
	public static void calculateEvaluationMeasure(ESimilarity pSimilarity)
	{
		List<String> lIgnore = new LinkedList<String>();
		List<String> allMovies = new LinkedList<String>(getAllMovies());
		
		int n = 10;
		double cummulatedPrecision = 0d;
		for(int i = 0; i < baseMovies.size(); i++)
		{
			cummulatedPrecision += PrecisionAndRecallString.precisionAt(DBFunctions.getNSimilarMovies(baseMovies.get(i), allMovies.size(), pSimilarity.toString(), allMovies), relatedMovies.get(baseMovies.get(i)), lIgnore, n);
		}
		System.out.printf("%15s & %.10f", pSimilarity.toString(), cummulatedPrecision/baseMovies.size());

		double APCummulated = 0d;
		for(int i = 0; i < baseMovies.size(); i++)
		{
			APCummulated += PrecisionAndRecallString.AP(DBFunctions.getNSimilarMovies(baseMovies.get(i), allMovies.size(), pSimilarity.toString(), allMovies), relatedMovies.get(baseMovies.get(i)), lIgnore);
		}
		System.out.printf(" &  %.10f", APCummulated/baseMovies.size());

		double totalNDCG = 0;
		for(int i = 0; i < baseMovies.size(); i++)
		{
			String movie = baseMovies.get(i);
			double NDCG = NDCGString.calculateNDCG(DBFunctions.getNSimilarMovies(movie, allMovies.size(), pSimilarity.toString(), allMovies), relatedMovies.get(movie), lIgnore);
			totalNDCG += NDCG;
		}
		System.out.printf(" & %.10f\n", totalNDCG/baseMovies.size());
		
	}
}
