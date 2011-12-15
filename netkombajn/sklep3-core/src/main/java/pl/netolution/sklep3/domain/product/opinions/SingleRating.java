package pl.netolution.sklep3.domain.product.opinions;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
public class SingleRating {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	ProductRatings productRatings;

	private Integer value;

	SingleRating(Integer value, ProductRatings productRatings) {
		this.value = value;
		this.productRatings = productRatings;
	}

	public SingleRating() {
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SingleRating other = (SingleRating) obj;
		return new EqualsBuilder().append(value, other.value).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

	public SingleRating fromInteger(int value) {
		this.value = value;
		return this;
	}

	public static SingleRating of(int ratingAsInt) {
		return new SingleRating().fromInteger(ratingAsInt);
	}

	public Integer value() {
		return value;
	}

}
