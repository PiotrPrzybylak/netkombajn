package pl.netolution.sklep3.domain;

import pl.netolution.sklep3.domain.enums.PaymentFormType;

public enum PaymentForm {

	TRANSFER(PaymentFormType.NORMAL_TRANSFER),
	CREDIT_CARD(PaymentFormType.ONLINE_TRANSFER),
	MTRANSFER(PaymentFormType.ONLINE_TRANSFER),
	MUTLITRANSFER(PaymentFormType.ONLINE_TRANSFER),
	BZWBK(PaymentFormType.ONLINE_TRANSFER),
	PEAKAO24(PaymentFormType.ONLINE_TRANSFER),
	INTELIGO(PaymentFormType.ONLINE_TRANSFER),
	NORDEA(PaymentFormType.ONLINE_TRANSFER),
	IPKO(PaymentFormType.ONLINE_TRANSFER),
	BPH(PaymentFormType.ONLINE_TRANSFER),
	ING(PaymentFormType.ONLINE_TRANSFER),
	LUKAS(PaymentFormType.ONLINE_TRANSFER),
	CITIBANK(PaymentFormType.ONLINE_TRANSFER),
	POLBANK(PaymentFormType.ONLINE_TRANSFER),
	KREDYTBANK(PaymentFormType.ONLINE_TRANSFER),
	BGZ(PaymentFormType.ONLINE_TRANSFER),
	MILLENIUM(PaymentFormType.ONLINE_TRANSFER),
	DEUTSCHEBANK(PaymentFormType.ONLINE_TRANSFER),
	RAIFFAISEN(PaymentFormType.ONLINE_TRANSFER),
	TEST(PaymentFormType.ONLINE_TEST_TRANSFER),
	CASH_ON_DELIVERY(PaymentFormType.CASH_ON_DELIVERY);

	private PaymentForm(PaymentFormType type) {
		this.type = type;
	}

	PaymentFormType type;

	public PaymentFormType getType() {
		return type;
	}

}
