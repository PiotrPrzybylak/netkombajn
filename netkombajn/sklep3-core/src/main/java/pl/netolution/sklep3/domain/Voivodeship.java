package pl.netolution.sklep3.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name = "voivodeships")
public class Voivodeship implements Serializable {

	private static final Logger log = Logger.getLogger(Voivodeship.class);

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return super.toString() + "id: " + id + " name: " + name;
	}

	@Override
	// TODO provide implementation recommened in Java Persistence .... and implement hashcode
	public boolean equals(Object obj) {
		log.debug("comparing this: " + this + " to other" + obj);
		Voivodeship other;
		if (obj instanceof Voivodeship) {
			other = (Voivodeship) obj;

		} else {
			return false;
		}

		return this.id.equals(other.id);

	}

}
