/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.falcon.semantic.client;

import org.json.JSONObject;
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
public class DenaClient {

    public static String publishOntology(String fileClassPath, String format) {

        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        //final String uri = "http://localhost:8090/api/v1/ontology/publish";
        final String uri = "http://falconsemanticmanager.euprojects.net/api/v1/ontology/publish";

        map.add("file", new ClassPathResource(fileClassPath));
        map.add("format", format);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;

    }

    public static String addInstances(String fileClassPath, String format) {

        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        //final String uri = "http://localhost:8090/api/v1/ontology/instances/publish";
        final String uri = "http://falconsemanticmanager.euprojects.net/api/v1/ontology/instances/publish";

        map.add("file", new ClassPathResource(fileClassPath));
        map.add("format", format);
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<LinkedMultiValueMap<String, Object>>(map, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;

    }

    public static String runQuery(String sparqlQuery) {

        final String uri = "http://falconsemanticmanager.euprojects.net/api/v1/ontology/query/run";
        //final String uri = "http://localhost:8090/api/v1/ontology/query/run";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>(sparqlQuery, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;
    }

    public static String getInstanceAttributes(String instanceURI) {

        final String uri = "http://falconsemanticmanager.euprojects.net/api/v1/ontology/instance/attributes";
        //final String uri = "http://localhost:8090/api/v1/ontology/instance/attributes";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>(instanceURI, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;
    }

    public static String getClassSubclasses(String classURI) {

        final String uri = "http://falconsemanticmanager.euprojects.net/api/v1/ontology/class/subclasses";
        //final String uri = "http://localhost:8090/api/v1/ontology/class/subclasses";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>(classURI, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return result;
    }

    public static JSONObject getClassAttributes(String classURI) {

        final String uri = "http://falconsemanticmanager.euprojects.net/api/v1/ontology/class/attributes";
        //final String uri = "http://localhost:8090/api/v1/ontology/class/attributes";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>(classURI, headers);

        String result = restTemplate.postForObject(uri, entity, String.class);

        return new JSONObject(result);
    }

    public static String getClassShema(String classURI) {

        final String uri = "http://falconsemanticmanager.euprojects.net/api/v1/ontology/class/schema";
        //final String uri = "http://localhost:8090/api/v1/ontology/class/schema";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> entity = new HttpEntity<>(classURI, headers);

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
        String publishedOntology = publishOntology("/files/dena.rdf", "RDF/XML");
        System.out.println("publishedOntology " + publishedOntology);


        //Add Extra Instances
        String instancesRequest = addInstances("/files/denaPostInstances.rdf", "RDF/XML");
        System.out.println("instancesRequest " + instancesRequest);

        //Do query example
        String queryResult = runQuery(sparqlQuery);
        System.out.println("queryResult " + queryResult);
        /*
        //get Instance Attributes
        String instanceAttributes = getInstanceAttributes("https://w3id.org/saref#Sensor15233");
        System.out.println("instanceAttributes " + instanceAttributes);

        //get Class Attributes
        JSONObject classAttributes = getClassAttributes("https://w3id.org/saref#Service");
        System.out.println("classAttributes " + classAttributes);

        //get Class subclasses
        String classSubclasses = getClassSubclasses("https://w3id.org/saref#Device");
        System.out.println("classSubclasses " + classSubclasses);

        //get Class schema
        String classSchema = getClassShema("https://w3id.org/saref#Service");
        System.out.println("classSchema " + classSchema);
        */
    }

}
