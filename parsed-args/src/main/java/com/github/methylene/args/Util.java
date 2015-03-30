package com.github.methylene.args;

public class Util {

  public static String[] nthrest(String[] args, int skip) {
    if (skip == 0)
      return args;
    if (skip > args.length)
      return null;
    String[] rest = new String[args.length - skip];
    System.arraycopy(args, skip, rest, 0, args.length - skip);
    return rest;
  }

  public static String[] rest(String[] args) {
    return nthrest(args, 1);
  }

}
