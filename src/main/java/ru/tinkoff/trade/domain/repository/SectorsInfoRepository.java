package ru.tinkoff.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.tinkoff.trade.domain.entity.SectorsInfo;

import java.util.Optional;
import java.util.UUID;

public interface SectorsInfoRepository extends JpaRepository<SectorsInfo, UUID> {

    @Query("select si from SectorsInfo si where si.industry = :industry " +
            "and si.industryGroup = :industryGroup " +
            "and si.industryGroupId = :industryGroupId " +
            "and si.industryId = :industryId " +
            "and si.sector = :sector " +
            "and si.sectorId = :sectorId ")
    Optional<SectorsInfo> findSectorInfo(String industry,
                                        String industryGroup,
                                        Integer industryGroupId,
                                        Integer industryId,
                                        String sector,
                                        Integer sectorId);
}
