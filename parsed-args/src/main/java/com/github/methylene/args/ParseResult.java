package com.github.methylene.args;

import java.util.Collections;
import java.util.List;

public class ParseResult {

  private final ParsedArgs parsedArgs;
  private final Outcome outcome;
  private final List<String> messages;

  private ParseResult(ParsedArgs parsedArgs, Outcome outcome, List<String> messages) {
    this.parsedArgs = parsedArgs;
    this.outcome = outcome;
    this.messages = messages;
  }

  static ParseResult success(ParsedArgs parsedArgs) {
    return new ParseResult(parsedArgs, Outcome.SUCCESS, Collections.<String>emptyList());
  }

  static ParseResult failure(List<String> reasons) {
    return new ParseResult(null, Outcome.FAILURE, reasons);
  }

  public boolean isSuccess() {
    return outcome == Outcome.SUCCESS;
  }

  public boolean isFailure() {
    return outcome == Outcome.FAILURE;
  }

  public List<String> getMessages() {
    return messages;
  }

  public ParsedArgs get() {
    if (isFailure())
      throw new IllegalStateException("failure: " + messages);
    return parsedArgs;
  }

}
