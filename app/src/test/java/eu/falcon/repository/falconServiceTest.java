package eu.falcon.repository;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.RDFNode;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Christos Paraskeva (ch.paraskeva at gmail dot com)
 */
public class falconServiceTest {

    @Ignore
    @Test
    public void JenaSparqlExample() {

        String sparqlQueryString1 = "PREFIX dbont: <http://dbpedia.org/ontology/> "
                + "PREFIX dbp: <http://dbpedia.org/property/>"
                + "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>"
                + "   SELECT ?musician  ?place"
                + "   WHERE { "
                + "       ?musician dbont:birthPlace ?place ."
                + "   }";

        Query query = QueryFactory.create(sparqlQueryString1);
        QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);

        ResultSet results = qexec.execSelect();
        ResultSetFormatter.out(System.out, results, query);

        qexec.close();
    }

    @Test
    public void queryTest() {

        String serviceURI = "http://192.168.3.15:3030/ds2/query";

//        String query = "SELECT ?AverageEnergy ?date ?AverageEnergy_MeasurementValue\n"
//                + "WHERE {\n"
//                + "	?AverageEnergy a <https://w3id.org/saref#AverageEnergy>.\n"
//                + "		?AverageEnergy <http://www.w3.org/2002/12/cal/ical#dtstart> ?date.\n"
//                + "		?AverageEnergy <https://w3id.org/saref#MeasurementValue> ?AverageEnergy_MeasurementValue.\n"
//                + "}";
        String query = "SELECT * WHERE {  ?sub ?pred ?obj} LIMIT 10";

        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        System.out.println("----MY RESULTS---");
        ResultSetFormatter.out(System.out, results);

        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode averageEnergy = solution.get("sub");
            System.out.println(averageEnergy);
        }
        q.close();
    }

}
