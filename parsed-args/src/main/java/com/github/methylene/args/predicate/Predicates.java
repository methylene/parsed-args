package com.github.methylene.args.predicate;

import com.github.methylene.args.Predicate;

import java.util.*;
import java.util.regex.Pattern;

public class Predicates {

  public static <T> Predicate<T> is(final T test) {
    return new Predicate<T>() {
      @Override
      public boolean matches(T arg) {
        return Objects.equals(test, arg);
      }
    };
  }

  public static <T> Predicate<T> in(T... test) {
    return in(new HashSet<T>(Arrays.asList(test)));
  }
  public static <T> Predicate<T> in(final Set<T> test) {
    return new Predicate<T>() {
      @Override
      public boolean matches(T arg) {
        return test.contains(arg);
      }
    };
  }

  public static <T> Predicate<T> or(Predicate<T>... predicates) {
    return or(Arrays.asList(predicates));
  }

  public static <T> Predicate<T> or(Predicate<T> p0, Predicate<T> p1) {
    ArrayList<Predicate<T>> predicates = new ArrayList<Predicate<T>>(2);
    predicates.add(p0);
    predicates.add(p1);
    return or(predicates);
  }

  public static <T> Predicate<T> or(Predicate<T> p0, Predicate<T> p1, Predicate<T> p2) {
    ArrayList<Predicate<T>> predicates = new ArrayList<Predicate<T>>(2);
    predicates.add(p0);
    predicates.add(p1);
    predicates.add(p2);
    return or(predicates);
  }

  public static <T> Predicate<T> or(Predicate<T> p0, Predicate<T> p1, Predicate<T> p2, Predicate<T> p3) {
    ArrayList<Predicate<T>> predicates = new ArrayList<Predicate<T>>(2);
    predicates.add(p0);
    predicates.add(p1);
    predicates.add(p2);
    predicates.add(p3);
    return or(predicates);
  }

  public static <T> Predicate<T> and(Predicate<T> p0, Predicate<T> p1) {
    ArrayList<Predicate<T>> predicates = new ArrayList<Predicate<T>>(2);
    predicates.add(p0);
    predicates.add(p1);
    return and(predicates);
  }

  public static <T> Predicate<T> and(Predicate<T> p0, Predicate<T> p1, Predicate<T> p2) {
    ArrayList<Predicate<T>> predicates = new ArrayList<Predicate<T>>(2);
    predicates.add(p0);
    predicates.add(p1);
    predicates.add(p2);
    return and(predicates);
  }

  public static <T> Predicate<T> and(Predicate<T> p0, Predicate<T> p1, Predicate<T> p2, Predicate<T> p3) {
    ArrayList<Predicate<T>> predicates = new ArrayList<Predicate<T>>(2);
    predicates.add(p0);
    predicates.add(p1);
    predicates.add(p2);
    predicates.add(p3);
    return and(predicates);
  }

  public static <T> Predicate<T> and(Predicate<T>... predicates) {
    return and(Arrays.asList(predicates));
  }

  public static <T> Predicate<T> and(final Collection<Predicate<T>> predicates) {
    if (predicates.size() == 1)
      return predicates.iterator().next();
    return new Predicate<T>() {
      @Override
      public boolean matches(T arg) {
        for (Predicate<T> p : predicates)
          if (!p.matches(arg))
            return false;
        return true;
      }
    };
  }

  public static <T> Predicate<T> or(final Collection<Predicate<T>> predicates) {
    if (predicates.size() == 1)
      return predicates.iterator().next();
    return new Predicate<T>() {
      @Override
      public boolean matches(T arg) {
        for (Predicate<T> p : predicates)
          if (p.matches(arg))
            return true;
        return false;
      }
    };
  }

  public static <T> Predicate<T> nothing() {
    return new Predicate<T>() {
      @Override
      public boolean matches(T arg) {
        return false;
      }
    };
  }

  public static <T> Predicate<T> anything() {
    return new Predicate<T>() {
      @Override
      public boolean matches(T arg) {
        return true;
      }
    };
  }

  public static <T> Predicate<T> notNull() {
    return new Predicate<T>() {
      @Override
      public boolean matches(T arg) {
        return arg != null;
      }
    };
  }


}
