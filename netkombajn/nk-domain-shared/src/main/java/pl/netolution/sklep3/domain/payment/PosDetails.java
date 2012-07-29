package pl.netolution.sklep3.domain.payment;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class PosDetails {

	@Enumerated(EnumType.STRING)
	private PaymentSystemType paymentSystemType;

	private String posId;

	private String posAuthorizationKey;

	private String externalSystemAuthorizationKey;

	private String posId2;

	public PosDetails() {
	}

	public PosDetails(PaymentSystemType paymentSystemType, String posId, String posAuthorizationKey, String externalSystemAuthorizationKey,
			String posId2) {
		this.paymentSystemType = paymentSystemType;
		this.posId = posId;
		this.posAuthorizationKey = posAuthorizationKey;
		this.externalSystemAuthorizationKey = externalSystemAuthorizationKey;
		this.posId2 = posId2;
	}

	public String getExternalSystemAuthorizationKey() {
		return externalSystemAuthorizationKey;
	}

	public String getPosAuthorizationKey() {
		return posAuthorizationKey;
	}

	public PaymentSystemType getPaymentSystemType() {
		return paymentSystemType;
	}

	public String getPosId() {
		return posId;
	}

	public String getPosId2() {
		return posId2;
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + paymentSystemType.hashCode();
		result = 37 * result + posId.hashCode();
		result = 37 * result + posAuthorizationKey.hashCode();
		result = 37 * result + externalSystemAuthorizationKey.hashCode();
		result = 37 * result + posId2.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PosDetails)) {
			return false;
		}
		PosDetails other = (PosDetails) obj;

		if (!(this.paymentSystemType == null ? other.paymentSystemType == null : this.paymentSystemType.equals(other.paymentSystemType))) {
			return false;
		}

		if (!(this.posId == null ? other.posId == null : this.posId.equals(other.posId))) {
			return false;
		}

		if (!(this.posAuthorizationKey == null ? other.posAuthorizationKey == null : this.posAuthorizationKey
				.equals(other.posAuthorizationKey))) {
			return false;
		}

		if (!(this.externalSystemAuthorizationKey == null ? other.externalSystemAuthorizationKey == null
				: this.externalSystemAuthorizationKey.equals(other.externalSystemAuthorizationKey))) {
			return false;
		}

		if (!(this.posId2 == null ? other.posId2 == null : this.posId2.equals(other.posId2))) {
			return false;
		}

		return true;
	}
}
