package similarity;

import database.DBConnection;
import node.SparqlWalk;
import org.apache.jena.rdf.model.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SummarizedLDSD {

    public static double LDSDweighted(String resourceA, String resourceB){
        if (resourceA.equals(resourceB)) {
            return 0d;
        }
        double ldsd = 1d / ((double)(1d + (double)nDDLR(resourceA,resourceB) + (double)nDDLR(resourceB,resourceA) + (double)nIDLI(resourceA,resourceB) + (double)nIDLO(resourceB,resourceA)));
        //System.out.println("LDSDweighted:"+ldsd);
        return ldsd;
    }

    /**
     * DONE
     * @param resourceA
     * @param resourceB
     * @param linkInstance
     * @return
     */
    public static double nDDLR(String resourceA, String resourceB){
        double sum = 0d;
        List<Resource> resources = new ArrayList<Resource>();
        resources = SparqlWalk.getDirectLinksBetween2Resources(resourceA,resourceB);
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;
        for (Resource resource : resources) {
            try {
                String query = String.format("SELECT * FROM summarization_music.propriedades_distintas_music WHERE property = \"%s\"", resource);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
                //Testing k = 100
                //System.out.println(String.format("%s %s %s", resource, rs.getString(1), rs.getString(2)));
                if (rs.next()) {
                    if (rs.getString(3) != null) {
                        sum = sum + ((resources.size() / (1 + Math.log(SparqlWalk.countTotalDirectLinksFromResourceAndProperty(resourceA, resource.getURI())))));
                    }
                } else {
                    continue;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;
        for (Resource resource : resources) {

            try {
                String query = String.format("SELECT * FROM summarization_music.propriedades_distintas_music WHERE property = \"%s\"", resource);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
                if(rs.next()){
                    //Testing k = 100
                    //System.out.println(String.format("%s %s %s", resource, rs.getString(1), rs.getString(2)));
                    if (rs.getString(3) != null) {
                        sum = sum + (SparqlWalk.getCountIndirectOutgoingLinkFrom2ResourcesAndLink(resourceA, resourceB, resource.getURI()) / (1 + Math.log(SparqlWalk.countIndirectOutgoingLinksFromResourceAndLink(resourceA, resource.getURI()))));
                    }
                } else {
                    continue;
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
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
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;
        for (Resource resource : resources) {
            try {
                String query = String.format("SELECT * FROM summarization_music.propriedades_distintas_music WHERE property = \"%s\"", resource);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
                //Testing k = 100
                //System.out.println(String.format("%s %s %s", resource, rs.getString(1), rs.getString(2)));
                if(rs.next()) {
                    if (rs.getString(3) != null) {
                        sum = sum + (SparqlWalk.getCountIndirectIncomingLinkFrom2ResourcesAndLink(resourceA, resourceB, resource.getURI()) / (1 + Math.log(SparqlWalk.countIndirectIncomingLinksFromResourceAndLink(resourceA, resource.getURI()))));
                    }
                } else {
                    continue;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        //System.out.println("sumFinal"+sum);
        return sum;
    }

    public static void main(String[] args) {

    }
}
