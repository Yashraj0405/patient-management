package com.pm.patientService.kafka;

import com.pm.patientService.model.Patient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {

    //KafkaTemplate - > used to send messages to Kafka topics. It provides various methods for sending messages, including synchronous and asynchronous options.
    //It sends messages in key-value format, where the key is typically a string and the value can be any serializable object. In this case, we are using byte[] as the value type, which allows us to send binary data to Kafka topics.
    @Autowired
    private KafkaTemplate<String,byte[]> kafkaTemplate;


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
            kafkaTemplate.send("patient", key, event.toByteArray())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Message sent successfully to topic={}, partition={}, offset={}",
                                    result.getRecordMetadata().topic(),
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        } else {
                            log.error("Failed to send Kafka message", ex);
                        }
                    });
        }catch (Exception e){
            log.error("Failed to send event: {}",event);
        }
    }


}
