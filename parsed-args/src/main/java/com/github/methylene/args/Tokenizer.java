package com.github.methylene.args;

import java.util.Iterator;
import java.util.Queue;


public final class Tokenizer {

  private final Predicate<String> weakBinding;
  private final Predicate<String> strongBinding;
  private final Predicate<String> atomic;
  private final String restDelimiter;

  private Tokenizer(Predicate<String> weakBinding, Predicate<String> strongBinding, Predicate<String> atomic, String restDelimiter) {
    this.weakBinding = weakBinding;
    this.strongBinding = strongBinding;
    this.atomic = atomic;
    this.restDelimiter = restDelimiter;
  }

  public static Tokenizer create(Predicate<String> weakBinding, Predicate<String> strongBinding, Predicate<String> atomic, String restDelimiter) {
    return new Tokenizer(weakBinding, strongBinding, atomic, restDelimiter);
  }

  private SimpleToken read(String first, Queue<Argument> rest) {

    if (rest.isEmpty())
      return SimpleToken.create(first);

    if (atomic.matches(first))
      return SimpleToken.create(first);

    if (strongBinding.matches(first))
      return SimpleToken.create(first, rest.poll().getArg());

    if (weakBinding.matches(first)
        && !weakBinding.matches(rest.peek().getArg())
        && !strongBinding.matches(rest.peek().getArg())
        && !atomic.matches(rest.peek().getArg()))
      return SimpleToken.create(first, rest.poll().getArg());

    return SimpleToken.create(first);

  }

  public Iterable<Token> tokenize(final String[] args) {
    return tokenize(Util.toQueue(args));
  }

  public Iterable<Token> tokenize(final Queue<Argument> queue) {
    return new Iterable<Token>() {
      @Override
      public Iterator<Token> iterator() {
        return new Iterator<Token>() {

          private boolean started = false;
          private boolean restMode = false;
          private Token nextToken = null;

          private Token next_(Argument nextArg) {
            SimpleToken next;
            if (nextArg == null) {
              next = null;
            } else if (restMode) {
              next = SimpleToken.create("--", nextArg.getArg());
            } else if (restDelimiter != null && restDelimiter.equals(nextArg.getArg())) {
              nextArg = queue.isEmpty() ? null : queue.poll();
              next = nextArg == null ? null : SimpleToken.create(restDelimiter, nextArg.getArg());
              restMode = true;
            } else {
              next = read(nextArg.getArg(), queue);
            }
            return nextArg == null ? null : Token.create(next, nextArg);
          }

          @Override
          public boolean hasNext() {
            if (!started) {
              started = true;
              nextToken = next_(queue.isEmpty() ? null : queue.poll());
            }
            return nextToken != null;
          }

          @Override
          public Token next() {
            Token tmp = nextToken;
            nextToken = next_(queue.isEmpty() ? null : queue.poll());
            return tmp;
          }
        };
      }
    };
  }

  public Predicate<String> getWeakBinding() {
    return weakBinding;
  }

  public Predicate<String> getStrongBinding() {
    return strongBinding;
  }

  public Predicate<String> getAtomic() {
    return atomic;
  }
}
