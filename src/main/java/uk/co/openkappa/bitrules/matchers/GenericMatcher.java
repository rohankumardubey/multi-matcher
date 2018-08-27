package uk.co.openkappa.bitrules.matchers;

import uk.co.openkappa.bitrules.Constraint;
import uk.co.openkappa.bitrules.Mask;
import uk.co.openkappa.bitrules.Matcher;

import java.util.function.Function;

public class GenericMatcher<T, U, MaskType extends Mask<MaskType>> implements Matcher<T, MaskType> {

  private final Function<T, U> accessor;
  private final GenericEqualityNode<U, MaskType> node;

  public GenericMatcher(Function<T, U> accessor, Class<MaskType> type, int max) {
    this.accessor = accessor;
    this.node = new GenericEqualityNode<>(type, Masks.singleton(type), max);
  }

  public MaskType match(T value, MaskType context) {
    return node.match(accessor.apply(value), context);
  }

  @Override
  public void addConstraint(Constraint constraint, int priority) {
    node.add((U) constraint.getValue(), priority);
  }

  @Override
  public void freeze() {
    node.optimise();
  }
}
