package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.ShotFiredEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private static final String TOPIC = "cc.battleships.shot";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void shoot(ShotFiredEvent event, String token) {
        try {
            String json = new ObjectMapper().writeValueAsString(event);

            logger.info("Sending " + json);

            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, json);
            record.headers().add(new RecordHeader("Authorization", ("Bearer " + token).getBytes()));

//            this.kafkaTemplate.send(TOPIC, json);
            this.kafkaTemplate.send(record);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}