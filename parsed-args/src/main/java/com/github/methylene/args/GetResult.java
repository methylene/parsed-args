package com.github.methylene.args;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetResult {

  private final List<TokenValue> tokens;

  public GetResult(List<TokenValue> tokens) {
    if (tokens == null)
      tokens = Collections.emptyList();
    this.tokens = tokens;
  }

  public List<String> getValues() {
    if (tokens.isEmpty())
      return Collections.emptyList();
    List<String> result = new ArrayList<String>(tokens.size());
    for (TokenValue val : tokens) {
      if (val.isFlag())
        throw new IllegalStateException("got flag but was expecting value");
      result.add(val.getValue());
    }
    return result;
  }

  public String getString() {
    if (tokens.isEmpty())
      return null;
    List<String> o = getValues();
    if (o.size() > 1)
      throw new IllegalStateException("multiple values");
    return o.get(0);
  }

  public boolean getFlag() {
    return getFlagCount() > 0;
  }

  public int getFlagCount() {
    int n = 0;
    for (TokenValue arg : tokens) {
      if (arg.isFlag())
        n++;
      else
        throw new IllegalStateException("got value but was expecting flag");
    }
    return n;
  }

  public Long getNumber(Long defaultValue) {
    String n = getString();
    if (n == null)
      return defaultValue;
    return Long.parseLong(n);
  }

  public Long getNumber() {
    return getNumber(null);
  }

  public boolean isNull() {
    return tokens.isEmpty();
  }

  public boolean isMixed() {
    if (tokens.isEmpty())
      return false;
    TokenValue.ValType type = tokens.get(0).getType();
    for (TokenValue val : tokens)
      if (val.getType() != type)
        return true;
    return false;
  }

  public boolean isValues() {
    if (tokens.isEmpty())
      return false;
    for (TokenValue val : tokens)
      if (val.getType() != TokenValue.ValType.VALUE)
        return false;
    return true;
  }

  public int getCount() {
    return tokens.size();
  }

  public boolean isSingleValue() {
    return tokens.size() == 1;
  }

  public boolean isFlag() {
    if (tokens.isEmpty())
      return false;
    for (TokenValue val : tokens)
      if (val.getType() != TokenValue.ValType.FLAG)
        return false;
    return true;
  }

}
