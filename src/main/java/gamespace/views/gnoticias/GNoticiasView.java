package gamespace.views.gnoticias;

import com.vaadin.collaborationengine.CollaborationAvatarGroup;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
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
import gamespace.data.entity.Noticias;
import gamespace.data.service.NoticiasService;
import gamespace.views.MainLayout;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("GNoticias")
@Route(value = "GNoticias/:noticiasID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
public class GNoticiasView extends Div implements BeforeEnterObserver {

    private final String NOTICIAS_ID = "noticiasID";
    private final String NOTICIAS_EDIT_ROUTE_TEMPLATE = "GNoticias/%s/edit";

    private Grid<Noticias> grid = new Grid<>(Noticias.class, false);

    CollaborationAvatarGroup avatarGroup;

    private TextField titulo;
    private TextField autor;
    private DateTimePicker feHoPublicacion;
    private TextField resumen;
    private TextField contenido;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private CollaborationBinder<Noticias> binder;

    private Noticias noticias;

    private NoticiasService noticiasService;

    public GNoticiasView(@Autowired NoticiasService noticiasService) {
        this.noticiasService = noticiasService;
        addClassNames("g-noticias-view", "flex", "flex-col", "h-full");

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
        grid.addColumn("titulo").setAutoWidth(true);
        grid.addColumn("autor").setAutoWidth(true);
        grid.addColumn("feHoPublicacion").setAutoWidth(true);
        grid.addColumn("resumen").setAutoWidth(true);
        grid.addColumn("contenido").setAutoWidth(true);
        grid.setItems(query -> noticiasService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(NOTICIAS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(GNoticiasView.class);
            }
        });

        // Configure Form
        binder = new CollaborationBinder<>(Noticias.class, userInfo);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.noticias == null) {
                    this.noticias = new Noticias();
                }
                binder.writeBean(this.noticias);

                noticiasService.update(this.noticias);
                clearForm();
                refreshGrid();
                Notification.show("Noticias details stored.");
                UI.getCurrent().navigate(GNoticiasView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the noticias details.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> noticiasId = event.getRouteParameters().get(NOTICIAS_ID).map(UUID::fromString);
        if (noticiasId.isPresent()) {
            Optional<Noticias> noticiasFromBackend = noticiasService.get(noticiasId.get());
            if (noticiasFromBackend.isPresent()) {
                populateForm(noticiasFromBackend.get());
            } else {
                Notification.show(String.format("The requested noticias was not found, ID = %d", noticiasId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GNoticiasView.class);
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
        titulo = new TextField("Titulo");
        autor = new TextField("Autor");
        feHoPublicacion = new DateTimePicker("Fe Ho Publicacion");
        feHoPublicacion.setStep(Duration.ofSeconds(1));
        resumen = new TextField("Resumen");
        contenido = new TextField("Contenido");
        Component[] fields = new Component[]{titulo, autor, feHoPublicacion, resumen, contenido};

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

    private void populateForm(Noticias value) {
        this.noticias = value;
        String topic = null;
        if (this.noticias != null && this.noticias.getId() != null) {
            topic = "noticias/" + this.noticias.getId();
            avatarGroup.getStyle().set("visibility", "visible");
        } else {
            avatarGroup.getStyle().set("visibility", "hidden");
        }
        binder.setTopic(topic, () -> this.noticias);
        avatarGroup.setTopic(topic);

    }
}