package gamespace.data.service;

import com.vaadin.flow.component.notification.Notification;
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

    public List<Cuestionarios> todosCuest() {
        return repository.findAll();
    }

    public Cuestionarios findByVideoj(String titulo) {
        List<Cuestionarios> cuestio = todosCuest();
//        for (Cuestionarios cuestionario : cuestio) {
//            if (cuestionario.getJuego().equals(titulo)) {
//                return cuestionario;
//            }
//        };
        for (int i = 0; i < cuestio.size(); i++) {
            Notification.show(cuestio.get(i).getJuego() + "");
        }
//        for (int i = 0; i < cuest.size(); i++) {
//            cuestionario = (Cuestionarios) cuest.get(i);
//            if (titulo == cuestionario.getJuego()) {
//                return cuestionario;
//            }
//        }
        return null;
    }
}
