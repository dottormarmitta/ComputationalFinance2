package esercizio5;

import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class RateConverter {

	/*
	 * Fields.
	 * Note we assume the following structure of given rates:
	 * Bond  = [P(T1,0), P(T2,0), ..., P(Tn,0)]
	 * Libor = [L(T1,0,0), L(T2,T1,0), ..., L(Tn, Tn-1, 0)]
	 */
	private enum RateType {
		Bond, Libor
	}
	private final double[] ratesArray;
	private RateType rateType;
	private final TimeDiscretization rateTimes;

	/*
	 * Constructors
	 */
	public RateConverter(double[] rates, String type , TimeDiscretization rateTimes) {
		this.ratesArray = rates;
		if (type == "bond" || type == "Bond" || type == "b" || type == "B" || type == "bnd" || type == "BOND") {
			this.rateType = RateType.Bond;
		} else {
			this.rateType = RateType.Libor;
		}
		this.rateTimes = rateTimes;
	}
	public RateConverter(double[] rates, String type , double[] rateTimes) {
		this(rates, type, new TimeDiscretizationFromArray(rateTimes));
	}
	public RateConverter(double[] rates, String type , double rateFraction) {
		this(rates, type, new TimeDiscretizationFromArray(rateFraction, rates.length, rateFraction));
	}

	/*
	 * Converter
	 */
	public double[] getRateConverted() {

		double[] convertedRate = new double[ratesArray.length];

		switch(rateType) {

		case Libor:
			/*
			 * In this case we have to return bond prices!!
			 */
			// We need to come up with the first price:
			convertedRate[0] = Math.pow(1 + ratesArray[0]*rateTimes.getTime(0), -1.0);
			// We can start computing with recursion:
			for (int t = 1; t < ratesArray.length; t++) {
				convertedRate[t] = Math.pow(1 + ratesArray[t]*rateTimes.getTimeStep(t-1), -1.0)*convertedRate[t-1];
			}
			break;

		case Bond:
			/*
			 * In this case we return Libors!!
			 */
			// We need to come up with first Libor:
			convertedRate[0] = Math.pow(rateTimes.getTime(0), -1.0)*(1/ratesArray[0] - 1);
			for (int t = 1; t < ratesArray.length; t++) {
				convertedRate[t] = Math.pow(rateTimes.getTimeStep(t-1), -1.0)*((ratesArray[t-1]/ratesArray[t]) - 1);
			}
		}
		return convertedRate;
	}


}
