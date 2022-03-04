package gamespace.views;

import gamespace.data.entity.User;
import gamespace.security.AuthenticatedUser;
import gamespace.views.ayuda.AyudaView;
//import gamespace.views.cuestionarios.CuestionariosView;
import gamespace.views.estadistica.EstadisticaView;
//import gamespace.gestionar.GestionarView;
//import gamespace.views.gvideojuego.gvideojuegoView;
//import gamespace.views.inicio.InicioView;
//import gamespace.views.noticia.NoticiaView;
import gamespace.views.videojuegos.VideojuegosView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import java.util.Optional;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import gamespace.views.administrarcuenta.AdministrarCuentaView;
import gamespace.views.gcuestionarios.GCuestionariosView;
import gamespace.views.gestionar.GestionarView;
import gamespace.views.gnoticias.GNoticiasView;
import gamespace.views.gusuarios.GUsuariosView;
import gamespace.views.gvideojuegos.GVideojuegosView;
import gamespace.views.noticias.NoticiasView;
import java.util.Map;
import java.util.TreeMap;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {

    private Map<String, Class<?>> viewByName;
    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        initial();
        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "flex-col", "w-full");

        Div layout = new Div();
        layout.addClassNames("flex", "h-xl", "items-center", "px-l");

        H1 appName = new H1("GameSpace");
        appName.addClassNames("my-0", "me-auto", "text-l");
        layout.add(appName);

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Avatar avatar = new Avatar(user.getName(), user.getProfilePictureUrl());
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(avatar);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Cerrar Sesión", e -> {
                authenticatedUser.logout();
            });

            Span name = new Span(user.getName());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Autenticarse");
            layout.add(loginLink);
        }
        header.add(layout, createMenu());
        return header;
    }
    private MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();

        if (accessChecker.hasAccess(viewByName.get("Inicio"))) {
            menuBar.addItem(createLink("Inicio", NoticiasView.class));
        }
        if (accessChecker.hasAccess(viewByName.get("Ayuda"))) {
            menuBar.addItem(createLink("Ayuda", AyudaView.class));
        }
        if (accessChecker.hasAccess(viewByName.get("Videojuegos"))) {
            menuBar.addItem(createLink("Videojuegos", VideojuegosView.class));
        }
        if (accessChecker.hasAccess(viewByName.get("Estadistica"))) {
            menuBar.addItem(createLink("Estadistica", EstadisticaView.class));
        }
        if (accessChecker.hasAccess(viewByName.get("Editar Cuenta"))) {
            menuBar.addItem(createLink("Editar Cuenta", AdministrarCuentaView.class));
        }
        if (accessChecker.hasAccess(viewByName.get("Gestionar"))) {
            MenuItem manage = menuBar.addItem("Gestionar");
            SubMenu manageSubMenu = manage.getSubMenu();
            if (accessChecker.hasAccess(viewByName.get("Gestionar Noticias"))) {
            manageSubMenu.addItem(createLink("Noticias", GNoticiasView.class));
            }
            if (accessChecker.hasAccess(viewByName.get("Gestionar Videojuegos"))) {
            manageSubMenu.addItem(createLink("Videojuegos", GVideojuegosView.class));
            }
            if (accessChecker.hasAccess(viewByName.get("Gestionar Cuestionarios"))) {
            manageSubMenu.addItem(createLink("Cuestionarios", GCuestionariosView.class));
            }
            /*if (accessChecker.hasAccess(viewByName.get("Gestionar Usuarios"))) {
            manageSubMenu.addItem(createLink("Usuarios", GUsuariosView.class));
            }*/
        }
    return menuBar;
    }

    private void initial() {
        viewByName = new TreeMap<>();

        viewByName.put("Inicio", NoticiasView.class);
        viewByName.put("Ayuda", AyudaView.class);
        viewByName.put("Videojuegos", VideojuegosView.class);
        viewByName.put("Estadistica", EstadisticaView.class);
        viewByName.put("Editar Cuenta", AdministrarCuentaView.class);
        viewByName.put("Gestionar", GestionarView.class);
        viewByName.put("Gestionar Cuestionarios", GCuestionariosView.class);
        viewByName.put("Gestionar Noticias", GNoticiasView.class);
        viewByName.put("Gestionar Videojuegos", GVideojuegosView.class);
       // viewByName.put("Gestionar Usuarios", GUsuariosView.class);
    }

    private RouterLink createLink(String title, Class<? extends Component> view) {
        RouterLink link = new RouterLink();
        link.setRoute(view);
        Span text = new Span(title);
        text.getStyle().set("color", "white");
        text.getStyle().set("text-decoration", "none");
        link.add(text);
        return link;
    }
}
