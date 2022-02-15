package gamespace.data.generator;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import gamespace.data.Role;
import gamespace.data.entity.Cuestionarios;
import gamespace.data.entity.Noticias;
import gamespace.data.entity.User;
import gamespace.data.entity.Usuario;
import gamespace.data.entity.Videojuego;
import gamespace.data.service.CuestionariosRepository;
import gamespace.data.service.NoticiasRepository;
import gamespace.data.service.UserRepository;
import gamespace.data.service.UsuarioRepository;
import gamespace.data.service.VideojuegoRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(PasswordEncoder passwordEncoder, UserRepository userRepository,
            UsuarioRepository usuarioRepository, NoticiasRepository noticiasRepository,
            CuestionariosRepository cuestionariosRepository, VideojuegoRepository videojuegoRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (userRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 2 User entities...");
            User user = new User();
            user.setName("John Normal");
            user.setUsername("user");
            user.setHashedPassword(passwordEncoder.encode("user"));
            user.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            user.setRoles(Collections.singleton(Role.USER));
            userRepository.save(user);
            User admin = new User();
            admin.setName("Emma Powerful");
            admin.setUsername("admin");
            admin.setHashedPassword(passwordEncoder.encode("admin"));
            admin.setProfilePictureUrl(
                    "https://images.unsplash.com/photo-1607746882042-944635dfe10e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=128&h=128&q=80");
            admin.setRoles(Stream.of(Role.USER, Role.ADMIN).collect(Collectors.toSet()));
            userRepository.save(admin);
            logger.info("... generating 100 Usuario entities...");
            ExampleDataGenerator<Usuario> usuarioRepositoryGenerator = new ExampleDataGenerator<>(Usuario.class,
                    LocalDateTime.of(2022, 2, 15, 0, 0, 0));
            usuarioRepositoryGenerator.setData(Usuario::setFirstName, DataType.FIRST_NAME);
            usuarioRepositoryGenerator.setData(Usuario::setUsuario, DataType.WORD);
            usuarioRepositoryGenerator.setData(Usuario::setContrasenna, DataType.TWO_WORDS);
            usuarioRepositoryGenerator.setData(Usuario::setRol, DataType.WORD);
            usuarioRepository.saveAll(usuarioRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Noticias entities...");
            ExampleDataGenerator<Noticias> noticiasRepositoryGenerator = new ExampleDataGenerator<>(Noticias.class,
                    LocalDateTime.of(2022, 2, 15, 0, 0, 0));
            noticiasRepositoryGenerator.setData(Noticias::setTitulo, DataType.SENTENCE);
            noticiasRepositoryGenerator.setData(Noticias::setAutor, DataType.FIRST_NAME);
            noticiasRepositoryGenerator.setData(Noticias::setFeHoPublicacion, DataType.DATETIME_LAST_10_YEARS);
            noticiasRepositoryGenerator.setData(Noticias::setResumen, DataType.SENTENCE);
            noticiasRepositoryGenerator.setData(Noticias::setContenido, DataType.SENTENCE);
            noticiasRepository.saveAll(noticiasRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Cuestionarios entities...");
            ExampleDataGenerator<Cuestionarios> cuestionariosRepositoryGenerator = new ExampleDataGenerator<>(
                    Cuestionarios.class, LocalDateTime.of(2022, 2, 15, 0, 0, 0));
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setCuestionario, DataType.UUID);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setJuego, DataType.WORD);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setDescripcion, DataType.SENTENCE);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setFecha, DataType.DATE_OF_BIRTH);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setUsuario, DataType.SENTENCE);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setCriterio1, DataType.SENTENCE);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setPromedio1, DataType.NUMBER_UP_TO_100);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setCriterio2, DataType.SENTENCE);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setPromedio2, DataType.NUMBER_UP_TO_100);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setCriterio3, DataType.SENTENCE);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setProedio3, DataType.NUMBER_UP_TO_100);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setCriterio4, DataType.SENTENCE);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setPromedio4, DataType.NUMBER_UP_TO_100);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setCriterio5, DataType.SENTENCE);
            cuestionariosRepositoryGenerator.setData(Cuestionarios::setPromedio5, DataType.NUMBER_UP_TO_100);
            cuestionariosRepository.saveAll(cuestionariosRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Videojuego entities...");
            ExampleDataGenerator<Videojuego> videojuegoRepositoryGenerator = new ExampleDataGenerator<>(
                    Videojuego.class, LocalDateTime.of(2022, 2, 15, 0, 0, 0));
            videojuegoRepositoryGenerator.setData(Videojuego::setTitulo, DataType.FIRST_NAME);
            videojuegoRepositoryGenerator.setData(Videojuego::setCuestionario, DataType.UUID);
            videojuegoRepositoryGenerator.setData(Videojuego::setDescrpcion, DataType.SENTENCE);
            videojuegoRepositoryGenerator.setData(Videojuego::setFechaLanzamiento, DataType.DATE_OF_BIRTH);
            videojuegoRepositoryGenerator.setData(Videojuego::setCover, DataType.BOOK_IMAGE_URL);
            videojuegoRepositoryGenerator.setData(Videojuego::setArchDescarga, DataType.PROFILE_PICTURE_URL);
            videojuegoRepository.saveAll(videojuegoRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}