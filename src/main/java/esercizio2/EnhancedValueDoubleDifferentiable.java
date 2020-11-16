package esercizio2;




import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

public class EnhancedValueDoubleDifferentiable implements EnhancedValueDifferentiable, ConvertableToFloatingPoint {

	private enum Operator {
		SQUARED, SQRT, ADD, SUB, MULT, DIV, EXP, ADDPROD
	}

	private static AtomicLong nextId = new AtomicLong();

	private Double value;
	private Operator operator;
	private List<EnhancedValueDoubleDifferentiable> arguments;
	private Long id;

	/**
	 * Creates a node from an operation.
	 *
	 * @param value Value of this node.
	 * @param operator Operator that lead to this value.
	 * @param arguments Arguments that were used in this operation.
	 */
	private EnhancedValueDoubleDifferentiable(Double value, Operator operator, List<EnhancedValueDoubleDifferentiable> arguments) {
		super();
		this.value = value;
		this.operator = operator;
		this.arguments = arguments;
		this.id = nextId.getAndIncrement();
	}

	/**
	 * Creates a node from a constant - a leaf node.
	 *
	 * @param value Value of this node.
	 */
	public EnhancedValueDoubleDifferentiable(Double value) {
		this(value, null, null);
	}

	/*
	 * The operations, implementing the interface.
	 */

	@Override
	public EnhancedValue squared() {
		return new EnhancedValueDoubleDifferentiable(value * value, Operator.SQUARED, List.of(this));
	}

	@Override
	public EnhancedValue sqrt() {
		return new EnhancedValueDoubleDifferentiable(Math.sqrt(value), Operator.SQRT, List.of(this));
	}

	@Override
	public EnhancedValue add(EnhancedValue x) {
		return new EnhancedValueDoubleDifferentiable(value + valueOf(x), Operator.ADD, List.of(this, (EnhancedValueDoubleDifferentiable)x));
	}

	@Override
	public EnhancedValue sub(EnhancedValue x) {
		return new EnhancedValueDoubleDifferentiable(value - valueOf(x), Operator.SUB, List.of(this, (EnhancedValueDoubleDifferentiable)x));
	}

	@Override
	public EnhancedValue mult(EnhancedValue x) {
		return new EnhancedValueDoubleDifferentiable(value * valueOf(x), Operator.MULT, List.of(this, (EnhancedValueDoubleDifferentiable)x));
	}

	@Override
	public EnhancedValue div(EnhancedValue x) {
		return new EnhancedValueDoubleDifferentiable(value / valueOf(x), Operator.DIV, List.of(this, (EnhancedValueDoubleDifferentiable)x));
	}

	@Override
	public EnhancedValue exp() {
		return new EnhancedValueDoubleDifferentiable(Math.exp(value), Operator.EXP, List.of(this));
	}

	@Override
	public EnhancedValue addProduct(EnhancedValue x, EnhancedValue y) {
		return new EnhancedValueDoubleDifferentiable(valueOf(x)*valueOf(y) + value, Operator.ADDPROD, List.of(this, (EnhancedValueDoubleDifferentiable)x, (EnhancedValueDoubleDifferentiable)y));
	}

	@Override
	public Double asFloatingPoint() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	/**
	 * Get the derivatives of a node with respect to all input nodes via a backward algorithmic differentiation (adjoint differentiation).
	 *
	 * @return A map x -> D which gives D = dy/dx, where y is this node and x is any input node.
	 */
	public Map<EnhancedValueDifferentiable, Double> getDerivativeWithRespectTo() {
		// The map that will contain the derivatives x -> dy/dx				// The map contains in iteration m the values d F
		Map<EnhancedValueDifferentiable, Double> derivativesWithRespectTo = new HashMap<>();
		// Init with dy / dy = 1
		derivativesWithRespectTo.put(this, 1.0);

		// This creates a set (queue) of objects, sorted ascending by their getID() value (last = highest ID)
		NavigableSet<EnhancedValueDoubleDifferentiable> nodesToProcess = new TreeSet<>((o1,o2) -> o1.getID().compareTo(o2.getID()));

		// Add the root note
		nodesToProcess.add(this);

		// Walk down the tree, always removing the node with the highest id and adding their arguments
		while(!nodesToProcess.isEmpty()) {

			// Get and remove the top most node.
			EnhancedValueDoubleDifferentiable currentNode = nodesToProcess.pollLast();

			List<EnhancedValueDoubleDifferentiable> currentNodeArguments = currentNode.getArguments();
			if(currentNodeArguments != null) {
				// Update the derivative as Di = Di + Dm * dxm / dxi (where Dm = dy/xm).
				propagateDerivativeToArguments(derivativesWithRespectTo, currentNode, currentNodeArguments);

				// Add all arguments to our queue of nodes we have to work on
				nodesToProcess.addAll(currentNode.getArguments());
			}
		}
		return derivativesWithRespectTo;
	}

	/**
	 * Apply the update rule Di = Di + Dm * dxm / dxi (where Dm = dy/xm).
	 *
	 * @param derivatives The map that contains the derivatives x -> dy/dx and will be updated, that is Di = dy/dx_{i}.
	 * @param node The current node (xm).
	 * @param arguments The (list of) arguments of the current node (the i's).
	 */
	private void propagateDerivativeToArguments(Map<EnhancedValueDifferentiable, Double> derivatives, EnhancedValueDoubleDifferentiable node, List<EnhancedValueDoubleDifferentiable> arguments) {

		switch(node.getOperator()) {
		case ADD:
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0),0.0) + derivatives.get(node) * 1.0);
			derivatives.put(arguments.get(1), derivatives.getOrDefault(arguments.get(1),0.0) + derivatives.get(node) * 1.0);
			break;
		case SUB:
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0),0.0) + derivatives.get(node) * 1.0);
			derivatives.put(arguments.get(1), derivatives.getOrDefault(arguments.get(1),0.0) - derivatives.get(node) * 1.0);
			break;
		case MULT:
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0),0.0) + derivatives.get(node) * arguments.get(1).asFloatingPoint());
			derivatives.put(arguments.get(1), derivatives.getOrDefault(arguments.get(1),0.0) + derivatives.get(node) * arguments.get(0).asFloatingPoint());
			break;
		case DIV:	// z = x/y
			double x = arguments.get(0).asFloatingPoint();
			double y = arguments.get(1).asFloatingPoint();
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0), 0.0) + derivatives.get(node) / y);
			derivatives.put(arguments.get(1), derivatives.getOrDefault(arguments.get(1), 0.0) - derivatives.get(node) * x / (y*y));
			break;
		case SQUARED:
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0),0.0) + derivatives.get(node) * 2 * arguments.get(0).asFloatingPoint());
			break;
		case SQRT:
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0),0.0) + derivatives.get(node) / 2 / Math.sqrt(arguments.get(0).asFloatingPoint()));
			break;
		case EXP: // u = e^z --> u' = e^z
			double z = arguments.get(0).asFloatingPoint();
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0),0.0) + derivatives.get(node) * Math.exp(z));
			break;
		case ADDPROD: // k = x + g*t
			double g = arguments.get(1).asFloatingPoint();
			double t = arguments.get(2).asFloatingPoint();
			derivatives.put(arguments.get(0), derivatives.getOrDefault(arguments.get(0), 0.0) + derivatives.get(node) * 1.0);
			derivatives.put(arguments.get(1), derivatives.getOrDefault(arguments.get(1), 0.0) + derivatives.get(node) * t);
			derivatives.put(arguments.get(2), derivatives.getOrDefault(arguments.get(2), 0.0) + derivatives.get(node) * g);
			break;
		default:
			break;

		}
	}

	@Override
	public EnhancedValue getDerivativeWithRespectTo(EnhancedValueDifferentiable x) {
		return new EnhancedValueDoubleDifferentiable(getDerivativeWithRespectTo().getOrDefault(x, 0.0));
	}


	public Operator getOperator() {
		return operator;
	}

	public List<EnhancedValueDoubleDifferentiable> getArguments() {
		return arguments;
	}

	Long getID() {
		return id.longValue();
	}

	/*
	 * IN ORDER TO RUN THE TEST I HAD TO CHANGE VISIBILITY: IT WAS SUPPOSED TO BE PRIVATE!!!!!!!!!!
	 *
	 */
	static double valueOf(EnhancedValue x) {
		return ((ConvertableToFloatingPoint)x).asFloatingPoint();
	}

}
