package ru.totsystems.dto;

public class SecurityDto {

    private String secId;
    private String regNumber;
    private String name;
    private String isin;
    private String emitentTitle;

    public SecurityDto() {
    }

    public SecurityDto(String secId, String regNumber, String name, String isin, String emitentTitle) {
        this.secId = secId;
        this.regNumber = regNumber;
        this.name = name;
        this.isin = isin;
        this.emitentTitle = emitentTitle;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getEmitentTitle() {
        return emitentTitle;
    }

    public void setEmitentTitle(String emitentTitle) {
        this.emitentTitle = emitentTitle;
    }
}
