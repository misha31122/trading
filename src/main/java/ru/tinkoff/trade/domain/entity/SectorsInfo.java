package ru.tinkoff.trade.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SectorsInfo {

    @Id
    @GeneratedValue
    public UUID id;

    @Column
    private String industry;

    @Column
    private String industryGroup;

    @Column
    private Integer industryGroupId;

    @Column
    private Integer industryId;

    @Column
    private String sector;

    @Column
    private Integer sectorId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sectorsInfo")
    private Set<Stock> stocks = new HashSet<>();
}
