package Esercizio4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.andreamazzon.exercise4.Swap;

import esercizio4.SwapWithoutFinmath;
import esercizio5.RateConverter;

class SwapTest {
	@Test
	void test() {
		double[] bondCurve = { 0.9986509108, 0.9949129829, 0.9897033769, 0.9835370208, 0.9765298116, 0.9689909565 };

		// First let us derive par yield for a swap with S = 0 and evenly distributed payments dt = 0.5
		Swap mySwap = new SwapWithoutFinmath(0.5, bondCurve, true);
		System.out.println("The par rate is:      " + mySwap.getParSwapRate(0.5));
		System.out.println("The par should be is: " + 0.012072414003214715);
		assertEquals(0.012072414003214715, mySwap.getParSwapRate(0.5));
		// Let us evaluate the value of the swap with the par rate: it should be zero!!
		double precision = 10E-15;
		double valueOfSwap = mySwap.getSwapValue(mySwap.getParSwapRate(0.5));
		System.out.println("The value of the swap paying the par is: " + valueOfSwap);
		assertEquals(0.0, valueOfSwap, precision);

		System.out.println();

		// Not even times:
		double[] times = {0.5, 1, 1.5, 2, 3, 3.5};
		Swap notEvenSwap = new SwapWithoutFinmath(times, bondCurve, true);
		double parRate = notEvenSwap.getParSwapRate();
		System.out.println("The par rate is:      " + parRate);
		System.out.println("The par should be is: " + 0.010070943043707224);
		assertEquals(0.010070943043707224, parRate);
		// Let us evaluate the value of the swap with the par rate: it should be zero!!
		double valueOfSwap2 = notEvenSwap.getSwapValue(parRate);
		System.out.println("The value of the swap paying the par is: " + valueOfSwap2);
		assertEquals(0.0, valueOfSwap2, precision);

		System.out.println();
		System.out.println("Now we try to convert Bond to floating: ");

		// Handout5
		RateConverter myRateConv = new RateConverter(bondCurve, "bond", times);
		double[] libors = myRateConv.getRateConverted();
		for (double libor : libors) {
			System.out.println(libor);
		}

		// Test whether the par rate is the same:
		Swap notEvenSwapLibor = new SwapWithoutFinmath(times, libors, false);
		double parRateLibor = notEvenSwapLibor.getParSwapRate();
		assertEquals(parRateLibor, parRate, precision);

	}

}
