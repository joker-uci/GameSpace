package gamespace.views.noticias;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import gamespace.data.service.NoticiasService;
import gamespace.views.MainLayout;
import gamespace.data.entity.Noticias;
import org.checkerframework.checker.units.qual.mol;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import gamespace.data.service.NoticiasRepository;

@PageTitle("Noticias")
@Route(value = "Noticias", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
@Tag("noticias-view")
@JsModule("./views/noticias/noticias-view.ts")
public class NoticiasView extends LitTemplate implements HasComponents, HasStyle {

    @Id
    private Select<String> sortBy;
    private Noticias noticias;
    private NoticiasService noticiasService;
    TextField filterText = new TextField();
    Header buscar = new Header();

    public NoticiasView(@Autowired NoticiasService noticiasService) {
        addClassNames("noticias-view", "flex", "flex-col", "h-full");
        sortBy.setItems("Reciente primero", "Antiguos primero");
        sortBy.setValue("Reciente primero");
        this.noticiasService = noticiasService;
        noticias = noticiasService.list().get(0);
        buscar.add(getToolbar());
        add(buscar);
        for (Noticias noticia : noticiasService.list()) {
            add(Card(noticia.getTitulo(), noticia.getAutor(), noticia.getFeHoPublicacion().toString(),
                    noticia.getResumen(), noticia.getContenido()));
        }

    }

    Div Card(String titulo, String autor, String feho, String resumen, String contenido) {
        Div div = new Div();
        div.setClassName("bg-contrast-5 flex flex-col items-start p-m rounded-l");

        Div dtitulo = new Div();
        Label tit = new Label();
        tit.setText(titulo);
        tit.setClassName("text-xl font-semibold");
        dtitulo.add(tit);

        Div dayf = new Div();
        dayf.setClassName("text-s text-secondary");
        Label aut = new Label();
        Label fecha = new Label();
        Label a = new Label();
        aut.setText(autor);
        fecha.setText(feho);
        a.setText(" - ");
        dayf.add(autor);
        dayf.add(a);
        dayf.add(fecha);
        //titulo.addClassName(className);

        Div dresumen = new Div();
        Label resu = new Label();
        resu.setText(resumen);
        resu.setClassName("my-m");
        dresumen.add(resu);

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
        div.add(dayf);
        div.add(dresumen);
        div.add(ver);
        return div;
    }

    /* private void For(int i, boolean b, int i0) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
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

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Criterio");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addContactButton = new Button(new Icon(VaadinIcon.SEARCH));
        addContactButton.addClickListener(click -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        return toolbar;
    }

    private void updateList() {

        //grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    void addContact() {
        if (filterText.getValue() != null) {
            removeAll();
            add(buscar);
            for (Noticias noticia : noticiasService.findAllNoticias(filterText.getValue())) {
                add(Card(noticia.getTitulo(), noticia.getAutor(), noticia.getFeHoPublicacion().toString(),
                        noticia.getResumen(), noticia.getContenido()));
            }
        } else {
            removeAll();
            add(buscar);
            for (Noticias noticia : noticiasService.list()) {
                add(Card(noticia.getTitulo(), noticia.getAutor(), noticia.getFeHoPublicacion().toString(),
                        noticia.getResumen(), noticia.getContenido()));
            }
        }

//        grid.asSingleSelect().clear();
//        editContact(new Contact());
    }
}
