package com.github.methylene.args;

public class SimpleToken {

  private final String key;
  private final TokenValue value;

  private SimpleToken(String key, TokenValue value) {
    this.key = key;
    this.value = value;
  }

  public static SimpleToken create(String key, String value) {
    return new SimpleToken(key, TokenValue.create(value));
  }

  public static SimpleToken create(String key) {
    return new SimpleToken(key, TokenValue.create());
  }

  public String getKey() {
    return key;
  }

  public TokenValue getValue() {
    return value;
  }

  public TokenValue.ValType getType() {
    return value.getType();
  }

}
