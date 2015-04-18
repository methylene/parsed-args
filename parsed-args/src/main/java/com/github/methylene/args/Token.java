package com.github.methylene.args;

public class Token {

  private final SimpleToken token;
  private final RawArgument source;

  private Token(SimpleToken token, RawArgument source) {
    this.token = token;
    this.source = source;
  }

  public static Token create(SimpleToken token, RawArgument source) {
    return new Token(token, source);
  }

  public SimpleToken getToken() {
    return token;
  }

  public RawArgument getSource() {
    return source;
  }

  public String getKey() {
    return token.getKey();
  }

  public String getValue() {
    return token.getValue().getValue();
  }

  public boolean isFlag() {
    return token.getValue().isFlag();
  }

  public boolean isValue() {
    return token.getValue().isValue();
  }

  public TokenValue.ValType getType() {
    return token.getType();
  }

}
