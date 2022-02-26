package gamespace.views.gcuestionarios;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
        grid.addColumn("cuestionario").setAutoWidth(true);
        grid.addColumn("juego").setAutoWidth(true);
        grid.addColumn("descripcion").setAutoWidth(true);
        grid.addColumn("fecha").setAutoWidth(true);
        grid.addColumn("usuario").setAutoWidth(true);
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
            if (this.cuestionario.getCriterio1() != null || this.cuestionario.getCriterio2() != null || this.cuestionario.getCriterio3() != null || this.cuestionario.getCriterio4() != null || this.cuestionario.getCriterio5() != null|| this.cuestionario.getDescripcion() != null|| this.cuestionario.getFecha() != null|| this.cuestionario.getJuego() != null) {
                save.setText("Guardar");
            }
            select.setVisible(false);
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Cuestionarios.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(promedio1).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("promedio1");
        binder.forField(promedio2).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("promedio2");
        binder.forField(proedio3).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("proedio3");
        binder.forField(promedio4).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("promedio4");
        binder.forField(promedio5).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("promedio5");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            save.setText("Crear nueva noticia");
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
               if (this.cuestionario.getCriterio1() == null || this.cuestionario.getCriterio2() == null || this.cuestionario.getCriterio3() == null || this.cuestionario.getCriterio4() == null || this.cuestionario.getCriterio5() == null|| this.cuestionario.getDescripcion() == null|| this.cuestionario.getFecha() == null|| this.cuestionario.getJuego() == null) {
                    Notification.show("Campos vacios");
                } else {
                cuestionarioService.update(this.cuestionario);
                clearForm();
                refreshGrid();
                Notification.show("Cuestionario details stored.");
                UI.getCurrent().navigate(GCuestionariosView.class);}
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the cuestionario details.");
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
            grid.getSelectedItems().stream().forEach((t) -> {
                cuestionarioService.delete(t.getId());
            });
            grid.setItems(query -> cuestionarioService.list(
                    PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                    .stream());
            refreshGrid();
            select.setVisible(true);
            delete.setVisible(false);
            save.setVisible(true);
            grid.setSelectionMode(Grid.SelectionMode.SINGLE);
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
        nombCuestionario = new TextField("Cuestionario");
        juego = new TextField("Juego");
        descripcion = new TextField("Descripcion");
        fecha = new DatePicker("Fecha");
        usuario = new TextField("Usuario");
        criterio1 = new TextField("Criterio1");
        promedio1 = new TextField("Promedio1");
        criterio2 = new TextField("Criterio2");
        promedio2 = new TextField("Promedio2");
        criterio3 = new TextField("Criterio3");
        proedio3 = new TextField("Proedio3");
        criterio4 = new TextField("Criterio4");
        promedio4 = new TextField("Promedio4");
        criterio5 = new TextField("Criterio5");
        promedio5 = new TextField("Promedio5");
        Component[] fields = new Component[]{nombCuestionario, juego, descripcion, fecha, usuario, criterio1, promedio1,
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
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Cuestionarios value) {
        this.cuestionario = value;
        binder.readBean(this.cuestionario);

    }
}
