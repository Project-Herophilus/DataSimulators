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
package io.connectedhealth_idaas.datasimulation.kic;

import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("ConfigurationProperties")
@ConfigurationProperties(prefix = "idaas")
public class ConfigProperties {

    //Variables
    // Kafka
    private String kafkaBrokers;
    // Platform Topics
    private String kicTopicName;

    // Getters
    // Getters: Kafka Brokers
    public String getKafkaBrokers() {
        return kafkaBrokers;
    }
    // Getters: Platform Topics
    public String getkicTopicName() { return kicTopicName; }

    // Setters
    // Setters: Kafka Brokers
    public void setKafkaBrokers(String kafkaBrokers) {
        this.kafkaBrokers = kafkaBrokers;
    }
    // Setters: Kafka Topics
    public void setkicTopicName(String kicTopicName) { this.kicTopicName = kicTopicName; }

}
