package com.github.methylene.args;

import java.util.ArrayList;
import java.util.Queue;

import static com.github.methylene.args.Util.DASHED_TOKEN;
import static com.github.methylene.args.Util.UNIX_NUMERIC_TOKEN;
import static com.github.methylene.args.Util.EQUALS_TOKEN;
import static com.github.methylene.args.predicate.Predicates.*;


public final class Tokenizer {

  private final Predicate weakBinding;
  private final Predicate strongBinding;
  private final Predicate atomic;

  private Tokenizer(Predicate weakBinding, Predicate strongBinding, Predicate atomic) {
    this.weakBinding = weakBinding;
    this.strongBinding = strongBinding;
    this.atomic = atomic;
  }

  public static Tokenizer create(Predicate weakBinding, Predicate strongBinding, Predicate atomic) {
    return new Tokenizer(weakBinding, strongBinding, atomic);
  }

  public static Tokenizer create() {
    return create(matches(DASHED_TOKEN), nothing(), or(is("-"), is("+"), matches(UNIX_NUMERIC_TOKEN), matches(EQUALS_TOKEN)));
  }

  public Token read(Queue<Argument> queue) {
    if (queue.isEmpty())
      return null;
    Argument token = queue.poll();
    String arg = token.getArg();

    if (atomic.matches(arg))
      return Token.create(token);

    if ("--".equals(arg)) {
      ArrayList<Argument> list = new ArrayList<Argument>(queue.size());
      while (!queue.isEmpty())
        list.add(queue.poll());
      return Token.create("--", list);
    }

    if (strongBinding.matches(arg) && !queue.isEmpty())
      return Token.create(token, queue.poll());

    if (weakBinding.matches(arg)
        && !queue.isEmpty()
        && !weakBinding.matches(queue.peek().getArg())
        && !strongBinding.matches(queue.peek().getArg())
        && !atomic.matches(queue.peek().getArg()))
      return Token.create(token, queue.poll());

    return Token.create(token);

  }

}
