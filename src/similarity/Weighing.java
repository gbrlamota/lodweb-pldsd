package similarity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.stat.inference.*;

//import org.primefaces.component.rating.Rating;

import database.DBFunctions;
import metric.NDCGString;
import metric.PrecisionAndRecallString;
import model.Movie;
import model.MovieRating;
import model.RatingScale;
import model.User;
import node.SimpleTriple;
import node.SparqlWalk;
import util.StringUtilsNode;

public class Weighing
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
	
	private static List<MovieRating> movieRatingList = new ArrayList<MovieRating>();
	private static Set<String> properties = new HashSet<String>();
	private static Map<String, Integer> objectFrequency = new HashMap<String, Integer>();
	private static Map<String, Double> propertyWeight = new HashMap<String, Double>();
	
	private static List<String> allMovies = new LinkedList<String>();
	private static List<String> baseMovies = new LinkedList<String>();
	private static Map<String, Set<String>> relatedMovies = new HashMap<String, Set<String>>();
	private static List<String> candidateMovies = new LinkedList<String>();

	private static String currentMovie1 = "";
	private static List<String> synchronizedControl = new ArrayList<String>();
	
	private static int totalMovies1;
	private static int totalMovies2;
	private static int currentPosition1;
	private static int currentPosition2;
	private static User user = new User(1048);		//1048 = 40 ratings; 6 = 71 ratings; 8=130 rat; 19=246 rat..

	public static User getUser() {
		return user;
	}
	public static void setUser(User user) {
		Weighing.user = user;
	}
	
	
	public static void main(String[] args)
	{
		loadLists(user);
		
		runWeigh(1); //run weigh for 1 level
		
		runSimilarities();
		
		//calculateEvaluationMeasure(ESimilarity.PLDSD);
		
		//calculateTTest();

	}
	
	private static void calculateTTest() {

		//double[] sample1 = { 1  , 2  , 3   ,4 , 3, 5, 6.1, 3.4, 2.9, 4.4};
		//double[] sample2 = { 5.2, 4.2, 7.24,4 , 5, 6, 4.1, 5.9, 7.0, 8.0};
			
		//LDSD
		double[] sample1 = {0.97084036,  0.948615699,  0.937301288,  0.934745063,  0.916338348,  0.916425819,  0.911712761,  0.910794907,  0.898451953,  0.894472055,  0.890385138,  0.887329423,  0.882561493,  0.883835785,  0.886282353,  0.88143512,  0.881581504,  0.880036732,  0.876410025,  0.870133337,  0.874311273,  0.861533436,  0.859857789,  0.858387005,  0.856322704,  0.855962165,  0.850552113,  0.848326913,  0.845011114,  0.844604696,  0.838232287,  0.841879019,  0.835025899,  0.829393074,  0.835095318,  0.838830879,  0.83097056,  0.833509152,  0.822998914,  0.829059472,  0.827443724,  0.832081737,  0.831631966,  0.817632644,  0.823763734,  0.828515395,  0.825617168,  0.825442405,  0.821692065,  0.825492439,  0.821247528,  0.815003282,  0.813015615,  0.813847649,  0.815126804,  0.811563739,  0.811505625,  0.805135422,  0.810876226,  0.810112201,  0.805975072,  0.801923542,  0.798664688,  0.793907413,  0.806287403,  0.795483382,  0.795074423,  0.800667943,  0.800252883,  0.799927857,  0.79452244,  0.789642234,  0.789584194,  0.78913842,  0.785205038,  0.791971302,  0.78002546,  0.789918054,  0.785651556,  0.773185069,  0.788653596,  0.783654214,  0.781161714,  0.784404863,  0.77187007,  0.783410381,  0.778774567,  0.779519071,  0.766522078,  0.773659408,  0.772090924,  0.765075428,  0.770946672,  0.774625633,  0.774606511,  0.774102653,  0.773907135,  0.773446268,  0.764606813,  0.773106004,  0.761841075,  0.771241605,  0.762227211,  0.770504712,  0.774403837,  0.760737227,  0.760199459,  0.761825122,  0.765762296,  0.765509236,  0.763906413,  0.751858095,  0.75905786,  0.7464326,  0.763397031,  0.763292852,  0.75349703,  0.75736389,  0.761196362,  0.764810649,  0.753888113,  0.758756818,  0.753764339,  0.757912891,  0.74432856,  0.746920502,  0.756979851,  0.745603561,  0.750028205,  0.747134724,  0.753867794,  0.743023038,  0.747031327,  0.752036799,  0.744065029,  0.743745907,  0.738090203,  0.743320192,  0.743266152,  0.74298623,  0.742886014,  0.747648115,  0.747157858,  0.746269035,  0.740062902,  0.745043158,  0.744823293,  0.739245496,  0.743685896,  0.737792976,  0.742573383,  0.747261593,  0.741627493,  0.740133197,  0.727386522,  0.738618109,  0.720545643,  0.73251211,  0.737263371,  0.725090418,  0.736685805,  0.735510512,  0.722602893,  0.728171464,  0.733873787,  0.733664537,  0.727663353,  0.73296597,  0.719829368,  0.718623196,  0.717786115,  0.723852871,  0.729505818,  0.728822063,  0.728452892,  0.725759569,  0.719162174,  0.718505425,  0.724033985,  0.713993068,  0.717262823,  0.702854147,  0.72279753,  0.722591099,  0.71435152,  0.726365357,  0.712197731,  0.71121644,  0.717427683,  0.710581524,  0.710430977,  0.715969565,  0.722194949,  0.708831489,  0.708095083,  0.713942812,  0.713833335,  0.713582849,  0.698875118,  0.696494686,  0.709966169,  0.702872576,  0.701465059,  0.708375552,  0.697504617,  0.714955124,  0.707873638,  0.696267391,  0.706662561,  0.698567564,  0.712370549,  0.712251272,  0.705245355,  0.703764721,  0.702652336,  0.695050982,  0.686591407,  0.690056904,  0.704410058,  0.696609863,  0.696490597,  0.686409682,  0.701216728,  0.699953952,  0.692168272,  0.69936717,  0.690856629,  0.690834117,  0.674250391,  0.698330863,  0.698181697,  0.680282861,  0.696173147,  0.687467351,  0.685934309,  0.667880136,  0.684700253,  0.684279578,  0.683617067,  0.673183755,  0.679623029,  0.678959985,  0.677229363,  0.664410643,  0.669548084,  0.669266358,  0.668633912,  0.668413254,  0.66693631,  0.664416327,  0.663538733,  0.663201896,  0.660045252,  0.625682201,  0.666529008,  0.655160166,  0.647366363,  0.634436104,  0.654092877,  0.641394799,  0.638957596,  0.629398715,  0.639714382,  0.626470277,  0.618300881,  0.614779989,  0.587654577,  0.584654356,  0.593168684,  0.589766683,  0.597102485,  0.592644408,  0.575756235,  0.560165296,  0.563216143,  0.578038093,  0.553293186,  0.563205547,  0.561794307,  0.532192011,  0.547286914,  0.545805952,  0.528568805,  0.525478293,  0.487972544,  0.481279679,  0.439725759,  0.421557468,  0.404719688,  0.398656812,  0.164396497};		
		
		//User #1
		
		//PLDSD5
		//double[] sample2 = {0.953362412,  0.916020428,  0.896439035,  0.890124588,  0.884938228,  0.875595308,  0.863010826,  0.86123444,  0.848471968,  0.848307839,  0.8458814,  0.843568769,  0.841940324,  0.837940529,  0.837197109,  0.837052026,  0.836699688,  0.836588396,  0.836162474,  0.831097005,  0.830472487,  0.827957871,  0.823833473,  0.82175713,  0.819370102,  0.817220474,  0.81406163,  0.811966985,  0.809660815,  0.806287403,  0.796667131,  0.795999959,  0.795465391,  0.794848935,  0.793480866,  0.792940901,  0.788754221,  0.788155457,  0.787195352,  0.786530684,  0.783440037,  0.779911449,  0.779251087,  0.778028377,  0.777584175,  0.775459689,  0.775384516,  0.774403837,  0.774055329,  0.773575823,  0.769939321,  0.768396687,  0.768351885,  0.76813121,  0.767906516,  0.767806272,  0.766662413,  0.764810649,  0.763449699,  0.763443165,  0.763335358,  0.761803255,  0.761734809,  0.759886056,  0.759586276,  0.758323142,  0.757122193,  0.753703226,  0.75365347,  0.753303491,  0.752771407,  0.752300873,  0.750705668,  0.749953924,  0.747902627,  0.746995565,  0.745562888,  0.744543078,  0.744110629,  0.744041153,  0.742258968,  0.739565334,  0.739364942,  0.73932293,  0.736863595,  0.736131666,  0.735383667,  0.734729341,  0.73179954,  0.730944436,  0.730149357,  0.729278052,  0.728967212,  0.726365357,  0.725187589,  0.724293018,  0.72383595,  0.723714357,  0.72244338,  0.722194949,  0.720403771,  0.719671109,  0.716713293,  0.715418327,  0.715153021,  0.714955124,  0.713562437,  0.712211272,  0.709595662,  0.705070149,  0.704410058,  0.703984817,  0.701769583,  0.699758042,  0.698510411,  0.698489001,  0.698181697,  0.692258292,  0.691628877,  0.678011416,  0.677412004,  0.677202885,  0.67580337,  0.673416259,  0.672585164,  0.66927794,  0.666529008,  0.664656398,  0.657290522,  0.655141254,  0.649353717,  0.64034427,  0.639714382,  0.606320948,  0.603136217,  0.597102485,  0.589797353,  0.578340346,  0.549498452,  0.545805952,  0.528568805,  0.481279679,  0.439725759};		
		//PLDSD4
		//double[] sample2 = {0.973797848,  0.953362412,  0.939949938,  0.919991777,  0.916834389,  0.916020428,  0.8996878,  0.896439035,  0.890124588,  0.884938228,  0.883480331,  0.880061816,  0.877158169,  0.875595308,  0.866101028,  0.863010826,  0.861605811,  0.86123444,  0.85585903,  0.855524811,  0.848471968,  0.848307839,  0.8458814,  0.845454829,  0.843568769,  0.842987029,  0.841940324,  0.837940529,  0.837197109,  0.837052026,  0.836699688,  0.836588396,  0.836162474,  0.835088467,  0.831097005,  0.830896733,  0.830472487,  0.829195085,  0.827957871,  0.823833473,  0.82175713,  0.821249734,  0.819370102,  0.817220474,  0.81406163,  0.813768524,  0.813027735,  0.811966985,  0.811139472,  0.809660815,  0.806287403,  0.805293499,  0.804922868,  0.803476466,  0.796667131,  0.795999959,  0.795465391,  0.794848935,  0.793480866,  0.792940901,  0.792259057,  0.788754221,  0.788155457,  0.787195352,  0.786530684,  0.783440037,  0.781771769,  0.779911449,  0.779251087,  0.778720961,  0.778702528,  0.778216836,  0.778028377,  0.777584175,  0.777256236,  0.775459689,  0.775384516,  0.774403837,  0.774055329,  0.773575823,  0.770182914,  0.769939321,  0.768396687,  0.768351885,  0.768229455,  0.76813121,  0.767906516,  0.767806272,  0.76760958,  0.766662413,  0.764810649,  0.763449699,  0.763443165,  0.763335358,  0.761803255,  0.761734809,  0.760606895,  0.759886056,  0.759586276,  0.758744092,  0.758323142,  0.757122193,  0.754095223,  0.753965191,  0.753703226,  0.75365347,  0.753303491,  0.752771407,  0.752300873,  0.750705668,  0.750060737,  0.749953924,  0.747902627,  0.747261593,  0.746995565,  0.745562888,  0.744543078,  0.744110629,  0.744041153,  0.742812369,  0.742527952,  0.742258968,  0.741133005,  0.739565334,  0.739364942,  0.73932293,  0.738695996,  0.736863595,  0.736131666,  0.73583383,  0.735383667,  0.734729341,  0.73179954,  0.730944436,  0.730149357,  0.729820279,  0.729278052,  0.728967212,  0.726365357,  0.725187589,  0.724293018,  0.72383595,  0.723714357,  0.72244338,  0.722194949,  0.722119977,  0.721449332,  0.72050828,  0.720403771,  0.720164661,  0.719671109,  0.717609239,  0.716713293,  0.715418327,  0.715153021,  0.714955124,  0.713562437,  0.71278655,  0.712370549,  0.712251272,  0.712211272,  0.709595662,  0.705070149,  0.704410058,  0.703984817,  0.70387128,  0.701769583,  0.701216728,  0.699953952,  0.699758042,  0.69936717,  0.698510411,  0.698489001,  0.69845,  0.698181697,  0.696233554,  0.693830653,  0.692998987,  0.692657987,  0.692258292,  0.691628877,  0.689832219,  0.68720632,  0.68194096,  0.678011416,  0.677412004,  0.677202885,  0.67580337,  0.673416259,  0.672585164,  0.66927794,  0.666666986,  0.666529008,  0.664656398,  0.657290522,  0.655141254,  0.654092877,  0.649353717,  0.64034427,  0.639714382,  0.637586901,  0.62990153,  0.611178066,  0.606320948,  0.603136217,  0.597102485,  0.592644408,  0.590038669,  0.589797353,  0.578340346,  0.578038093,  0.563205547,  0.561794307,  0.549498452,  0.547286914,  0.545805952,  0.528568805,  0.525478293,  0.487972544,  0.481279679,  0.439725759,  0.404719688}; 
		//PLDSD3
		double[] sample2 = {0.973797848,  0.953362412,  0.941374492,  0.939949938,  0.920434897,  0.919991777,  0.916834389,  0.916020428,  0.902516539,  0.8996878,  0.896439035,  0.892707147,  0.892412373,  0.890124588,  0.889381351,  0.888486178,  0.884938228,  0.883480331,  0.880061816,  0.877158169,  0.875595308,  0.866101028,  0.863010826,  0.861605811,  0.86123444,  0.860898158,  0.85585903,  0.855524811,  0.854284136,  0.848471968,  0.848307839,  0.8458814,  0.845454829,  0.84362109,  0.843568769,  0.842987029,  0.841940324,  0.837940529,  0.837197109,  0.837052026,  0.836699688,  0.836588396,  0.836162474,  0.835088467,  0.831097005,  0.830896733,  0.830472487,  0.830307325,  0.829195085,  0.827957871,  0.823833473,  0.82175713,  0.821249734,  0.819370102,  0.817891504,  0.817220474,  0.81437885,  0.81406163,  0.813768524,  0.813027735,  0.811966985,  0.811139472,  0.809660815,  0.808474889,  0.806287403,  0.805293499,  0.804922868,  0.803878251,  0.803476466,  0.803161855,  0.801230366,  0.796667131,  0.796612904,  0.796196443,  0.795999959,  0.795465391,  0.794848935,  0.793480866,  0.792940901,  0.792508564,  0.792259057,  0.791077687,  0.788754221,  0.788155457,  0.787773328,  0.787195352,  0.786530684,  0.783440037,  0.783152529,  0.781771769,  0.780314065,  0.779911449,  0.779251087,  0.778720961,  0.778702528,  0.778216836,  0.778028377,  0.777584175,  0.777509228,  0.777256236,  0.777075177,  0.775459689,  0.775384516,  0.774749763,  0.774403837,  0.774055329,  0.773575823,  0.770791172,  0.770182914,  0.769939321,  0.768396687,  0.768351885,  0.768229455,  0.76813121,  0.767906516,  0.767806272,  0.76760958,  0.766662413,  0.76578931,  0.764810649,  0.763449699,  0.763443165,  0.763335358,  0.762631762,  0.761803255,  0.761773436,  0.761734809,  0.760606895,  0.759886056,  0.759586276,  0.758744092,  0.758323142,  0.757122193,  0.756985188,  0.754389103,  0.754095223,  0.753965191,  0.753703226,  0.75365347,  0.753395752,  0.753303491,  0.752771407,  0.752300873,  0.751447902,  0.750705668,  0.750271673,  0.750060737,  0.749953924,  0.74896965,  0.748618561,  0.747902627,  0.747261593,  0.746995565,  0.745562888,  0.744543078,  0.744110629,  0.744041153,  0.743768783,  0.742812369,  0.742527952,  0.742258968,  0.741133005,  0.740347239,  0.739788501,  0.739565334,  0.739364942,  0.73932293,  0.738695996,  0.737918748,  0.736863595,  0.736131666,  0.73583383,  0.735383667,  0.734729341,  0.734376089,  0.73179954,  0.731544488,  0.730944436,  0.730149357,  0.729820279,  0.729809449,  0.729278052,  0.728967212,  0.728769871,  0.727152008,  0.726365357,  0.725187589,  0.724293018,  0.72383595,  0.723714357,  0.723577166,  0.72244338,  0.722194949,  0.722119977,  0.721449332,  0.72050828,  0.720403771,  0.720164661,  0.719671109,  0.717609239,  0.716713293,  0.716697629,  0.715418327,  0.715196015,  0.715153021,  0.714955124,  0.714717323,  0.714056211,  0.713562437,  0.71278655,  0.712370549,  0.712251272,  0.712211272,  0.710799962,  0.70973988,  0.709595662,  0.70905537,  0.705070149,  0.704410058,  0.703984817,  0.70387128,  0.701769583,  0.701216728,  0.699953952,  0.699758042,  0.69936717,  0.698510411,  0.698489001,  0.69845,  0.698330863,  0.698181697,  0.696233554,  0.696173147,  0.695287755,  0.693830653,  0.692998987,  0.692657987,  0.692258292,  0.691628877,  0.689832219,  0.687835773,  0.68720632,  0.685563691,  0.68194096,  0.678278447,  0.678011416,  0.677412004,  0.677202885,  0.67580337,  0.673416259,  0.672585164,  0.672266206,  0.66927794,  0.666666986,  0.666529008,  0.664656398,  0.657290522,  0.655141254,  0.654092877,  0.65165303,  0.649353717,  0.64034427,  0.639714382,  0.637586901,  0.62990153,  0.626592329,  0.613808551,  0.611178066,  0.606320948,  0.603136217,  0.597102485,  0.592644408,  0.590038669,  0.589797353,  0.578340346,  0.578038093,  0.56909995,  0.563205547,  0.561794307,  0.549498452,  0.547286914,  0.545805952,  0.528568805,  0.525478293,  0.487972544,  0.481279679,  0.439725759,  0.421557468,  0.404719688,  0.398656812,  0.164396497}; 
		//double t_statistic;
		//TTest ttest = new TTest();
		//t_statistic = ttest.t(sample1, sample2);
		//System.out.println(Double.toString( t_statistic) );


		System.out.println(TestUtils.pairedT(sample1, sample2));//t statistics 
		System.out.println(TestUtils.pairedTTest(sample1, sample2));//p value 
		System.out.println(TestUtils.pairedTTest(sample1, sample2, 0.000001)); 
	}
	
	
	private static void loadLists(User user)
	{
		DBFunctions dbFunctions = getDatabaseConnection();
		synchronized (dbFunctions)
		{
			movieRatingList = dbFunctions.getRatingsByUser(user);
			//allMovies = dbFunctions.getAllMovies();//limitado a 200
		}
		
		String baseMovie = "";
		Set<String> related = new LinkedHashSet<String>();
		int count = 0;
		
		//building a set of 5 stars movies
		for (MovieRating rating : movieRatingList) {
			if (rating.getRating() == 5) {
				if (count == 0) { //first movie returned will be the base movie
					baseMovie = rating.getMovie().getUri();
					
				}else {
					related.add(rating.getMovie().getUri());
				}
				count++;
			}
		}
		if (!baseMovie.isEmpty()) {
			baseMovies.add(baseMovie);
			relatedMovies.put(baseMovie, related);
		}	
		related = new LinkedHashSet<String>();
		baseMovie = "";
		count = 0;
		
		//building a set of 4 stars movies
		/*for (MovieRating rating : movieRatingList) {
			if (rating.getRating() == 4) {
				if (count == 0) {
					baseMovie = rating.getMovie().getUri();
					
				}else {
					related.add(rating.getMovie().getUri());
				}
				count++;
			}
		}
		if (!baseMovie.isEmpty()) {
			baseMovies.add(baseMovie);
			relatedMovies.put(baseMovie, related);
		}	
		related = new LinkedHashSet<String>();
		baseMovie = "";
		count = 0;*/
		
		//building a set of 3 stars movies
		/*for (MovieRating rating : movieRatingList) {
			if (rating.getRating() == 3) {
				if (count == 0) {
					baseMovie = rating.getMovie().getUri();
					
				}else {
					related.add(rating.getMovie().getUri());
				}
				count++;
			}
		}
		if (!baseMovie.isEmpty()) {
			baseMovies.add(baseMovie);
			relatedMovies.put(baseMovie, related);
		}	
		related = new LinkedHashSet<String>();
		baseMovie = "";
		count = 0;*/
		
		//building a set of 2 stars movies
		/*for (MovieRating rating : movieRatingList) {
			if (rating.getRating() == 2) {
				if (count == 0) {
					baseMovie = rating.getMovie().getUri();
					
				}else {
					related.add(rating.getMovie().getUri());
				}
				count++;
			}
		}
		if (!baseMovie.isEmpty()) {
			baseMovies.add(baseMovie);
			relatedMovies.put(baseMovie, related);
		}	
		related = new LinkedHashSet<String>();
		baseMovie = "";
		count = 0;*/
		
		//building a set of 1 stars movies
		/*for (MovieRating rating : movieRatingList) {
			if (rating.getRating() == 1) {
				if (count == 0) {
					baseMovie = rating.getMovie().getUri();
					
				}else {
					related.add(rating.getMovie().getUri());
				}
				count++;
			}
		}
		if (!baseMovie.isEmpty()) {
			baseMovies.add(baseMovie);
			relatedMovies.put(baseMovie, related);
		}	*/
		
		//building the candidate movies list
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
		
		//removing baseMovies from candidateMovies list
		//TODO revisar essa logica
		candidateMovies.removeAll(baseMovies);
		for(String baseMovie2 : baseMovies)
		{
			candidateMovies.removeAll(relatedMovies.get(baseMovie2));	
		}
		
		//building the allMovies list
		allMovies = getAllMovies();
		
	}
	
	private static List<String> getAllMovies()
	{
		allMovies.addAll(candidateMovies);
		allMovies.addAll(baseMovies);
		for(String baseMovie : baseMovies)
		{
			allMovies.addAll(relatedMovies.get(baseMovie));	
		}
		
		return new LinkedList<>(allMovies);
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
	

	public static void runSimilarities()
	{		
		currentPosition1 = 0;
		currentPosition2 = 0;
		totalMovies1 = baseMovies.size();
		totalMovies2 = allMovies.size();
		
		int nProcessors = Runtime.getRuntime().availableProcessors();
		System.out.println(nProcessors + " PROCESSORS AVAILABLE");
		List<Thread> threads = new ArrayList<Thread>();
		
		for (int i=0; i < nProcessors; i++)
		{
			WeighingSimilarityCalculator calc = new WeighingSimilarityCalculator(String.valueOf(i));
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
	
	public static void calculateEvaluationMeasure(ESimilarity pSimilarity)
	{
		List<String> lIgnore = new LinkedList<String>();
		
		//Precision@5
		int n = 5;
		double cummulatedPrecision = 0d;
		for(int i = 0; i < baseMovies.size(); i++)
		{
			cummulatedPrecision += PrecisionAndRecallString.precisionAt(DBFunctions.getPersonalizedNSimilarMovies(baseMovies.get(i), 
					allMovies.size(), pSimilarity.toString(), allMovies, user), relatedMovies.get(baseMovies.get(i)), lIgnore, n);
		}
		System.out.printf("%15s & %.10f", pSimilarity.toString(), cummulatedPrecision/baseMovies.size());

		//Precision@10
		n = 10;
		cummulatedPrecision = 0d;
		for(int i = 0; i < baseMovies.size(); i++)
		{
			cummulatedPrecision += PrecisionAndRecallString.precisionAt(DBFunctions.getPersonalizedNSimilarMovies(baseMovies.get(i), 
					allMovies.size(), pSimilarity.toString(), allMovies, user), relatedMovies.get(baseMovies.get(i)), lIgnore, n);
		}
		System.out.printf(" & %.10f", cummulatedPrecision/baseMovies.size());
		
		//MAP
		double APCummulated = 0d;
		for(int i = 0; i < baseMovies.size(); i++)
		{
			APCummulated += PrecisionAndRecallString.AP(DBFunctions.getPersonalizedNSimilarMovies(baseMovies.get(i), 
					allMovies.size(), pSimilarity.toString(), allMovies, user), relatedMovies.get(baseMovies.get(i)), lIgnore);
		}
		System.out.printf(" &  %.10f", APCummulated/baseMovies.size());

		//NDCG
		double totalNDCG = 0;
		for(int i = 0; i < baseMovies.size(); i++)
		{
			String movie = baseMovies.get(i);
			double NDCG = NDCGString.calculateNDCG(DBFunctions.getPersonalizedNSimilarMovies(movie, 
					allMovies.size(), pSimilarity.toString(), allMovies, user), relatedMovies.get(movie), lIgnore);
			totalNDCG += NDCG;
		}
		System.out.printf(" & %.10f\n", totalNDCG/baseMovies.size());
		
	}
	
	private static void storeWeight(User user, int level) {
	
		DBFunctions dbFunctions = getDatabaseConnection();
		synchronized (dbFunctions)
		{
			dbFunctions.storeWeightsByUser(user, propertyWeight, level);
		}
	
	}


	public static MovieRating findByMovieURIIn(Collection<MovieRating> movieRatingList, String movieURIIn) {
	    return movieRatingList.stream().filter(movieRating -> movieURIIn.equals(movieRating.getMovie().getUri())).findFirst().orElse(null);
	}
	
	/**
	 * processing user personalized weight of properties
	 */
	public static int runWeigh(int nlevels)
	{
		if (nlevels > 0) {
			List<String> movieList = new ArrayList<String>();
			List<SimpleTriple> triples = new ArrayList<SimpleTriple>();
			List<SimpleTriple> allTriples = new ArrayList<SimpleTriple>();
			
			//1st loop: calculates the frequency of each object regard each movie in the user model (outcoming links)
			for(MovieRating movieRating : movieRatingList) {
				
				movieList.add(movieRating.getMovie().getUri());
				
				//TODO tenho que salvar as triplas a cada iteração para ter uma lista completa de todas as triplas analisadas
				triples = SparqlWalk.getDBpediaObjectsTripletBySubjectURI(movieRating.getMovie().getUri());
				allTriples.addAll(triples);
				
				for(SimpleTriple triple : triples) {
					
					//Adiciona a property no Set
					properties.add(triple.getPredicate());
					//conta a frenquência do object e adiciona ou incrementa no Hash 
					if (objectFrequency.containsKey(triple.getObject()))
						objectFrequency.put(triple.getObject(), objectFrequency.get(triple.getObject())+1); //incrementa a entrada no Hash
					else
						objectFrequency.put(triple.getObject(), 1); //cria nova entrada no Hash
				}
				
			}
			
			//2nd loop: calculates the frequency of each object regard each movie in the user model (incoming links)
			for(MovieRating movieRating : movieRatingList) {
				
				movieList.add(movieRating.getMovie().getUri());
				
				//TODO retorna as triplas em ordem iversa, ou seja, coloca o sujeito como se fosse objeto, 
				//para facilirar os calculos no proximo loop
				triples = SparqlWalk.getDBpediaObjectsTripletByObjectURI(movieRating.getMovie().getUri());
				allTriples.addAll(triples);
				
				for(SimpleTriple triple : triples) {
					
					//Adiciona a property no Set
					properties.add(triple.getPredicate());
					//conta a frenquência do object e adiciona ou incrementa no Hash 
					if (objectFrequency.containsKey(triple.getObject()))
						objectFrequency.put(triple.getObject(), objectFrequency.get(triple.getObject())+1); //incrementa a entrada no Hash
					else
						objectFrequency.put(triple.getObject(), 1); //cria nova entrada no Hash
				}
				
			}
			
			//3rd loop: calculates the weight for each property in properties list
			for(String property : properties) {
				
				/* TODO melhorar essa forma de resgatar as triplas pelas propriedades, 
				 		está pegando apenas as triplas em que o subject é um dos filmes da lista */
				/*resgatando as triplas por propriedades dentro da lista montada com todas as triplas no loop anterior e 
				nao novamente no SPARQL*/
				//triples = SparqlWalk.getDBpediaObjectTripletByPropertyURIBySubjectList(property, StringUtilsNode.formatURIList(movieList));
				triples = getTriplesByPropertyURIBySubjectList(property, StringUtilsNode.formatURIList(movieList), allTriples);
										
				int  rating = 0;
				int frequencySum = 0;
				int count = 0;
				Integer freq = null;
				
				for(SimpleTriple triple : triples) {
					//calculating the equations's numerator 
					MovieRating movieRating = findByMovieURIIn(movieRatingList, triple.getSubject());
					if (movieRating != null) {
						rating = RatingScale.returnScale(movieRating.getRating());
						freq = objectFrequency.get(triple.getObject());
						if (freq != null) {
							frequencySum +=  freq*rating;
							count++;	
						}
					}
				}
				
				//calculating the equations's denominator 
				if (count != 0) {
					Double d = new Double(frequencySum/count);
					propertyWeight.put(property, d);
				}	
				
			}
			
			normalizeWeigh();
			storeWeight(user, nlevels); 
			
			//buildind the new rating list just in case that there is another level to compute
			/*movieRatingList.clear();
			for (String key : objectFrequency.keySet()) {
	        Integer value = objectFrequency.get(key);
	        movieRatingList.add(new MovieRating(user, new Movie(key), value)); 
			}	*/
			
			//clearing all lists just in case that there is another level to compute
			/*properties.clear();
			propertyWeight.clear();
			objectFrequency.clear();*/
			
			//return runWeigh(nlevels-1);
			return 0;
			
		}else return 0;
		
	}
	
	private static List<SimpleTriple> getTriplesByPropertyURIBySubjectList(String property, String movieList,
			List<SimpleTriple> allTriples) {

		List<SimpleTriple> triples = new ArrayList<SimpleTriple>();
				
		for (SimpleTriple simpleTriple : allTriples) {
			if (simpleTriple.getPredicate().equals(property) && movieList.contains(simpleTriple.getSubject()))
				triples.add(simpleTriple);
		}
		return triples;
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

