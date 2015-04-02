package com.github.methylene.args.predicate;

import com.github.methylene.args.Predicate;

import java.util.*;
import java.util.regex.Pattern;

public class Predicates {

  public static Predicate is(final String test) {
    return new Predicate() {
      @Override
      public boolean matches(String arg) {
        return Objects.equals(test, arg);
      }
    };
  }

  public static Predicate in(String... test) {
    final Set<String> testSet = new HashSet<String>(Arrays.asList(test));
    return new Predicate() {
      @Override
      public boolean matches(String arg) {
        return testSet.contains(arg);
      }
    };
  }

  public static Predicate or(Predicate... predicates) {
    final Predicate[] unmodifiablePredicates = Arrays.copyOf(predicates, predicates.length);
    return new Predicate() {
      @Override
      public boolean matches(String arg) {
        for (Predicate p : unmodifiablePredicates)
          if (p.matches(arg))
            return true;
        return false;
      }
    };
  }

  public static Predicate matches(final Pattern p) {
    return new Predicate() {
      @Override
      public boolean matches(String arg) {
        return p.matcher(arg).matches();
      }
    };
  }

  public static Predicate nothing() {
    return new Predicate() {
      @Override
      public boolean matches(String arg) {
        return false;
      }
    };
  }

}
