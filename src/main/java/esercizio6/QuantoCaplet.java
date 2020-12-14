package esercizio6;


public class QuantoCaplet {

	/*
	 * Field specifying parameters of the model
	 */
	private final double sigmaLibor;
	private final double sigmaFfx;
	private final double covariationLF;
	private final double strike;
	private final double forwardToday;
	private final double maturity;
	private final double fixing;

	/*
	 * Constructor
	 */
	public QuantoCaplet(double sigmaLibor, double sigmaFfx, double covariationLF, double strike, double forwardToday, double maturity, double fixing) {
		this.sigmaLibor = sigmaLibor;
		this.sigmaFfx = sigmaFfx;
		this.covariationLF = covariationLF;
		this.strike = strike;
		this.forwardToday = forwardToday;
		this.maturity = maturity;
		this.fixing = fixing;
	}

	/*
	 * Getting terminal value of our Quanto Caplet
	 */
	// Value today:
	public double getValueToday(double domesticDiscountFactor, double exchangeRate) {
		// Here the exchange rate is assumed to be foreign to domestic!
		return getTerminalValue()*domesticDiscountFactor*exchangeRate;
	}
	// Value in T2
	public double getTerminalValue() {
		double vol;
		double adjustedForward;
		adjustedForward = Math.exp(-fixing*covariationLF*sigmaFfx*sigmaLibor)*forwardToday;
		vol = sigmaLibor;
		Caplet myForeignCaplet = new Caplet(vol, strike, adjustedForward, maturity, fixing);
		return myForeignCaplet.getTerminalValue();
	}

}
