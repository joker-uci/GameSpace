package gamespace.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import gamespace.data.entity.User;
import gamespace.security.AuthenticatedUser;
import gamespace.views.administrarcuenta.AdministrarCuentaView;
import gamespace.views.ayuda.AyudaView;
import gamespace.views.estadistica.EstadisticaView;
import gamespace.views.gcuestionarios.GCuestionariosView;
import gamespace.views.gestionar.GestionarView;
import gamespace.views.gnoticias.GNoticiasView;
import gamespace.views.gusuarios.GUsuariosView;
import gamespace.views.gvideojuegos.GVideojuegosView;
import gamespace.views.noticias.NoticiasView;
import gamespace.views.selectbuscar.SelectBuscarView;
import gamespace.views.videojuegos.VideojuegosView;
import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames("flex", "h-m", "items-center", "px-s", "relative", "text-secondary");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames("font-medium", "text-s", "whitespace-nowrap");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                // Use Lumo classnames for suitable font size and margin
                addClassNames("me-s", "text-l");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

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
            userMenu.addItem("Logout", e -> {
                authenticatedUser.logout();
            });

            Span name = new Span(user.getName());
            name.addClassNames("font-medium", "text-s", "text-secondary");

            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        Nav nav = new Nav();
        nav.addClassNames("flex", "gap-s", "overflow-auto", "px-m");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("flex", "list-none", "m-0", "p-0");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }

        }

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //
                new MenuItemInfo("Noticias", "la la-newspaper", NoticiasView.class), //

                new MenuItemInfo("Administrar Cuenta", "la la-user", AdministrarCuentaView.class), //

                new MenuItemInfo("Ayuda", "la la-question", AyudaView.class), //

                new MenuItemInfo("Videojuegos", "la la-gamepad", VideojuegosView.class), //

                new MenuItemInfo("GUsuarios", "la la-user-check", GUsuariosView.class), //

                new MenuItemInfo("Gestionar", "la la-cogs", GestionarView.class), //

                new MenuItemInfo("GNoticias", "la la-newspaper", GNoticiasView.class), //

                new MenuItemInfo("GCuestionarios", "la la-list-ol", GCuestionariosView.class), //

                new MenuItemInfo("GVideojuegos", "la la-gamepad", GVideojuegosView.class), //

                new MenuItemInfo("Estadistica", "la la-chalkboard-teacher", EstadisticaView.class), //

                new MenuItemInfo("Select Buscar", "la la-search", SelectBuscarView.class), //

        };
    }

}
