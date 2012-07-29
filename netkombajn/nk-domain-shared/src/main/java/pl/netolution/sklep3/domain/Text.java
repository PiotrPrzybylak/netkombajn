package pl.netolution.sklep3.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "texts")
public class Text implements Serializable {

	public static final String CONTACT_MAIN_TEXT_NAME = "contact_main";

	public static final String CSS_FOR_IE = "css_for_ie";

	public static final String CUSTOM_HEADER = "custom_header";

	public static final String CUSTOM_FOOTER = "custom_footer";

	public static final String ACCEPT_POLICY_TEXT = "ACCEPT_POLICY_TEXT";

	@Id
	@GeneratedValue
	private long id;

	private String name;

	private String text;

	private String title;

	private String style;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

}
