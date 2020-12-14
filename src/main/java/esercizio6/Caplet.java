package esercizio6;

import net.finmath.functions.NormalDistribution;

public class Caplet {

	/*
	 * Field specifying parameters of the model
	 */
	private final double sigma;
	private final double strike;
	private final double forwardToday;
	private final double maturity;
	private final double fixing;

	/*
	 * Constructor
	 */
	public Caplet(double sigma, double strike, double forwardToday, double maturity, double fixing) {
		this.sigma = sigma;
		this.strike = strike;
		this.forwardToday = forwardToday;
		this.maturity = maturity;
		this.fixing = fixing;
	}

	/*
	 * Getting value of the Caplet:
	 */
	// Value today
	public double getValueToday(double discountFactor) {
		return discountFactor*getTerminalValue();
	}
	// Value in T2
	public double getTerminalValue() {
		double dPlus  = getDPlus();
		double dMinus = getDMinus();
		double liborWeighted = this.forwardToday*NormalDistribution.cumulativeDistribution(dPlus);
		double strikeWeighted = this.strike*NormalDistribution.cumulativeDistribution(dMinus);
		double periodLength = maturity - fixing;
		return (liborWeighted - strikeWeighted)*periodLength;
	}

	/*
	 * Black model useful methods:
	 */
	private double getDPlus() {
		double numerator = Math.log(this.forwardToday/this.strike) +
				0.5*this.sigma*this.sigma*this.fixing;
		double denominator = this.sigma*Math.sqrt(this.fixing);
		return numerator/denominator;
	}
	private double getDMinus() {
		double numerator = Math.log(this.forwardToday/this.strike) -
				0.5*this.sigma*this.sigma*this.fixing;
		double denominator = this.sigma*Math.sqrt(this.fixing);
		return numerator/denominator;
	}



}
