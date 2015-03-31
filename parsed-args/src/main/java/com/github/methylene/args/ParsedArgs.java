package com.github.methylene.args;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ParsedArgs {

  private final Map<String, Object> parsed;

  private static final ParsedArgsFactory FACTORY = new ParsedArgsFactory();

  public static ParsedArgsFactory factory() {
    return FACTORY;
  }

  ParsedArgs(Map<String, Object> parsed) {
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

  public Object get(String arg) {
    return parsed.get(mapKey(arg));
  }

  public boolean contains(String arg) {
    return parsed.containsKey(mapKey(arg));
  }

  public int getInt(String arg, Integer defaultValue) {
    Object n = get(arg);
    if (!(n instanceof String))
      throw new IllegalArgumentException("not a string: " + arg + " == " + n);
    return Integer.parseInt((String) n);
  }

  public int getInt(String arg) {
    return getInt(arg, null);
  }

  public List<String> getKeys() {
    return new ArrayList<String>(parsed.keySet());
  }

}
