package gamespace.views.login;

//import com.example.application.data.entity.User;
//import com.example.application.data.service.UserRepository;
import com.sun.java.swing.plaf.windows.resources.windows;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.login.LoginI18n.ErrorMessage;
import com.vaadin.flow.component.notification.Notification;
import gamespace.data.entity.User;
import gamespace.data.service.UserRepository;
import static java.awt.SystemColor.window;
import org.springframework.security.crypto.password.PasswordEncoder;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {

    UserRepository userRepository;
    TextField username;
    TextField name;
    PasswordField password;
    PasswordEncoder passwordEncoder;
    PasswordField confirmPassword;

    public LoginView(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        setAction("login");
        LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("GameSpace" + userRepository.count());
        i18n.getHeader().setDescription("Autenticarse usando usuario y contraseña");
        i18n.setAdditionalInformation(null);
        i18n.getForm().setForgotPassword("Registrarse");
        i18n.getForm().setTitle("Autenticarse");
        i18n.getForm().setUsername("Usuario:");
        i18n.getForm().setPassword("Contraseña:");
//        this.addLoginListener((t) -> {
//            System.out.println("Usuario:" + t.getUsername());
//            System.out.println("Contraseña:" + t.getPassword());
//        });
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Registrarse");

        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        this.addForgotPasswordListener(event -> {
            dialog.open();
            // this.close();
            //codigo de regstrar usuario

        });
//        if (i18n.getForm().getUsername().equals(userRepository.findByUsername(i18n.getForm().getUsername()))) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setTitle("Error");
        errorMessage.setMessage("Usuario no existente o  contraseña incorrecta");
        i18n.setErrorMessage(errorMessage);
//        Notification.show("Usuario no existente");
        //Notification.show(System.getProperties().getProperty("user.dir"));
        //Notification.show(this.getClass().getResource("/").getPath());
//        Notification.show(window.getClass().getResource(" ")/*.getPath()*/+"");
        
//        } 
        setI18n(i18n);

        setOpened(true);

    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
        H2 headline = new H2("Crear nueva cuenta");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        name = new TextField("Nombre");
        //TextField lastName = new TextField("Last name");
        username = new TextField("Usuario");
        password = new PasswordField("Contraseña");
        password.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        confirmPassword = new PasswordField("Confirmar contraseña");
        password.setHelperText("Debe tener como mínimo 8 caracteres, una letra y un símbolo");
        password.setErrorMessage("No es válido");
        FormLayout formLayout = new FormLayout();
        formLayout.add(name, username, password, confirmPassword);
        formLayout.setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("500px", 2)
        );
        // Stretch the username field over 2 columns
        formLayout.setColspan(username, 2);
        Button cancelButton = new Button("Cancel", e -> {
            password.setInvalid(false);
            name.setInvalid(false);
            username.setInvalid(false);
            confirmPassword.setInvalid(false);
            password.setErrorMessage("No es válido");

            name.clear();
            username.clear();
            password.clear();
            confirmPassword.clear();
            password.setInvalid(false);
            dialog.close();
            password.setInvalid(false);
        });
        Button saveButton;//validar
        saveButton = new Button("Save", e -> {
            User user = new User();
            user.setName(name.getValue());
            user.setUsername(username.getValue());
            User pusuario = new User();
            user.setHashedPassword(passwordEncoder.encode(password.getValue()));
            for (int i = 0; i < userRepository.count(); i++) {
                //pusuario=userRepository.findByUsername(username.getValue());
                pusuario = userRepository.findAll().get(i);
                if (pusuario.getUsername().equals(username.getValue())) {
                    username.setInvalid(true);
                    username.setErrorMessage("Usuario ya existe");
                };
            };
            if (name.isEmpty()) {
                name.setInvalid(true);
                name.setErrorMessage("Campo vacio");
            };
            if (!confirmPassword.getValue().equals(password.getValue())) {
                confirmPassword.setErrorMessage("No coincide con el campo contraseña");
                confirmPassword.setInvalid(true);
                password.setErrorMessage("No coincide con el campo confirmar contraseña");
                password.setInvalid(true);
            }

            if (password.isEmpty()) {
                password.setInvalid(true);
                password.setErrorMessage("Campo vacio");
            };

            if (confirmPassword.isEmpty()) {
                confirmPassword.setInvalid(true);
                confirmPassword.setErrorMessage("Campo vacio");
            };

            if (username.isEmpty()) {
                username.setInvalid(true);
                username.setErrorMessage("Campo vacio");
            };
            if (name.isInvalid() || username.isInvalid() || password.isInvalid() || confirmPassword.isInvalid()) {
            } else {
                Notification.show(confirmPassword.isInvalid() + confirmPassword.getValue() + "" + password.isInvalid() + password.getValue() + "" + name.isInvalid() + "" + username.isInvalid());
                userRepository.save(user);

                password.setInvalid(false);
                name.setInvalid(false);
                username.setInvalid(false);
                confirmPassword.setInvalid(false);

                password.setErrorMessage("No es válido");

                name.clear();
                username.clear();
                password.clear();
                confirmPassword.clear();
                password.setInvalid(false);
                dialog.close();
                password.setInvalid(false);

                dialog.close();
            };

        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton,
                saveButton);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(headline, formLayout,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }

}
