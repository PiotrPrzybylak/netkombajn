package pl.netolution.sklep3.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("product")
public class ProductPicture extends Picture {

	private static final int DEFAULT_PICTURE_ORDER = 1;

	private int pictureOrder = DEFAULT_PICTURE_ORDER;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductPicture() {

	}

	public ProductPicture(String originalName) {
		super.setOriginalName(originalName);
	}

	public int getPictureOrder() {
		return pictureOrder;
	}

	public void setPictureOrder(int pictureOrder) {
		this.pictureOrder = pictureOrder;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
