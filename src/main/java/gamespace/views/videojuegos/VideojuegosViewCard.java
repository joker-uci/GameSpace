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

    public VideojuegosViewCard(String titulo, String cover, String descripcion, String felan, String cuestionario, String ardesc, boolean isAuth) {
        this.isAuth = isAuth;
        this.image.setSrc(cover);
        this.image.setAlt("ssssssssssssssssss");
        this.header.setText(titulo);
        this.subtitle.setText(felan);
        this.text.setText(descripcion);

        //-----------------------------Boton de cuestionario
        Button cuest = new Button("Cuestionario", e -> {
            //ver.addClassName(className);
            Dialog dialog = new Dialog();
            dialog.getElement()
                    .setAttribute("aria-label", "ccccccccccccccccccccc");
            dialog.open();
            VerticalLayout dialogLayout = createDialogLayout(dialog, cuestionario, "bbbbbbbbbbbbb");
            dialog.add(dialogLayout);
        });
        //-----------------------------Boton de descargar
        Button desc = new Button("Descargar", e -> {
            Notification.show(ardesc);
        });
        
        if (isAuth) {
            badge.add(desc);
            badge.add(cuest);
            }else{badge.setText("Debe Autenticarse");};
    }

    private static VerticalLayout createDialogLayout(Dialog dialog, String titulo, String contenido) {
        H2 headline = new H2(titulo);
        headline.getStyle().set("margin", "var(--lumo-space-m) 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        Paragraph paragraph = new Paragraph(contenido);

        Button closeButton = new Button("Cerrar");
        closeButton.addClickListener(e -> dialog.close());

        Button saveButton = new Button("Guardar");
        //closeButton.addClickListener(e -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(headline, paragraph,
                saveButton, closeButton);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        dialogLayout.setAlignSelf(FlexComponent.Alignment.END, saveButton, closeButton);

        return dialogLayout;
    }

    private void initial() {
        viewByName = new TreeMap<>();
        viewByName.put("Editar Cuenta", AdministrarCuentaView.class);
    }
}
