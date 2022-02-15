package gamespace.views.ayuda;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.littemplate.LitTemplate;
import com.vaadin.flow.component.template.Id;

@JsModule("./views/ayuda/ayuda-view-card.ts")
@Tag("ayuda-view-card")
public class AyudaViewCard extends LitTemplate {

    @Id
    private Image image;

    @Id
    private Span header;

    @Id
    private Span subtitle;

    @Id
    private Paragraph text;

    @Id
    private Span badge;

    public AyudaViewCard(String text, String url) {
        this.image.setSrc(url);
        this.image.setAlt(text);
        this.header.setText("Title");
        this.subtitle.setText("Card subtitle");
        this.text.setText(
                "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut.");
        this.badge.setText("Label");
    }
}
