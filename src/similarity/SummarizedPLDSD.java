package similarity;

import database.DBConnection;
import database.DBFunctions;
import model.User;
import node.NodeUtil;
import node.SparqlWalk;
import org.apache.jena.base.Sys;
import org.apache.jena.rdf.model.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SummarizedPLDSD {
    private static DBFunctions dbFunctions = null;

    public static synchronized DBFunctions getDatabaseConnection()
    {
        if(dbFunctions==null)
        {
            dbFunctions = new DBFunctions();
        }
        return dbFunctions;
    }

    public static double PLDSDweighted(String resourceA, String resourceB, User user){
        if (resourceA.equals(resourceB)) {
            return 0d;
        }
        double ldsd = 1d / ((double)(1d + (double)nDDLR(resourceA,resourceB,user) + (double)nDDLR(resourceB,resourceA,user) + (double)nIDLI(resourceA,resourceB,user) + (double)nIDLO(resourceB,resourceA,user)));
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
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;

        for (Resource resource : resources) {
            try {
                String query = String.format("SELECT * FROM summarization.distinct_property WHERE property = \"%s\"", resource);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                if (rs.getString(6) == "1") {
                    synchronized (dbFunctions) {
                        //if weight = 0 the link is not being computed
                        //changed for: if the weight is 0.5 for instance, then this property has 1.5x chances to influence tha calculation
                        weight = dbFunctions.getPropertyWeightByUser(user, resource.getURI()) + 1;
                    }
                    nLinks = SparqlWalk.getCountDirectLinkFrom2ResourcesAndLink(resourceA, resourceB, resource.getURI());
                    if (nLinks != resources.size())
                        System.out.println("ENCONTREI O ERRO!");

                    //TODO: debugar para ver se a quantidade de links (resources.size()) é sempre a mesma ou se muda de acordo com o link
                    sum = sum + (((nLinks * weight) / (1 + Math.log(SparqlWalk.countTotalDirectLinksFromResourceAndProperty(resourceA, resource.getURI())))));
                    //sum = sum + ( nLinks / (1 + Math.log(SparqlWalk.countTotalDirectLinksFromResourceAndProperty(resourceA,resource.getURI()))));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;

        for (Resource resource : resources) {
            try {
                String query = String.format("SELECT * FROM summarization.distinct_property WHERE property = \"%s\"", resource);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();

                if (rs.getString(6) == "1") {
                    synchronized (dbFunctions)
                    {
                        //if weight = 0 the link is not being computed
                        //changed to: if the weight is 0.5 for instance, then this property has 1.5x chances to influence tha calculation
                        weight = dbFunctions.getPropertyWeightByUser(user, resource.getURI()) + 1;
                    }

                    sum = sum + ((SparqlWalk.getCountIndirectOutgoingLinkFrom2ResourcesAndLink(resourceA, resourceB,resource.getURI()) * weight)
                            / (1 + Math.log(SparqlWalk.countIndirectOutgoingLinksFromResourceAndLink(resourceA,resource.getURI()))));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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

        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs;

        for (Resource resource : resources) {
            try {
                String query = String.format("SELECT * FROM summarization.distinct_property WHERE property = \"%s\"", resource);
                ps = conn.prepareStatement(query);
                rs = ps.executeQuery();
                //Testing k = 100
                System.out.println(String.format("%s %s %s", resource, rs.getString(1), rs.getString(2)));
                if (rs.getString(6) == "1") {
                    synchronized (dbFunctions)
                    {
                        //if weight = 0 the link is not being computed
                        //changed to: if the weight is 0.5 for instance, then this property has 1.5x chances to influence tha calculation
                        weight = dbFunctions.getPropertyWeightByUser(user, resource.getURI()) + 1;
                    }

                    sum = sum + ((SparqlWalk.getCountIndirectIncomingLinkFrom2ResourcesAndLink(resourceA,resourceB,resource.getURI()) * weight )
                            / (1 + Math.log(SparqlWalk.countIndirectIncomingLinksFromResourceAndLink(resourceA,resource.getURI()))));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        //System.out.println("sumFinal"+sum);
        return sum;
    }
}
