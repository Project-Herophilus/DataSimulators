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
package io.connectedhealth_idaas.datasimulation.fhir;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/*
 *
 */

@Component
public class CamelConfiguration extends RouteBuilder {

  public static final String ROUTE_ID = "FHIRSimulator";

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
        .convertBodyTo(String.class)
        .to("kafka:{{idaas.kic.topic.name}}?brokers={{idaas.kafka.brokers}}");

    /*
     *  FHIR File to FHIR Server
     *  It will automatically create the directory for you, you will just need to place files in it
     *
     */

    from("timer://pollTimer?period={{idaas.processing.count}}")
        // Auditing
        .routeId(ROUTE_ID)
        .routeDescription(ROUTE_ID)
        .setBody(simple("Executed Event at "+ "${date:now:yyyy-MM-dd} "+ "${date:now:HH:mm:ss:SSS}"))
        .convertBodyTo(String.class)
        .setProperty("processingtype").constant("fhir-sim")
        .setProperty("appname").constant("iDAAS-DataSimulator-FHIR")
        .setProperty("industrystd").constant("NA")
        .setProperty("messagetrigger").constant("NA")
        .setProperty("component").simple("${routeId}")
        .setProperty("camelID").simple("${camelId}")
        .setProperty("exchangeID").simple("${exchangeId}")
        .setProperty("internalMsgID").simple("${id}")
        .setProperty("bodyData").simple("${body}")
        .setProperty("processname").constant("Input")
        .setProperty("auditdetails").constant("FHIR Simulation event was processed, parsed and put into topic")
        .loop(constant("{{idaas.processing.count}}"))
            .copy()
            .to("log://" + ROUTE_ID + "?showAll=true")
            .to("direct:auditing")
        .end();

  }
}
