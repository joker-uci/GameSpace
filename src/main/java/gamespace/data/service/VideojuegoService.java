package gamespace.data.service;

import gamespace.data.entity.Noticias;
import gamespace.data.entity.Videojuego;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VideojuegoService {

    private VideojuegoRepository repository;

    public VideojuegoService(@Autowired VideojuegoRepository repository) {
        this.repository = repository;
    }

    public Optional<Videojuego> get(UUID id) {
        return repository.findById(id);
    }

    public Videojuego update(Videojuego entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Videojuego> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public List<Videojuego> list() {
        return repository.findAll();
    }
    public int count() {
        return (int) repository.count();
    }

}
