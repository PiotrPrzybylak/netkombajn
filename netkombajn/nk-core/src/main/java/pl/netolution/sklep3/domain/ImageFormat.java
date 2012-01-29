package pl.netolution.sklep3.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import pl.netolution.sklep3.domain.enums.ImageFormatName;

@Entity
@Table(name = "image_formats")
public class ImageFormat {

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	private ImageFormatName name;

	private String description;

	private int maxWidth;

	private int maxHeight;

	public int getMaxWidth() {
		return maxWidth;
	}

	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	public int getMaxHeight() {
		return maxHeight;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(ImageFormatName name) {
		this.name = name;
	}

	public ImageFormatName getName() {
		return name;
	}
}
