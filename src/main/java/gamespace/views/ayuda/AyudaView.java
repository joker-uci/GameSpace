package gamespace.views.ayuda;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import gamespace.views.MainLayout;

@PageTitle("Ayuda")
@Route(value = "Ayuda", layout = MainLayout.class)
@AnonymousAllowed
@Tag("ayuda-view")
@JsModule("./views/ayuda/ayuda-view.ts")
public class AyudaView extends LitTemplate implements HasComponents, HasStyle {

//    @Id
//    private Select<String> sortBy;

    public AyudaView() {
        addClassNames("ayuda-view", "flex", "flex-col", "h-full");
        
        add(Card("¿Es necesario registrarme en el sitio? ", "No, la navegación es plena en todo el sitio, solo es necesario registrarse si el usuario desea realizar comentarios o votaciones sobre los videojuegos o artículos."));
        add(Card("¿Qué necesito para registrarme en el portal? ","Para registrarse se debe completar el formulario e introducir una contraseña. Nótese que los campos marcados con un * son obligatorios. Una vez registrado en el sitio, en lo adelante, solamente es necesario autenticarse, es decir, teclear su usuario y su contraseña en la parte superior de la página de inicio."));
        add(Card("¿Cómo puedo recuperar mi contraseña en caso de perderla? ","Para recuperar la contraseña se necesita completar el campo correo en el formulario de registro y hacer clic sobre el texto Olvidé mi contraseña, inmediatamente se le enviará un mensaje con la contraseña al correo provisto. En caso de haber realizado el registro sin correo electrónico, puede contactarnos usando los datos de la sección Contacto."));
        add(Card("¿Dónde puedo descargas los videojuegos? ","En los detalles del videojuego deseado hay una opción que se llama descarga."));
        add(Card("¿Cómo publico en el portal el resultado de mi Ranking? ","Si ya tienes una cuenta en el portal, el usuario y el pin de seguridad deben coincidir con el usuario y el pin del perfil del videojuego que estás jugando. Los videojuegos deben mostrar un botón publicar ranking en dependencia del contenido del videojuego.\n" +
"\n" +
"Si no tiene usuario creado previamente en el portal, automáticamente se creará uno, con el mismo usuario y pin del videojuego. El portal le dirá entonces cuál será su contraseña."));
        add(Card("¿Cómo colaborar con Cosmox? ","Si usted desea colaborar con noticias para enriquecer nuestro portal con los últimos acontecimientos en el área de los videojuegos, puede enviar un correo con la propuesta a la dirección vertex@uci.cu. Su noticia será analizada por un equipo de moderadores, quienes se encargarán de aprobar su publicación en el portal.")); 
    }
    
        Div Card (String titulo, String contenido){
        Div div = new Div();
        div.setClassName("bg-contrast-5 flex flex-col items-start p-m rounded-l");
        
        Div dtitulo = new Div();
         Label tit = new Label();
        tit.setText(titulo);
        tit.setClassName("text-xl font-semibold");
        dtitulo.add(tit);
        
        Button ver = new Button("Ver", e -> {
        //ver.addClassName(className);
        Dialog dialog = new Dialog();
        dialog.getElement()
                .setAttribute("aria-label", titulo);
        dialog.open();
        VerticalLayout dialogLayout = createDialogLayout(dialog, titulo, contenido);
        dialog.add(dialogLayout);
        });
        
        div.add(dtitulo);
        div.add(ver);
        return div;
}
        private static VerticalLayout createDialogLayout(Dialog dialog, String titulo, String contenido) {
        H2 headline = new H2(titulo);
        headline.getStyle().set("margin", "var(--lumo-space-m) 0")
                .set("font-size", "1.5em").set("font-weight", "bold");

        Paragraph paragraph = new Paragraph(contenido);

        Button closeButton = new Button("Cerrar");
        closeButton.addClickListener(e -> dialog.close());

        VerticalLayout dialogLayout = new VerticalLayout(headline, paragraph,
                closeButton);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        dialogLayout.setAlignSelf(FlexComponent.Alignment.END, closeButton);

        return dialogLayout;
    }
}