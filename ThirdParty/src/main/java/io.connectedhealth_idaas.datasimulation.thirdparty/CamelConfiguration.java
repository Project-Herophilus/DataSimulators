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
package io.connectedhealth_idaas.datasimulation.thirdparty;

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


  @Bean
  private KafkaEndpoint kafkaEndpoint() {
    KafkaEndpoint kafkaEndpoint = new KafkaEndpoint();
    return kafkaEndpoint;
  }

  private String getKafkaTopicUri(String topic) {
    return "kafka:" + topic + "?brokers=" + config.getKafkaBrokers();
  }
  private String getTimer(String timerSeconds) {
    return "timer://pollTimer?period=" + timerSeconds ;
  }
  private Integer getCounter(Integer recordCount) {
    return recordCount ;
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
     *
     */
    from(getTimer(config.getTimerSeconds()))
            // Auditing
            .routeId("kicFHIRSimulator")
            .routeDescription("kicDataSimulator")
            .setBody(simple("Executed Event at "+ "${date:now:yyyy-MM-dd} "+ "${date:now:HH:mm:ss:SSS}"))
            .convertBodyTo(String.class)
            .setProperty("processingtype").constant("kic-sim")
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
            //.wireTap("direct:auditing")
            //.loop(25).copy()
            .loop(getCounter(config.getProcessingCount())).copy()
            /* //.transform(body().append("B"))
               .routeId("kicFHIRSimulator2")
               .routeDescription("kicDataSimulator2")
               .convertBodyTo(String.class)
               .setProperty("processingtype").constant("kic-sim")
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
               //.transform(body()).copy()*/
            .to("direct:auditing")
            .end()
            .to("mock:loop")
    ;

  }
}
