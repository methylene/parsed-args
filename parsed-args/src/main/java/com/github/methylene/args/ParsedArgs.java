package com.github.methylene.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParsedArgs {

  private final Map<Character, String[]> parsed;

  private ParsedArgs(Map<Character, String[]> parsed) {
    this.parsed = parsed;
  }

  /**
   * Check if args is null or empty.
   * @param args an argument array
   * @return true if args is not empty
   */
  static boolean seq(String[] args) {
    if (args == null || args.length == 0)
      return false;
    return true;
  }

  /**
   * <p>Get the first argument group in the args array.</p>
   * <p>Only single character arguments are supported.</p>
   * <p>The long form with two dashes and equality sign is not supported.</p>
   * <p>The abbreviated form (no space between argument name and argument) is parsed as follows:</p>
   *
   * <pre><code>
   *   System.out.println(Arrays.toString(next(new String[]{"-n2"})));
   *   // => [n, 2]
   *   System.out.println(Arrays.toString(next(new String[]{"-n", "2"})));
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
  static String[] next(String[] args) {
    if (!seq(args))
      return args;
    if (!args[0].startsWith("-") || args[0].length() == 1) // only single character option names allowed
      throw new IllegalArgumentException("invalid option name: " + args[0]);
    if (args[0].length() > 2) { // for example: -n2
      String[] newArgs = new String[args.length + 1];
      newArgs[0] = args[0].substring(1, 2);
      newArgs[1] = args[0].substring(2);
      System.arraycopy(args, 1, newArgs, 2, args.length - 1);
      args = newArgs;
    } else {
      args[0] = args[0].substring(1, 2);
    }
    for (int i = 1; i < args.length; i += 1)
      if (args[i].startsWith("-"))
        return Arrays.copyOf(args, i);
    return args;
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
  static String[] rest(String[] args) {
    if (!seq(args))
      return args;
    for (int i = 1; i < args.length; i += 1)
      if (args[i].startsWith("-")) {
        String[] result = new String[args.length - i];
        System.arraycopy(args, i, result, 0, result.length);
        return result;
      }
    return null;
  }

  private static Map<Character, String[]> parseMap(String[] args) {
    HashMap<Character, String[]> m = new HashMap<Character, String[]>();
    while (seq(args)) {
      String[] next = next(args);
      String[] arg = new String[next.length - 1];
      System.arraycopy(next, 1, arg, 0, arg.length);
      m.put(next[0].charAt(0), arg);
      args = rest(args);
    }
    return m;
  }

  public static ParsedArgs parse(String[] s) {
    return new ParsedArgs(parseMap(s));
  }

  public String[] get(char arg) {
    return parsed.get(arg);
  }

  public int getInt(char arg, Integer defaultValue) {
    String[] n = parsed.get(arg);
    if (n == null || n.length == 0)
      if (defaultValue != null)
        return defaultValue;
    else
      throw new IllegalArgumentException("no value: " + arg);
    if (n.length > 1)
      throw new IllegalArgumentException("multiple values: " + arg);
    return Integer.parseInt(n[0]);
  }

  public int getInt(char arg) {
    return getInt(arg, null);
  }

}
