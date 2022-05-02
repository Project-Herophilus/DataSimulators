# DataSimulators Background
Specific Data Simulators designed to help with implementation of iDaaS.

# Reference Architecture/Design Pattern Based Platform 
Just to provide a few visuals to how to visualize it as we talk about each submodule within each part of the platform.
Below please find a visual that does visualize the entire iDaaS capabilities set. The key thing to note is while each specific iDaaS capability is purpose built and designed for any type of customer public or hybrid cloud our focus is on meeting data where it is securely and at scale.

Here is a detailed and Cloud Agnostic visual:<br/>
![iDaaS Cloud Agnostic Data Flow.png](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/Platform/Images/iDAAS-Platform/Implementations-Gen-CloudAgnostic.png)
<br/>

# iDaaS-Data Simulators Modules
Like most of the repositories provided for usage, The iDaaS Data Simulators repository consists of numerous modules within it.
The reason for this approach is because iDaaS Connect focus is about providing a wide variety of connectivity options based
on specific needs. Here are the modules that are in iDaaS-DataSimulators.

## iDaaS-DataSimulator-HL7
Data Simulator HL7 is intended to work specifically with HL7 based systems.
<br>
[Data Simulator HL7 Readme](HL7/README.md)

## iDaaS-DataSimulator-FHIR
Data Simulator FHIR is intended to work specifically with FHIR based systems.
<br>
[Data Simulator FHIR Readme](FHIR/README.md)

## iDaaS-DataSimulator-KIC
Data Simulator KIC is intended to work specifically to test iDaaS KIC.<br>
[Data Simulator KIC Readme](KIC/README.md)

## iDaaS-DataSimulator-ThirdParty
Data Simulator KIC is intended to work specifically to test iDaaS KIC.<br>
[Data Simulator ThirdParty Readme](ThirdParty/README.md)

# Add-Ons
To support any iDaaS branded artifact there are a subset of assets avaiable to assist in the implementation : <br/>

| Add-On                                                                                                                             | Description                                                                                                                                                                                                                                                                                                               |
|------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Project Herophilus Technical Content](https://github.com/Project-Herophilus/Project-Herophilus-Assets/tree/main/Platform/Content) | Variety of content specific to iDaaS - PowerPoints, Word Docs, and Draw.IO diagrams                                                                                                                                                                                                                                       |
| [Test Data](https://github.com/Project-Herophilus/Project-Herophilus-Assets/tree/main/Testing)                                     | Test data for all healthcare industry standards work                                                                                                                                                                                                                                                                      |
| [Synthetic Data](https://github.com/Project-Herophilus/DataSynthesis)                                                              | When it comes to building or testing assets having lots of data to resemble you production data is critical, it also helps drive innovation. We have open sourced a synthetic data platform and continue to enahnce it based on feedback. It currently contains over 18 billion specific data attributes that can be used |

# Administering the Platform - Management and Insight of Components
Administering and seeing what the plaform is doing is also critical. Within each specific repository there is an administrative user interface that allows for monitoring and insight into the connectivity of any endpoint. Additionally, there is also the implementation to enable implementations to build there own by exposing the metadata. The data is exposed and can be used in numerous very common tools like Data Dog, Prometheus and so forth.
This capability to enable would require a few additional properties to be set.

Below is a generic visual of how this looks (the visual below is specific to iDaaS Connect HL7): <br/>

![iDaaS Platform - Visuals - iDaaS Data Flow - Detailed](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/Platform/Images/iDAAS-Platform/iDaaS-Mgmt-UI.png)

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
For all iDaaS design patterns it should be assumed that you will either install as part of this effort, or have the following:

- Java JDK
   Java is what everything is developed in. The current supported JDK release(s) are 1.8 and 11. We strongly recommend 11 as 
   all the build actions and activities we do are based on this JDK release.
   <a href="https://developers.redhat.com/products/openjdk/download" target=_blank>OpenJDK Download Site</a>
- An existing Kafka (or some flavor of it) up and running. Red Hat currently implements AMQ-Streams based on Apache Kafka; 
   however, we have implemented iDaaS with numerous Kafka implementations. Please see the following files we have 
   included to try and help: <br/>
   [Kafka](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/Kafka.md) <br/>
   [KafkaWindows](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/KafkaWindows.md) <br/>
   No matter the platform chosen it is important to know that the Kafka out of the box implementation might require some changes depending
   upon your implementation needs. Here are a few we have made to ensure: <br/>
   In <kafka>/config/consumer.properties file we will be enhancing the property of auto.offset.reset to earliest. This is intended to enable any new
   system entering the group to read ALL the messages from the start. <br/>
   auto.offset.reset=earliest <br/>
- Some understanding of building, deploying Java artifacts and the commands associated. If using Maven commands then 
  Maven would need to be intalled and runing for the environment you are using. More details about Maven can be found
  [here](https://maven.apache.org/install.html). This can all be done from an editor or command line, whatever the implementer is most comfortable with. 
- An internet connection with active internet connectivity, this is to ensure that if any Maven commands are
  run and any libraries need to be pulled down they can.<br/>
- Something to view Kafka topics with as you are developing and a potential interface for production when and if needed.
  Depending on your Kafka implementation can make this a non-issue; however, we wanted to make you aware of this need 
  as being able to see data in every component natively is key for validating and implementing any solution. There are 
  several open or inexpensive options to chose from and within the community we have used all of them below with success.
  - Open Source and Web based: [Provectus](https://github.com/provectus/kafka-ui)
  - Open Source and Web based: [Kafdrop](https://github.com/obsidiandynamics/kafdrop)
  - Open Source and Web based: [Kowl](https://github.com/redpanda-data/kowl)
  - Desktop Based and Paid Product after trial: [Offset Explorer](https://www.kafkatool.com/) 

