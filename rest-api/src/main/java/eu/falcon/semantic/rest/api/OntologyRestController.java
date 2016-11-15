package eu.falcon.semantic.rest.api;


import eu.falcon.semantic.rest.response.RestResponse;
import eu.falcon.semantic.rest.response.BasicResponseCode;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contains all the rest endpoints regarding with authentication actions.
 */
@RestController
@RequestMapping("/api/v1/ontology")
public class OntologyRestController {

//    @Autowired
//    private IUserService<User, Long> userService;

    @RequestMapping(path = "/publish", method = RequestMethod.GET)
    public RestResponse insertOntologyToTriplestore() {
        File initialFile = null;
        try {
            InputStream inputStream;

            initialFile = new File("/home/eleni/Downloads/iot-energyldao.owl");
            inputStream = new FileInputStream(initialFile);

//triplestore datasource
            String serviceURI = "http://192.168.3.15:3030/ds2/data";

            DatasetAccessor accessor;
            accessor = DatasetAccessorFactory.createHTTP(serviceURI);
            Model m = ModelFactory.createDefaultModel();
            String base = "http://samle-project.com/";
            m.read(inputStream, base, "RDF/XML");
            accessor.add(m);
            inputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(OntologyRestController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new RestResponse(BasicResponseCode.SUCCESS, Message.ONTOLOGY_CREATED, "uploaded file name" + initialFile.getName());
    }

    @RequestMapping(path = "/instances/publish", method = RequestMethod.GET)
    public RestResponse insertInstancesToTriplestore() {
        File initialFile = null;
        try {
            InputStream inputStream;

            //inputStream = new ByteArrayInputStream(instances.getBytes(StandardCharsets.UTF_8));
            initialFile = new File("/home/eleni/Downloads/analyticsid128_version1_kmeans_resultdocumentnt.n3");
            inputStream = new FileInputStream(initialFile);

//triplestore datasource
            String serviceURI = "http://192.168.3.15:3030/ds2/data";

            DatasetAccessor accessor;
            accessor = DatasetAccessorFactory.createHTTP(serviceURI);
            Model m = ModelFactory.createDefaultModel();
            String base = "http://samle-project.com/";
            m.read(inputStream, base, "Turtle");
            accessor.add(m);
            inputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(OntologyRestController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new RestResponse(BasicResponseCode.SUCCESS, Message.IMPORTED_INSTANCES, "imported instances" + initialFile.getName());
    }

    @RequestMapping(path = "/query/run", method = RequestMethod.GET)
    public RestResponse executeQueryToTriplestore() {

        String serviceURI = "http://192.168.3.15:3030/ds2/data";

//        String query = "SELECT ?AverageEnergy ?date ?AverageEnergy_MeasurementValue\n"
//                + "WHERE {\n"
//                + "	?AverageEnergy a <https://w3id.org/saref#AverageEnergy>.\n"
//                + "		?AverageEnergy <http://www.w3.org/2002/12/cal/ical#dtstart> ?date.\n"
//                + "		?AverageEnergy <https://w3id.org/saref#MeasurementValue> ?AverageEnergy_MeasurementValue.\n"
//                + "}";
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
                + "SELECT * WHERE {\n"
                + "  ?sub ?pred ?obj .\n"
                + "} \n"
                + "LIMIT 10";

        QueryExecution q = QueryExecutionFactory.sparqlService(serviceURI, query);
        ResultSet results = q.execSelect();

        ResultSetFormatter.out(System.out, results);

//        while (results.hasNext()) {
//            QuerySolution solution = results.nextSolution();
//            RDFNode averageEnergy = solution.get("AverageEnergy");
//            System.out.println(averageEnergy);
//        }
        return new RestResponse(BasicResponseCode.SUCCESS, null, "test");
    }

    @RequestMapping(path = "/schema", method = RequestMethod.GET)
    public RestResponse getOntologySchema() {

        return new RestResponse(BasicResponseCode.SUCCESS, null, "test");
    }

    /**
     * The exposed endpoint is used via the UI, which attempts to create a new
     * user to the database.
     *
     * @param user A JSON object which will be casted to a User (java) object
     * @return RestResponse object
     */
    @RequestMapping(method = RequestMethod.POST)
    public RestResponse create(@RequestBody String user
    ) {
        

        return new RestResponse(BasicResponseCode.SUCCESS, Message.ONTOLOGY_CREATED);
    }

    /**
     * Inner class containing all the static messages which will be used in an
     * RestResponse.
     *
     */
    private final static class Message {

        final static String ONTOLOGY_CREATED = "Ontology has been created";
        final static String IMPORTED_INSTANCES = "New Instances have been imported";
    }
}
