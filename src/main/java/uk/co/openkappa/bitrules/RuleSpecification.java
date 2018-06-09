package uk.co.openkappa.bitrules;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.util.Objects.requireNonNull;


public class RuleSpecification<Key, Classification> {

  public static <K, C> Builder<K, C> newRule(String ruleId) {
    return new Builder<>(ruleId);
  }

  public static class Builder<K, C> {

    private final String id;
    private Map<K, Constraint> constraints = new TreeMap<>();
    private int priority;
    private C classification;

    public Builder(String id) {
      this.id = id;
    }

    public Builder eq(K key, Object value) {
      return constraint(key, Constraint.equalTo(value));
    }

    public Builder lt(K key, Comparable<?> value) {
      return constraint(key, Constraint.lessThan(value));
    }

    public Builder le(K key, Comparable<?> value) {
      return constraint(key, Constraint.lessThanOrEqualTo(value));
    }

    public Builder gt(K key, Comparable<?> value) {
      return constraint(key, Constraint.greaterThan(value));
    }

    public Builder ge(K key, Comparable<?> value) {
      return constraint(key, Constraint.greaterThanOrEqualTo(value));
    }

    public Builder<K, C> priority(int value) {
      if (value >= 0xFFFF) {
        throw new IllegalArgumentException("priority " + value + " too high (max: " + 0xFFFF + ")");
      }
      this.priority = value;
      return this;
    }

    public Builder<K, C> constraint(K key, Constraint constraint) {
      this.constraints.put(requireNonNull(key), requireNonNull(constraint));
      return this;
    }

    public Builder<K, C> classification(C classification) {
      this.classification = requireNonNull(classification);
      return this;
    }


    public RuleSpecification<K, C> build() {
      if (constraints.isEmpty()) {
        throw new IllegalStateException("Unconstrained rule");
      }
      return new RuleSpecification<>(id, constraints, priority, requireNonNull(classification));
    }
  }

  private String id;
  private Map<Key, Constraint> constraints;
  private int priority;
  private Classification classification;

  public RuleSpecification(String id,
                           Map<Key, Constraint> constraints,
                           int priority,
                           Classification classification) {
    this.id = id;
    this.constraints = constraints;
    this.priority = priority;
    this.classification = classification;
  }

  public RuleSpecification() {
  }

  public String getId() {
    return id;
  }

  public Map<Key, Constraint> getConstraints() {
    return constraints;
  }

  public int getPriority() {
    return priority;
  }

  public Classification getClassification() {
    return classification;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RuleSpecification<?, ?> that = (RuleSpecification<?, ?>) o;
    return priority == that.priority &&
            Objects.equals(id, that.id) &&
            Objects.equals(constraints, that.constraints) &&
            Objects.equals(classification, that.classification);
  }

  @Override
  public int hashCode() {

    return Objects.hash(id, constraints, priority, classification);
  }
}
