package eu.falcon.semantic.rest.api;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import eu.falcon.semantic.rest.response.RestResponse;
import eu.falcon.semantic.rest.response.BasicResponseCode;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * Contains all the rest endpoints regarding with authentication actions.
 */
@RestController
@RequestMapping("/api/v1/ontology")
public class OntologyRestController {

    @Value("${fuseki.triplestore}")
    private String triplestorURL;

    @Value("${fuseki.dataset}")
    private String triplestoreDataset;

    ///////////////////////////////////////////////
    //Get datasets
    ///////////////////////////////////////////////
    @RequestMapping(value = "/query/datasets")
    public RestResponse getDatasetNames() {
        RestTemplate restTemplate = new RestTemplate();
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        final String uri = triplestorURL+"/$/server";

        String result = restTemplate.getForObject(uri, String.class);
        JSONObject jsonObj = new JSONObject(result);
        JSONArray jsonArray = jsonObj.getJSONArray("datasets");
        JSONObject response = new JSONObject();
        Collection<String> datasets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String dataset = jsonArray.getJSONObject(i).getString("ds.name");
            datasets.add(dataset);
        }
        response.put("datasets", datasets);
        //System.out.println(result);
        return new RestResponse(BasicResponseCode.SUCCESS, Message.QUERY_EXECUTED, response.toString());
    }

    ///////////////////////////////////////////////
    //POST CALLS
    ///////////////////////////////////////////////
    @RequestMapping(value = "/query/run", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
    public RestResponse executeQueryToTriplestorePOST(@RequestBody String tobject) {

        String serviceURI = triplestorURL + "/" + triplestoreDataset + "/query";

        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, tobject);
        ResultSet results = q.execSelect();
        //ResultSetFormatter.out(System.out, results);

        // write to a ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ResultSetFormatter.outputAsJSON(outputStream, results);

        // and turn that into a String
        String json = new String(outputStream.toByteArray());
        JSONObject jsonObject = new JSONObject(json);
        //System.out.println("Results as json" + json);
        //System.out.println("Results as string " + ResultSetFormatter.asText(results));

        q.close();

        return new RestResponse(BasicResponseCode.SUCCESS, Message.QUERY_EXECUTED, jsonObject.toString());
    }

    @RequestMapping(value = "/instances/publish", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
    public RestResponse insertInstancesToTriplestorePOST(@RequestParam("file") MultipartFile initialFile, @RequestParam("format") String format) {

        try {
            InputStream inputStream = initialFile.getInputStream();
            String serviceURI = triplestorURL + "/" + triplestoreDataset + "/data";

            DatasetAccessor accessor;
            accessor = DatasetAccessorFactory.createHTTP(serviceURI);
            Model m = ModelFactory.createDefaultModel();
            String base = "http://samle-project.com/";
            m.read(inputStream, base, format);
            accessor.add(m);
            inputStream.close();
            m.close();

        } catch (IOException ex) {
            Logger.getLogger(OntologyRestController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new RestResponse(BasicResponseCode.SUCCESS, Message.IMPORTED_INSTANCES, "imported instances" + initialFile.getName());
    }

    @RequestMapping(value = "/publish", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
    public RestResponse publishOntologyToTriplestorePOST(@RequestParam("file") MultipartFile initialFile,
            @RequestParam("format") String format,
            @RequestParam("dataset") String dataset) {

        try {

            Webb webb = Webb.create();
            Response<String> result = webb
                    .post(triplestorURL+"/$/datasets/")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body("dbName="+dataset+"&dbType=tdb").asString();
            
            
            
            InputStream inputStream = initialFile.getInputStream();
            String serviceURI = triplestorURL + "/" + dataset + "/data";

            DatasetAccessor accessor;
            accessor = DatasetAccessorFactory.createHTTP(serviceURI);
            Model m = ModelFactory.createDefaultModel();
            String base = "http://samle-project.com/";
            m.read(inputStream, base, format);
            accessor.add(m);
            inputStream.close();
            m.close();
            
            

         
        } catch (IOException ex) {
            Logger.getLogger(OntologyRestController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new RestResponse(BasicResponseCode.SUCCESS, Message.ONTOLOGY_CREATED, "A new Ontology is inserted" + initialFile.getName());
    }

    @RequestMapping(value = "/instance/attributes", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
    public RestResponse getInstanceAttributes(@RequestBody String instanceURI) {

//        String query = "select distinct  ?property  where {\n"
//                + "         ?instance a <" + instanceURI + "> . \n"
//                + "  ?instance ?property ?obj . }";
        String query = "select distinct  ?instanceproperties  where {\n"
                + "        <" + instanceURI + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?class  .\n"
                + "        <" + instanceURI + "> ?instanceproperties ?obj .}";

        String serviceURI = triplestorURL + "/" + triplestoreDataset + "/query";

        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        JSONArray instancepropertiesArray = new JSONArray();
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode subclass = solution.get("instanceproperties");
            instancepropertiesArray.put(subclass.toString());

        }

        JSONObject instance = new JSONObject();
        instance.put("Instance", instanceURI);
        instance.put("properties", instancepropertiesArray);

        q.close();

        return new RestResponse(BasicResponseCode.SUCCESS, Message.QUERY_EXECUTED, instance.toString());
    }

    @RequestMapping(value = "/class/subclasses", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
    public RestResponse getClassSubclasses(@RequestBody String classURI) {

        String query = "SELECT distinct ?subclasses WHERE {\n"
                + "  ?subclasses <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + classURI + ">\n"
                + "}";

        String serviceURI = triplestorURL + "/" + triplestoreDataset + "/query";

        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        JSONArray subclassesArray = new JSONArray();
        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode subclass = solution.get("subclasses");
            subclassesArray.put(subclass.toString());

        }

        JSONObject mainClass = new JSONObject();
        mainClass.put("class", classURI);
        mainClass.put("subclasses", subclassesArray);
        q.close();

        return new RestResponse(BasicResponseCode.SUCCESS, Message.QUERY_EXECUTED, mainClass.toString());
    }

    @RequestMapping(value = "/class/attributes", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
    public RestResponse getClassAttributesRest(@RequestBody String classURI) {

        JSONArray classAttributes = getClassAttributes(classURI);

        return new RestResponse(BasicResponseCode.SUCCESS, Message.QUERY_EXECUTED, classAttributes.toString());
    }

    @RequestMapping(value = "/class/schema", method = RequestMethod.POST, headers = "Accept=application/xml, application/json")
    public RestResponse getClassShema(@RequestBody String classURI) {

        System.out.println("mpika edo");

        String query = "SELECT distinct ?subclasses WHERE {\n"
                + "  ?subclasses <http://www.w3.org/2000/01/rdf-schema#subClassOf> <" + classURI + ">\n"
                + "}";

        String serviceURI = triplestorURL + "/" + triplestoreDataset + "/query";

        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        List<String> subclasses = new ArrayList<String>();
        JSONArray subclassesArray = new JSONArray();

        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode subclass = solution.get("subclasses");
            subclasses.add(subclass.toString());
            subclassesArray.put(subclass.toString());

        }

        JSONObject mainClass = new JSONObject();
        mainClass.put("URI", classURI);
        mainClass.put("attributes", getClassAttributes(classURI));

        JSONArray subclassesAttributesArray = new JSONArray();

        for (String subclass : subclasses) {
            JSONObject subClassObject = new JSONObject();
            subClassObject.put("URI", subclass);
            subClassObject.put("attributes", getClassAttributes(subclass));
            subclassesAttributesArray.put(subClassObject);

        }

        JSONObject response = new JSONObject();
        response.put("mainClass", mainClass);
        response.put("subclasses", subclassesAttributesArray);

        q.close();

        return new RestResponse(BasicResponseCode.SUCCESS, Message.QUERY_EXECUTED, response.toString());
    }

    private JSONArray getClassAttributes(String classURI) {

        String query = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "PREFIX owl:  <http://www.w3.org/2002/07/owl#>\n"
                + "\n"
                + "SELECT ?classproperties ?a\n"
                + "WHERE {\n"
                + "  values ?propertyType { owl:DatatypeProperty owl:ObjectProperty }\n"
                + "  ?classproperties a ?propertyType ;\n"
                + "            rdfs:domain <" + classURI + "> .\n"
                + "}";

        String serviceURI = triplestorURL + "/" + triplestoreDataset + "/query";

        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        JSONArray classproperties = new JSONArray();

        while (results.hasNext()) {
            QuerySolution solution = results.nextSolution();
            RDFNode classproperty = solution.get("classproperties");
            classproperties.put(classproperty.toString());
        }
        q.close();

        return classproperties;

    }

    /**
     * Inner class containing all the static messages which will be used in an
     * RestResponse.
     *
     */
    private final static class Message {

        final static String ONTOLOGY_CREATED = "Ontology has been created";
        final static String IMPORTED_INSTANCES = "New Instances have been imported";
        final static String QUERY_EXECUTED = "QUERY is succesfully executed";
    }
}
