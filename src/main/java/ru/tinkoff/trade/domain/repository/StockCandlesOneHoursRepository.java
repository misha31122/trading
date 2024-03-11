package ru.tinkoff.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.trade.domain.entity.StockCandlesOneHours;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface StockCandlesOneHoursRepository extends JpaRepository<StockCandlesOneHours, UUID> {

    Optional<StockCandlesOneHours> findByDateTimeAndStockId(ZonedDateTime dateTime, UUID id);
}
