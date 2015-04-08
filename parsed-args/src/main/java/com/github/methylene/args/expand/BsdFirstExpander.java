package com.github.methylene.args.expand;

import com.github.methylene.args.Token;
import com.github.methylene.args.TokenExpander;

import static java.util.Collections.singletonList;

import java.util.List;

public class BsdFirstExpander implements TokenExpander {

  private final int pos;

  public BsdFirstExpander(int pos) {
    if (pos < 0)
      throw new IllegalArgumentException();
    this.pos = pos;
  }

  public BsdFirstExpander() {this(0);}

  @Override
  public List<Token> expand(Token arg) {
    if (this.expands(arg))
      return BsdExpander.doExpand(arg);
    return singletonList(arg);
  }

  @Override
  public boolean expands(Token arg) {
    return this.pos == arg.getSource().getPos() && arg.isFlag();
  }

}
