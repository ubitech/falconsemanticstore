# SAMPLE
> A spring-boot REST API for publishing Ontologies, adding instances and quering a Triplestore

##Publish a new Ontology  

REST - API  
http://IP/api/v1/ontology/publish  

POST request parameters  
 {file, format}  


#Add Extra Instances  

REST - API  
http://IP/api/v1/ontology/instances/publish  

POST request parameters  
 {file, format}  


#Submit sparql query  
http://IP/api/v1/ontology/query/run  

POST  
{query}  

#Get Instance Attributes   
http://IP/api/v1/ontology/instance/attributes  

POST   
{InstanceURI}  

#Get Class Attributes   
http://IP/api/v1/ontology/class/attributes  

POST   
{ClassURI}   

#Get Class subClasses   
http://IP/api/v1/ontology/class/subclasses

POST   
{ClassURI}  

