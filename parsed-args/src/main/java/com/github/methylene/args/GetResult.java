package com.github.methylene.args;

public class GetResult<E> {

  private final Outcome outcome;
  private final String message;
  private final E value;


  private GetResult(Outcome outcome, String message, E value) {
    this.outcome = outcome;
    this.message = message;
    this.value = value;
  }

  static <E> GetResult<E> success(E result) {
    return new GetResult<E>(Outcome.SUCCESS, null,  result);
  }

  static <E> GetResult<E> failure(String reason) {
    return new GetResult<E>(Outcome.FAILURE,reason, null);
  }

  public boolean isSuccess() {
    return outcome == Outcome.SUCCESS;
  }

  public boolean isFailure() {
    return outcome == Outcome.FAILURE;
  }

  public String getMessage() {
    return message;
  }

  public E get() {
    if (isFailure())
      throw new IllegalAccessError("failure: " + getMessage());
    return value;
  }
}
