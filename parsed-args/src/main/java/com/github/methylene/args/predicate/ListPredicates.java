package com.github.methylene.args.predicate;

import com.github.methylene.args.Predicate;
import com.github.methylene.args.Token;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ListPredicates {

  public static Predicate<List<Token>> matches(final Predicate<Token> predicate) {
    return new Predicate<List<Token>>() {
      @Override
      public boolean matches(List<Token> tokens) {
        if (tokens == null)
          tokens = Collections.emptyList();
        for (Token token: tokens)
          if (!predicate.matches(token))
            return false;
        return true;
      }
    };
  }

  public static Predicate<List<Token>> matches(final Pattern pattern) {
    return matches(TokenPredicates.matches(StringPredicates.matches(pattern)));
  }

  public static Predicate<List<Token>> hasCardinality(final Predicate<Integer> cardinality) {
    return new Predicate<List<Token>>() {
      @Override
      public boolean matches(List<Token> arg) {
        if (arg == null)
          arg = Collections.emptyList();
        return cardinality.matches(arg.size());
      }
    };
  }

  public static Predicate<List<Token>> isSingleValue() {
    return Predicates.and(isValue(), hasCardinality(IntegerPredicates.exactly(1)));
  }

  public static Predicate<List<Token>> isSingleFlag() {
    return Predicates.and(isFlag(), hasCardinality(IntegerPredicates.exactly(1)));
  }

  public static Predicate<List<Token>> isPresent() {
    return hasCardinality(IntegerPredicates.geq(1));
  }

  public static Predicate<List<Token>> isValue() {
    return matches(TokenPredicates.isValue());
  }

  public static Predicate<List<Token>> isFlag() {
    return matches(TokenPredicates.isFlag());
  }

}
