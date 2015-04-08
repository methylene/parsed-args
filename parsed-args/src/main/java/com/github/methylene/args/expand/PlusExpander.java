package com.github.methylene.args.expand;

import com.github.methylene.args.SimpleToken;
import com.github.methylene.args.Token;
import com.github.methylene.args.TokenExpander;

import java.util.List;
import java.util.regex.Matcher;

import static com.github.methylene.args.Util.PLUS_TOKEN;
import static java.util.Collections.singletonList;

public class PlusExpander implements TokenExpander {

  @Override
  public List<Token> expand(Token arg) {
    if (arg.isFlag()) {
      Matcher matcher = PLUS_TOKEN.matcher(arg.getKey());
      if (matcher.matches()) {
        return singletonList(Token.create(SimpleToken.create(matcher.group(1), matcher.group(2)), arg.getSource()));
      }
    }
    return singletonList(arg);
  }

  @Override
  public boolean expands(Token arg) {
    return arg.isFlag() && PLUS_TOKEN.matcher(arg.getKey()).matches();
  }

}
