package com.github.methylene.args.predicate;

import com.github.methylene.args.Predicate;

import java.util.regex.Pattern;

public class StringPredicates {

  public static Predicate<String> notEmpty() {
    return new Predicate<String>() {
      @Override
      public boolean matches(String arg) {
        return arg != null && !arg.isEmpty();
      }
    };
  }

  public static Predicate<String> notBlank() {
    return new Predicate<String>() {
      @Override
      public boolean matches(String arg) {
        return arg != null && !arg.trim().isEmpty();
      }
    };
  }

  public static Predicate<String> matches(final Pattern p) {
    return new Predicate<String>() {
      @Override
      public boolean matches(String arg) {
        return p.matcher(arg).matches();
      }
    };
  }


}

