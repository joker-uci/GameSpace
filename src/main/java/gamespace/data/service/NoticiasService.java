package gamespace.data.service;

import gamespace.data.entity.Noticias;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NoticiasService {

    private NoticiasRepository repository;

    public NoticiasService(@Autowired NoticiasRepository repository) {
        this.repository = repository;
    }

    public Optional<Noticias> get(UUID id) {
        return repository.findById(id);
    }

    public Noticias update(Noticias entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Noticias> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Noticias> list() {
        return repository.findAll();
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Noticias> findAllNoticias(String stringFilter) {
        List<Noticias> lista = new ArrayList();
        if (stringFilter == null || stringFilter.isEmpty()) {
            return repository.findAll();
        } else {
            for (Noticias noticia : repository.findAll()) {
                if (noticia.getAutor().equals(stringFilter)||noticia.getContenido().equals(stringFilter)||noticia.getFeHoPublicacion().toString().equals(stringFilter)||noticia.getResumen().equals(stringFilter)||noticia.getTitulo().equals(stringFilter)) {
                    lista.add(noticia);
                }
            }
             return lista;
        }
    }

}
