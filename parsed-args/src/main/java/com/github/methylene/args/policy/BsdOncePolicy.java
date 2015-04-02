package com.github.methylene.args.policy;

import com.github.methylene.args.MapperPolicy;
import com.github.methylene.args.Token;

import static java.util.Collections.singletonList;

import java.util.List;

public class BsdOncePolicy implements MapperPolicy {

  boolean canBsd = true;

  @Override
  public List<Token> expand(Token arg) {
    if (canBsd && arg.isFlag()) {
      canBsd = false;
      return BsdPolicy.doExpand(arg);
    }
    return singletonList(arg);
  }

  @Override
  public boolean expands(Token arg) {
    return canBsd && arg.isFlag();
  }

}
