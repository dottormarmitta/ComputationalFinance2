package esercizio5;

import java.util.Map;

import net.finmath.rootfinder.BisectionSearch;

public class Bootstrap {

	/*
	 * Fields:
	 */
	// Here I store par rates in this way: s3 -> (t, s3)
	Map<Double, Double> parRates;
	// I need my time step:
	double timeStep;
	// Here I store bond. I build a map: b(T_i, 0) -> (t, b(T_i, 0))
	Map<Double, Double> firstBonds;
	// I store in a double the sum of term structure
	private double sumOfStucture = 0.0;

	/*
	 * Constructors
	 */
	public Bootstrap(Map<Double, Double> bonds, Map<Double, Double> parRates, double timeStep) {
		this.parRates = parRates;
		this.firstBonds = bonds;
		this.timeStep = timeStep;
	}

	/*
	 * Method to get the bond curve:
	 */
	public Map<Double, Double> getBondStructure() {

		Map<Double, Double> currentBond   = this.firstBonds;
		Map<Double, Double> localParRates = this.parRates;

		// I start iterating (knowing that I know the first two bonds).
		// I can know every P(t_i, 0) as long as T_1 = n (i.e. until I
		// have an available S_i).

		double t = 3.0*timeStep; // This is first bond
		while (!localParRates.isEmpty()) {
			if(parRates.get(t) != null) { // If I have par rate for this time, I am able to compute
				sumOfStucture += currentBond.get(t	- timeStep);
				if (currentBond.get(t) == null) {
					currentBond.put(t, (currentBond.get(timeStep) - sumOfStucture*parRates.get(t)*timeStep)/(1 + parRates.get(t)*timeStep));
					localParRates.remove(t);
					t += timeStep;
				}
			} else { // I am not able anymore to go with usual method but I need bisection!!!
				sumOfStucture += currentBond.get(t - timeStep);
				currentBond = goWithBisection(currentBond, t, currentBond, parRates.get(t+timeStep));
				sumOfStucture += currentBond.get(t);
				localParRates.remove(t+timeStep);
				t += 2*timeStep;
			}
		}

		return currentBond;
	}

	/*
	 * Bisection method:
	 */
	private Map<Double, Double> goWithBisection(Map<Double, Double> Bond, double t, Map<Double, Double> currentBond2, double swapRate) {
		Map<Double, Double> currentBond   = Bond;
		BisectionSearch rootFinder = new BisectionSearch(0.0001, currentBond.get(t	- timeStep));
		while(!rootFinder.isDone()) {
			final double x = rootFinder.getNextPoint();
			//value of the difference between for the new trial
			final double y = differenceSwapRateAtMissingBond(swapRate, x, currentBond, t);
			rootFinder.setValue(y);
		}
		currentBond.put(t+timeStep, rootFinder.getBestPoint());
		currentBond.put(t, interpolate(currentBond.get(t-timeStep), currentBond.get(t+timeStep)));
		return currentBond;

	}

	/*
	 * Interpolation method:
	 */
	private Double interpolate(double bondT0, double bondT1) {
		return Math.exp(0.5*(Math.log(bondT0) + Math.log(bondT1)));
	}

	/*
	 * Function builder:
	 */
	private double differenceSwapRateAtMissingBond(double swapRate, double missingBond, Map<Double, Double> currentBond, double t) {
		/*
		 * By means of the root finder algorithm, a value of missingBond will be computed in order
		 * to the following quantity to be close to zero
		 */
		return (firstBonds.get(timeStep) - missingBond) /
				(timeStep * (sumOfStucture + interpolate(currentBond.get(t	- timeStep), missingBond) + missingBond)) - swapRate;
	}


}
