package esercizio1;

import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class AssetOrNothingCheck {

	public static void main(String[] args) throws CalculationException {
		final double stock0 = 100.00;
		final double strike = 100.00;
		final double volatility = 0.3;
		//final double myTimeHoriz = 1.75;
		final double T = 3.0;
		final int numberOfSimulation = 2000000;
		
		// I create my time discretization model
		final double initialTime = 0;
		final int numberOfTimeSteps = 100;
		final double timeStep = T / numberOfTimeSteps;
		final TimeDiscretization myTimeDiscretization = new TimeDiscretizationFromArray(initialTime,
				numberOfTimeSteps, timeStep);
		
		
		// I now build my BlackScholes model:
		final AssetModelMonteCarloSimulationModel bsModel = new MonteCarloBlackScholesModel(
				myTimeDiscretization, numberOfSimulation, stock0, 0.0, volatility);
		
		// I create my option
		AssetOrNothing myOptiAssetOrNothing = new AssetOrNothing(T, strike);
		
		// I build the evaluation model
		double value = myOptiAssetOrNothing.getValue(bsModel);
		System.out.println("My asset value is: " + (1/stock0)*value);
		
		// Analytic:
		double delta = net.finmath.functions.AnalyticFormulas.blackScholesOptionDelta(stock0, 0, volatility, T, strike);
		System.out.println("My delta is: " + delta);

	}

}
