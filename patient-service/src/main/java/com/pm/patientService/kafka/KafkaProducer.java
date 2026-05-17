package com.pm.patientService.kafka;

import com.pm.patientService.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {

    //KafkaTemplate - > used to send messages to Kafka topics. It provides various methods for sending messages, including synchronous and asynchronous options.
    //It sends messages in key-value format, where the key is typically a string and the value can be any serializable object. In this case, we are using byte[] as the value type, which allows us to send binary data to Kafka topics.
    private final KafkaTemplate<String,byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient){
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try{
            log.info("Sending event: {}",event);
            String key = patient.getId().toString();  // Use patient ID as key
            kafkaTemplate.send("patient", key, event.toByteArray());
        }catch (Exception e){
            log.error("Failed to send event: {}",event);
        }
    }


}
