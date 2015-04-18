package com.github.methylene.args;

import com.github.methylene.args.predicate.ListPredicates;
import com.github.methylene.args.predicate.TokenPredicates;

import java.util.List;

public class TokenValue {

  public enum ValType {
    PROPERTY(ListPredicates.matches(TokenPredicates.isValue())),
    FLAG(ListPredicates.matches(TokenPredicates.isFlag()));
    private final Predicate<List<Token>> predicate;
    ValType(Predicate<List<Token>> predicate) {this.predicate = predicate;}
    public Predicate<List<Token>> getPredicate() {
      return predicate;
    }
  }

  private static final TokenValue FLAG = new TokenValue(null, ValType.FLAG);

  private final String val;
  private final ValType type;

  private TokenValue(String val, ValType type) {
    this.val = val;
    this.type = type;
  }

  public static TokenValue create(String val) {
    if (val == null)
      throw new IllegalArgumentException("val can not be null");
    return new TokenValue(val, ValType.PROPERTY);
  }

  public static TokenValue create() {
    return FLAG;
  }

  public ValType getType() {
    return type;
  }

  public boolean isFlag() {
    return type == ValType.FLAG;
  }

  public boolean isValue() {
    return type == ValType.PROPERTY;
  }

  public String getValue() {
    return val;
  }

}
