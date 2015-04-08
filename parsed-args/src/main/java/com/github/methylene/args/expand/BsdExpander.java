package com.github.methylene.args.expand;

import com.github.methylene.args.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;
import static java.util.regex.Pattern.compile;

public class BsdExpander implements TokenExpander {

  static final Pattern BSD_PATTERN = compile("[a-zA-Z]+");

  static List<Token> doExpand(Token arg) {
    if (arg.isFlag() && BSD_PATTERN.matcher(arg.getKey()).matches()) {
      List<Token> bsdFlags = new ArrayList<Token>(arg.getKey().length());
      for (int i = 0; i < arg.getKey().length(); i++) {
        bsdFlags.add(Token.create(SimpleToken.create(Character.toString(arg.getKey().charAt(i))), arg.getSource()));
      }
      return bsdFlags;
    }
    return singletonList(arg);
  }

  @Override
  public List<Token> expand(Token arg) {
    return doExpand(arg);
  }

  @Override
  public boolean expands(Token arg) {
    return arg.isFlag() && BSD_PATTERN.matcher(arg.getKey()).matches();
  }

}
