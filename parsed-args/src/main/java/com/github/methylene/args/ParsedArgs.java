package com.github.methylene.args;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParsedArgs {

  private final Map<String, List<String>> parsed;

  private static final ParsedArgsFactory FACTORY = new ParsedArgsFactory();

  public static ParsedArgsFactory factory() {
    return FACTORY;
  }

  ParsedArgs(Map<String, List<String>> parsed) {
    this.parsed = parsed;
  }

  private static String mapKey(String arg) {
    if (arg == null || arg.length() == 0)
      throw new IllegalArgumentException("can not get nothing");
    if (arg.startsWith("-"))
      return arg;
    if (arg.length() == 1)
      return "-" + arg;
    return "--" + arg;
  }

  public List<String> get(String arg) {
    return parsed.get(mapKey(arg));
  }

  public boolean contains(String arg) {
    return parsed.containsKey(mapKey(arg));
  }

  public int getInt(String arg, Integer defaultValue) {
    List<String> n = get(arg);
    if (n == null || n.size() == 0)
      if (defaultValue != null)
        return defaultValue;
      else
        throw new IllegalArgumentException("no value: " + arg);
    if (n.size() > 1)
      throw new IllegalArgumentException("multiple values: " + arg + ": " + n);
    return Integer.parseInt(n.get(0));
  }

  public int getInt(String arg) {
    return getInt(arg, null);
  }

  public List<String> getKeys() {
    return new ArrayList<String>(parsed.keySet());
  }

}
