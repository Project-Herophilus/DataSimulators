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

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class CamelConfiguration extends RouteBuilder {

  @Override
  public void configure() throws Exception {

    onException(Exception.class)
      .handled(true)
      .log(LoggingLevel.ERROR,"${exception}");

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
        .convertBodyTo(String.class)
        .to("kafka:opsmgmt_platformtransactions?brokers={{idaas.kafka.brokers}}");


    /*
     *  HL7 File to HL7 Server
     *  It will automatically create the directory for you, you will just need to place files in it
     *
     */
        from("file:src/{{idaas.hl7.adt.dir}}?delete=true")
             .routeDescription("hl7ADTSimulator")
             .routeId("hl7ADTSimulator")
            .choice()
              .when(simple("{{idaas.processADT}}"))
              // Multiple Messages
              .otherwise()
              // Single Message
              .convertBodyTo(String.class)
              .setProperty("processingtype").constant("hl7-sim")
              .setProperty("appname").constant("iDAAS-Connect-HL7")
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
              .to("mllp:{{idaas.adt.host}}:{{idaas.adt.port}}")
            .end()
            // Process Acks that come back ??
        ;
    /*
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

     */
    ;
  }
}
