# DataSimulators Background
Data Simulators are designed and intended to help with specific types of implementations. While they can benefit and help specifically with iDaaS they can, and have also been used for non-iDaaS specific implementations. Just to provide a visualization to show 
how Data Simulators and how they fit into integration efforts. Below please find a visual that does visualize the entire 
iDaaS capabilities set. The key thing to note is while each specific iDaaS capability is purpose built and designed for any type of customer public or hybrid cloud our focus is on meeting data where it is securely and at scale.

Here is a detailed and Cloud Agnostic visual:<br/>
![iDaaS Cloud Agnostic Data Flow.png](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/images/iDaaS-Platform/Implementations/Implementations-Gen-CloudAgnostic.png)
<br/>

## iDaaS - Data Simulators Modules
Like most of the repositories provided for usage, The iDaaS Data Simulators repository consists of numerous modules within it.
The reason for this approach is because iDaaS Connect focus is about providing a wide variety of connectivity options based
on specific needs. Here are the modules that are in iDaaS-DataSimulators.

| iDaaS Data Simulators                                  | Purpose                                                                                                      |                                                                           
|--------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| [iDaaS-DataSimulator-HL7](HL7/README.md)               | This data simulator is specifically designed to work with HL7 based systems                                  |
| [iDaaS-DataSimulator-FHIR](FHIR/README.md)             | This data simulator is specifically designed to work with FHIR based systems                                 |
| [iDaaS-DataSimulator-ThirdParty](ThirdParty/README.md) | This data simulator is specifically designed to work with Files/SFTP and other legacy protocol based systems |
| [iDaaS-DataSimulator-KIC](KIC/README.md)               | This data simulator is intended to work specifically to test iDaaS KIC platform                              |

## Administering the Platform - Management and Insight of Components
Administering and seeing what the plaform is doing is also critical. Within each specific repository there is an administrative user interface that allows for monitoring and insight into the connectivity of any endpoint. Additionally, there is also the implementation to enable implementations to build there own by exposing the metadata. The data is exposed and can be used in numerous very common tools like Data Dog, Prometheus and so forth.
This capability to enable would require a few additional properties to be set.

Below is a generic visual of how this looks (the visual below is specific to iDaaS Connect HL7): <br/>

![iDaaS Platform - Visuals - iDaaS Data Flow - Detailed](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/images/iDaaS-Platform/iDaaS-Mgmt-UI.png)

Every asset has its own defined specific port, we have done this to ensure multiple solutions can be run simultaneously.

## Administrative Interface(s) Specifics
For all the URL links we have made them localhost based, simply change them to the server the solution is running on.

| iDaaS Data Simulators           | Port | Admin URL                                        | JMX URL                                                                                   |                                                                                   
|---------------------------------|------|--------------------------------------------------|-------------------------------------------------------------------------------------------| 
| iDaaS Data Simulator HL7        | 9960 | http://localhost:9960/actuator/hawtio/index.html | http://localhost:9960/actuator/jolokia/read/org.apache.camel:context=*,type=routes,name=* | 
| iDaaS Data Simulator FHIR       | 9961 | http://localhost:9961/actuator/hawtio/index.html | http://localhost:9961/actuator/jolokia/read/org.apache.camel:context=*,type=routes,name=* |  
| iDaaS Data Simulator KIC        | 9962 | http://localhost:9962/actuator/hawtio/index.html | http://localhost:9962/actuator/jolokia/read/org.apache.camel:context=*,type=routes,name=* |  
| iDaaS Data Simulator ThirdParty | 9963 | http://localhost:9963/actuator/hawtio/index.html | http://localhost:9963/actuator/jolokia/read/org.apache.camel:context=*,type=routes,name=* |  

# Platform General Pre-Requisites
For all iDaaS design patterns it should be assumed that you will either install as part of this effort, or need the [following](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/PreRequisites.md) on any environment you choose to implement the platfom on.

