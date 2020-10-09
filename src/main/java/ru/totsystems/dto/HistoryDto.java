package ru.totsystems.dto;

import java.time.LocalDate;

public class HistoryDto {

    private Long id;
    private String boardId;
    private String secId;
    private String shortName;
    private LocalDate tradeDate;
    private Double numTrades;
    private Double value;
    private Double volume;
    private Double open;
    private Double close;
    private Double low;
    private Double high;

    public HistoryDto() {
    }

    public HistoryDto(String boardId, String secId, String shortName, LocalDate tradeDate, Double numTrades, Double value, Double volume, Double open, Double close, Double low, Double high) {
        this.boardId = boardId;
        this.secId = secId;
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

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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
