package com.github.methylene.args;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public final class Util {

  public static final Pattern UNIX_NUMERIC_TOKEN = compile("(-[a-zA-Z])([0-9\\-/.,+]+)");

  public static final Pattern EQUALS_TOKEN = compile("(-{1,2}[a-zA-Z][a-zA-Z0-9_\\-/.,+]*)=(.*)");

  public static final Pattern PLUS_TOKEN = Pattern.compile("(\\+)(.*)");

  public static final Pattern DASHED_TOKEN = compile("-{1,2}[a-zA-Z][a-zA-Z0-9_\\-/.,+]*");


  //  static Queue<String> toQueue(String[] args) {
//    LinkedList<String> queue = new LinkedList<String>();
//    for (int i = args.length - 1; i >= 0; i--)
//      queue.push(args[i]);
//    return queue;
//  }
  static Queue<Argument> toQueue(String[] args) {
    LinkedList<Argument> queue = new LinkedList<Argument>();
    for (int i = args.length - 1; i >= 0; i--)
      queue.push(new Argument(args[i], i));
    return queue;
  }

  static String[] nthrest(String[] args, int skip) {
    if (skip < 0)
      throw new IllegalArgumentException("negative number not allowed");
    if (skip == 0)
      return args;
    if (skip >= args.length)
      return null;
    String[] rest = new String[args.length - skip];
    System.arraycopy(args, skip, rest, 0, args.length - skip);
    return rest;
  }

  static String[] rest(String[] args) {
    return nthrest(args, 1);
  }

  static void complain() {
    throw new IllegalArgumentException();
  }

}
