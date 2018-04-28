package com.openkappa.bitrules.config;

import com.openkappa.bitrules.Rule;
import com.openkappa.bitrules.nodes.ComparableRule;

import java.util.Comparator;
import java.util.function.Function;

public class ComparableAttribute<T, U> implements Attribute<T> {

  private final Comparator<U> comparator;
  private final Function<T, U> accessor;

  public ComparableAttribute(Comparator<U> comparator, Function<T, U> accessor) {
    this.comparator = comparator;
    this.accessor = accessor;
  }


  @Override
  public Rule<T> toRule() {
    return new ComparableRule<>(accessor, comparator);
  }
}