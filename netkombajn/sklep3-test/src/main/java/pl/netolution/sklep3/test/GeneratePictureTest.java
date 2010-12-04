package pl.netolution.sklep3.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GeneratePictureTest {
	public static void main(String[] args) {

		try {
			System.out.println("zgrywanie obrazka");
			//URL pictureUrl = new URL("http://online.incom.pl/NBWeb/Produkty/TowarObraz.aspx?symbol=ZUAXPCIA0100");
			URL pictureUrl = new URL("http://www.wp.pl");
			System.out.println(pictureUrl.getContent());
			InputStream is = pictureUrl.openStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String theLine;
			while ((theLine = br.readLine()) != null) {
				System.out.println(theLine);
			}
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
