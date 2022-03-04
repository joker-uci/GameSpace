package gamespace.views.videojuegos;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.template.Id;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import gamespace.data.entity.Videojuego;
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

    public VideojuegosView(@Autowired VideojuegoService videojuegoService, AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        //System.out.println("authenticatedUser > " + this.authenticatedUser.get() );
        addClassNames("videojuegos-view", "flex", "flex-col", "h-full");
        sortBy.setItems("Popularidad", "Recientes primero", "Antiguos primero");
        sortBy.setValue("Popularidad");
        this.videojuegoService = videojuegoService;
        videojuego = videojuegoService.list().get(0);

        for (Videojuego videojuego : videojuegoService.list()) {
            add(new VideojuegosViewCard(videojuego.getTitulo(), videojuego.getCover(), 
                    videojuego.getDescrpcion(), videojuego.getFechaLanzamiento().toString(),
                    videojuego.getCuestionario(), videojuego.getArchDescarga(), this.authenticatedUser.get().isPresent()));
        };

    }

}
