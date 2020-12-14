package esercizio6;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class CapletTest {

	@Test
	void test() {
		double sigma = 0.20;
		double strike = 0.0091;
		double forwardToday = 0.0099;
		double maturity = 2.5;
		double fixing = 1.5;
		double discountFactor = 0.98;

		// My value:
		Caplet myCaplet = new Caplet(sigma, strike, forwardToday, maturity, fixing);
		double myValueToday = myCaplet.getValueToday(discountFactor);

		// Finmath value:
		double finmathCaplet =
				net.finmath.functions.AnalyticFormulas.blackModelCapletValue(forwardToday, sigma, fixing, strike, maturity - fixing, discountFactor);
		System.out.println("My value is      " + myValueToday);
		System.out.println("Finmath value is " + finmathCaplet);

		// Test:
		Assert.assertEquals(finmathCaplet, myValueToday, 10E-15);

		/*
		 * Quanto caplet:
		 */
		double sigmaLibor = 0.3;
		double sigmaFfx = 0.2;
		double covariationLF = 0.4;
		double strike1 = 0.044;
		double forwardToday1 = 0.05;
		double maturity1 = 2.0;
		double fixing1 = 1.0;
		double exchangeRate = 0.9;
		double notional = 10000;
		double domesticDiscount = 0.91;

		QuantoCaplet myQuantoCaplet = new QuantoCaplet(sigmaLibor, sigmaFfx, covariationLF, strike1, forwardToday1, maturity1, fixing1);
		System.out.println("Qaunto caplet value: " + notional*myQuantoCaplet.getValueToday(domesticDiscount, exchangeRate));
	}

}

