package com.github.methylene.args.policy;

import com.github.methylene.args.MapperPolicy;
import com.github.methylene.args.Token;
import com.github.methylene.args.TokenValue;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;
import static java.util.regex.Pattern.compile;

public class BsdPolicy implements MapperPolicy {

  static final Pattern BSD_PATTERN = compile("[a-zA-Z]+");

  static List<Token> doExpand(Token arg) {
    if (arg.isFlag() && BSD_PATTERN.matcher(arg.getKey()).matches()) {
      List<Token> bsdFlags = new ArrayList<Token>(arg.getKey().length());
      for (int i = 0; i < arg.getKey().length(); i++) {
        bsdFlags.add(new Token(Character.toString(arg.getKey().charAt(i)), singletonList(TokenValue.create()), arg.getSource()));
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
