package ru.tinkoff.trade.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.tinkoff.trade.domain.enums.CandleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MacdIndicatorInfo {

  @Id
  @GeneratedValue
  public UUID id;

  @Column
  private BigDecimal value;

  @Column
  private ZonedDateTime dateTimePeriod;

  @Column
  private Integer shortBarCount;

  @Column
  private Integer longBarCount;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  @NotNull
  private CandleType type;

  @OneToOne(fetch = FetchType.LAZY)
  private StockCandles stockCandles;
}
