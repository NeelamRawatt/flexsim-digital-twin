package com.postnord.simulation_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
    
    @Value("${kafka.topics.simulation-trigger}")
    private String triggerTopicName;

    @Value("${kafka.partitions.simulation-trigger}")
    private int triggerPartitions;

    @Value("${kafka.topics.simulation-status}")
    private String statusTopicName;

    @Value("${kafka.partitions.simulation-status}")
    private int statusPartitions;
    
    @Bean
    public NewTopic simulationTriggerTopic() {
        return TopicBuilder.name(triggerTopicName).partitions(triggerPartitions).replicas(1).build();
    }

    @Bean
    public NewTopic simulationStatusTopic() {
        return TopicBuilder.name(statusTopicName).partitions(statusPartitions).replicas(1).build();
    }


}
