package com.github.methylene.args;

import java.util.*;

public final class ParsedArgs {

  private final Map<String, List<Token>> parsed;

  ParsedArgs(Map<String, List<Token>> parsed) {
    this.parsed = Collections.unmodifiableMap(parsed);
  }

  public GetResult<List<String>> getValues(String key) {
    List<Token> o = parsed.get(key);
    if (o == null)
      return GetResult.success(Collections.<String>emptyList());
    List<String> result = new ArrayList<String>();
    for (Token arg : o) {
      for (TokenValue val : arg.getValues()) {
        if (val.isFlag())
          return GetResult.failure("got flag but was expecting value");
        result.add(val.getValue());
      }
    }
    return GetResult.success(result);
  }

  public GetResult<String> getString(String key) {
    GetResult<List<String>> o = getValues(key);
    if (o.isFailure())
      return GetResult.failure(o.getMessage());
    if (o.get().size() != 1)
      return GetResult.failure("multiple values");
    return GetResult.success(o.get().get(0));
  }

  public GetResult<Boolean> getFlag(String key) {
    GetResult<Integer> flags = getFlagCount(key);
    if (flags.isFailure())
      return GetResult.failure(flags.getMessage());
    return GetResult.success(flags.get() > 0);
  }

  public GetResult<Integer> getFlagCount(String key) {
    List<Token> o = parsed.get(key);
    if (o == null)
      return GetResult.success(0);
    int n = 0;
    for (Token arg : o) {
      for (TokenValue val : arg.getValues()) {
        if (val.isFlag())
          n++;
        else
          return GetResult.failure("got value but was expecting flag");
      }
    }
    return GetResult.success(n);
  }

  public GetResult<Long> getNumber(String arg, Long defaultValue) {
    GetResult<String> n = getString(arg);
    if (n.isFailure())
      return GetResult.failure(n.getMessage());
    try {
      return GetResult.success(Long.parseLong(n.get()));
    } catch (RuntimeException e) {
      return GetResult.failure(e.getMessage());
    }
  }

  public GetResult<Long> getNumber(String arg) {
    return getNumber(arg, null);
  }

  public List<String> getKeys() {
    return new ArrayList<String>(parsed.keySet());
  }

  public Map<String, List<Token>> getMap() {
    return parsed;
  }

}
