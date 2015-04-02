package com.github.methylene.args;

import java.util.List;

public interface MapperPolicy {
  List<Token> expand(Token arg);
  boolean expands(Token arg);
}
