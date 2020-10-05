package ru.totsystems.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "history", uniqueConstraints = {@UniqueConstraint(columnNames = {"boardid", "secid", "trade_date"})})
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "boardid")
    private String boardId;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "secid")
    private Security secId;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "trade_date")
    private LocalDate tradeDate;

    @Column(name = "num_trades")
    private Double numTrades;

    @Column(name = "value")
    private Double value;

    @Column(name = "volume")
    private Double volume;

    @Column(name = "open")
    private Double open;

    @Column(name = "close")
    private Double close;

    @Column(name = "low")
    private Double low;

    @Column(name = "high")
    private Double high;

    public History() {
    }

    public History(String boardId, Security security, String shortName, LocalDate tradeDate, Double numTrades, Double value, Double volume, Double open, Double close, Double low, Double high) {
        this.boardId = boardId;
        this.secId = security;
        this.shortName = shortName;
        this.tradeDate = tradeDate;
        this.numTrades = numTrades;
        this.value = value;
        this.volume = volume;
        this.open = open;
        this.close = close;
        this.low = low;
        this.high = high;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Security getSecId() {
        return secId;
    }

    public void setSecId(Security secId) {
        this.secId = secId;
    }

    public LocalDate getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(LocalDate tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Double getNumTrades() {
        return numTrades;
    }

    public void setNumTrades(Double numTrades) {
        this.numTrades = numTrades;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }
}
