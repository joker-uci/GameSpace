package gamespace.data.service;

import gamespace.data.entity.Noticias;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticiasRepository extends JpaRepository<Noticias, UUID> {

}