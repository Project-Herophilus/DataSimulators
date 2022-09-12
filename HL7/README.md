# HL7 Data Simulator

## Pre-Requisites
For all iDaaS design patterns it should be assumed that you will either install as part of this effort, or have the following:

1. An existing Kafka (or some flavor of it) up and running. We have implemented iDaaS with numerous Kafka implementations. 
Please see the following files we have included to try and help: <br/>
[Kafka Non Windows](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/Kafka.md) <br/>
[Kafka Windows](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/KafkaWindows.md) <br/>
No matter the platform chosen it is important to know that the Kafka out of the box implementation might require some 
changes depending upon your implementation needs. Here are a few we have made to ensure: <br/>
In <kafka>/config/consumer.properties file we will be enhancing the property of auto.offset.reset to earliest. This is intended to enable any new
system entering the group to read ALL the messages from the start. <br/>
auto.offset.reset=earliest <br/>
2. Java JDK: Java is what everything is developed in. The current supported JDK release(s) are 1.8 and 11. We strongly 
recommend 11 as all the build actions and activities we do are based on this JDK release.
<a href="https://developers.redhat.com/products/openjdk/download" target=_blank>OpenJDK Download Site</a>.
3. Maven: Some understanding of building, deploying Java artifacts and the commands associated. If using Maven commands then 
Maven would need to be intalled and runing for the environment you are using. More details about Maven can be 
found [here](https://maven.apache.org/install.html)<br/>
4. An internet connection with active internet connectivity, this is to ensure that if any Maven commands are
run and any libraries need to be pulled down they can.<br/>

# Scenario(s)
This scenario follows a very common general clinical care implementation pattern for processing HL7 transactions. The 
implementation pattern involves one system sending data to another system via the HL7 message standard protocol (MLLP). 
The HL7 data simulator will connect and send a configurable number of HL7 transactions to the defined HL7 server 
host and port. When the HL7 message is processed by the HL7 Server it will generate an HL7 ACK that is receives.

 <img src="https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/images/iDaaS-Platform/DataFlow-HL7.png" width="800" height="600">

|Identifier | Description |
| ------------ | ----------- |
| Healthcare Facility| MCTN |
| Sending EMR/EHR | MMS |
| HL7 Message Events | ADT (Admissions, Discharge and Transfers),ORM (Orders),ORU (Results) |
<br/>

It is important to know that for every HL7 Message Type/Event there is a specifically defined, and dedicated, HL7 socket 
server endpoint.

## Repositories Involved with This Scenario


# Start The Engine!!!
This section covers the running of the solution. There are several options to start the Engine Up!!!

## Step 1: Kafka Server To Connect To
In order for ANY processing to occur you must have a Kafka server running that this accelerator is configured to connect to.
Please see the following files we have included to try and help: <br/>
[Kafka Non Windows](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/Kafka.md)<br/>
[Kafka Windows](https://github.com/RedHat-Healthcare/iDaaS-Demos/blob/master/KafkaWindows.md)<br/>
As with all implementations we also have container based implementations of Kafka as well. These can be very simple to implement
and include. 

## Step 2: How To Get, Build and Run iDaaS-Connect Assets
Within each submodule/design pattern/reference architecture in this repository there is a specific README.md. It is
intended to follow a specific format that covers a solution definition, how we look to continually improve, pre-requisities,
implementation details including specialized configuration, known issues and their potential resolutions.
However, there are a lot of individual capabilities, we have tried to keep content relevant and specific to
cover specific topics.
- For cloning, building and running of assets that content can be found
  [here](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/CloningBuildingRunningSolution.md).
- Within each implementation there is a management console, the management console provides the same
  interface and capabilities no matter what implementation you are working within, some specifics and
  and details can be found [here](https://github.com/Project-Herophilus/Project-Herophilus-Assets/blob/main/AdministeringPlatform.md).

## Specific Implementation Configuration Details
There are several key details you must make decisions on and also configure. For all of these you will
need to update the application.properties in accordance with these decisions.
- For every HL7 based connection you can specify a specific directory, port and whether or not
  you want to process the ACK/NAK responses.
- CCDA. Key setting is whether you want to automatically convert the data with the setting
  idaas.ccdaConvert=true
- processTerminologies - if you want to process terminologies based on the data dlowing through the
  HL7 and CCDA transactions. If idaas.processTerminologies=true then all transactions will go to a specifically
  defined component for another set of assets to process.
- convertToFHIR - is about specifically converting only HL7 messages to FHIR automatically with the
  setting idaas.convert2FHIR=true
- Coming soon are the ability to automatically anonymize or deidentify data. with the settings
  idaas.deidentify=false and idaas.anonymize=false


## Step 3: Running the App: Maven Commands or Code Editor
This section covers how to get the application started.
+ Maven: go to the directory of where you have this code. Specifically, you want to be at the same level as the POM.xml file and execute the
following command: <br/>
```
mvn clean install
 ```
You can run the individual efforts with a specific command, it is always recommended you run the mvn clean install first.
Here is the command to run the design pattern from the command line: <br/>
```
mvn spring-boot:run
 ```
Depending upon if you have every run this code before and what libraries you have already in your local Maven instance it could take a few minutes.
+ Code Editor: You can right click on the Application.java in the /src/<application namespace> and select Run

# Running the Java JAR
If you don't run the code from an editor or from the maven commands above. You can compile the code through the maven
commands above to build a jar file. Then, go to the /target directory and run the following command: <br/>
```
java -jar <jarfile>.jar 
 ```

### Design Pattern/Accelerator Configuration
All iDaaS Design Pattern/Accelelrators have application.properties files to enable some level of reusability of code and simplfying configurational enhancements.<br/>
In order to run multiple iDaaS integration applications we had to ensure the internal http ports that
the application uses. In order to do this we MUST set the server.port property otherwise it defaults to port 8080 and ANY additional
components will fail to start. iDaaS Connect HL7 uses 9980. You can change this, but you will have to ensure other applications are not
using the port you specify.

```properties
server.port=9980
```
Once built you can run the solution by executing `./platform-scripts/start-solution.sh`.
The script will startup Kafka and iDAAS server.

Alternatively, if you have a running instance of Kafka, you can start a solution with:
`./platform-scripts/start-solution-with-kafka-brokers.sh --idaas.kafkaBrokers=host1:port1,host2:port2`.
The script will startup iDAAS server.

It is possible to overwrite configuration by:
1. Providing parameters via command line e.g.
`./start-solution.sh --idaas.adtPort=10009`
2. Creating an application.properties next to the idaas-connect-hl7.jar in the target directory
3. Creating a properties file in a custom location `./start-solution.sh --spring.config.location=file:./config/application.properties`

Supported properties include (for this accelerator there is a block per message type that follows the same pattern):
```properties
server.port=9980
# Kafka Configuration - use comma if multiple kafka servers are needed
idaas.kafkaBrokers=localhost:9092
# Basics on properties
idaas.hl7ADT_Directory=data/adt
idaas.adtPort=10001
idaas.adtACKResponse=true
idaas.adtTopicName=mctn_mms_adt
idaas.hl7ORM_Directory=data/orm
```


Happy using and coding....

