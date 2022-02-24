package gamespace.data.entity;

import gamespace.data.AbstractEntity;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Videojuego extends AbstractEntity {

    private String titulo;
    private String cuestionario;
    private String descrpcion;
    private LocalDate fechaLanzamiento;
    @Lob
    private String cover;
    @Lob
    private String archDescarga;

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getCuestionario() {
        return cuestionario;
    }
    public void setCuestionario(String cuestionario) {
        this.cuestionario = cuestionario;
    }
    public String getDescrpcion() {
        return descrpcion;
    }
    public void setDescrpcion(String descrpcion) {
        this.descrpcion = descrpcion;
    }
    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }
    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getArchDescarga() {
        return archDescarga;
    }
    public void setArchDescarga(String archDescarga) {
        this.archDescarga = archDescarga;
    }

}
