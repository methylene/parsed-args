package com.github.methylene.args;

import java.util.Collections;
import java.util.List;

public class ParseResult {

  private final ParsedArgs parsedArgs;
  private final List<String> messages;

  private ParseResult(ParsedArgs parsedArgs, List<String> messages) {
    this.parsedArgs = parsedArgs;
    this.messages = messages;
  }

  static ParseResult success(ParsedArgs parsedArgs) {
    return new ParseResult(parsedArgs, Collections.<String>emptyList());
  }

  static ParseResult failure(List<String> reasons) {
    assert reasons != null && reasons.size() > 0;
    return new ParseResult(null, reasons);
  }

  public boolean isSuccess() {
    return parsedArgs != null;
  }

  public boolean isFailure() {
    return !isSuccess();
  }

  public List<String> getMessages() {
    return messages;
  }

  public ParsedArgs get() {
    return parsedArgs;
  }

}
