package gamespace.data.service;

import gamespace.data.entity.Cuestionarios;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuestionariosRepository extends JpaRepository<Cuestionarios, UUID> {

}