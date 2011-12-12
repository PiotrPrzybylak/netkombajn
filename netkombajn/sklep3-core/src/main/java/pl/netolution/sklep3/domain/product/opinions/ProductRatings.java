package pl.netolution.sklep3.domain.product.opinions;

import static org.hibernate.annotations.CascadeType.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

@Entity
public class ProductRatings {

	@Id
	@GeneratedValue
	private Long id;
	@OneToMany(mappedBy = "productRatings")
	@Cascade({ PERSIST, SAVE_UPDATE, DELETE_ORPHAN })
	private List<SingleRating> ratings = new ArrayList<SingleRating>();

	private Long productId;

	ProductRatings() {
	}

	private ProductRatings(Integer[] ratingsAsIntegers) {
		for (Integer element : ratingsAsIntegers) {
			ratings.add(new SingleRating(element, this));
		}
	}

	public static ProductRatings fromList(Integer... ratingsAsIntegers) {
		return new ProductRatings(ratingsAsIntegers);
	}

	public void forProduct(Long productId) {
		this.productId = productId;
	}

	public Long getId() {
		return id;
	}

	public SingleRating getAverageRating() {
		return ratings.get(0);
	}

	public Long getProductId() {
		return productId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ratings == null) ? 0 : ratings.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductRatings other = (ProductRatings) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ratings == null) {
			if (other.ratings != null)
				return false;
		} else if (!ratings.equals(other.ratings))
			return false;
		return true;
	}

}
