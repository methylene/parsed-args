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
    if (reasons == null || reasons.size() == 0)
      throw new IllegalStateException("failure without messages, this should never happen");
    return new ParseResult(null, reasons);
  }

  /**
   * Check if parsing was successful.
   * If this method returns true, {@link #getMessages} will return an empty list and
   * {@link #get} will not return null.
   * @return true if parsing was successful
   */
  public boolean isSuccess() {
    return parsedArgs != null;
  }

  /**
   * Check if parsing was successful.
   * If this method returns true, {@link #getMessages} will return a non-empty list and
   * {@link #get} will return null.
   * @return true if parsing was successful
   */
  public boolean isFailure() {
    return !isSuccess();
  }

  /**
   * Get the list of parsing errors. If this method returns an empty list,
   * then there were no parsing errors, and {@link #isSuccess} will return {@code true}.
   * @return
   */
  public List<String> getMessages() {
    return messages;
  }

  /**
   * Get the parsing result or {@code null} if there was an error.
   * @return the parsed arguments or {@code null} if  parsing failed
   */
  public ParsedArgs get() {
    return parsedArgs;
  }

}
