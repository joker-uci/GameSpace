package gamespace.views.gusuarios;

import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import gamespace.data.entity.Usuario;
import gamespace.data.service.UsuarioService;
import gamespace.views.MainLayout;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("GUsuarios")
@Route(value = "GUsuarios/:usuarioID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
public class GUsuariosView extends Div implements BeforeEnterObserver {

    private final String USUARIO_ID = "usuarioID";
    private final String USUARIO_EDIT_ROUTE_TEMPLATE = "GUsuarios/%s/edit";

    private Grid<Usuario> grid = new Grid<>(Usuario.class, false);

    CollaborationAvatarGroup avatarGroup;
    private Usuario usuario;
    private TextField firstName;
    private TextField nombreUsuario;
    private TextField contrasenna;
    private TextField rol;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private CollaborationBinder<Usuario> binder;

    

    private UsuarioService usuarioService;

    public GUsuariosView(@Autowired UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        addClassNames("g-usuarios-view", "flex", "flex-col", "h-full");

        // UserInfo is used by Collaboration Engine and is used to share details
        // of users to each other to able collaboration. Replace this with
        // information about the actual user that is logged, providing a user
        // identifier, and the user's real name. You can also provide the users
        // avatar by passing an url to the image as a third parameter, or by
        // configuring an `ImageProvider` to `avatarGroup`.
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), "Steve Lange");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        avatarGroup = new CollaborationAvatarGroup(userInfo, null);
        avatarGroup.getStyle().set("visibility", "hidden");

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("firstName").setAutoWidth(true);
        grid.addColumn("usuario").setAutoWidth(true);
        grid.addColumn("contrasenna").setAutoWidth(true);
        grid.addColumn("rol").setAutoWidth(true);
        grid.setItems(query -> usuarioService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(USUARIO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(GUsuariosView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Usuario.class, userInfo);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.usuario == null) {
                    this.usuario = new Usuario();
                }
                binder.writeBean(this.usuario);

                usuarioService.update(this.usuario);
                clearForm();
                refreshGrid();
                Notification.show("Usuario details stored.");
                UI.getCurrent().navigate(GUsuariosView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the usuario details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> usuarioId = event.getRouteParameters().get(USUARIO_ID).map(UUID::fromString);
        if (usuarioId.isPresent()) {
            Optional<Usuario> usuarioFromBackend = usuarioService.get(usuarioId.get());
            if (usuarioFromBackend.isPresent()) {
                populateForm(usuarioFromBackend.get());
            } else {
                Notification.show(String.format("The requested usuario was not found, ID = %d", usuarioId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GUsuariosView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        nombreUsuario = new TextField("Usuario");
        contrasenna = new TextField("Contrasenna");
        rol = new TextField("Rol");
        Component[] fields = new Component[]{firstName, nombreUsuario, contrasenna, rol};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(avatarGroup, formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Usuario value) {
        this.usuario = value;
        String topic = null;
        if (this.usuario != null && this.usuario.getId() != null) {
            topic = "usuario/" + this.usuario.getId();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.usuario);
        avatarGroup.setTopic(topic);

    }
}