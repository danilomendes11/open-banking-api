package unicamp.mc946.openbankingapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Asset {

    @Id
    @Column(name = "asset_name")
    private String name;

    @Column
    private String issuerAccId;

    public Asset() {
    }

    public Asset(String name, String issuerAccId) {
        this.name = name;
        this.issuerAccId = issuerAccId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuerAccId() {
        return issuerAccId;
    }

    public void setIssuerAccId(String issuerAccId) {
        this.issuerAccId = issuerAccId;
    }
}
