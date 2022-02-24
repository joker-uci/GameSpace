package gamespace.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gamespace.data.AbstractEntity;
import javax.persistence.Entity;

@Entity
public class Usuario extends AbstractEntity {

    private String userName;
    private String firstName;
    @JsonIgnore
    private String contrasenna;
    private String rol;

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getContrasenna() {
        return contrasenna;
    }
    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

}
