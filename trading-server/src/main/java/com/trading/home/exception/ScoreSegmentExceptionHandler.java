package com.trading.home.exception;

import com.trading.home.scoresegment.ScoreSegmentExceptionResponse;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import java.time.Instant;
import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ScoreSegmentExceptionHandler {

  @GrpcExceptionHandler(ScoreSegmentException.class)
  public StatusRuntimeException handleValidationError(ScoreSegmentException cause) {

    Instant time = Instant.now();
    Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
        .setNanos(time.getNano()).build();

    ScoreSegmentExceptionResponse exceptionResponse =
        ScoreSegmentExceptionResponse.newBuilder()
            .setErrorCode(cause.getErrorCode())
            .setTimestamp(timestamp)
            .build();


    Status status = Status.newBuilder()
        .setCode(Code.INVALID_ARGUMENT.getNumber())
        .setMessage("Invalid score segment")
        .addDetails(Any.pack(exceptionResponse))
        .build();

    return StatusProto.toStatusRuntimeException(status);
  }
}
