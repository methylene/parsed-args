package com.github.methylene.args;

import java.util.Iterator;
import java.util.Queue;

public final class Tokenizer {

  // binds the next token if it exists and  is not special
  private final Predicate<String> weakBinding;

  // binds the next token if it exists
  private final Predicate<String> strongBinding;

  // never binds the next token
  private final Predicate<String> atomic;

  // usually "--"
  private final String restDelimiter;

  private Tokenizer(Predicate<String> weakBinding, Predicate<String> strongBinding, Predicate<String> atomic,
                    String restDelimiter) {
    this.weakBinding = weakBinding;
    this.strongBinding = strongBinding;
    this.atomic = atomic;
    this.restDelimiter = restDelimiter;
  }

  /**
   * Create a new tokenizer that uses the provided predicates.
   * @param weakBinding the weak binding predicate
   * @param strongBinding the strong binding predicate
   * @param atomic the atomic predicate
   * @param restDelimiter the rest delimiter
   * @return a tokenizer
   */
  public static Tokenizer create(Predicate<String> weakBinding, Predicate<String> strongBinding,
                                 Predicate<String> atomic, String restDelimiter) {
    return new Tokenizer(weakBinding, strongBinding, atomic, restDelimiter);
  }

  private SimpleToken read(String first, Queue<RawArgument> rest) {

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

  /**
   * Read the arguments into a list of tokens.
   * @param args a string array
   * @return an iterable
   */
  public Iterable<Token> tokenize(final String[] args) {
    return tokenize(Util.toQueue(args));
  }

  private Iterable<Token> tokenize(final Queue<RawArgument> queue) {
    return new Iterable<Token>() {
      @Override
      public Iterator<Token> iterator() {
        return new Iterator<Token>() {

          private boolean started = false;
          private boolean restMode = false;
          private Token nextToken = null;

          private Token next_(RawArgument nextArg) {
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

          @Override public void remove() {
            throw new UnsupportedOperationException();
          }

        };
      }
    };
  }

  /**
   * Get the weak binding predicate of this instance.
   * An argument that matches this predicate will bind
   * the following one if it exists and is not either weakly binding, strongly binding,
   * atomic or a rest delimiter.
   * @return the weak binding predicate
   */
  public Predicate<String> getWeakBinding() {
    return weakBinding;
  }

  /**
   * Get the strong binding predicate of this instance.
   * An argument that matches this predicate will bind
   * the following one if it exists.
   * This will typically include any explicitly declared parameters.
   * @return the strong binding predicate
   */
  public Predicate<String> getStrongBinding() {
    return strongBinding;
  }

  /**
   * Get the atomic predicate of this instance.
   * An argument that matches this predicate will never bind the following token.
   * If it is not itself bound by a strong binding token or the rest delimiter,
   * it will be read as a <i>flag</i>.
   * @return the atomic predicate
   */
  public Predicate<String> getAtomic() {
    return atomic;
  }

}
