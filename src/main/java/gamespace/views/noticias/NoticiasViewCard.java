package gamespace.views.noticias;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;

@JsModule("./views/noticias/noticias-view-card.ts")
@Tag("noticias-view-card")
public class NoticiasViewCard extends LitTemplate{
    @Id
    private Div div;
    public NoticiasViewCard(String titulo, String autor, String feho, String resumen) {
        //Div div = new Div();
        div.setClassName("bg-contrast-5 flex flex-col items-start p-m rounded-l");
        
        Label tit = new Label();
        tit.setText(titulo);
        tit.setClassName("text-xl font-semibold");

        Div ayf = new Div();
        ayf.setClassName("text-s text-secondary");
        Label aut = new Label();
        Label fecha = new Label();
        Label a = new Label();
        aut.setText(autor);
        fecha.setText(feho);
        a.setText(" - ");
        ayf.add(autor);
        ayf.add(a);
        ayf.add(fecha);
        //titulo.addClassName(className);
        
        Label resu = new Label();
        resu.setText(resumen);
        resu.setClassName("my-m");
        
        Button ver = new Button();
        //ver.addClassName(className);
        ver.setText("Ver");
        
        div.add(titulo);
        div.add(ayf);
        div.add(resumen);
        div.add(ver);
    }
}
