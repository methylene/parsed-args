package com.github.methylene.args;

class Util {

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

}
