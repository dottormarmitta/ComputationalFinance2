package esercizio1;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.stochastic.RandomVariable;
import net.finmath.stochastic.Scalar;

public class AssetOrNothing extends net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct {
	
	// Parameters
	final double maturity;
	final double strike;
			
			
	// Constructor
	public AssetOrNothing(double maturity, double strike) {
		this.maturity = maturity;
		this.strike = strike;
	}
			

	@Override
	public RandomVariable getValue(double evaluationTime, AssetModelMonteCarloSimulationModel model)
			throws CalculationException {
		
		// I get value at S(T):
		RandomVariable underlyingAtMaturity = model.getAssetValue(maturity, 0);
		
		// I have to build the payoff function bearing in mind the indicator {S(T) > K}
				RandomVariable values = underlyingAtMaturity.sub(strike).choose(underlyingAtMaturity, new Scalar(0.0));
				
		// I discount the payoffs to time zero:
		RandomVariable numeraireAtMaturity	= model.getNumeraire(maturity);
		RandomVariable monteCarloWeights    = model.getMonteCarloWeights(maturity);
		values = values.div(numeraireAtMaturity).mult(monteCarloWeights);
		
		
		// I compound until the moment in which I am:
		RandomVariable	numeraireAtEvalTime			= model.getNumeraire(evaluationTime);
		RandomVariable	monteCarloWeightsAtEvalTime	= model.getMonteCarloWeights(evaluationTime);
		values = values.mult(numeraireAtEvalTime).div(monteCarloWeightsAtEvalTime);
				
		// I return what I have:
		return values;
	}

}
