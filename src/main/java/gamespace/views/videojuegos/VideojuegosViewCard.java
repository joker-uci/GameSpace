package gamespace.views.videojuegos;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import gamespace.data.entity.User;
import gamespace.security.AuthenticatedUser;
import gamespace.views.MainLayout;
import gamespace.views.administrarcuenta.AdministrarCuentaView;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import gamespace.data.entity.Cuestionarios;
import gamespace.data.service.CuestionariosService;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;

@JsModule("./views/videojuegos/videojuegos-view-card.ts")
@Tag("videojuegos-view-card")
public class VideojuegosViewCard extends LitTemplate {

    @Id
    private Image image;

    @Id
    private Span header;

    @Id
    private Span subtitle;

    @Id
    private Paragraph text;

    @Id
    private Span badge;

    private Map<String, Class<?>> viewByName;

    boolean isAuth;
    private Cuestionarios cuestionario;

    private CuestionariosService cuestionarioService;

    public VideojuegosViewCard(String titulo, String cover, String descripcion, String felan, String cuestionario, String ardesc, boolean isAuth, CuestionariosService cuestionarioService) {
        this.isAuth = isAuth;
        this.image.setSrc(cover);
        this.image.setAlt("ssssssssssssssssss");
        this.header.setText(titulo);
        this.subtitle.setText(felan);
        this.text.setText(descripcion);
        this.cuestionarioService = cuestionarioService;//-------cuestionarios
        //Cuestionarios cuestio = this.cuestionarioService.findByVideoj(titulo);
        Notification.show(this.cuestionarioService.findByVideoj(titulo).getDescripcion());
        //-----------------------------Boton de cuestionario
        Button cuest = new Button("Cuestionario", e -> {
            //ver.addClassName(className);
            Dialog dialog = new Dialog();
            dialog.getElement()
                    .setAttribute("aria-label", "ccccccccccccccccccccc");
            dialog.open();
            VerticalLayout dialogLayout = createDialogLayout(dialog, titulo/*, cuestio*/);
            dialog.add(dialogLayout);
        });
        //-----------------------------Boton de descargar
        Button desc = new Button("Descargar", e -> {
            Notification.show(ardesc);
        });

        if (isAuth) {
            badge.add(desc);
            badge.add(cuest);
        } else {
            badge.setText("Debe Autenticarse");
        };
    }

    private static VerticalLayout createDialogLayout(Dialog dialog, String titulo/*, Cuestionarios cuestio*/) {
        H2 headline = new H2("Videojuego: " + titulo);
        headline.getStyle().set("margin", "var(--lumo-space-m) 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
        Header header = new Header(headline);
        Paragraph paragraph = new Paragraph("Cuestionario:" + "\n" /*+ cuestio.getDescripcion()*/);
        //Notification.show(cuestio.getDescripcion());
        VerticalLayout scrollContent = new VerticalLayout(paragraph, cuestxjuego());

        Scroller scroller = new Scroller(scrollContent);

        Button closeButton = new Button("Cerrar");
        closeButton.addClickListener(e -> dialog.close());

        Button saveButton = new Button("Guardar");
        //closeButton.addClickListener(e -> dialog.close());
        
        
        Footer footer = new Footer();
        HorizontalLayout buttonLayout = new HorizontalLayout(closeButton,
                saveButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.add(buttonLayout);
        VerticalLayout dialogLayout = new VerticalLayout(header, scroller,
                footer);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        //dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        dialogLayout.getStyle().remove("width");
        dialogLayout.setAlignSelf(FlexComponent.Alignment.STRETCH, saveButton, closeButton);
        dialogLayout.setClassName("dialog-no-padding-example-overlay");
        return dialogLayout;
    }

    private void initial() {
        viewByName = new TreeMap<>();
        viewByName.put("Editar Cuenta", AdministrarCuentaView.class);
    }

    private static VerticalLayout cuestxjuego() {
        H3 personalInformationTitle = new H3("Personal information");
        personalInformationTitle.setId("personal-title");
        TextField firstNameField = new TextField("First name");
        TextField lastNameField = new TextField("Last name");
        DatePicker birthdatePicker = new DatePicker("Birthdate");
        //birthdatePicker.setInitialPosition(LocalDate.of(1990, 1, 1));

        VerticalLayout section = new VerticalLayout(personalInformationTitle,
                firstNameField, lastNameField, birthdatePicker);
        section.setPadding(false);
        section.setSpacing(false);
        section.setAlignItems(FlexComponent.Alignment.STRETCH);
        section.getElement().setAttribute("role", "region");
        section.getElement().setAttribute("aria-labelledby",
                personalInformationTitle.getId().get());

        return section;
    }
//    private static VerticalLayout createEmployeeInformationSection() {
//        H3 employmentInformationTitle = new H3("Employment information");
//        employmentInformationTitle.setId("employment-title");
//
//        TextField positionField = new TextField("Position");
//        TextArea informationArea = new TextArea("Additional information");
//
//        VerticalLayout section = new VerticalLayout(employmentInformationTitle,
//                positionField, informationArea);
//        section.setPadding(false);
//        section.setSpacing(false);
//        section.setAlignItems(FlexComponent.Alignment.STRETCH);
//        section.getElement().setAttribute("role", "region");
//        section.getElement().setAttribute("aria-labelledby",
//                employmentInformationTitle.getId().get());
//
//        return section;
//    }
}
