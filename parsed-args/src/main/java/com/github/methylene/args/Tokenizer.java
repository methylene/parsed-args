package com.github.methylene.args;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;


public final class Tokenizer {

  private final Predicate<String> weakBinding;
  private final Predicate<String> strongBinding;
  private final Predicate<String> atomic;

  private Tokenizer(Predicate<String> weakBinding, Predicate<String> strongBinding, Predicate<String> atomic) {
    this.weakBinding = weakBinding;
    this.strongBinding = strongBinding;
    this.atomic = atomic;
  }

  public static Tokenizer create(Predicate<String> weakBinding, Predicate<String> strongBinding, Predicate<String> atomic) {
    return new Tokenizer(weakBinding, strongBinding, atomic);
  }

  private Token read(Queue<Argument> queue) {
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

  public Iterable<Token> tokenize(final String[] args) {
    return tokenize(Util.toQueue(args));
  }

  public Iterable<Token> tokenize(final Queue<Argument> queue) {
    return new Iterable<Token>() {
      @Override
      public Iterator<Token> iterator() {
        return new Iterator<Token>() {
          boolean started = false;
          private Token next = null;

          @Override
          public boolean hasNext() {
            if (!started) {
              started = true;
              next = read(queue);
            }
            return next != null;
          }

          @Override
          public Token next() {
            Token tmp = next;
            this.next = read(queue);
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
