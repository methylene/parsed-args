package com.github.methylene.args.policy;

import com.github.methylene.args.MapperPolicy;
import com.github.methylene.args.Token;

import java.util.List;
import java.util.regex.Matcher;

import static com.github.methylene.args.Util.EQUALS_TOKEN;
import static java.util.Collections.singletonList;

public class EqualsPolicy implements MapperPolicy {

  @Override
  public List<Token> expand(Token arg) {
    if (arg.isFlag()) {
      Matcher matcher = EQUALS_TOKEN.matcher(arg.getKey());
      if (matcher.matches()) {
        return singletonList(Token.create(matcher.group(1), matcher.group(2), arg.getSource().get(0)));
      }
    }
    return singletonList(arg);
  }

  @Override
  public boolean expands(Token arg) {
    return arg.isFlag() && EQUALS_TOKEN.matcher(arg.getKey()).matches();
  }

}