package gamespace.data.entity;

import gamespace.data.AbstractEntity;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Entity;

@Entity
public class Cuestionarios extends AbstractEntity {

    private UUID cuestionario;
    private String juego;
    private String descripcion;
    private LocalDate fecha;
    private String usuario;
    private String criterio1;
    private Integer promedio1;
    private String criterio2;
    private Integer promedio2;
    private String criterio3;
    private Integer proedio3;
    private String criterio4;
    private Integer promedio4;
    private String criterio5;
    private Integer promedio5;

    public UUID getCuestionario() {
        return cuestionario;
    }
    public void setCuestionario(UUID cuestionario) {
        this.cuestionario = cuestionario;
    }
    public String getJuego() {
        return juego;
    }
    public void setJuego(String juego) {
        this.juego = juego;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getUsuario() {
        return usuario;
    }
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getCriterio1() {
        return criterio1;
    }
    public void setCriterio1(String criterio1) {
        this.criterio1 = criterio1;
    }
    public Integer getPromedio1() {
        return promedio1;
    }
    public void setPromedio1(Integer promedio1) {
        this.promedio1 = promedio1;
    }
    public String getCriterio2() {
        return criterio2;
    }
    public void setCriterio2(String criterio2) {
        this.criterio2 = criterio2;
    }
    public Integer getPromedio2() {
        return promedio2;
    }
    public void setPromedio2(Integer promedio2) {
        this.promedio2 = promedio2;
    }
    public String getCriterio3() {
        return criterio3;
    }
    public void setCriterio3(String criterio3) {
        this.criterio3 = criterio3;
    }
    public Integer getProedio3() {
        return proedio3;
    }
    public void setProedio3(Integer proedio3) {
        this.proedio3 = proedio3;
    }
    public String getCriterio4() {
        return criterio4;
    }
    public void setCriterio4(String criterio4) {
        this.criterio4 = criterio4;
    }
    public Integer getPromedio4() {
        return promedio4;
    }
    public void setPromedio4(Integer promedio4) {
        this.promedio4 = promedio4;
    }
    public String getCriterio5() {
        return criterio5;
    }
    public void setCriterio5(String criterio5) {
        this.criterio5 = criterio5;
    }
    public Integer getPromedio5() {
        return promedio5;
    }
    public void setPromedio5(Integer promedio5) {
        this.promedio5 = promedio5;
    }

}
