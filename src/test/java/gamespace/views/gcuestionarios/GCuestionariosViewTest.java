/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamespace.views.gcuestionarios;

import com.vaadin.flow.router.BeforeEnterEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gamespace.data.Role;
import gamespace.data.entity.Cuestionarios;
import gamespace.data.entity.User;
import gamespace.data.entity.Usuario;
import gamespace.data.service.CuestionariosService;
import gamespace.data.service.UserRepository;
import gamespace.data.service.UserService;
import gamespace.security.SecurityConfiguration;
import java.util.Collections;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author JoKeR
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GCuestionariosViewTest {

    Cuestionarios cuestionario;
    @Autowired
    CuestionariosService cuestionarioService;

    public GCuestionariosViewTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        cuestionario = new Cuestionarios();
        cuestionario.setCriterio1("asdfg");
        cuestionario.setCriterio2("asbdgfh");
        cuestionario.setCriterio3("davsmyh");
        cuestionario.setCriterio4("awfbtui");
        cuestionario.setCriterio5("GRTRTHG");
        cuestionario.setDescripcion("edrtgyu");
        cuestionario.setFecha(cuestionarioService.todosCuest().get(1).getFecha());
        cuestionario.setJuego("weetyumytnrbr");
        cuestionario.setPromedio1(1);
        cuestionario.setPromedio2(2);
        cuestionario.setProedio3(3);
        cuestionario.setPromedio4(4);
        cuestionario.setPromedio5(5);
        cuestionario.setUsuario("ewrtyuior");
        cuestionarioService.update(cuestionario);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of beforeEnter method, of class GCuestionariosView.
     */
    @Test
    public void testBeforeEnter() {
        boolean expResult = true;
        boolean result = cuestionarioService.findByVideoj(cuestionario.getJuego()).equals(cuestionario);
        assertEquals(expResult, result);
    }

}
