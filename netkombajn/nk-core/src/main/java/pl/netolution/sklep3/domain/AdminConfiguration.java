package pl.netolution.sklep3.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import pl.netolution.sklep3.domain.payment.PosDetails;

@Entity
@Table(name = "admin_configuration")
public class AdminConfiguration {

	@Id
	@GeneratedValue
	private long id;

	private String email;

	private boolean sendOrderEmail;

	private int profitMargin;

	private int productsOnPage;

	@Embedded
	private PosDetails posDetails;

	private Integer maxHitsNumber;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isSendOrderEmail() {
		return sendOrderEmail;
	}

	public void setSendOrderEmail(boolean sendOrderEmail) {
		this.sendOrderEmail = sendOrderEmail;
	}

	public int getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(int profitMargin) {
		this.profitMargin = profitMargin;
	}

	public int getProductsOnPage() {
		return productsOnPage;
	}

	public void setProductsOnPage(int productsOnPage) {
		this.productsOnPage = productsOnPage;
	}

	public PosDetails getPosDetails() {
		return posDetails;
	}

	public void setPosDetails(PosDetails posDetails) {
		this.posDetails = posDetails;
	}

	public Integer getMaxHitsNumber() {

		return maxHitsNumber;
	}

	public void setMaxHitsNumber(Integer maxHitsNumber) {
		this.maxHitsNumber = maxHitsNumber;
	}

}
