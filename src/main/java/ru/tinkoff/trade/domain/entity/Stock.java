package ru.tinkoff.trade.domain.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import ru.tinkoff.trade.invest.dto.V1Share;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
@NoArgsConstructor
public class Stock {

  @Id
  @GeneratedValue
  public UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String figi;

  @Type(type = "jsonb")
  @ToString.Exclude
  @Column(name = "share_data", columnDefinition = "json", nullable = false)
  private V1Share shareData;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @ToString.Exclude
  @JoinColumn(name = "sectors_info_id", referencedColumnName = "id")
  private SectorsInfo sectorsInfo;
}
