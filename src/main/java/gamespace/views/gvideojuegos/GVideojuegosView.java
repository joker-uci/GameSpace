package gamespace.views.gvideojuegos;

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
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import elemental.json.Json;
import gamespace.data.entity.Videojuego;
import gamespace.data.service.VideojuegoService;
import gamespace.views.MainLayout;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriUtils;

@PageTitle("GVideojuegos")
@Route(value = "GVideojuegos/:videojuegoID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed("admin")
public class GVideojuegosView extends Div implements BeforeEnterObserver {

    private final String VIDEOJUEGO_ID = "videojuegoID";
    private final String VIDEOJUEGO_EDIT_ROUTE_TEMPLATE = "GVideojuegos/%s/edit";

    private Grid<Videojuego> grid = new Grid<>(Videojuego.class, false);

    private TextField titulo;
    private TextField cuestionario;
    private TextField descrpcion;
    private DatePicker fechaLanzamiento;
    private Upload cover;
    private Image coverPreview;
    private Upload archDescarga;
    private Image archDescargaPreview;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Videojuego> binder;

    private Videojuego videojuego;

    private VideojuegoService videojuegoService;

    public GVideojuegosView(@Autowired VideojuegoService videojuegoService) {
        this.videojuegoService = videojuegoService;
        addClassNames("g-videojuegos-view", "flex", "flex-col", "h-full");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("titulo").setAutoWidth(true);
        grid.addColumn("cuestionario").setAutoWidth(true);
        grid.addColumn("descrpcion").setAutoWidth(true);
        grid.addColumn("fechaLanzamiento").setAutoWidth(true);
        LitRenderer<Videojuego> coverRenderer = LitRenderer
                .<Videojuego>of("<img style='height: 64px' src=${item.cover} />")
                .withProperty("cover", Videojuego::getCover);
        grid.addColumn(coverRenderer).setHeader("Cover").setWidth("68px").setFlexGrow(0);

        LitRenderer<Videojuego> archDescargaRenderer = LitRenderer.<Videojuego>of(
                "<span style='border-radius: 50%; overflow: hidden; display: flex; align-items: center; justify-content: center; width: 64px; height: 64px'><img style='max-width: 100%' src=${item.archDescarga} /></span>")
                .withProperty("archDescarga", Videojuego::getArchDescarga);
        grid.addColumn(archDescargaRenderer).setHeader("Arch Descarga").setWidth("96px").setFlexGrow(0);

        grid.setItems(query -> videojuegoService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(VIDEOJUEGO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(GVideojuegosView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Videojuego.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        attachImageUpload(cover, coverPreview);
        attachImageUpload(archDescarga, archDescargaPreview);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.videojuego == null) {
                    this.videojuego = new Videojuego();
                }
                binder.writeBean(this.videojuego);
                this.videojuego.setCover(coverPreview.getSrc());
                this.videojuego.setArchDescarga(archDescargaPreview.getSrc());

                videojuegoService.update(this.videojuego);
                clearForm();
                refreshGrid();
                Notification.show("Videojuego details stored.");
                UI.getCurrent().navigate(GVideojuegosView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the videojuego details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> videojuegoId = event.getRouteParameters().get(VIDEOJUEGO_ID).map(UUID::fromString);
        if (videojuegoId.isPresent()) {
            Optional<Videojuego> videojuegoFromBackend = videojuegoService.get(videojuegoId.get());
            if (videojuegoFromBackend.isPresent()) {
                populateForm(videojuegoFromBackend.get());
            } else {
                Notification.show(String.format("The requested videojuego was not found, ID = %s", videojuegoId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(GVideojuegosView.class);
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
        cuestionario = new TextField("Cuestionario");
        descrpcion = new TextField("Descrpcion");
        fechaLanzamiento = new DatePicker("Fecha Lanzamiento");
        Label coverLabel = new Label("Cover");
        coverPreview = new Image();
        coverPreview.setWidth("100%");
        cover = new Upload();
        cover.getStyle().set("box-sizing", "border-box");
        cover.getElement().appendChild(coverPreview.getElement());
        Label archDescargaLabel = new Label("Arch Descarga");
        archDescargaPreview = new Image();
        archDescargaPreview.setWidth("100%");
        archDescarga = new Upload();
        archDescarga.getStyle().set("box-sizing", "border-box");
        archDescarga.getElement().appendChild(archDescargaPreview.getElement());
        Component[] fields = new Component[]{titulo, cuestionario, descrpcion, fechaLanzamiento, coverLabel, cover,
                archDescargaLabel, archDescarga};

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

    private void attachImageUpload(Upload upload, Image preview) {
        ByteArrayOutputStream uploadBuffer = new ByteArrayOutputStream();
        upload.setAcceptedFileTypes("image/*");
        upload.setReceiver((fileName, mimeType) -> {
            return uploadBuffer;
        });
        upload.addSucceededListener(e -> {
            String mimeType = e.getMIMEType();
            String base64ImageData = Base64.getEncoder().encodeToString(uploadBuffer.toByteArray());
            String dataUrl = "data:" + mimeType + ";base64,"
                    + UriUtils.encodeQuery(base64ImageData, StandardCharsets.UTF_8);
            upload.getElement().setPropertyJson("files", Json.createArray());
            preview.setSrc(dataUrl);
            uploadBuffer.reset();
        });
        preview.setVisible(false);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Videojuego value) {
        this.videojuego = value;
        binder.readBean(this.videojuego);
        this.coverPreview.setVisible(value != null);
        if (value == null) {
            this.coverPreview.setSrc("");
        } else {
            this.coverPreview.setSrc(value.getCover());
        }
        this.archDescargaPreview.setVisible(value != null);
        if (value == null) {
            this.archDescargaPreview.setSrc("");
        } else {
            this.archDescargaPreview.setSrc(value.getArchDescarga());
        }

    }
}
