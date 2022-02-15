package gamespace.data.service;

import gamespace.data.entity.Videojuego;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideojuegoRepository extends JpaRepository<Videojuego, UUID> {

}