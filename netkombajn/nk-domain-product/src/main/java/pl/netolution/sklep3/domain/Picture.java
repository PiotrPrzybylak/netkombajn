package pl.netolution.sklep3.domain;

import java.io.Serializable;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "pictures")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("rudimentary")
public class Picture implements Serializable {

	public static final Picture PICTURE_NONE;

	static {
		PICTURE_NONE = new Picture();
		PICTURE_NONE.setId(1L);
	}

	@Id
	@GeneratedValue
	private Long id;

	private String originalName;

	public Picture() {
	}

	public Picture(String originalName) {
		this.originalName = originalName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

}
