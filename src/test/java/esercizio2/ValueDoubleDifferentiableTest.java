package esercizio2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ValueDoubleDifferentiableTest {

	// Simple trial
	@Test
	void test1() {
		EnhancedValueDifferentiable x0 = new EnhancedValueDoubleDifferentiable(6.0);
		EnhancedValueDifferentiable x1 = new EnhancedValueDoubleDifferentiable(2.0);

		EnhancedValue y = x0.div(x1);

		Double derivativeAnalytic = 1.0 / 2.0;
		EnhancedValue derivativeAlgorithmic = ((EnhancedValueDifferentiable)y).getDerivativeWithRespectTo(x0);
		assertEquals(derivativeAnalytic, EnhancedValueDoubleDifferentiable.valueOf(derivativeAlgorithmic), 1E-15, "partial derivative dy/dx");
	}

	// Trial of dy/da where y = exp(a*a + a*b*b) using a = 1 and b = 2
	@Test
	void test2() {
		EnhancedValueDifferentiable a = new EnhancedValueDoubleDifferentiable(2.0);
		EnhancedValueDifferentiable b = new EnhancedValueDoubleDifferentiable(3.0);

		EnhancedValue y = (a.squared().addProduct(a, b.squared())).exp();

		Double derivativeAnalytic = 13.0 * Math.exp(22);
		EnhancedValue derivativeAlgorithmic = ((EnhancedValueDifferentiable)y).getDerivativeWithRespectTo(a);
		assertEquals(derivativeAnalytic, EnhancedValueDoubleDifferentiable.valueOf(derivativeAlgorithmic), 1E-15, "partial derivative dy/dx");
	}

	// Part of handout3: exercise 1
	@Test
	void TestAdditionWithLoop() {
		/*
		 * With these values, I get error in the first test (line 48)
		 */
		int n = 102;
		double value = 12.55455;
		EnhancedValueDoubleDifferentiable x = new EnhancedValueDoubleDifferentiable(value);
		EnhancedValueDoubleDifferentiable fx = new EnhancedValueDoubleDifferentiable(0.0);
		for (int i = 1; i <= n; i++) {
			fx = (EnhancedValueDoubleDifferentiable) fx.add(x);
		}
		assertEquals(value*n, fx.asFloatingPoint(), 1E-15);
		EnhancedValue derivativeAlgorithmic = fx.getDerivativeWithRespectTo(x);
		assertEquals((n), EnhancedValueDoubleDifferentiable.valueOf(derivativeAlgorithmic));
		System.out.print(EnhancedValueDoubleDifferentiable.valueOf(derivativeAlgorithmic));
		// Warning: I had to add the method asFloatingPoint() to the interface EnhancedValue
		// but I also had it already implemented in the class EnhancedValueDoublev which is
		// IMPLEMENTING EnhancedValue (together with the class ConvertableToFloatingPoint).

	}



}
