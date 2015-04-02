package com.github.methylene.args;

import java.util.*;

public final class ParsedArgs {

  private final Map<String, List<Token>> parsed;

  ParsedArgs(Map<String, List<Token>> parsed) {
    this.parsed = Collections.unmodifiableMap(parsed);
  }

  public List<String> getList(String key) {
    List<Token> o = parsed.get(key);
    if (o == null)
      return null;
    List<String> result = new ArrayList<String>();
    for (Token arg: o) {
      for (TokenValue val: arg.getValues()) {
        if (val.isFlag())
          throw new IllegalArgumentException("cannot read as string: " + arg);
        result.add(val.getVal());
      }
    }
    return result;
  }

  public String getString(String key) {
    List<String> o = getList(key);
    if (o == null)
      return null;
    if (o.size() != 1)
      throw new IllegalArgumentException("multiple values: " + key + ": " + o);
    return o.get(0);
  }

  public Integer getFlag(String key) {
    List<Token> o = parsed.get(key);
    if (o == null)
      return null;
    int n = 0;
    for (Token arg: o) {
      for (TokenValue val: arg.getValues()) {
        if (!val.isFlag())
          throw new IllegalArgumentException("cannot read as flag: " + arg);
        n++;
      }
    }
    return n;
  }

  public Long getNumber(String arg, Long defaultValue) {
    String n = getString(arg);
    if (n == null)
      return defaultValue;
    return Long.parseLong(n);
  }

  public Long getNumber(String arg) {
    return getNumber(arg, null);
  }

  public List<String> getKeys() {
    return new ArrayList<String>(parsed.keySet());
  }

  public Map<String, List<Token>> getMap() {
    return parsed;
  }

}
