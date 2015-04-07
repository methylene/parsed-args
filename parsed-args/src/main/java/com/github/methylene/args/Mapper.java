package com.github.methylene.args;

import com.github.methylene.args.expand.EqualsExpander;
import com.github.methylene.args.expand.PlusExpander;
import com.github.methylene.args.expand.ShortNumericExpander;

import java.util.*;

import static com.github.methylene.args.Util.*;
import static com.github.methylene.args.predicate.Predicates.*;
import static com.github.methylene.args.predicate.StringPredicates.matches;
import static java.util.Collections.singletonList;

public class Mapper {

  private final List<TokenExpander> expanders;
  private final Tokenizer tokenizer;

  private Mapper(List<TokenExpander> expanders, Tokenizer tokenizer) {
    this.expanders = expanders;
    this.tokenizer = tokenizer;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private List<TokenExpander> expanders = Arrays.asList(
        new ShortNumericExpander(),
        new EqualsExpander(),
        new PlusExpander()
    );

    private Predicate<String> weakBinding = matches(DASHED_TOKEN);
    private Predicate<String> strongBinding = nothing();
    private Predicate<String> atomic = or(is("-"), is("+"), matches(UNIX_NUMERIC_TOKEN), matches(EQUALS_TOKEN));

    private Builder() {}

    public Builder setWeakBinding(Predicate<String> weakBinding) {
      this.weakBinding = weakBinding;
      return this;
    }

    public Builder setStrongBinding(Predicate<String> strongBinding) {
      this.strongBinding = strongBinding;
      return this;
    }

    public Builder setAtomic(Predicate<String> atomic) {
      this.atomic = atomic;
      return this;
    }

    public Builder setExpanders(List<TokenExpander> expanders) {
      this.expanders = expanders;
      return this;
    }

    public Mapper build() {
      return new Mapper(expanders, Tokenizer.create(weakBinding, strongBinding, atomic));
    }

  }

  private List<Token> expand(Token arg) {
    for (TokenExpander expander : expanders)
      if (expander.expands(arg))
        return expander.expand(arg);
    return singletonList(arg);
  }

  public Map<String, List<Token>> build(final String[] args) {
    final Map<String, List<Token>> map = new LinkedHashMap<String, List<Token>>(args.length);
    for (Token token: tokenizer.tokenize(args)) {
      for (Token expanded : expand(token)) {
        List<Token> tokens = map.get(expanded.getKey());
        if (tokens == null)
          tokens = new ArrayList<Token>();
        tokens.add(expanded);
        map.put(expanded.getKey(), tokens);
      }
    }
    return map;
  }

  public Tokenizer getTokenizer() {
    return tokenizer;
  }

  public List<TokenExpander> getExpanders() {
    return expanders;
  }

}
