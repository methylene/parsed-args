package com.github.methylene.args;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

public class Token {

  private final String key;
  private final List<TokenValue> values;
  private final List<Argument> source;

  public Token(String key, List<TokenValue> values, List<Argument> source) {
    this.key = key;
    this.values = unmodifiableList(values);
    this.source = unmodifiableList(source);
  }

  public static Token create(String key, List<Argument> source) {
    List<TokenValue> vals = new ArrayList<TokenValue>(source.size());
    for (Argument arg : source)
      vals.add(TokenValue.create(arg.getArg()));
    return new Token(key, vals, source);
  }

  public static Token create(String key, String value, Argument source) {
    return new Token(key, singletonList(TokenValue.create(value)), singletonList(source));
  }

  public static Token create(Argument key, Argument value) {
    return new Token(key.getArg(), singletonList(TokenValue.create(value.getArg())), asList(key, value));
  }

  public static Token create(Argument source) {
    return new Token(source.getArg(), singletonList(TokenValue.create()), singletonList(source));
  }

  public String getKey() {
    return key;
  }

  public List<Argument> getSource() {
    return source;
  }

  public boolean isFlag() {
    return values.size() == 1 && values.get(0).isFlag();
  }

  public boolean isValue() {
    return values.size() == 1 && !values.get(0).isFlag();
  }

  public String getValue() {
    if (!isValue())
      throw new IllegalStateException("no value");
    return values.get(0).getValue();
  }

  public List<TokenValue> getValues() {
    return values;
  }

}
