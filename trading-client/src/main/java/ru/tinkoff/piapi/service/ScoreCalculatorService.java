package ru.tinkoff.piapi.service;

import static com.trading.home.scoresegment.ScoreSegmentErrorCode.INVALID_ID_NUMBER_VALUE;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.UInt64Value;
import com.google.rpc.Status;
import com.trading.home.cityscore.CityScoreExceptionResponse;
import com.trading.home.cityscore.CityScoreRequest;
import com.trading.home.cityscore.CityScoreResponse;
import com.trading.home.cityscore.CityScoreServiceGrpc;
import ru.tinkoff.piapi.domain.IncomeBracketMultiplierInfo;
import ru.tinkoff.piapi.exception.ScoreSegmentException;
import ru.tinkoff.piapi.model.ScoreCalculatorRequest;
import com.trading.home.scoresegment.ScoreSegmentExceptionResponse;
import com.trading.home.scoresegment.ScoreSegmentRequest;
import com.trading.home.scoresegment.ScoreSegmentResponse;
import com.trading.home.scoresegment.ScoreSegmentServiceGrpc;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.StatusProto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class ScoreCalculatorService {

  @GrpcClient("city-score")
  private CityScoreServiceGrpc.CityScoreServiceBlockingStub cityScoreStub;

  @GrpcClient("score-segment")
  private ScoreSegmentServiceGrpc.ScoreSegmentServiceBlockingStub scoreSegmentStub;

  @Autowired
  private IncomeBracketMultiplierInfoService incomeBracketMultiplierInfoService;


  public BigInteger calculateScore(ScoreCalculatorRequest scoreCalculatorRequest) {
    IncomeBracketMultiplierInfo selectedIncomeBracketMultiplerInfo = getIncomeBracketMultiplerInfo(scoreCalculatorRequest.getIncomeBracketMultiplierId());

    BigInteger scoreSegment = getScoreSegment(scoreCalculatorRequest.getIdNumber());
    Integer cityScore = getCityScore(scoreCalculatorRequest.getCityCode());
    BigInteger score = BigInteger.valueOf(selectedIncomeBracketMultiplerInfo.getMultiplier().intValue())
        .multiply(scoreSegment)
        .add(BigInteger.valueOf(cityScore.intValue()));

    return score;
  }

  private BigInteger getScoreSegment(BigInteger idNumber) {
    ScoreSegmentRequest scoreSegmentRequest = ScoreSegmentRequest.newBuilder()
        .setIdNumber(UInt64Value.newBuilder().setValue(idNumber.longValue()).build())
        .build();
    try {
      ScoreSegmentResponse scoreSegmentResponse = scoreSegmentStub.calculateScoreSegment(scoreSegmentRequest);
      return new BigInteger(scoreSegmentResponse.getScoreSegment().toString());
    } catch (Exception e){
      Status status = StatusProto.fromThrowable(e);
      for (Any any : status.getDetailsList()) {
        if (!any.is(ScoreSegmentExceptionResponse.class)) {
          continue;
        }
        try {
          ScoreSegmentExceptionResponse exceptionResponse = any.unpack(ScoreSegmentExceptionResponse.class);
          System.out.println("timestamp: " + exceptionResponse.getTimestamp() +
              ", errorCode : " + exceptionResponse.getErrorCode());
        } catch (InvalidProtocolBufferException ex) {
          ex.printStackTrace();
        }
      }
      // System.out.println(status.getCode() + " : " + status.getDescription());
    }

    // return a default value
    return BigInteger.ONE;
  }

  private Integer getCityScore(Integer cityCode) {
    CityScoreRequest cityScoreRequest = CityScoreRequest.newBuilder()
        .setCityCode(cityCode)
        .build();
    try {
      CityScoreResponse cityScoreResponse = cityScoreStub.calculateCityScore(cityScoreRequest);
      return cityScoreResponse.getCityScore();
    } catch (StatusRuntimeException e){
      Status status = StatusProto.fromThrowable(e);
      for (Any any : status.getDetailsList()) {
        if (!any.is(CityScoreExceptionResponse.class)) {
          continue;
        }
        try {
          CityScoreExceptionResponse exceptionResponse = any.unpack(CityScoreExceptionResponse.class);
          System.out.println("timestamp: " + exceptionResponse.getTimestamp() +
              ", errorCode : " + exceptionResponse.getErrorCode());
        } catch (InvalidProtocolBufferException ex) {
          ex.printStackTrace();
        }
      }
      // System.out.println(status.getCode() + " : " + status.getDescription());
    }

    // return a default value
    return Integer.valueOf(1);
  }

  private IncomeBracketMultiplierInfo getIncomeBracketMultiplerInfo(Long incomeBracketMultiplerInfoId) {
    Optional<IncomeBracketMultiplierInfo> multiplierInfo = incomeBracketMultiplierInfoService.findById(incomeBracketMultiplerInfoId);
    if (!multiplierInfo.isPresent()) {
      throw new ScoreSegmentException(INVALID_ID_NUMBER_VALUE);
    }
    return multiplierInfo.get();
  }
}