package unicamp.mc946.openbankingapi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "client")
public class User {

    @Id
    @Column(unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column
    private String privateKey;

    @Column
    private String stellarAccId;

    public User(String login, String password, String name, String privateKey, String stellarAccId) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.privateKey = privateKey;
        this.stellarAccId = stellarAccId;
    }

    public User() {}

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStellarAccId() {
        return stellarAccId;
    }

    public void setStellarAccId(String stellarAccId) {
        this.stellarAccId = stellarAccId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
