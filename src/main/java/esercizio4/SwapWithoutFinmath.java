package esercizio4;

import com.andreamazzon.exercise4.Swap;

import esercizio5.RateConverter;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class SwapWithoutFinmath implements Swap {

	/*
	 * Throughout, recall that the payment structure of the swap is the following:
	 *
	 *                 ^              ^
	 * 				   |       ^      |
	 * 	               |       |      |
	 *   |------|------|--\/\--|------|---->
	 *   0      T1     T2      Tn-1   Tn
	 *
	 *   So we have no payments at T1 and the first payment is at T2
	 */

	/*
	 * Fields of the class
	 */
	private final TimeDiscretization swapTimes;
	private double[] termStructure;
	private final int curveLength;

	/*
	 * First I need to set up the constructors
	 */
	// With TimeDiscretization:
	public SwapWithoutFinmath(TimeDiscretization swapTimes, double[] givenTerm, boolean isBondCurve) {
		this.swapTimes = swapTimes;
		this.curveLength = givenTerm.length;
		// What happens if not bond curve is given? It is given Libor!!
		this.termStructure = isBondCurve ? givenTerm : liborToBond(givenTerm);
	}
	// With array of doubles:
	public SwapWithoutFinmath(double[] swapTimes, double[] givenTerm, boolean isBondCurve) {
		this(new TimeDiscretizationFromArray(swapTimes), givenTerm, isBondCurve);
	}
	// With constant time step:
	public SwapWithoutFinmath(double yearFraction, double[] givenTerm, boolean isBondCurve) {
		this(new TimeDiscretizationFromArray(yearFraction, givenTerm.length, yearFraction), givenTerm, isBondCurve);
	}

	/*
	 * To be added different constructor according to different input possibilities
	 */

	/*
	 * We have the relationship L(T-1, T) = (1/DT)*((P(T - 1) - P(T))/P(T))
	 */
	private double[] liborToBond(double[] libors) {

		RateConverter rateConverter = new RateConverter(libors, "Libors", this.swapTimes);

		return rateConverter.getRateConverted();
	}

	/*
	 * Get annuities:
	 * Recall that annuity is \sum_{i = 1}^{n-1}(Dt*P(Dt))
	 */
	private double getAnnuity() {

		double annuity = 0.0;

		for (int t = 1; t < curveLength; t++) {
			annuity += termStructure[t]*swapTimes.getTimeStep(t - 1);
		}

		return annuity;
	}
	// Efficient if evenly distributed:
	private double getAnnuity(double yearFraction) {

		double annuity = 0.0;

		for (int t = 1; t < curveLength; t++) {
			annuity += termStructure[t];
		}

		return annuity*yearFraction;
	}



	@Override
	public double getSwapValue(double[] swapRates) {

		// Floating leg value: P(1) - P(n)
		double floatingLegValue = termStructure[0] - termStructure[curveLength -1];

		// Fixed leg value is sum of discounted swap rates:
		double fixedLegValue = 0.0;
		for (int i = 0; i < swapRates.length; i++) {
			fixedLegValue += swapRates[i]*termStructure[i+1]*swapTimes.getTimeStep(i);
		}

		// We pay fix and receive float, hence:
		return floatingLegValue - fixedLegValue;
	}

	@Override
	public double getSwapValue(double[] swapRates, double yearFraction) {

		// Floating leg value: P(1) - P(n)
		double floatingLegValue = termStructure[0] - termStructure[curveLength -1];

		// Fixed leg value is sum of discounted swap rates:
		double fixedLegValue = 0.0;
		for (int i = 0; i < swapRates.length; i++) {
			fixedLegValue += swapRates[i]*termStructure[i+1];
		}

		// We pay fix and receive float, hence:
		return floatingLegValue - fixedLegValue*yearFraction;
	}

	@Override
	public double getSwapValue(double singleSwapRate) {

		// Floating leg value: P(1) - P(n)
		double floatingLegValue = termStructure[0] - termStructure[curveLength -1];

		// Fixed leg value is sum of discounted swap rates:
		double fixedLegValue = 0.0;
		for (int i = 1; i < curveLength; i++) {
			fixedLegValue += singleSwapRate*termStructure[i]*swapTimes.getTimeStep(i-1);
		}

		// We pay fix and receive float, hence:
		return floatingLegValue - fixedLegValue;
	}

	@Override
	public double getSwapValue(double singleSwapRate, double yearFraction) {

		// Floating leg value: P(1) - P(n)
		double floatingLegValue = termStructure[0] - termStructure[curveLength -1];

		// Fixed leg value is sum of discounted swap rates:
		double fixedLegValue = 0.0;
		for (int i = 1; i < curveLength; i++) {
			fixedLegValue += singleSwapRate*termStructure[i];
		}

		// We pay fix and receive float, hence:
		return floatingLegValue - fixedLegValue*yearFraction;
	}

	@Override
	public double getParSwapRate() {
		return (termStructure[0] - termStructure[curveLength -1]) / getAnnuity();
	}

	@Override
	public double getParSwapRate(double yearFraction) {
		return (termStructure[0] - termStructure[curveLength -1]) / getAnnuity(yearFraction);
	}

}
