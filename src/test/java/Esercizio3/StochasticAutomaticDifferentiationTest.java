package Esercizio3;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import org.junit.jupiter.api.Test;

import net.finmath.montecarlo.RandomVariableFromDoubleArray;
import net.finmath.montecarlo.automaticdifferentiation.RandomVariableDifferentiable;
import net.finmath.montecarlo.automaticdifferentiation.backward.RandomVariableDifferentiableAAD;
import net.finmath.montecarlo.automaticdifferentiation.forward.RandomVariableDifferentiableAD;
import net.finmath.stochastic.RandomVariable;

class StochasticAutomaticDifferentiationTest {
	// @TODO:
	/*
	 * THIS EX. SHEET HAS TO BE FIXED!!!!
	 */

	// a)
	private int len = 100;
	private RandomVariable constructAndReturnRandomVariable() {
		Random randomizer = new Random(47);
		double[] realisations = new double[len];
		for (int i = 0; i < len; i++) {
			realisations[i] = randomizer.nextDouble()*2;
		}
		RandomVariable myRandomVariable = new RandomVariableFromDoubleArray(0.0, realisations);
		return myRandomVariable;
	}

	// b)
	RandomVariable xValue = constructAndReturnRandomVariable();
	RandomVariable yValue = constructAndReturnRandomVariable();
	RandomVariableDifferentiable xBackward = new RandomVariableDifferentiableAAD(new RandomVariableDifferentiableAAD(xValue));
	RandomVariableDifferentiable yBackward = new RandomVariableDifferentiableAAD(new RandomVariableDifferentiableAAD(yValue));
	/*
	 * DUBBIO: qual è la differenza tra la linea 35 e tra la 36?
	 */
	RandomVariableDifferentiable xForward  = new RandomVariableDifferentiableAD(xValue);
	RandomVariableDifferentiable yForward  = new RandomVariableDifferentiableAD(new RandomVariableDifferentiableAD(yValue));

	// c)
	RandomVariableDifferentiable resultBackward = (RandomVariableDifferentiable) xBackward.squared().addProduct(xBackward, yBackward.squared()).exp();
	RandomVariableDifferentiable resultForward  = (RandomVariableDifferentiable) xForward.squared().addProduct(xForward, yForward.squared()).exp();


	// d)
	private long getBackwardGradient(RandomVariableDifferentiable result) {
		Instant start = Instant.now();
		result.getGradient();
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		return timeElapsed.getSeconds();
	}
	private long getForwardGradient(RandomVariableDifferentiable result) {
		Instant start1 = Instant.now();
		result.getGradient();
		Instant end1 = Instant.now();
		Duration timeElapsed = Duration.between(start1, end1);
		return timeElapsed.getSeconds();
	}

	@Test
	void test() {
		long timeBckw = 0;
		long timeFwd  = 0;
		for (int i = 0; i < 1000; i++) {
			timeBckw += getBackwardGradient(resultBackward);
			System.out.println(timeBckw);
			timeFwd  += getForwardGradient(resultForward);
		}
		System.out.println("Average time took for Bck is: " + timeBckw/1000);
		System.out.println("Average time took for Fwd is: " + timeFwd/1000);
	}

}
