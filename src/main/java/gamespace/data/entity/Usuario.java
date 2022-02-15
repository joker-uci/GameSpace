package gamespace.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gamespace.data.AbstractEntity;
import javax.persistence.Entity;

@Entity
public class Usuario extends AbstractEntity {

    private String firstName;
    private String usuario;
    @JsonIgnore
    private String contrasenna;
    private String rol;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
