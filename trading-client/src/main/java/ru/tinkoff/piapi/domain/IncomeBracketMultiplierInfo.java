package ru.tinkoff.piapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomeBracketMultiplierInfo {

  private Long id;

  private String currency;

  private Integer minThreshold;

  private Integer maxThreshold;

  private Integer multiplier;
}
