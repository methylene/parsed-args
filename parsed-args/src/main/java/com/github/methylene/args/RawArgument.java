package com.github.methylene.args;

public class RawArgument {

  private final String arg;
  private final int pos;

  public RawArgument(String arg, int pos) {
    this.arg = arg;
    this.pos = pos;
  }

  public String getArg() {
    return arg;
  }

  public int getPos() {
    return pos;
  }

}
