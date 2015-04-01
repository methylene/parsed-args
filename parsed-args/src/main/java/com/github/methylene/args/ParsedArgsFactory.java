package com.github.methylene.args;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.methylene.args.Util.nthrest;
import static com.github.methylene.args.Util.rest;
import static java.util.Arrays.copyOf;

final class ParsedArgsFactory {

  private ParsedArgsFactory() {}

  static final Pattern NUMERIC = Pattern.compile("[0-9\\-.,+]+");

  static boolean shortOpt(char c) {
    return Character.isDigit(c) || c == '-' || c == '.' || c == ',' || c == '+';
  }

  static boolean startsOpt(String opt) {
    if (opt.length() == 0)
      return false;
    char char0 = opt.charAt(0);
    return char0 == '-' || char0 == '+' && opt.length() > 1;
  }

  static String[][] nextShort(String[] args) {
    if (args == null || args.length == 0)
      throw new IllegalArgumentException("empty input");
    if (args[0].length() > 2) {
      if (shortOpt(args[0].charAt(2))) {
        if (!NUMERIC.matcher(args[0].substring(2)).matches())
          throw new IllegalArgumentException("mixing numbers and characters: " + args[0]);
        return new String[][]{
            new String[]{
                args[0].substring(0, 2),
                args[0].substring(2)
            },
            rest(args)
        };
      } else {
        for (int i = 3; i < args[0].length(); i++)
          if (shortOpt(args[0].charAt(i)))
            throw new IllegalArgumentException("mixing numbers and characters: " + args[0]);
        if (args.length > 1 && !startsOpt(args[1]))
          throw new IllegalArgumentException("ambiguous association: " + args[1]);
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

  static String[][] nextLong(String[] args) {
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
      if (args.length == 1 || startsOpt(args[1])) {
        return new String[][]{
            copyOf(args, 1),
            rest(args)
        };
      }
      return new String[][]{copyOf(args, 2), nthrest(args, 2)};
    }
  }

  static String[][] next(String[] args) {
    if (args == null || args.length == 0)
      return null;
    if (!startsOpt(args[0]))
      throw new IllegalArgumentException("invalid option: " + args[0]);
    if ("-".equals(args[0]))
      return new String[][]{new String[]{"-"}, rest(args)};
    if ("--".equals(args[0]))
      return new String[][]{args, null};
    if (args[0].startsWith("--"))
      return nextLong(args);
    if (args[0].startsWith("+"))
      return new String[][]{new String[]{"+", args[0].substring(1)}, rest(args)};
    return nextShort(args);
  }

  @SuppressWarnings("unchecked")
  static Map<String, Object> parse(String[] args) {
    Map<String, Object> m = new LinkedHashMap<String, Object>();
    if (args.length >= 1 && !startsOpt(args[0]))
      args[0] = "-" + args[0];
    String[][] state = new String[][]{null, args};
    while ((state = next(state[1])) != null) {
      String[] group = state[0];
      String key = group[0];
      Object values = m.get(key);
      for (int i = 1; i < group.length; i++) {
        if (values instanceof Boolean)
          throw new IllegalArgumentException("repeating flag: " + key);
        if (values instanceof String) {
          String tmp = (String) values;
          values = new ArrayList<String>();
          ((List<String>) values).add(tmp);
        }
        if (values == null)
          values = new ArrayList<String>();
        ((List<String>) values).add(group[i]);
      }
      if (values instanceof Boolean)
        throw new IllegalArgumentException("repeating flag: " + key);
      if (values instanceof String)
        throw new IllegalArgumentException("re-using key as flag: " + key);
      m.put(key, values == null ? Boolean.TRUE : ((List<String>) values).size() == 1 ? ((List<String>) values).get(0) : values);
    }
    return m;
  }

}
