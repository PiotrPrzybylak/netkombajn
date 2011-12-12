package pl.netolution.sklep3.domain.product.opinions;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class SingleRating {

	private Long id;

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

	public SingleRating of(int value) {
		this.value = value;
		return this;
	}

}
