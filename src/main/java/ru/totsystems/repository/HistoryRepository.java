package ru.totsystems.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.totsystems.model.History;
import ru.totsystems.model.Security;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllBySecId(Security secId);

    List<History> findAllByTradeDate(LocalDate tradeDate);

    List<History> findAllBySecIdAndTradeDate(Security secId, LocalDate tradeDate);

    List<History> findAllByBoardIdAndSecIdAndTradeDate(String boardId, Security secId, LocalDate tradeDate);

    @Query("SELECT s.secId, s.regNumber, s.name, s.emitentTitle, h.tradeDate, h.numTrades, h.open, h.close " +
            "FROM Security s JOIN History h ON s.secId = h.secId")
    List<?> findAllDop(Sort sortBy);

    @Query("SELECT s.secId, s.regNumber, s.name, s.emitentTitle, h.tradeDate, h.numTrades, h.open, h.close " +
            "FROM Security s JOIN History h ON s.secId = h.secId " +
            "WHERE s.emitentTitle = :emitentTitle")
    List<?> findAllDopByEmitentTitle(@Param("emitentTitle") String emitentTitle, Sort sortBy);

    @Query("SELECT s.secId, s.regNumber, s.name, s.emitentTitle, h.tradeDate, h.numTrades, h.open, h.close " +
            "FROM Security s JOIN History h ON s.secId = h.secId " +
            "WHERE h.tradeDate = :tradeDate")
    List<?> findAllDopByTradeDate(@Param("tradeDate") LocalDate tradeDate, Sort sortBy);

    @Query("SELECT s.secId, s.regNumber, s.name, s.emitentTitle, h.tradeDate, h.numTrades, h.open, h.close " +
            "FROM Security s JOIN History h ON s.secId = h.secId " +
            "WHERE s.emitentTitle = :emitentTitle AND h.tradeDate = :tradeDate")
    List<?> findAllDopByEmitentTitleAndTradeDate(
            @Param("emitentTitle") String emitentTitle,
            @Param("tradeDate") LocalDate tradeDate,
            Sort sortBy
    );
}
