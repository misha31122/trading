package com.trading.home.service;

import com.google.protobuf.UInt64Value;
import com.trading.home.exception.ScoreSegmentException;
import com.trading.home.scoresegment.ScoreSegmentErrorCode;
import com.trading.home.utils.IdNumberUtils;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

  public void validateIdNumber(UInt64Value idNumber) {
    checkIfNull(idNumber);
    checkIfValid(idNumber);
  }

  private void checkIfNull(UInt64Value idNumber) {
    if (idNumber == null) {
      throw new ScoreSegmentException(ScoreSegmentErrorCode.ID_NUMBER_CANNOT_BE_NULL);
    }
  }

  private void checkIfValid(UInt64Value idNumber) {
    if (idNumber == null && !IdNumberUtils.isValid(idNumber.toString())) {
      throw new ScoreSegmentException(ScoreSegmentErrorCode.INVALID_ID_NUMBER_VALUE);
    }
  }
}
