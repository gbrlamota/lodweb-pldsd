package similarity;

import database.DBConnection;
import database.DBFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

class FilteredElement {
    String property;
    String object;
    String frequency;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}

public class DiNoia {
    private static DBFunctions dbFunctions = null;

    public static synchronized DBFunctions getDatabaseConnection()
    {
        if(dbFunctions==null)
        {
            dbFunctions = new DBFunctions();
        }
        return dbFunctions;
    }

    public static List filterBy() throws SQLException {
        List<FilteredElement> filtered_list = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;

        ps = conn.prepareStatement("SELECT * FROM summarization.abstat WHERE avgS > 1");
        ResultSet rs = ps.executeQuery();
        Integer i = 0;
        while (rs.next()) {
            i++;
            FilteredElement filteredElement = new FilteredElement();
            filteredElement.setProperty(rs.getString(1));
            filteredElement.setObject(rs.getString(2));
            filteredElement.setFrequency(rs.getString(3));
            filtered_list.add(filteredElement);
        }
        ps.close();
        conn.close();
        return filtered_list;
    }

    public static HashMap<String, Integer> selectDistinctP(List<FilteredElement> filtered_list) {
        HashMap<String, Integer> distinct_elements = new HashMap<>();

        for(int i = 0; i < filtered_list.size(); i++) {
            FilteredElement element = filtered_list.get(i);
            if(distinct_elements != null && distinct_elements.containsKey(element.getProperty())) {
                if(distinct_elements.get(element.getProperty()) < Integer.parseInt(element.getFrequency())) {
                    distinct_elements.put(element.getProperty(), Integer.parseInt(element.getFrequency()));
                }
            }
            else {
                distinct_elements.put(element.getProperty(), Integer.parseInt(element.getFrequency()));
            }
        }
        return distinct_elements;
    }

    // function to sort hashmap by values
    public static List<Map.Entry<String, Integer>> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        return list;
    }

    public static List<String> selectFeatures(int k, List<Map.Entry<String, Integer>> sorted_map) throws SQLException {
        List<String> features = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = null;

        for(int i = sorted_map.size() - 1; i >= sorted_map.size() - k; i--) {
            if(i == 534) {
                System.out.println("Entrei");
            }
            String query =String.format("UPDATE summarization.propriedades_distintas SET `active_535` = true WHERE property = \"%s\"", sorted_map.get(i).getKey()) ;
            System.out.println(query);
            ps = conn.prepareStatement(query);
            boolean rs = ps.execute();
            System.out.println(sorted_map.get(i).getKey() + " " + i);
            features.add(sorted_map.get(i).getKey());
        }
        ps.close();
        conn.close();

        return features;
    }

    public List<String> getSelectedFeatures() throws SQLException {
        HashMap<String, Integer> elements = selectDistinctP(filterBy());
        List<Map.Entry<String, Integer>> sorted_map = sortByValue(elements);
        return selectFeatures(535, sorted_map);
    }

    public static void main(String[] args) throws SQLException {
        HashMap<String, Integer> elements = selectDistinctP(filterBy());
        System.out.println("Primeiro passo");
        List<Map.Entry<String, Integer>> sorted_map = sortByValue(elements);
        System.out.println("Segundo passo");
        selectFeatures(535, sorted_map);
    }
}
