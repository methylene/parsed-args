package com.github.methylene.args.predicate;

import com.github.methylene.args.Predicate;

public class IntegerPredicates {

  public static Predicate<Integer> even() {
    return new Predicate<Integer>() {
      @Override
      public boolean matches(Integer arg) {
        return arg != null && arg % 2 == 0;
      }
    };
  }

  public static Predicate<Integer> odd() {
    return new Predicate<Integer>() {
      @Override
      public boolean matches(Integer arg) {
        return arg != null && arg % 2 != 0;
      }
    };
  }

  public static Predicate<Integer> exactly(int n) {
    return Predicates.is(n);
  }

  public static Predicate<Integer> geq(final int n) {
    return new Predicate<Integer>() {
      @Override
      public boolean matches(Integer arg) {
        return arg != null && arg >= n;
      }
    };
  }

  public static Predicate<Integer> leq(final int n) {
    return new Predicate<Integer>() {
      @Override
      public boolean matches(Integer arg) {
        return arg != null && arg <= n;
      }
    };
  }

}
