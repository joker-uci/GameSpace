package gamespace.views.gcuestionarios;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import static com.vaadin.flow.component.Tag.H1;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import gamespace.data.entity.Cuestionarios;
import gamespace.data.service.CuestionariosService;
import gamespace.views.MainLayout;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("GCuestionarios")
@Route(value = "GCuestionarios/:cuestionarioID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
public class GCuestionariosView extends Div implements BeforeEnterObserver {

    private final String CUESTIONARIO_ID = "cuestionarioID";
    private final String CUESTIONARIO_EDIT_ROUTE_TEMPLATE = "GCuestionarios/%s/edit";

    private Grid<Cuestionarios> grid = new Grid<>(Cuestionarios.class, false);

    private TextField nombCuestionario;
    private TextField juego;
    private TextField descripcion;
    private DatePicker fecha;
    private TextField usuario;
    private TextField criterio1;
    private TextField promedio1;
    private TextField criterio2;
    private TextField promedio2;
    private TextField criterio3;
    private TextField proedio3;
    private TextField criterio4;
    private TextField promedio4;
    private TextField criterio5;
    private TextField promedio5;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Crear un nuevo registro");
    private Button select = new Button("Seleccionar para eliminar");//creado
    private Button delete = new Button("Eliminar");//creado

    private BeanValidationBinder<Cuestionarios> binder;

    private Cuestionarios cuestionario;

    private CuestionariosService cuestionarioService;

    public GCuestionariosView(@Autowired CuestionariosService cuestionarioService) {
        this.cuestionarioService = cuestionarioService;
        addClassNames("g-cuestionarios-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        delete.setVisible(false);
        add(splitLayout);

        // Configure Grid
//        grid.addColumn("cuestionario").setAutoWidth(true);
        grid.addColumn("juego").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);
        grid.addColumn("fecha").setAutoWidth(true);
//        grid.addColumn("usuario").setAutoWidth(true);
        grid.addColumn("criterio1").setAutoWidth(true);
        grid.addColumn("promedio1").setAutoWidth(true);
        grid.addColumn("criterio2").setAutoWidth(true);
        grid.addColumn("promedio2").setAutoWidth(true);
        grid.addColumn("criterio3").setAutoWidth(true);
        grid.addColumn("proedio3").setAutoWidth(true);
        grid.addColumn("criterio4").setAutoWidth(true);
        grid.addColumn("promedio4").setAutoWidth(true);
        grid.addColumn("criterio5").setAutoWidth(true);
        grid.addColumn("promedio5").setAutoWidth(true);
        grid.setItems(query -> cuestionarioService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CUESTIONARIO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(GCuestionariosView.class);
            }
            if (!this.cuestionario.getCriterio1().isEmpty() || !this.cuestionario.getCriterio2().isEmpty() || !this.cuestionario.getCriterio3().isEmpty() || !this.cuestionario.getCriterio4().isEmpty() || !this.cuestionario.getCriterio5().isEmpty() || !this.cuestionario.getDescripcion().isEmpty() || this.cuestionario.getFecha() != null || !this.cuestionario.getJuego().isEmpty()) {
                save.setText("Guardar");
            }
            select.setVisible(false);
        });

// Configure Form
        binder = new BeanValidationBinder<>(Cuestionarios.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(promedio1).withConverter(new StringToIntegerConverter("Solo numeros permitidos"))
                .bind("promedio1");
        binder.forField(promedio2).withConverter(new StringToIntegerConverter("Solo numeros permitidos"))
                .bind("promedio2");
        binder.forField(proedio3).withConverter(new StringToIntegerConverter("Solo numeros permitidos"))
                .bind("proedio3");
        binder.forField(promedio4).withConverter(new StringToIntegerConverter("Solo numeros permitidos"))
                .bind("promedio4");
        binder.forField(promedio5).withConverter(new StringToIntegerConverter("Solo numeros permitidos"))
                .bind("promedio5");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            save.setText("Crear nuevo cuestionario");
            select.setVisible(true);
            delete.setVisible(false);
            save.setVisible(true);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.cuestionario == null) {
                    this.cuestionario = new Cuestionarios();
                }
                binder.writeBean(this.cuestionario);
                if (this.cuestionario.getCriterio1().isEmpty() || this.cuestionario.getCriterio2().isEmpty() || this.cuestionario.getCriterio3().isEmpty() || this.cuestionario.getCriterio4().isEmpty() || this.cuestionario.getCriterio5().isEmpty() || this.cuestionario.getDescripcion().isEmpty() || this.cuestionario.getFecha() == null || this.cuestionario.getJuego().isEmpty()) {
                    Notification.show("Campos vacios");
                } else {
                    cuestionarioService.update(this.cuestionario);
                    clearForm();

                    save.setText("Crear nuevo cuestionario");///--------------------------corregir en los otros
                    select.setVisible(true);///--------------------------corregir en los otros
                    delete.setVisible(false);///--------------------------corregir en los otros
                    save.setVisible(true);
                    Notification.show("Cuestionario guardado");
                    UI.getCurrent().navigate(GCuestionariosView.class);
                    refreshGrid();
                }
            } catch (ValidationException validationException) {
                Notification.show("Un error ha ocurrido mientras se guardaba el cuestionario");
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
            VerticalLayout dialogLayout = createDialogLayout(dialog, cuestionarioService, grid, delete, select, save);
            dialog.add(dialogLayout);
            dialog.open();
//            grid.getSelectedItems().stream().forEach((t) -> {
//                cuestionarioService.delete(t.getId());
//            });
//            grid.setItems(query -> cuestionarioService.list(
//                    PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
//                    .stream());
//
//            select.setVisible(true);
//            delete.setVisible(false);
//            save.setVisible(true);
//            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
//            Notification.show("Cuestionario elminado");
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> cuestionarioId = event.getRouteParameters().get(CUESTIONARIO_ID).map(UUID::fromString);
        if (cuestionarioId.isPresent()) {
            Optional<Cuestionarios> cuestionarioFromBackend = cuestionarioService.get(cuestionarioId.get());
            if (cuestionarioFromBackend.isPresent()) {
                populateForm(cuestionarioFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested cuestionario was not found, ID = %s", cuestionarioId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GCuestionariosView.class);
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
//        nombCuestionario = new TextField("Cuestionario");

        juego = new TextField("Videojuegouego");
        descripcion = new TextField("Descripción");
        fecha = new DatePicker("Fecha de creacrión");
//        usuario = new TextField("Usuario");
        criterio1 = new TextField("Criterio A");
        promedio1 = new TextField("Promedio A");
        criterio2 = new TextField("Criterio B");
        promedio2 = new TextField("Promedio B");
        criterio3 = new TextField("Criterio C");
        proedio3 = new TextField("Proedio C");
        criterio4 = new TextField("Criterio D");
        promedio4 = new TextField("Promedio D");
        criterio5 = new TextField("Criterio E");
        promedio5 = new TextField("Promedio E");
        Component[] fields = new Component[]{/*nombCuestionario, */juego, descripcion, fecha/*, usuario*/, criterio1, promedio1,
            criterio2, promedio2, criterio3, proedio3, criterio4, promedio4, criterio5, promedio5};

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
        select.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, select, delete, cancel);
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

    private void populateForm(Cuestionarios value) {
        this.cuestionario = value;
        binder.readBean(this.cuestionario);
    }

    private VerticalLayout createDialogLayout(Dialog dialog, CuestionariosService cuestionarioService, Grid<Cuestionarios> grid, Button delete, Button select, Button save) {
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
                cuestionarioService.delete(t.getId());
            });
            grid.setItems(query -> cuestionarioService.list(
                    PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                    .stream());
            select.setVisible(true);
            delete.setVisible(false);
            save.setVisible(true);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
            Notification.show("Cuestionairo eliminado");
            refreshGrid();
            dialog.close();
            
        });
        Button cancelar = new Button("Cancelar", e -> {
            save.setText("Crear nuevo cuestionario");
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
