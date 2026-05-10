package com.pm.patientService.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {

    // For unary calls, we can use the blocking stub
    private BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort){
        log.info("Initializing BillingServiceGrpcClient with server address: {} and port: {}", serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort)
                .usePlaintext() // Disable TLS for simplicity
                .build();

        billingServiceBlockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId,String patientName,String patientEmail) {

        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(patientName)
                .setEmail(patientEmail)
                .build();

        log.info("Sending billing account creation request for patientId: {}", patientId);

        try {
            BillingResponse response = billingServiceBlockingStub.createBillingAccount(request);
            log.info("Received billing response: accountId={}, status={}", response.getAccountId(), response.getStatus());
            return response;
        } catch (Exception e) {
            log.error("Error while creating billing account for patientId {}: {}", patientId, e.getMessage());
            throw e; // Rethrow or handle as needed
        }

    }
}

