package com.github.methylene.args;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetResult {

  private final TokenValue.ValType type;
  private final List<TokenValue> tokens;

  GetResult(List<TokenValue> tokens) {
    if (tokens == null) {
      this.type = null;
      this.tokens = Collections.emptyList();
    } else {
      this.type = tokens.get(0).getType();
      this.tokens = tokens;
      for (TokenValue val : tokens) {
        if (val.getType() != type) {
          throw new IllegalArgumentException("mixing flags and parameters");
        }
      }
    }
  }


  public List<String> getValues() {
    if (tokens.isEmpty())
      return Collections.emptyList();
    if (!isParameter())
      throw new IllegalStateException("cannot get values of a flag");
    List<String> result = new ArrayList<String>(tokens.size());
    for (TokenValue val : tokens)
      result.add(val.getValue());
    return result;
  }

  public String getValue() {
    if (tokens.isEmpty())
      return null;
    List<String> o = getValues();
    if (o.size() > 1)
      throw new IllegalStateException("cannot get single value: multiple values found");
    return o.get(0);
  }

  public boolean isPresent() {
    return count() > 0;
  }

  public int count() {
    return tokens.size();
  }

  public Long parseLong(Long defaultValue) {
    String n = getValue();
    if (n == null)
      return defaultValue;
    return Long.parseLong(n);
  }

  public Long parseLong() {
    return parseLong(null);
  }

  public boolean isParameter() {
    if (tokens.isEmpty())
      return false;
    for (TokenValue val : tokens)
      if (val.getType() != TokenValue.ValType.PARAMETER)
        return false;
    return true;
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
