package esercizio5;

import java.util.HashMap;
import java.util.Map;

public class BootsrapTrial {

	public static void main(String[] args) {

		/*
		 * I find convenient to store the price of a bond, namely P(t, 0),
		 * in a map where the keys are the time instants and the values the
		 * value of the bond. In this way I can refer at any moment to the
		 * price of the bond of the time instant wanted!
		 */
		Map<Double, Double> bonds = new HashMap<>();

		bonds.put(0.5, 0.98); // P(T_1, 0)
		bonds.put(1.0, 0.975); // P(T_2, 0)

		/*
		 * I find convenient to store the par rates, namely S(t),
		 * in a map where the keys are the time instants and the values the
		 * value of the swap rate. In this way I know exactly the time
		 * in which I am operating!
		 */
		Map<Double, Double> parRates = new HashMap<>();
		parRates.put(1.5, 0.0086); //s_3
		parRates.put(2.0, 0.0077);
		parRates.put(2.5, 0.0073);
		parRates.put(3.0, 0.0084);
		parRates.put(4.0, 0.0075);
		parRates.put(5.0, 0.0085); //s_10
		parRates.put(6.0, 0.0095);
		parRates.put(7.0, 0.0092); //s_14
		double timeStep = 0.5;

		Bootstrap myObject = new Bootstrap(bonds, parRates, timeStep);
		System.out.println(myObject.getBondStructure());

	}

}
