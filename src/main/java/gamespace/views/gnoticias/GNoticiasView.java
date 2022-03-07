package gamespace.views.gnoticias;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import gamespace.data.entity.Videojuego;
import gamespace.data.service.VideojuegoService;

@PageTitle("GNoticias")
@Route(value = "GNoticias/:noticiasID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
public class GNoticiasView extends Div implements BeforeEnterObserver {

    private final String NOTICIAS_ID = "noticiasID";
    private final String NOTICIAS_EDIT_ROUTE_TEMPLATE = "GNoticias/%s/edit";

    private Grid<Noticias> grid = new Grid<>(Noticias.class, false);

    private TextField titulo;
    private TextField autor;
    private DateTimePicker feHoPublicacion;
    private TextField resumen;
    private TextField contenido;

    private Button cancel = new Button("Cancelar");
    private Button save = new Button("Crear nueva noticia");//modificado el tititulo
    private Button select = new Button("Seleccionar para eliminar");//creado
    private Button delete = new Button("Eliminar");//creado

    private BeanValidationBinder<Noticias> binder;

    private Noticias noticias;

    private NoticiasService noticiasService;

    public GNoticiasView(@Autowired NoticiasService noticiasService) {
        this.noticiasService = noticiasService;
        addClassNames("g-noticias-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        delete.setVisible(false);//ocultar el eliminar
        add(splitLayout);
        // Configure Grid
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
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
            // cambio de nombre del boton guardar y ocultar el select cuando se esta modificando
            if (!this.noticias.getAutor().isEmpty() || this.noticias.getFeHoPublicacion() == null || !this.noticias.getContenido().isEmpty() || !this.noticias.getResumen().isEmpty() || !this.noticias.getTitulo().isEmpty()) {
                save.setText("Guardar");
            }
            select.setVisible(false);
        });
        // Configure Form
        binder = new BeanValidationBinder<>(Noticias.class);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.bindInstanceFields(this);
//cancelar
        cancel.addClickListener(e -> {
            clearForm();
            //configuracion de botones al cancelar
            save.setText("Crear nueva noticia");
            select.setVisible(true);
            delete.setVisible(false);
            save.setVisible(true);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            refreshGrid();
        });
//salvar
        save.addClickListener(e -> {
            try {
                if (this.noticias == null) {
                    this.noticias = new Noticias();
                }
                binder.writeBean(this.noticias);
                //--------validar aki q no sean campos vacios
                if (this.noticias.getAutor().isEmpty() || this.noticias.getFeHoPublicacion() == null || this.noticias.getContenido().isEmpty() || this.noticias.getResumen().isEmpty() || this.noticias.getTitulo().isEmpty()) {
                    Notification.show("Campos vacios");
                } else {
                    noticiasService.update(this.noticias);
                    Notification.show("Noticia guardada");
                    clearForm();
                    save.setText("Crear nueva noticia");///--------------------------corregir en los otros
                    select.setVisible(true);///--------------------------corregir en los otros
                    delete.setVisible(false);///--------------------------corregir en los otros
                    save.setVisible(true);///--------------------------corregir en los otros
                    UI.getCurrent().navigate(GNoticiasView.class);
                    refreshGrid();
                }
            } catch (ValidationException validationException) {
                Notification.show("Un error ha ocurrido mientras se guardaba la noticia");
            }
        });
        //select
        select.addClickListener(e -> {
            grid.setSelectionMode(Grid.SelectionMode.MULTI);
            // Notification.show("Noticias eliminadas");
            select.setVisible(false);
            delete.setVisible(true);
            save.setVisible(false);
        });
        //delete
        delete.addClickListener(e -> {
            Dialog dialog = new Dialog();
            VerticalLayout dialogLayout = createDialogLayout(dialog, noticiasService, grid, delete, select, save);
            dialog.add(dialogLayout);
            dialog.open();
//            grid.getSelectedItems().stream().forEach((t) -> {
//                noticiasService.delete(t.getId());
//            });
//            grid.setItems(query -> noticiasService.list(
//                    PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
//                    .stream());
//            Notification.show("Noticia eliminada");/////--------------------------corregir en los otros

//            select.setVisible(true);
//            delete.setVisible(false);
//            save.setVisible(true);
//            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        });
    }

    //@Override;
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> noticiasId = event.getRouteParameters().get(NOTICIAS_ID).map(UUID::fromString);
        if (noticiasId.isPresent()) {
            Optional<Noticias> noticiasFromBackend = noticiasService.get(noticiasId.get());
            if (noticiasFromBackend.isPresent()) {
                populateForm(noticiasFromBackend.get());
            } else {
                Notification.show(String.format("The requested noticias was not found, ID = %s", noticiasId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GNoticiasView.class);
            }
        }
    }
//panel de edicion

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
        feHoPublicacion = new DateTimePicker("Fecha y hora de la publiación");
        feHoPublicacion.setStep(Duration.ofSeconds(1));
        resumen = new TextField("Resumen");
        contenido = new TextField("Contenido");
        Component[] fields = new Component[]{titulo, autor, feHoPublicacion, resumen, contenido};
        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        //botones agregados estilo
        select.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(select, save, delete, cancel);
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
        grid.getLazyDataView().refreshAll();
        grid.select(null);
        UI.getCurrent().getPage().reload();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Noticias value) {
        this.noticias = value;
        binder.readBean(this.noticias);
    }

    private VerticalLayout createDialogLayout(Dialog dialog, NoticiasService noticiasService, Grid<Noticias> grid, Button delete, Button select, Button save) {
        H1 headline = new H1("¡Precaución!");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0 0 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
        H4 texto = new H4("¿Está usted seguro que desea eliminar?");
        VerticalLayout fieldLayout = new VerticalLayout(texto);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Button aceptar = new Button("Aceptar", e -> {
            grid.getSelectedItems().stream().forEach((t) -> {
                noticiasService.delete(t.getId());
            });
            grid.setItems(query -> noticiasService.list(
                    PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                    .stream());
            select.setVisible(true);
            delete.setVisible(false);
            save.setVisible(true);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            Notification.show("Noticia eliminada");
            refreshGrid();
            dialog.close();
        });
        Button cancelar = new Button("Cancelar", e -> {
            save.setText("Crear nueva noticia");
            select.setVisible(true);
            delete.setVisible(false);
            save.setVisible(true);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            dialog.close();
        });
        cancelar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        aceptar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout(aceptar,
                cancelar);
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout,
                buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");

        return dialogLayout;
    }
}
