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
import java.io.File;
import java.util.Optional;
import java.util.TreeMap;
import java.io.FileInputStream;
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
import com.vaadin.flow.server.StreamResource;
import gamespace.data.entity.Cuestionarios;
import gamespace.data.service.CuestionariosService;
import java.io.InputStream;
import javafx.scene.control.Alert;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.Embedded;
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
    Cuestionarios cuestionar;
    TextField promedioA = new TextField();
    TextField promedioB = new TextField();
    TextField promedioC = new TextField();
    TextField promedioD = new TextField();
    TextField promedioE = new TextField();

    AuthenticatedUser authenticatedUser;

    public VideojuegosViewCard(String titulo,
            String cover,
            String descripcion,
            String felan,
            String cuestionario,
            String ardesc,
            boolean isAuth,
            CuestionariosService cuestionarioService) {
        this.isAuth = isAuth;
        this.image.setSrc(cover);
        this.image.setAlt("ssssssssssssssssss");
        this.header.setText(titulo);
        this.subtitle.setText(felan);
        this.text.setText(descripcion);
        this.cuestionarioService = cuestionarioService;//-------cuestionarios
        List<Cuestionarios> cuestio = cuestionarioService.todosCuest();
        cuestionar = new Cuestionarios();
        for (int i = 0; i < cuestio.size(); i++) {
            if (cuestio.get(i).getJuego().equals(titulo)) {
//                Notification.show(cuestio.get(i).getJuego() + "");
                cuestionar = cuestio.get(i);
            }
        }
        //-----------------------------Boton de cuestionario
        Button cuest = new Button("Cuestionario");
        if (cuestionar.getCriterio1()==null) {
            cuest.setVisible(false);
        }
        cuest.addClickListener(e -> {
            //ver.addClassName(className);
            Dialog dialog = new Dialog();
            dialog.getElement()
                    .setAttribute("aria-label", "ccccccccccccccccccccc");
            dialog.open();
            VerticalLayout dialogLayout = createDialogLayout(dialog, titulo, cuestionar);
            dialog.add(dialogLayout);
        });
        //----------------------------------------------------------------------------------------------Boton de descargar
        Button desc = new Button("Descargar", e -> {
//            Notification.show(ardesc);
            StreamResource source = new StreamResource(titulo, () -> {
                String path = ardesc;
                File initialFile = new File(path);
                InputStream targetStream;
                try {
                    targetStream = new FileInputStream(initialFile);
                    return targetStream;
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(VideojuegosViewCard.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            });
        });

        if (isAuth) {
            badge.add(desc);
            badge.add(cuest);
        } else {
            badge.setText("Debe Autenticarse");
        };
    }

    private VerticalLayout createDialogLayout(Dialog dialog, String titulo, Cuestionarios cuestionar) {
        H2 headline = new H2("Videojuego: " + titulo);
        headline.getStyle().set("margin", "var(--lumo-space-m) 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
        Header header = new Header(headline);
        Paragraph paragraph = new Paragraph("Cuestionario: " + cuestionar.getDescripcion());
        //Notification.show(cuestio.getDescripcion());
        VerticalLayout scrollContent = new VerticalLayout(paragraph, cuestxjuego(cuestionar));
//        promedioA.setInvalid(false);
//        promedioA.setErrorMessage("Campo vacio");
//        promedioB.setInvalid(false);
//        promedioB.setErrorMessage("Campo vacio");
//        promedioC.setInvalid(false);
//        promedioC.setErrorMessage("Campo vacio");
//        promedioD.setInvalid(false);
//        promedioD.setErrorMessage("Campo vacio");
//        promedioE.setInvalid(false);
//        promedioE.setErrorMessage("Campo vacio");
        Scroller scroller = new Scroller(scrollContent);
        Button closeButton = new Button("Cerrar");
        closeButton.addClickListener(e -> dialog.close());
        Button saveButton = new Button("Guardar");
        if (promedioA.isEmpty() && promedioB.isEmpty() && promedioC.isEmpty() && promedioD.isEmpty() && promedioE.isEmpty()) {
            saveButton.setEnabled(true);
        }
//        if (promedioA.isInvalid()||promedioB.isInvalid()||promedioC.isInvalid()||promedioD.isInvalid()||promedioE.isInvalid()) {
//             saveButton.setEnabled(false);
//        }
        closeButton.addClickListener(e -> {
            cuestionario.setCriterio1(promedioA.getValue());
            cuestionario.setCriterio2(promedioB.getValue());
            cuestionario.setCriterio2(promedioC.getValue());
            cuestionario.setCriterio2(promedioD.getValue());
            cuestionario.setCriterio2(promedioE.getValue());
            cuestionario.setCriterio1(cuestionar.getCriterio1());
            cuestionario.setCriterio2(cuestionar.getCriterio2());
            cuestionario.setCriterio3(cuestionar.getCriterio3());
            cuestionario.setCriterio4(cuestionar.getCriterio4());
            cuestionario.setCriterio5(cuestionar.getCriterio5());
            cuestionario.setDescripcion(cuestionar.getDescripcion());
            cuestionario.setJuego(cuestionar.getJuego());
            cuestionario.setUsuario(authenticatedUser.get().get().getUsername());
            cuestionario.setFecha(now());
            cuestionarioService.update(cuestionario);
            dialog.close();
        });

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

    private VerticalLayout cuestxjuego(Cuestionarios cuestionar) {
        H3 personalInformationTitle = new H3("Criterios a responder: Llene los campos con valores del 1 al 100 dependiendo de los criterios(100 valor máximo)");
        personalInformationTitle.setId("personal-title");
        promedioA.setLabel(cuestionar.getCriterio1());
        promedioB.setLabel(cuestionar.getCriterio2());
        promedioC.setLabel(cuestionar.getCriterio3());
        promedioD.setLabel(cuestionar.getCriterio4());
        promedioE.setLabel(cuestionar.getCriterio5());
        VerticalLayout section = new VerticalLayout(personalInformationTitle,
                promedioA, promedioB, promedioC, promedioD, promedioE);
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
