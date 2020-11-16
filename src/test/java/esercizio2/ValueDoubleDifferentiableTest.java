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
		EnhancedValueDifferentiable a = new EnhancedValueDoubleDifferentiable(1.0);
		EnhancedValueDifferentiable b = new EnhancedValueDoubleDifferentiable(2.0);

		EnhancedValue y = (a.squared().addProduct(a, b.squared())).exp();

		Double derivativeAnalytic = 6.0 * Math.exp(5);
		EnhancedValue derivativeAlgorithmic = ((EnhancedValueDifferentiable)y).getDerivativeWithRespectTo(a);
		assertEquals(derivativeAnalytic, EnhancedValueDoubleDifferentiable.valueOf(derivativeAlgorithmic), 1E-15, "partial derivative dy/dx");
	}



}
