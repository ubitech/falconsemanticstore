/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.falcon.semantic.client;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Eleni Fotopoulou <efotopoulou@ubitech.eu>
 */
public class Client {

    public static String runQuery(String sparqlQuery) {

        final String uri = "http://localhost:8090/api/v1/ontology/query/run";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

//        JSONObject queryRequest = new JSONObject();
//        queryRequest.put("query", sparqlQuery);
        HttpEntity<String> entity = new HttpEntity<>(sparqlQuery, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;
    }

    public static String addInstances(String fileClassPath, String format) {

        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        final String uri = "http://localhost:8090/api/v1/ontology/instances/publish";

        map.add("file", new ClassPathResource(fileClassPath));
        map.add("format", format);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;

    }

    public static String publishOntology(String fileClassPath, String format) {

        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        final String uri = "http://localhost:8090/api/v1/ontology/publish";

        map.add("file", new ClassPathResource(fileClassPath));
        map.add("format", format);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;

    }

    public static void main(String[] args) {
        String sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT * WHERE {\n"
                + "  ?sub ?pred ?obj .\n"
                + "} \n"
                + "LIMIT 10";

        //Publish Ontology example
        String publishedOntology = publishOntology("/files/iot-energyldao.owl", "RDF/XML");
        System.out.println("publishedOntology " + publishedOntology);

        //Add Extra Instances
        String instancesRequest = addInstances("/files/analyticsid128_version1_kmeans_resultdocumentnt.n3", "Turtle");
        System.out.println("instancesRequest " + instancesRequest);

        //Do query example
        String queryResult = runQuery(sparqlQuery);
        System.out.println("queryResult " + queryResult);

    }

}
