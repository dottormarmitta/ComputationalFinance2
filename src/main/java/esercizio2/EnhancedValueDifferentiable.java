package esercizio2;

public interface EnhancedValueDifferentiable extends EnhancedValue {

	/**
	 * Takes the derivative of the object with respect to x
	 *
	 * @return the result in a new object
	 */
	EnhancedValue getDerivativeWithRespectTo(EnhancedValueDifferentiable x);

}
