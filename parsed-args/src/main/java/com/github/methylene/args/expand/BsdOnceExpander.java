package com.github.methylene.args.expand;

import com.github.methylene.args.TokenExpander;
import com.github.methylene.args.Token;

import static java.util.Collections.singletonList;

import java.util.List;

public class BsdOnceExpander implements TokenExpander {

  boolean canBsd = true;

  @Override
  public List<Token> expand(Token arg) {
    if (canBsd && arg.isFlag()) {
      canBsd = false;
      return BsdExpander.doExpand(arg);
    }
    return singletonList(arg);
  }

  @Override
  public boolean expands(Token arg) {
    return canBsd && arg.isFlag();
  }

}
