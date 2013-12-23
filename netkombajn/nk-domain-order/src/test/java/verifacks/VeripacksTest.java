package verifacks;

import org.junit.Test;
import org.veripacks.VeripacksBuilder$;


public class VeripacksTest {

	@Test
	public void runVeripacksTest() {
		  VeripacksBuilder$.MODULE$.build()
		    .verify("com.netkombajn.eshop")
		    .throwIfNotOk();
		}
}
