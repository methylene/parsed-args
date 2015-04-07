package com.github.methylene.args;

import java.util.List;

public interface TokenExpander {
  List<Token> expand(Token arg);
  boolean expands(Token arg);
}
