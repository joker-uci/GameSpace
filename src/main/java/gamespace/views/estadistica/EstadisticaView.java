package gamespace.views.estadistica;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import gamespace.data.service.CuestionariosRepository;
import gamespace.data.service.NoticiasRepository;
import gamespace.data.service.UserRepository;
import gamespace.data.service.VideojuegoRepository;
import gamespace.views.MainLayout;
import gamespace.views.estadistica.ServiceHealth.Status;
import javax.annotation.security.RolesAllowed;

@PageTitle("Estadistica")
@Route(value = "Estadistica", layout = MainLayout.class)
@RolesAllowed("admin")
public class EstadisticaView extends Main {

    UserRepository userRepository;
    NoticiasRepository noticiasRepository;
    CuestionariosRepository cuestionariosRepository;
    VideojuegoRepository videojuegoRepository;

    public EstadisticaView(UserRepository userRepository, NoticiasRepository noticiasRepository, CuestionariosRepository cuestionariosRepository, VideojuegoRepository videojuegoRepository) {
        this.cuestionariosRepository = cuestionariosRepository;
        addClassName("estadistica-view");
        String canUsua = userRepository.count() + "";
        String canNot = noticiasRepository.count() + "";
        String canCuest = cuestionariosRepository.count() + "";
        String canVid = videojuegoRepository.count() + "";
        Board board = new Board();
        board.addRow(createHighlight("Cantidad de usuarios", canUsua, 33.7), createHighlight("Cantidad de noticias", canNot, -112.45),
                createHighlight("Cantidad de cuestionarios", canCuest, 3.9), createHighlight("Cantidad de videojuegos", canVid, 0.0));
        board.addRow(createViewEvents(cuestionariosRepository));
        board.addRow(createServiceHealth(), createResponseTimes(userRepository, cuestionariosRepository, videojuegoRepository));
        add(board);
    }

    private Component createHighlight(String title, String value, Double percentage) {
        VaadinIcon icon = VaadinIcon.ARROW_UP;
        String prefix = "";
        String theme = "badge";

        if (percentage == 0) {
            prefix = "±";
        } else if (percentage > 0) {
            prefix = "+";
            theme += " success";
        } else if (percentage < 0) {
            icon = VaadinIcon.ARROW_DOWN;
            theme += " error";
        }

        H2 h2 = new H2(title);
        h2.addClassNames("font-normal", "m-0", "text-secondary", "text-xs");

        Span span = new Span(value);
        span.addClassNames("font-semibold", "text-3xl");

        Icon i = icon.create();
        i.addClassNames("box-border", "p-xs");

        Span badge = new Span(i, new Span(prefix + percentage.toString()));
        badge.getElement().getThemeList().add(theme);

        VerticalLayout layout = new VerticalLayout(h2, span/*, badge*/);
        layout.addClassName("p-l");
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    private Component createViewEvents(CuestionariosRepository cuestionariosRepository) {
        // Header
//        Select year = new Select();
//        year.setItems("2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021");
//        year.setValue("2021");
//        year.setWidth("100px");

        HorizontalLayout header = createHeader("Gráfico de jugabilidad", "Ejes (Valor opinado/Criterio encuestado)");
//        header.add(year);

        // Chart
        Chart chart = new Chart(ChartType.AREA);
        Configuration conf = chart.getConfiguration();

        XAxis xAxis = new XAxis();
        xAxis.setCategories(cuestionariosRepository.findAll().get(1).getCriterio1(), cuestionariosRepository.findAll().get(1).getCriterio2(), cuestionariosRepository.findAll().get(1).getCriterio3(), cuestionariosRepository.findAll().get(1).getCriterio4(), cuestionariosRepository.findAll().get(1).getCriterio5());
        conf.addxAxis(xAxis);

        conf.getyAxis().setTitle("Values");

        PlotOptionsArea plotOptions = new PlotOptionsArea();
        plotOptions.setPointPlacement(PointPlacement.ON);
        conf.addPlotOptions(plotOptions);
        for (int i = 0; i < cuestionariosRepository.findAll().size(); i++) {
            conf.addSeries(new ListSeries(cuestionariosRepository.findAll().get(i).getJuego(), cuestionariosRepository.findAll().get(i).getPromedio1(), cuestionariosRepository.findAll().get(i).getPromedio2(), cuestionariosRepository.findAll().get(i).getProedio3(), cuestionariosRepository.findAll().get(i).getPromedio4(), cuestionariosRepository.findAll().get(i).getPromedio5()));
        }
        
//        conf.addSeries(new ListSeries(cuestionariosRepository.findAll().get(2).getJuego(), cuestionariosRepository.findAll().get(1).getPromedio1(), cuestionariosRepository.findAll().get(1).getPromedio2(), cuestionariosRepository.findAll().get(1).getProedio3(), cuestionariosRepository.findAll().get(1).getPromedio4(), cuestionariosRepository.findAll().get(1).getPromedio5()));
//        conf.addSeries(new ListSeries(cuestionariosRepository.findAll().get(3).getJuego(), cuestionariosRepository.findAll().get(1).getPromedio1(), cuestionariosRepository.findAll().get(1).getPromedio2(), cuestionariosRepository.findAll().get(1).getProedio3(), cuestionariosRepository.findAll().get(1).getPromedio4(), cuestionariosRepository.findAll().get(1).getPromedio5()));
//        conf.addSeries(new ListSeries(cuestionariosRepository.findAll().get(4).getJuego(), cuestionariosRepository.findAll().get(1).getPromedio1(), cuestionariosRepository.findAll().get(1).getPromedio2(), cuestionariosRepository.findAll().get(1).getProedio3(), cuestionariosRepository.findAll().get(1).getPromedio4(), cuestionariosRepository.findAll().get(1).getPromedio5()));

        // Add it all together
        VerticalLayout viewEvents = new VerticalLayout(header, chart);
        viewEvents.addClassName("p-l");
        viewEvents.setPadding(false);
        viewEvents.setSpacing(false);
        viewEvents.getElement().getThemeList().add("spacing-l");
        return viewEvents;
    }

    private Component createServiceHealth() {
        // Header
        HorizontalLayout header = createHeader("Service health", "Input / output");

        // Grid
        Grid<ServiceHealth> grid = new Grid();
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setAllRowsVisible(true);

        grid.addColumn(new ComponentRenderer<>(serviceHealth -> {
            Span status = new Span();
            String statusText = getStatusDisplayName(serviceHealth);
            status.getElement().setAttribute("aria-label", "Status: " + statusText);
            status.getElement().setAttribute("title", "Status: " + statusText);
            status.getElement().getThemeList().add(getStatusTheme(serviceHealth));
            return status;
        })).setHeader("").setFlexGrow(0).setAutoWidth(true);
        grid.addColumn(ServiceHealth::getCity).setHeader("City").setFlexGrow(1);
        grid.addColumn(ServiceHealth::getInput).setHeader("Input").setAutoWidth(true).setTextAlign(ColumnTextAlign.END);
        grid.addColumn(ServiceHealth::getOutput).setHeader("Output").setAutoWidth(true)
                .setTextAlign(ColumnTextAlign.END);

        grid.setItems(new ServiceHealth(Status.EXCELLENT, "Münster", 324, 1540),
                new ServiceHealth(Status.OK, "Cluj-Napoca", 311, 1320),
                new ServiceHealth(Status.FAILING, "Ciudad Victoria", 300, 1219));

        // Add it all together
        VerticalLayout serviceHealth = new VerticalLayout(header, grid);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        return serviceHealth;
    }

    private Component createResponseTimes(UserRepository userRepository, CuestionariosRepository cuestionariosRepository, VideojuegoRepository videojuegoRepository) {
        HorizontalLayout header = createHeader("Reporte de datos", "Grafico de pastel de cantidad de usuarios, cuestionarios y videojuegos");

        // Chart
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Usuarios", userRepository.count()));
        series.add(new DataSeriesItem("Cuestionarios", cuestionariosRepository.count()));
        series.add(new DataSeriesItem("Videojuegos", videojuegoRepository.count()));

        conf.addSeries(series);

        // Add it all together
        VerticalLayout serviceHealth = new VerticalLayout(header, chart);
        serviceHealth.addClassName("p-l");
        serviceHealth.setPadding(false);
        serviceHealth.setSpacing(false);
        serviceHealth.getElement().getThemeList().add("spacing-l");
        return serviceHealth;
    }

    private HorizontalLayout createHeader(String title, String subtitle) {
        H2 h2 = new H2(title);
        h2.addClassNames("text-xl", "m-0");

        Span span = new Span(subtitle);
        span.addClassNames("text-secondary", "text-xs");

        VerticalLayout column = new VerticalLayout(h2, span);
        column.setPadding(false);
        column.setSpacing(false);

        HorizontalLayout header = new HorizontalLayout(column);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setSpacing(false);
        header.setWidthFull();
        return header;
    }

    private String getStatusDisplayName(ServiceHealth serviceHealth) {
        Status status = serviceHealth.getStatus();
        if (status == Status.OK) {
            return "Ok";
        } else if (status == Status.FAILING) {
            return "Failing";
        } else if (status == Status.EXCELLENT) {
            return "Excellent";
        } else {
            return status.toString();
        }
    }

    private String getStatusTheme(ServiceHealth serviceHealth) {
        Status status = serviceHealth.getStatus();
        String theme = "badge primary small";
        if (status == Status.EXCELLENT) {
            theme += " success";
        } else if (status == Status.FAILING) {
            theme += " error";
        }
        return theme;
    }

}
