package gamespace.data.entity;

import gamespace.data.AbstractEntity;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Videojuego extends AbstractEntity {

    private String titulo;
    private UUID cuestionario;
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
    public UUID getCuestionario() {
        return cuestionario;
    }
    public void setCuestionario(UUID cuestionario) {
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
