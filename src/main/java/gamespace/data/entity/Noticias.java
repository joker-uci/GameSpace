package gamespace.data.entity;

import gamespace.data.AbstractEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;

@Entity
public class Noticias extends AbstractEntity {

    private String titulo;
    private String autor;
    private LocalDateTime feHoPublicacion;
    private String resumen;
    private String contenido;

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getAutor() {
        return autor;
    }
    public void setAutor(String autor) {
        this.autor = autor;
    }
    public LocalDateTime getFeHoPublicacion() {
        return feHoPublicacion;
    }
    public void setFeHoPublicacion(LocalDateTime feHoPublicacion) {
        this.feHoPublicacion = feHoPublicacion;
    }
    public String getResumen() {
        return resumen;
    }
    public void setResumen(String resumen) {
        this.resumen = resumen;
    }
    public String getContenido() {
        return contenido;
    }
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

}
