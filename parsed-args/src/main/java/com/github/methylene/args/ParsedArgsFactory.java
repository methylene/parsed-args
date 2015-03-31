package com.github.methylene.args;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.Arrays.copyOf;

import static com.github.methylene.args.Util.nthrest;
import static com.github.methylene.args.Util.rest;

public class ParsedArgsFactory {

  public static final Pattern NUMBERS = Pattern.compile("[0-9]+");

  /**
   * <p>Split the argument into two arrays: The first "short option" group, and all remaining arguments.
   * A short option group starts with a single dash, followed by single character called the option name.</p>
   * <p/>
   * <p>If the option name is immediately followed by more characters,
   * these are treated as the value of the option group. This is called an abbreviated short option.</p>
   * <p/>
   * <p>If the short option group is not abbreviated, more tokens are added to the group until the input is
   * exhausted or the first token starting with a dash is encountered.</p>
   * <p/>
   * <p>The following are equivalent:</p>
   * <p/>
   * <pre><code>
   *   nextShort(new String[]{"-n2"}); // abbreviated short option
   *   nextShort(new String[]{"-n", "2"}); // short option
   * </code></pre>
   * <p/>
   * <p>The input of this method must not be empty.</p>
   *
   * @param args arguments array
   * @return split into first option group and rest
   * @throws java.lang.IllegalArgumentException if {@code args} is empty
   */
  String[][] nextShort(String[] args) {
    if (args == null || args.length == 0)
      throw new IllegalArgumentException("empty input");
    if (args[0].length() > 2) { //TODO: cases number vs multiple boolean args
      if (NUMBERS.matcher(Character.toString(args[0].charAt(2))).matches()) {
        if (!NUMBERS.matcher(args[0].substring(2)).matches())
          throw new IllegalArgumentException();
        return new String[][]{
            new String[]{
                args[0].substring(0, 2),
                args[0].substring(2)
            },
            rest(args)
        };
      } else {
        String[] head = {
            args[0].substring(0, 2),
        };
        args[0] = "-" + args[0].substring(2);
        return new String[][]{
            head,
            args
        };
      }
    }
    if (args.length == 1 || args[1].startsWith("-")) {
      return new String[][]{
          copyOf(args, 1),
          rest(args)
      };
    }
    return new String[][]{copyOf(args, 2), nthrest(args, 2)};
  }


  /**
   * <p>Split the argument into two arrays: The first "long option" group, and all remaining arguments.
   * A long option group starts with two dashes, followed by a string of length greater than 1, called the option name.
   * </p>
   * <p/>
   * <p>If the option name contains the equals sign {@code '='}, the remaining characters after the equals sign
   * form the option value, and the group ends.</p>
   * <p/>
   * <p>Otherwise more tokens are added to the group until the input is
   * exhausted or the first token starting with a dash is encountered.</p>
   *
   * @param args arguments array
   * @return split into first option group and rest
   * @throws java.lang.IllegalArgumentException if {@code args} is empty
   */
  String[][] nextLong(String[] args) {
    if (args == null || args.length == 0)
      throw new IllegalArgumentException("empty input");
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
      if (args.length == 1 || args[1].startsWith("-")) {
        return new String[][]{
            copyOf(args, 1),
            rest(args)
        };
      }
      return new String[][]{copyOf(args, 2), nthrest(args, 2)};
    }
  }

  /**
   * <p>Split the first argument group from the remaining groups.</p>
   * <p>Any argument that starts with a dash marks the beginning of an argument group.</p>
   * <p>There are two "special" tokens: {@code '-'} and {@code '--'}.</p>
   * <p>{@code '-'} becomes the group name of an argument group without any argument values.</p>
   * <p>{@code '--'} becomes the group name of an argument group containing all remaining arguments,
   * regardless whether they start with a dash or not.</p>
   *
   * @param args an argument array
   * @return if args is not empty,
   * an array of length 2, containing the first group and the remaining arguments
   * @throws java.lang.IllegalArgumentException if {@code args} is not empty and
   *                                            {@code args[0]} does not start with a dash
   */
  String[][] next(String[] args) {
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

  private Map<String, List<String>> parseMap(String[] args) {
    HashMap<String, List<String>> m = new HashMap<String, List<String>>();
    String[][] state = new String[][]{null, args};
    while ((state = next(state[1])) != null) {
      String[] group = state[0];
      String key = group[0];
      List<String> values = m.get(key);
      if (values == null)
        values = new ArrayList<String>();
      for (int i = 1; i < group.length; i++)
        values.add(group[i]);
      m.put(key, values);
    }
    return m;
  }

  public ParsedArgs parse(String... s) {
    return new ParsedArgs(parseMap(s));
  }

}
