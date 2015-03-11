package com.github.methylene.args;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ParsedArgs {

  private final Map<String, List<String>> parsed;

  private ParsedArgs(Map<String, List<String>> parsed) {
    this.parsed = parsed;
  }

  /**
   * Check if args is null or empty.
   * @param args an argument array
   * @return true if args is not empty
   */
  static boolean seq(String[] args) {
    return  args != null && args.length != 0;
  }

  private static String[] rest(String[] args, int skip) {
    String[] rest = new String[args.length - skip];
    System.arraycopy(args, skip, rest, 0, rest.length);
    return rest;
  }

  private static String[] rest(String[] args) {
    return rest(args, 1);
  }

  private static String[][] nextShort(String[] args) {
    if (args[0].length() > 2) { // for example: -n2
      String[] newArgs = new String[args.length + 1];
      newArgs[0] = args[0].substring(0, 2);
      newArgs[1] = args[0].substring(2);
      System.arraycopy(args, 1, newArgs, 2, args.length - 1);
      args = newArgs;
    }
    for (int i = 1; i < args.length; i += 1)
      if (args[i].startsWith("-")) {
        return new String[][]{
            Arrays.copyOf(args, i),
            rest(args, i)
        };
      }
    return new String[][]{args, null};
  }


  private static String[][] nextLong(String[] args) {
    int idx = args[0].indexOf('=');
    if (idx >= 0) {
      if (args[0].length() > idx + 1) {
        return new String[][]{
            new String[]{
                args[0].substring(0, idx),
                args[0].substring(idx + 1)
            },
            rest(args)
        };
      } else {
        return new String[][]{
            new String[]{
                args[0].substring(idx)
            },
            rest(args)
        };
      }
    } else {
      for (int i = 1; i < args.length; i += 1)
        if (args[i].startsWith("-")) {
          return new String[][]{
              Arrays.copyOf(args, i),
              rest(args, i)
          };
        }
      return new String[][]{args, null};
    }
  }

  /**
   * <p>Get the first argument group in the args array.</p>
   * <p>The abbreviated form of a short option (no space between argument name and argument value)
   * is parsed as follows:</p>
   *
   * <pre><code>
   *   System.out.println(Arrays.toString(next(new String[]{"-n2"})));
   *   // => [n, 2]
   * </code></pre>
   *
   * @param args an argument array
   * @return if args is not empty,
   * an array of length at least 1, where the first entry (the argument name) is always of length 1.
   * Otherwise the argument is returned.
   * @throws java.lang.IllegalArgumentException if {@code args} is not empty and
   * {@code args[0]} does not start with a dash, or has no characters following the dash
   */
  static String[][] next(String[] args) {
    if (args == null || args.length == 0)
      return null;
    if (!args[0].startsWith("-")) // only single character option names allowed
      throw new IllegalArgumentException("option name must start with a dash: " + args[0]);
    if ("-".equals(args[0]))
      return new String[][]{new String[]{"-"}, rest(args)};
    if ("--".equals(args[0]))
      return new String[][]{args, null};
    if (args[0].startsWith("--"))
      return nextLong(args);
    return nextShort(args);
  }

  /**
   * Get the remaining arguments after the first group.
   * <pre><code>
   *   System.out.println(Arrays.toString(rest(new String[]{"-n", "1", "-m2"})));
   *   // => [-m2]
   * </code></pre>
   * @param args an argument array
   * @return the argument groups that do not belong to the first argument group
   */
//  static String[] rest(String[] args) {
//    if (!seq(args))
//      return args;
//    for (int i = 1; i < args.length; i += 1)
//      if (args[i].startsWith("-")) {
//        String[] result = new String[args.length - i];
//        System.arraycopy(args, i, result, 0, result.length);
//        return result;
//      }
//    return null;
//  }
  private static Map<String, List<String>> parseMap(String[] args) {
    HashMap<String, List<String>> m = new HashMap<String, List<String>>();
    String[][] state = new String[][]{null, args};
    while ((state = next(state[1])) != null) {
      String[] group = state[0];
      String key = group[0];
      List<String> values = m.get(key);
      if (values == null)
        values = new ArrayList<String>();
      for (int i = 1; i < group.length; i += 1)
        values.add(group[i]);
      m.put(key, values);
    }
    return m;
  }

  public static ParsedArgs parse(String... s) {
    return new ParsedArgs(parseMap(s));
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
      throw new IllegalArgumentException("multiple values: " + arg);
    return Integer.parseInt(n.get(0));
  }

  public int getInt(String arg) {
    return getInt(arg, null);
  }

  public List<String> getKeys() {
    return new ArrayList<String>(parsed.keySet());
  }

}
