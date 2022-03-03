package gamespace.views.login;

//import com.example.application.data.entity.User;
//import com.example.application.data.service.UserRepository;
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
import gamespace.data.entity.User;
import gamespace.data.service.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

        
@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {
UserRepository userRepository;
TextField username;
TextField name;
PasswordField password;
PasswordEncoder passwordEncoder;

    public LoginView( UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository= userRepository;
        this.passwordEncoder= passwordEncoder;
        setAction("login");
        LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("GameSpace"+userRepository.count());
        i18n.getHeader().setDescription("Autenticarse usando usuario y contraseña");
        i18n.setAdditionalInformation(null);
        i18n.getForm().setForgotPassword("Registrarse");
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Create new employee");

        VerticalLayout dialogLayout = createDialogLayout(dialog);
        dialog.add(dialogLayout);

        this.addForgotPasswordListener(event -> {
            dialog.open();
            // this.close();
            //codigo de regstrar usuario

        });
        setI18n(i18n);

        setOpened(true);
    }
    
    private VerticalLayout createDialogLayout(Dialog dialog) {
        H2 headline = new H2("Crear nueva cuenta");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        name = new TextField("First name");
        //TextField lastName = new TextField("Last name");
        username = new TextField("Username");
        password = new PasswordField("Password");
        password.setHelperText("A password must be at least 8 characters. It has to have at least one letter and one digit.");
        password.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        password.setErrorMessage("Not a valid password");
        PasswordField confirmPassword = new PasswordField("Confirm password");
        confirmPassword.setHelperText("A password must be at least 8 characters. It has to have at least one letter and one digit.");
        confirmPassword.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        confirmPassword.setErrorMessage("Not a valid password");
        FormLayout formLayout = new FormLayout();
        formLayout.add(
                name, /*lastName,*/
                username,
                password, confirmPassword
        );
formLayout.setResponsiveSteps(
                // Use one column by default
                new ResponsiveStep("0", 1),
                // Use two columns, if layout's width exceeds 500px
                new ResponsiveStep("500px", 2)
        );
        // Stretch the username field over 2 columns
        formLayout.setColspan(username, 2);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        Button saveButton = new Button("Save", e ->{
        User user = new User();
        user.setName(name.getValue());
        user.setUsername(username.getValue());
        
        user.setHashedPassword(passwordEncoder.encode(password.getValue()));
        userRepository.save(user);
        dialog.close();
        });//validar
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
