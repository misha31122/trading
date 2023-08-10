package com.trading.home.service;

import com.google.protobuf.UInt64Value;
import com.trading.home.scoresegment.ScoreSegmentRequest;
import com.trading.home.scoresegment.ScoreSegmentResponse;
import com.trading.home.scoresegment.ScoreSegmentServiceGrpc;
import io.grpc.stub.StreamObserver;
import java.math.BigInteger;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ScoreSegmentService extends ScoreSegmentServiceGrpc.ScoreSegmentServiceImplBase {

  @Autowired
  private ValidationService validationService;

  @Override
  public void calculateScoreSegment(ScoreSegmentRequest request, StreamObserver<ScoreSegmentResponse> responseObserver) {
    // System.out.println("Request received from client:\n" + request);

    validationService.validateIdNumber(request.getIdNumber());

    BigInteger idNumber = new BigInteger(request.getIdNumber().toString());

    BigInteger scoreSegment = idNumber.mod(new BigInteger("9"));
    scoreSegment = scoreSegment.compareTo(BigInteger.ZERO)  == 0 ? BigInteger.ONE : scoreSegment;

    ScoreSegmentResponse response = ScoreSegmentResponse.newBuilder()
        .setScoreSegment(UInt64Value.newBuilder().setValue(scoreSegment.longValue()))
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}