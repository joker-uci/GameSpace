package gamespace.data.service;

import gamespace.data.entity.Cuestionarios;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CuestionariosService {

    private CuestionariosRepository repository;

    public CuestionariosService(@Autowired CuestionariosRepository repository) {
        this.repository = repository;
    }

    public Optional<Cuestionarios> get(UUID id) {
        return repository.findById(id);
    }

    public Cuestionarios update(Cuestionarios entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Cuestionarios> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List <Cuestionarios> todosCuest() {
        List Cuestionarios = new ArrayList();
        for(Cuestionarios cuestionario:repository.findAll()){
        Cuestionarios.add(cuestionario);
        };
        return Cuestionarios;
    }
    public Cuestionarios findByVideoj(String titulo) {
        List cuest = new ArrayList();
        cuest.addAll(todosCuest());
        Cuestionarios cuestionario = new Cuestionarios();
        
        for (int i = 0; i < cuest.size(); i++) {
            cuestionario = (Cuestionarios) cuest.get(i);
            if (titulo == cuestionario.getJuego()) {
                return cuestionario;
            }
        }
        return null;
    }
}
