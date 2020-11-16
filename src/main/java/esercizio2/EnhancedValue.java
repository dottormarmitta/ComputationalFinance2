package esercizio2;

public interface EnhancedValue {
	/**
	 * Applies x*x to this object x.
	 *
	 * @return New object representing the result.
	 */
	EnhancedValue squared();

	/**
	 * Applies sqrt(x) to this object x.
	 *
	 * @return New object representing the result.
	 */
	EnhancedValue sqrt();

	/**
	 * Applies a+x to this object a.
	 *
	 * @return New object representing the result.
	 */
	EnhancedValue add(EnhancedValue x);

	/**
	 * Applies a-x to this object a.
	 *
	 * @return New object representing the result.
	 */
	EnhancedValue sub(EnhancedValue x);

	/**
	 * Applies a*x to this object a.
	 *
	 * @return New object representing the result.
	 */
	EnhancedValue mult(EnhancedValue x);

	/**
	 * Applies a/x to this object a.
	 *
	 * @return New object representing the result.
	 */
	EnhancedValue div(EnhancedValue x);

	/**
	 * Applies exp(x) to this object
	 *
	 * @return new object representing the result
	 */
	EnhancedValue exp();

	/**
	 * Applies ??
	 *
	 * @return ??
	 */
	EnhancedValue addProduct(EnhancedValue x, EnhancedValue y);

}
