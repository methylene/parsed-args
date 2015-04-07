package com.github.methylene.args.predicate;

import com.github.methylene.args.Predicate;
import com.github.methylene.args.Token;

public class TokenPredicates {

  public static Predicate<Token> isValue() {
    return new Predicate<Token>() {
      @Override
      public boolean matches(Token arg) {
        return arg.isValue();
      }
    };
  }

  public static Predicate<Token> isFlag() {
    return new Predicate<Token>() {
      @Override
      public boolean matches(Token arg) {
        return arg.isFlag();
      }
    };
  }

  public static Predicate<Token> matches(final Predicate<String> predicate) {
    return new Predicate<Token>() {
      @Override
      public boolean matches(Token arg) {
        return arg.isValue() && predicate.matches(arg.getValue());
      }
    };
  }

}
