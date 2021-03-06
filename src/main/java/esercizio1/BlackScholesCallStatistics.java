package esercizio1;

import java.util.Random;

import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.products.AbstractAssetMonteCarloProduct;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.stochastic.RandomVariable;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;

public class BlackScholesCallStatistics {

	public static void main(String[] args) throws CalculationException {

		// Assignment a):
		final double initialTime = 0.0;
		final double maturity = 3.5;
		final int numberOfSteps = 1000;
		final double deltaT = maturity / numberOfSteps;
		TimeDiscretization myTimeDiscretization = new TimeDiscretizationFromArray(initialTime, numberOfSteps, deltaT);
		final int numberOfPaths = 10000;
		final double stock0 = 116.32;
		final double rf = 0.02;
		final double vol = 0.32;
		MonteCarloBlackScholesModel bsModel = new MonteCarloBlackScholesModel(myTimeDiscretization, numberOfPaths, stock0, rf, vol);

		// Assignment b):
		Random randomGenerator = new Random();
		final int numberOfPrices = 100;
		final double[] myDoublesArray = new double[numberOfPrices];

		// Assignment c):
		final double callStrike = 115.00;
		final AbstractAssetMonteCarloProduct myAppleCall = new EuropeanOption(3.5, callStrike);
		myDoublesArray[0] = myAppleCall.getValue(bsModel);
		// It is better to initialize here line 46 & 47 with
		AssetModelMonteCarloSimulationModel bsModelI; // Always declare the most general type!!
		int seed;

		// Assignment d):
		for (int i = 1; i < numberOfPrices; i++) {
			seed = randomGenerator.nextInt();
			//AssetModelMonteCarloSimulationModel bsModelI = bsModel.getCloneWithModifiedSeed(j);
			bsModelI = bsModel.getCloneWithModifiedSeed(seed);
			myDoublesArray[i] = myAppleCall.getValue(bsModelI);
		}

		// Assignment e):
		RandomVariable myRandomVariable = new RandomVariableFromDoubleArray(0.0, myDoublesArray); // Line 166 of RVFDA class

		// Assignment f):
		System.out.println("The average is:  " + myRandomVariable.getAverage());
		System.out.println("The maximum is:  " + myRandomVariable.getMax());
		System.out.println("The minimum is:  " + myRandomVariable.getMin());
		System.out.println("The variance is: " + myRandomVariable.getSampleVariance());
		System.out.println("The analytic value of the call is: " + AnalyticFormulas.blackScholesOptionValue(stock0, rf, vol, maturity, callStrike));

		/*
		 * Assignment g):
		 *
		 * I notice that increasing numberOfPaths (line 25) the average of my prices gets closer and closer to the analytical value of
		 * the call. Moreover the variance is extremely reduced!
		 */

	}

}
