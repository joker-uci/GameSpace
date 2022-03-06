package gamespace.views.administrarcuenta;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gamespace.data.entity.SamplePerson;
import gamespace.data.entity.User;
import gamespace.data.service.SamplePersonService;
import gamespace.data.service.UserRepository;
import gamespace.security.AuthenticatedUser;
import gamespace.views.MainLayout;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@PageTitle("Editar Cuenta")
@Route(value = "Administrar-Cuenta", layout = MainLayout.class)
@RolesAllowed("user")
@Uses(Icon.class)
public class AdministrarCuentaView extends Div {

    private TextField firstName = new TextField("Nombre");
    private Text lastName = new Text("Usuario : " + "");
    private PasswordField password = new PasswordField("Contraseña");

    private PasswordField confirmPassword = new PasswordField("Confirmar contraseña");
    private Button cancel = new Button("Cancelar");
    private Button save = new Button("Guardar");
    private Button eliminar = new Button("Eliminar cuenta");
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    private AuthenticatedUser authenticatedUser;

    private Binder<SamplePerson> binder = new Binder(SamplePerson.class);

    public AdministrarCuentaView(SamplePersonService personService, AuthenticatedUser authenticatedUser, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUser = authenticatedUser;
        addClassName("administrar-cuenta-view");
        password.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        password.setHelperText("Debe tener como mínimo 8 caracteres, una letra y un símbolo");
        password.setErrorMessage("No es válido");
        User maybeUser = userRepository.findById(authenticatedUser.get().get().getId()).get();

        add(createTitle());
        add(createFormLayout(maybeUser));
        add(createButtonLayout());

        binder.bindInstanceFields(this);
        cancel.addClickListener(e -> {
            confirmPassword.setInvalid(false);
            password.setInvalid(false);
            firstName.setInvalid(false);
            clearForm();
        });

        save.addClickListener(e -> {
            maybeUser.setName(this.firstName.getValue());
            if (!confirmPassword.getValue().equals(password.getValue())) {
                confirmPassword.setErrorMessage("No coincide con el campo contraseña");
                confirmPassword.setInvalid(true);
                password.setErrorMessage("No coincide con el campo confirmar contraseña");
                password.setInvalid(true);
            };
            if (firstName.isEmpty()) {
                firstName.setInvalid(true);
                firstName.setErrorMessage("Campo vacio");
            };
            if (!password.isInvalid() || !confirmPassword.isInvalid() || !firstName.isInvalid()) {
                this.userRepository.save(maybeUser);
                UI.getCurrent().getPage().reload();
            Notification.show("Modificaciones guardadas");
            }
        });

        eliminar.addClickListener(e -> {
            Dialog dialog = new Dialog();
            dialog.getElement().setAttribute("aria-label", "Create new employee");
            VerticalLayout dialogLayout = createDialogLayout(dialog, maybeUser, this.firstName);
            dialog.add(dialogLayout);
            dialog.open();
        });
    }

    private void clearForm() {
        this.firstName.setValue(this.userRepository.findById(authenticatedUser.get().get().getId()).get().getName());
        this.password.clear();
        this.confirmPassword.clear();
    }

    private Component createTitle() {
        return new H3("Editar Cuenta");
    }

    private Component createFormLayout(User maybeUser) {
        FormLayout formLayout = new FormLayout();
        this.lastName.setText("Usuario : " + maybeUser.getUsername());
        this.firstName.setValue(maybeUser.getName());
        formLayout.add(firstName, lastName, password, confirmPassword);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        eliminar.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        buttonLayout.add(eliminar);
        return buttonLayout;
    }

    private VerticalLayout createDialogLayout(Dialog dialog, User maybeUser, TextField firstName) {
        H1 headline = new H1("¡Precaución!");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
        H4 texto = new H4("¿Está usted seguro que desea eliminar?");
        VerticalLayout fieldLayout = new VerticalLayout(texto);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Button aceptar = new Button("Aceptar", e -> {
            //maybeUser.setHashedPassword("admin");//--------no elimina el usuario actual
            this.userRepository.delete(maybeUser);
            this.authenticatedUser.logout();
            dialog.close();
        });
        Button cancelar = new Button("Cancelar", e -> dialog.close());
        cancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(aceptar,
                cancelar);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        return dialogLayout;
    }

}
