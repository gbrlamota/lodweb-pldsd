package similarity;

import io.github.parklize.measure.*;
import node.SimpleTriple;
import node.SparqlWalk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

//import org.apache.jena.rdf.model.Resource;

import io.github.parklize.conf.RESIMConf.PropertyRestriction;

public class TestingResim2 {

	/**
	 * Método que usa o SparqlWalk para recuperar a lista de propriedades, mas retorna apenas as que são outgoing
	 * e precisa de um Resource de exemplo para gerar as triplas (se desse para juntar os projetos eu faria tudo pelo SparqlWalk)
	 * @param uri
	 * @return
	 */
/*	public static List<String> getObjectPropertiesFromResource(String uri){
		List<SimpleTriple> triplesList = SparqlWalk.getDBpediaObjecsTripletBySubjectURI(uri);
		Set<String> propertiesSet = new HashSet<String>(); 
		String property = new String();
		for (SimpleTriple triple : triplesList){
			//só adiciona as propriedades que não forem dbo
			if (!triple.getPredicate().matches("^http://dbpedia.org/ontology/.*"))
				propertiesSet.add("<"+triple.getPredicate()+">");
		}
		
		for (String p : propertiesSet){
			System.out.println(p);
		}
		List<String> objectPropertyList = new ArrayList<String>(propertiesSet);
		return objectPropertyList;
	}*/
	
	/**
	 * Método que carrega dois arquivos com as uris de outgoing e ingoing (previamente exportados do virtuoso)
	 * e monta a lista de propriedades para avaliação do Resim. Esse pega todas, de todos os filmes, ida e volta.
	 * @return
	 * @throws IOException
	 */
	public static List<String> getAdditionalPropertyList() throws IOException{
		Set<String> propertiesSet = new HashSet<String>(); 
		String linha = new String();
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Gabriela\\Google Drive\\UFBA\\Doutorado\\Submissões\\properties-film-outgoing.txt"));
		while ((linha = br.readLine()) != null) {
				propertiesSet.add("<"+linha+">");
		}
		
		br.close();
		br = new BufferedReader(new FileReader("C:\\Users\\Gabriela\\Google Drive\\UFBA\\Doutorado\\Submissões\\properties-film-ingoing.txt"));
		while ((linha = br.readLine()) != null) {
			propertiesSet.add("<"+linha+">");
		}
		
		br.close();
		
		List<String> objectPropertyList = new ArrayList<String>(propertiesSet);
		
		//teste se importou corretamente
		for (String p : objectPropertyList){
			System.out.println(p);
		}
		
		return objectPropertyList;
	}
	
	public static void main(String[] args) {
		
		
		// DBpedia list of additional no literal properties for film domain based on Alladin Film properties
		//List<String> additionalPropertyList_dbpedia = getObjectPropertiesFromResource("http://dbpedia.org/resource/Aladdin_(1992_Disney_film)");
		
		List<String> additionalPropertyList_dbpedia = new ArrayList<String>();
		
		try {
			additionalPropertyList_dbpedia = getAdditionalPropertyList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// DBpedia settings - without giving prefix list, using property patterns instead
		// it will filter properties which start with ""http://dbpedia.org/ontology"
		ResourceSimilarityMeasure rsmForDBpedia = new ResourceSimilarityMeasure(PropertyRestriction.SamePropertyPath,
																	"http://dbpedia.org/sparql", null, "http://dbpedia.org/ontology/", 
																	null, null, additionalPropertyList_dbpedia, null);	
		
		System.out.println("Start calculating Resim for <http://dbpedia.org/resource/Aladdin_(1992_Disney_film)> and  <http://dbpedia.org/resource/300_(film)>");
				
		System.out.println(
		rsmForDBpedia.getSimilarity("<http://dbpedia.org/resource/Aladdin_(1992_Disney_film)>", "<http://dbpedia.org/resource/300_(film)>", 2));
		

	}
			
	
	// DBpedia list of additional no literal properties for film domain (only for tests)
	/*final static List<String> additionalPropertyList_dbpedia = Arrays.asList(
			"<http://www.w3.org/2000/01/rdf-schema#seeAlso>",
			"<http://purl.org/dc/terms/subject>",
			"<http://purl.org/linguistics/gold/hypernym>",
			"<http://dbpedia.org/property/studio>"
			);*/

}
