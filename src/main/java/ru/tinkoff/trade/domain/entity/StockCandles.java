package ru.tinkoff.trade.domain.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.tinkoff.trade.domain.enums.CandleType;
import ru.tinkoff.trade.invest.dto.V1HistoricCandle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StockCandles {

  @Id
  @GeneratedValue
  public UUID id;

  @Type(type = "jsonb")
  @ToString.Exclude
  @Column(name = "candle", columnDefinition = "json", nullable = false)
  private V1HistoricCandle candle;

  @ToString.Exclude
  @ManyToOne
  @JoinColumn(name = "stock_id", referencedColumnName = "id")
  private Stock stock;

  @Column(nullable = false)
  private ZonedDateTime dateTime;

  @Column(name = "type")
  @Enumerated(EnumType.STRING)
  @NotNull
  private CandleType type;

  @OneToOne(mappedBy = "stockCandles", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @ToString.Exclude
  @Fetch(FetchMode.JOIN)
  private MacdIndicatorInfo macdIndicatorInfo;
}
