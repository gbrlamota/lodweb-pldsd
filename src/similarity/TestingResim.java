package similarity;

import io.github.parklize.measure.*;
import io.github.parklize.conf.RESIMConf.PropertyRestriction;
import java.util.*;

public class TestingResim {
	
	public static void main(String[] args) {
		
		// DBpedia settings - without prefix list format
		ResourceSimilarityMeasure rsmForDBpedia = new ResourceSimilarityMeasure(
							PropertyRestriction.SamePropertyPath,
							"http://dbpedia.org/sparql", 
							null, null, 
							includeInboundPropertyList_dbpedia, 
							includeOutboundPropertyList_dbpedia, 
							null, null);	
		
		System.out.println(
		rsmForDBpedia.getSimilarity("<http://dbpedia.org/resource/Hong_Kong>", "<http://dbpedia.org/resource/Singapore>", 2));

	}

	// DBpeida outbound properties
	final static List<String> includeOutboundPropertyList_dbpedia = Arrays.asList(
			"<http://purl.org/dc/terms/subject>",
			//"<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>",
			"<http://dbpedia.org/ontology/isPartOf>",
			"<http://dbpedia.org/ontology/country>",
			"<http://dbpedia.org/ontology/timeZone>",
			"<http://dbpedia.org/ontology/type>",
			"<http://dbpedia.org/ontology/region>",
			"<http://dbpedia.org/ontology/province>",
			"<http://dbpedia.org/ontology/leaderName>",
			"<http://dbpedia.org/ontology/saint>",
			"<http://dbpedia.org/ontology/neighboringMunicipality>",
			"<http://dbpedia.org/ontology/district>",
			"<http://dbpedia.org/ontology/governmentType>",
			"<http://dbpedia.org/ontology/daylightSavingTimeZone>",
			"<http://dbpedia.org/ontology/frazioni>",
			"<http://dbpedia.org/ontology/state>",
			"<http://dbpedia.org/ontology/leaderParty>",
			"<http://dbpedia.org/ontology/part>",
			"<http://dbpedia.org/ontology/twinCity>"
			);
		
	// DBpeida inbound properties
	final static List<String> includeInboundPropertyList_dbpedia = Arrays.asList(
			"<http://dbpedia.org/ontology/birthPlace>",
			"<http://dbpedia.org/ontology/location>",
			"<http://dbpedia.org/ontology/deathPlace>",
			"<http://dbpedia.org/ontology/city>",
			"<http://dbpedia.org/ontology/hometown>",
			"<http://dbpedia.org/ontology/recordedIn>",
			"<http://dbpedia.org/ontology/residence>",
			"<http://dbpedia.org/ontology/isPartOf>",
			"<http://dbpedia.org/ontology/headquarter>",
			"<http://dbpedia.org/ontology/locationCity>",
			"<http://dbpedia.org/ontology/routeJunction>",
			"<http://dbpedia.org/ontology/broadcastArea>",
			"<http://dbpedia.org/ontology/builder>",
			"<http://dbpedia.org/ontology/routeStart>",
			"<http://dbpedia.org/ontology/nearestCity>",
			"<http://dbpedia.org/ontology/routeEnd>",
			"<http://dbpedia.org/ontology/ground>",
			"<http://dbpedia.org/ontology/foundationPlace>",
			"<http://dbpedia.org/ontology/assembly>",
			"<http://dbpedia.org/ontology/restingPlace>",
			"<http://dbpedia.org/ontology/place>",
			"<http://dbpedia.org/ontology/district>",
			"<http://dbpedia.org/ontology/countySeat>",
			"<http://dbpedia.org/ontology/locatedInArea>",
			"<http://dbpedia.org/ontology/country>",
			"<http://dbpedia.org/ontology/capital>",
			"<http://dbpedia.org/ontology/region>",
			"<http://dbpedia.org/ontology/populationPlace>",
			"<http://dbpedia.org/ontology/neighboringMunicipality>",
			"<http://dbpedia.org/ontology/largestCity>",
			"<http://dbpedia.org/ontology/type>",
			"<http://dbpedia.org/ontology/garrison>",
			"<http://dbpedia.org/ontology/owner>",
			"<http://dbpedia.org/ontology/homeport>",
			"<http://dbpedia.org/ontology/part>",
			"<http://dbpedia.org/ontology/regionServed>",
			"<http://dbpedia.org/ontology/team>",
			"<http://dbpedia.org/ontology/mouthMountain>",
			"<http://dbpedia.org/ontology/mouthPlace>",
			"<http://dbpedia.org/ontology/billed>",
			"<http://dbpedia.org/ontology/campus>",
			"<http://dbpedia.org/ontology/arrondissement>",
			"<http://dbpedia.org/ontology/origin>",
			"<http://dbpedia.org/ontology/county>",
			"<http://dbpedia.org/ontology/almaMater>",
			"<http://dbpedia.org/ontology/metropolitanBorough>",
			"<http://dbpedia.org/ontology/education>",
			"<http://dbpedia.org/ontology/premierePlace>",
			"<http://dbpedia.org/ontology/training>",
			"<http://dbpedia.org/ontology/state>",
			"<http://dbpedia.org/ontology/picture>",
			"<http://dbpedia.org/ontology/localAuthority>",
			"<http://dbpedia.org/ontology/majorShrine>",
			"<http://dbpedia.org/ontology/twinCity>",
			"<http://dbpedia.org/ontology/highschool>",
			"<http://dbpedia.org/ontology/municipality>",
			"<http://dbpedia.org/ontology/operator>",
			"<http://dbpedia.org/ontology/spokenIn>",
			"<http://dbpedia.org/ontology/locationCountry>",
			"<http://dbpedia.org/ontology/administrativeDistrict>",
			"<http://dbpedia.org/ontology/nationality>",
			"<http://dbpedia.org/ontology/sourcePlace>"
			);

}
