package esercizio2;

public class EnhancedValueDouble implements EnhancedValue, ConvertableToFloatingPoint {

	private Double value;

	public EnhancedValueDouble(Double value) {
		this.value = value;
	}

	/*
	 * Here I had implemented the method (it was in ConvertableToFloatingPoint)
	 */
	@Override
	public Double asFloatingPoint() {
		return value;
	}

	@Override
	public EnhancedValue squared() {
		return new EnhancedValueDouble(value * value);
	}

	@Override
	public EnhancedValue sqrt() {
		return new EnhancedValueDouble(Math.sqrt(value));
	}

	@Override
	public EnhancedValue add(EnhancedValue x) {
		return new EnhancedValueDouble(this.value + valueOf(x));
	}

	private static Double valueOf(EnhancedValue x) {
		return ((ConvertableToFloatingPoint)x).asFloatingPoint();
	}


	@Override
	public EnhancedValue sub(EnhancedValue x) {
		return new EnhancedValueDouble(this.value - valueOf(x));
	}

	@Override
	public EnhancedValue mult(EnhancedValue x) {
		return new EnhancedValueDouble(this.value * valueOf(x));
	}

	@Override
	public EnhancedValue div(EnhancedValue x) {
		return new EnhancedValueDouble(this.value / valueOf(x));
	}

	@Override
	public EnhancedValue exp() {
		return new EnhancedValueDouble(Math.exp(this.value));
	}

	@Override
	public EnhancedValue addProduct(EnhancedValue x, EnhancedValue y) {
		return new EnhancedValueDouble(this.value + valueOf(x)*valueOf(y));
	}

}
