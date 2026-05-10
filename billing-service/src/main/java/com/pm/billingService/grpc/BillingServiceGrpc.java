package com.pm.billingService.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingServiceGrpc extends billing.BillingServiceGrpc.BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpc.class);

    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {

        log.info("Received billing request for user: {}", request.getPatientId());

        //Business logic to create billing account - save to database, call other services, etc.

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12345") // Simulated account ID
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
