package ru.totsystems.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "securities")
public class Security {

    @Id
    @Column(name = "secid")
    private String secId;

    @Column(name = "reg_number")
    private String regNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "isin")
    private String isin;

    @Column(name = "emitent_title")
    private String emitentTitle;

    public Security() {
    }

    public Security(String secId, String regNumber, String name, String isin, String emitentTitle) {
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
