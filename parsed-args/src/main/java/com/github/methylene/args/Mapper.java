package com.github.methylene.args;

import com.github.methylene.args.policy.EqualsPolicy;
import com.github.methylene.args.policy.PlusPolicy;
import com.github.methylene.args.policy.ShortNumericPolicy;

import java.util.*;

import static com.github.methylene.args.Util.DASHED_TOKEN;
import static com.github.methylene.args.Util.UNIX_NUMERIC_TOKEN;
import static com.github.methylene.args.Util.EQUALS_TOKEN;
import static com.github.methylene.args.predicate.Predicates.*;
import static java.util.Collections.singletonList;

public class Mapper {

  private final List<MapperPolicy> policies;
  private final Tokenizer partitioner;

  private Mapper(List<MapperPolicy> policies, Tokenizer partitioner) {
    this.policies = policies;
    this.partitioner = partitioner;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private List<MapperPolicy> policies = Arrays.asList(
        new ShortNumericPolicy(),
        new EqualsPolicy(),
        new PlusPolicy()
    );

    private Predicate weakBinding = matches(DASHED_TOKEN);
    private Predicate strongBinding = nothing();
    private Predicate atomic = or(is("-"), is("+"), matches(UNIX_NUMERIC_TOKEN), matches(EQUALS_TOKEN));

    private Builder() {}

    public Builder setWeakBinding(Predicate weakBinding) {
      this.weakBinding = weakBinding;
      return this;
    }

    public Builder setStrongBinding(Predicate strongBinding) {
      this.strongBinding = strongBinding;
      return this;
    }

    public Builder setAtomic(Predicate atomic) {
      this.atomic = atomic;
      return this;
    }

    public Builder setPolicies(List<MapperPolicy> policies) {
      this.policies = policies;
      return this;
    }

    public Mapper build() {
      return new Mapper(policies, Tokenizer.create(weakBinding, strongBinding, atomic));
    }

  }

  public Map<String, List<Token>> build(final String[] args) {
    return build(Util.toQueue(args));
  }

  private List<Token> expand(Token arg) {
    for (MapperPolicy policy : policies) {
      if (policy.expands(arg))
        return policy.expand(arg);
    }
    return singletonList(arg);
  }

  private Map<String, List<Token>> build(final Queue<Argument> queue) {
    final Map<String, List<Token>> map = new LinkedHashMap<String, List<Token>>(queue.size());
    Token parsed;
    while ((parsed = partitioner.read(queue)) != null) {
      for (Token expanded : expand(parsed)) {
        List<Token> args = map.get(expanded.getKey());
        if (args == null)
          args = new ArrayList<Token>();
        args.add(expanded);
        map.put(expanded.getKey(), args);
      }
    }
    return map;
  }

}
