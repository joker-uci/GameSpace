package gamespace.views.videojuegos;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import gamespace.data.entity.Cuestionarios;
import gamespace.data.entity.Noticias;
import gamespace.data.entity.Videojuego;
import gamespace.data.service.CuestionariosService;
import gamespace.data.service.VideojuegoService;
import gamespace.security.AuthenticatedUser;
import gamespace.views.MainLayout;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Videojuegos")
@Route(value = "Videojuegos", layout = MainLayout.class)
@AnonymousAllowed
@Tag("videojuegos-view")
@JsModule("./views/videojuegos/videojuegos-view.ts")
public class VideojuegosView extends LitTemplate implements HasComponents, HasStyle {

    @Id
    private Select<String> sortBy;
    private Videojuego videojuego;
    private VideojuegoService videojuegoService;
    private AuthenticatedUser authenticatedUser;
    private Cuestionarios cuestionario;
    private CuestionariosService cuestionarioService;
    TextField filterText = new TextField();
    Header buscar = new Header();

    public VideojuegosView(@Autowired VideojuegoService videojuegoService, AuthenticatedUser authenticatedUser, CuestionariosService cuestionarioService) {
        this.authenticatedUser = authenticatedUser;
        this.cuestionarioService = cuestionarioService;
        //System.out.println("authenticatedUser > " + this.authenticatedUser.get() );
        addClassNames("videojuegos-view", "flex", "flex-col", "h-full");
        sortBy.setItems("Popularidad", "Recientes primero", "Antiguos primero");
        sortBy.setValue("Popularidad");
        this.videojuegoService = videojuegoService;
        videojuego = videojuegoService.list().get(0);
        buscar.add(getToolbar(cuestionarioService));
        add(buscar);
        for (Videojuego videojuego : videojuegoService.list()) {
            add(new VideojuegosViewCard(videojuego.getTitulo(), videojuego.getCover(),
                    videojuego.getDescrpcion(), videojuego.getFechaLanzamiento().toString(),
                    videojuego.getCuestionario(), videojuego.getArchDescarga(), this.authenticatedUser.get().isPresent(), cuestionarioService));
        };
    }

    private HorizontalLayout getToolbar(CuestionariosService cuestionarioService) {
        filterText.setPlaceholder("Criterio");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        Button addContactButton = new Button(new Icon(VaadinIcon.SEARCH));
        addContactButton.addClickListener(click -> addContact(cuestionarioService));

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        toolbar.setWidthFull();
        return toolbar;
    }

    private void updateList() {

        //grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    void addContact(CuestionariosService cuestionarioService) {
        this.cuestionarioService = cuestionarioService;
        if (filterText.getValue() != null) {
            removeAll();
            add(buscar);
            for (Videojuego videoju : videojuegoService.findAllVideo(filterText.getValue())) {
                add(new VideojuegosViewCard(videoju.getTitulo(), videoju.getCover(),
                        videoju.getDescrpcion(),
                        videoju.getFechaLanzamiento().toString(),
                        videoju.getCuestionario(),
                        videoju.getArchDescarga(),
                        this.authenticatedUser.get().isPresent(),
                        cuestionarioService));
            }
        } else {
            removeAll();
            add(buscar);
            for (Videojuego videoju : videojuegoService.list()) {
                add(new VideojuegosViewCard(videoju.getTitulo(), videoju.getCover(),
                        videoju.getDescrpcion(), videoju.getFechaLanzamiento().toString(),
                        videoju.getCuestionario(), videoju.getArchDescarga(), this.authenticatedUser.get().isPresent(), cuestionarioService));
            }
        }

//        grid.asSingleSelect().clear();
//        editContact(new Contact());
    }
}
