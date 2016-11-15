package eu.falcon.repository;

import net.minidev.json.JSONObject;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Christos Paraskeva (ch.paraskeva at gmail dot com)
 */
public class falconClientTest {

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

    @Ignore
    @Test
    public void queryTest() {

        String serviceURI = "http://192.168.3.15:3030/ds2/query";

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

    @Ignore
    @Test
    public void postCallToRunQueryTest() {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT * WHERE {\n"
                + "  ?sub ?pred ?obj .\n"
                + "} \n"
                + "LIMIT 10";

        final String uri = "http://localhost:8080/api/v1/ontology/query/run";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        JSONObject queryRequest = new JSONObject();
        queryRequest.put("query", query);

        HttpEntity<String> entity = new HttpEntity<>(queryRequest.toString(), headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        System.out.println(result);
    }

    @Ignore
    @Test
    public void postCallToAddInstancesTest() {
        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        final String uri = "http://localhost:8080/api/v1/ontology/publish";

        map.add("file", new ClassPathResource("/files/analyticsid128_version1_kmeans_resultdocumentnt.n3"));
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(
                map, headers);
        String result = restTemplate.postForObject(uri, entity, String.class);

        System.out.println("result for postCallToAddInstancesTest " + result);
    }

}
