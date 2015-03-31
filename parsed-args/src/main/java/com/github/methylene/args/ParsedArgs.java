package com.github.methylene.args;

import java.util.*;

public final class ParsedArgs {

  private final Map<String, Object> parsed;

  private ParsedArgs(Map<String, Object> parsed) {
    this.parsed = Collections.unmodifiableMap(parsed);
  }

  static String mapKey(String arg) {
    if (arg == null || arg.length() == 0)
      throw new IllegalArgumentException("no input");
    if (arg.startsWith("-"))
      return arg;
    if (arg.length() == 1)
      return "-" + arg;
    return "--" + arg;
  }

  public Object get(String arg) {
    return parsed.get(mapKey(arg));
  }

  @SuppressWarnings("unchecked")
  public List<String> getList(String arg) {
    Object o = parsed.get(mapKey(arg));
    if (o == null)
      return null;
    if (o instanceof String)
      return Collections.singletonList((String) o);
    if (o instanceof List)
      return (List<String>) o;
    throw new IllegalArgumentException("can not coerce to list: " + arg + " == " + o);
  }

  public boolean getFlag(String arg) {
    Object o = parsed.get(mapKey(arg));
    if (o == null)
      return false;
    if (o instanceof Boolean)
      return (Boolean) o;
    if (o instanceof String)
      return Boolean.valueOf((String) o);
    throw new IllegalArgumentException("can not coerce to boolean: " + arg + " == " + o);
  }

  public Long getNumber(String arg, Long defaultValue) {
    Object n = get(arg);
    if (n == null)
      return defaultValue;
    if (!(n instanceof String))
      throw new IllegalArgumentException("can not coerce to int: " + arg + " == " + n);
    return Long.parseLong((String) n);
  }

  public Long getNumber(String arg) {
    return getNumber(arg, null);
  }

  public List<String> getKeys() {
    return new ArrayList<String>(parsed.keySet());
  }

  public static ParsedArgs parse(String... args) {
    return new ParsedArgs(ParsedArgsFactory.parse(args));
  }

  public Map<String, Object> getMap() {
    return parsed;
  }

}
