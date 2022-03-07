/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamespace.views.login;

import gamespace.data.Role;
import gamespace.data.entity.User;
import gamespace.data.entity.Usuario;
import gamespace.data.service.UserRepository;
import gamespace.data.service.UserService;
import gamespace.security.SecurityConfiguration;
import java.util.Collections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class LoginViewTest {

    Usuario usuario;
    
    @Autowired
    UserService userService;
    private User user;

    public LoginViewTest() {
    }

    @Before
    public void setUpClass() {
        user = new User();
        user.setName("Virulo");
        user.setUsername("asdfdgh");
        user.setHashedPassword("user1111");
        user.setRoles(Collections.singleton(Role.USER));
        userService.update(user);
    }

    @AfterClass
    public static void tearDownClass() {
    }
    @After
    public void tearDown() {
    }

    @Test
    public void Test() {
        boolean expResult = true;
        boolean result = userService.findByUsername(user.getUsername()).equals(user);
        assertEquals(expResult, result);
    }

}
