/*
 * Copyright 2019 Red Hat, Inc.
 * <p>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package io.connectedhealth_idaas.datasimulation.hl7;

import java.util.ArrayList;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaEndpoint;
//import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
//import javax.jms.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
//import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/*
 *
 */

@Component
public class CamelConfiguration extends RouteBuilder {
  private static final Logger log = LoggerFactory.getLogger(CamelConfiguration.class);

  @Autowired
  private ConfigProperties config;

 /* @Bean
  private HL7MLLPNettyEncoderFactory hl7Encoder() {
    HL7MLLPNettyEncoderFactory hl7mllp = new HL7MLLPNettyEncoderFactory();
    hl7mllp.setCharset("iso-8859-1");
    //encoder.setConvertLFtoCR(true);
    return hl7mllp;
  }
  @Bean
  private HL7MLLPNettyDecoderFactory hl7Decoder() {
    HL7MLLPNettyDecoderFactory decoder = new HL7MLLPNettyDecoderFactory();
    decoder.setCharset("iso-8859-1");
    return decoder;
  }*/

  @Bean
  private KafkaEndpoint kafkaEndpoint() {
    KafkaEndpoint kafkaEndpoint = new KafkaEndpoint();
    return kafkaEndpoint;
  }

  @Bean
  private KafkaComponent kafkaComponent(KafkaEndpoint kafkaEndpoint) {
    KafkaComponent kafka = new KafkaComponent();
    return kafka;
  }

  private String getKafkaTopicUri(String topic) {
    return "kafka:" + topic + "?brokers=" + config.getKafkaBrokers();
  }

  private String getHL7Uri(String hostID, int port) {
    String portNumber = String.valueOf(port);
    String svrConnection = "netty4:tcp://"+ hostID +":" + portNumber + "?sync=true&decoder=#hl7Decoder&encoder=#hl7Encoder";
    return svrConnection;
  }

  private String getHL7Uri2(String hostID, int port) {
    String mllpConnection = "mllp:"+ hostID + ":" + port;
    return mllpConnection;
  }

  private String getHL7UriDirectory(String dirName) {
    return "file:src/" + dirName + "?delete=true";
  }

  @Override
  public void configure() throws Exception {

    /*
     * Direct actions used across platform
     *
     */
    from("direct:auditing")
        .routeId("KIC-KnowledgeInsightConformance")
        .setHeader("messageprocesseddate").simple("${date:now:yyyy-MM-dd}")
        .setHeader("messageprocessedtime").simple("${date:now:HH:mm:ss:SSS}")
        .setHeader("processingtype").exchangeProperty("processingtype")
        .setHeader("industrystd").exchangeProperty("industrystd")
        .setHeader("component").exchangeProperty("componentname")
        .setHeader("messagetrigger").exchangeProperty("messagetrigger")
        .setHeader("processname").exchangeProperty("processname")
        .setHeader("auditdetails").exchangeProperty("auditdetails")
        .setHeader("camelID").exchangeProperty("camelID")
        .setHeader("exchangeID").exchangeProperty("exchangeID")
        .setHeader("internalMsgID").exchangeProperty("internalMsgID")
        .setHeader("bodyData").exchangeProperty("bodyData")
        .convertBodyTo(String.class).to(getKafkaTopicUri("opsmgmt_platformtransactions"));
    /*
     * Direct Logging
     */
    from("direct:logging")
        .routeId("Logging")
        .log(LoggingLevel.INFO, log, "Transaction Message: [${body}]");

    /*
     *  HL7 File to HL7 Server
     *  It will automatically create the directory for you, you will just need to place files in it
     *
     */
        from(getHL7UriDirectory(config.getHl7ADT_Directory()))
            // Auditing
            .routeId("hl7ADTSimulator")
            .routeDescription("hl7ADTSimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("ADT")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getAdtHost(),config.getAdtPort()))
            // Process Acks that come back ??
        ;

    from(getHL7UriDirectory(config.getHl7ORM_Directory()))
            // Auditing
            .routeId("hl7ORMSimulator")
            .routeDescription("hl7ORMSimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("ORM")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getOrmHost(),config.getOrmPort()))
            // Process Acks that come back ??
    ;
    from(getHL7UriDirectory(config.getHl7ORU_Directory()))
            // Auditing
            .routeId("hl7ORUSimulator")
            .routeDescription("hl7ORUSimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("ORU")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getOruHost(),config.getOruPort()))
            // Process Acks that come back ??
    ;
    from(getHL7UriDirectory(config.getHl7MFN_Directory()))
            // Auditing
            .routeId("hl7MFNSimulator")
            .routeDescription("hl7MFNSimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("MFN")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getMfnHost(),config.getMfnPort()))
            // Process Acks that come back ??
    ;

    from(getHL7UriDirectory(config.getHl7MDM_Directory()))
            // Auditing
            .routeId("hl7MDMSimulator")
            .routeDescription("hl7MDMSimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("MDM")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getMdmHost(),config.getMdmPort()))
            // Process Acks that come back ??
    ;

    from(getHL7UriDirectory(config.getHl7RDE_Directory()))
            // Auditing
            .routeId("hl7RDESimulator")
            .routeDescription("hl7RDESimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("RDE")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getRdeHost(),config.getRdePort()))
            // Process Acks that come back ??
    ;

    from(getHL7UriDirectory(config.getHl7SCH_Directory()))
            // Auditing
            .routeId("hl7SCHSimulator")
            .routeDescription("hl7SCHSimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("SCH")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getSchHost(),config.getSchPort()))
            // Process Acks that come back ??
    ;

    from(getHL7UriDirectory(config.getHl7VXU_Directory()))
            // Auditing
            .routeId("hl7VXUSimulator")
            .routeDescription("hl7VXUSimulator")
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("hl7-sim")
            .setProperty("appname").constant("iDAAS-Connect-ThirdParty")
            .setProperty("industrystd").constant("HL7")
            .setProperty("messagetrigger").constant("VXU")
            .setProperty("component").simple("${routeId}")
            .setProperty("camelID").simple("${camelId}")
            .setProperty("exchangeID").simple("${exchangeId}")
            .setProperty("internalMsgID").simple("${id}")
            .setProperty("bodyData").simple("${body}")
            .setProperty("processname").constant("Input")
            .setProperty("auditdetails").constant("${file:name} - was processed, parsed and put into topic")
            .wireTap("direct:auditing")
            .to(getHL7Uri2(config.getVxuHost(),config.getVxuPort()))
            // Process Acks that come back ??
    ;
  }
}
