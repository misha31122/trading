package ru.tinkoff.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.trade.domain.entity.Stock;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {

    Optional<Stock> findByFigi(String figi);
}
