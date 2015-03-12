package com.github.methylene.args;

public class Util {

  public static String[] rest(String[] args, int skip) {
    String[] rest = new String[args.length - skip];
    System.arraycopy(args, skip, rest, 0, rest.length);
    return rest;
  }

  public static String[] rest(String[] args) {
    return rest(args, 1);
  }

}
