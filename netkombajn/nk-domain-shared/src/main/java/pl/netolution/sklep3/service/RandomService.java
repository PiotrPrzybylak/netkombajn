package pl.netolution.sklep3.service;

import java.util.Random;

public class RandomService {

	public int getRandomNumber(int max) {
		// TODO Maybe Random should be a field ?
		return new Random().nextInt(max);
	}

	public String getRandomPassword() {
		Random r = new Random();
		return Long.toString(Math.abs(r.nextLong()), 36);
	}
}
