package com.github.methylene.args;

import com.github.methylene.args.expand.BsdFirstExpander;
import com.github.methylene.args.expand.EqualsExpander;
import com.github.methylene.args.expand.PlusExpander;
import com.github.methylene.args.expand.ShortNumericExpander;
import com.github.methylene.args.predicate.Predicates;

import java.util.*;

import static com.github.methylene.args.Util.*;
import static com.github.methylene.args.predicate.Predicates.*;
import static com.github.methylene.args.predicate.StringPredicates.matches;
import static java.util.Collections.singletonList;

public class Mapper {

  private final List<TokenExpander> expanders;
  private final Tokenizer tokenizer;

  public enum Flavour {
    DEFAULT, BSD, BASIC
  }

  private Mapper(List<TokenExpander> expanders, Tokenizer tokenizer) {
    this.expanders = expanders;
    this.tokenizer = tokenizer;
  }

  public static Builder builder() {
    return builder(Flavour.DEFAULT);
  }

  /**
   * Convenience method to get a pre-populated builder
   * @param flavour a builder flavour
   * @return a builder
   */
  public static Builder builder(Flavour flavour) {
    switch (flavour) {
      case DEFAULT:
        return new Builder()
            .setRestDelimiter("--")
            .addExpander(new ShortNumericExpander())
            .addExpander(new EqualsExpander())
            .addExpander(new PlusExpander())
            .setWeakBinding(matches(DASHED_TOKEN))
            .setStrongBinding(Predicates.<String>nothing())
            .setAtomic(or(is("-"), is("+"), matches(UNIX_NUMERIC_TOKEN), matches(EQUALS_TOKEN)));
      case BASIC:
        return new Builder()
            .setWeakBinding(Predicates.<String>nothing())
            .setStrongBinding(Predicates.<String>nothing())
            .setAtomic(Predicates.<String>nothing());
      case BSD:
        return builder(Flavour.DEFAULT).addExpander(new BsdFirstExpander());
      default:
        throw new IllegalArgumentException("unknown flavour: " + flavour);
    }
  }

  public static class Builder {

    private List<TokenExpander> expanders = new ArrayList<TokenExpander>();

    private Predicate<String> weakBinding;
    private Predicate<String> strongBinding;
    private Predicate<String> atomic;

    private String restDelimiter;

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

    public Builder addExpander(TokenExpander expander) {
      this.expanders.add(expander);
      return this;
    }

    public Builder setExpanders(List<TokenExpander> expanders) {
      this.expanders = expanders;
      return this;
    }

    public Builder setRestDelimiter(String restDelimiter) {
      this.restDelimiter = restDelimiter;
      return this;
    }

    public Mapper build() {
      return new Mapper(expanders, Tokenizer.create(weakBinding, strongBinding, atomic, restDelimiter));
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
