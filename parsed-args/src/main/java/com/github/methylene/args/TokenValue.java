package com.github.methylene.args;

public class TokenValue {

  enum ValType {
    VALUE, FLAG
  }

  private static final TokenValue FLAG = new TokenValue(null, ValType.FLAG);

  private final String val;
  private final ValType type;

  TokenValue(String val, ValType type) {
    this.val = val;
    this.type = type;
  }

  public static TokenValue create(String val) {
    if (val == null)
      throw new IllegalArgumentException("val can not be null");
    return new TokenValue(val, ValType.VALUE);
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
    return type == ValType.VALUE;
  }

  public String getValue() {
    return val;
  }

}
