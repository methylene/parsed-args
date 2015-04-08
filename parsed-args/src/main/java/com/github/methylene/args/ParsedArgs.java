package com.github.methylene.args;

import java.util.*;

public final class ParsedArgs {

  private final Map<String, List<Token>> parsed;

  ParsedArgs(Map<String, List<Token>> parsed) {
    LinkedHashMap<String, List<Token>> copy = new LinkedHashMap<String, List<Token>>(parsed.size());
    for (Map.Entry<String, List<Token>> e: parsed.entrySet()) {
      copy.put(e.getKey(), Collections.unmodifiableList(e.getValue()));
    }
    this.parsed = Collections.unmodifiableMap(copy);
  }

  public GetResult get(String key) {
    List<Token> o = parsed.get(key);
    if (o == null)
      return new GetResult(Collections.<TokenValue>emptyList());
    ArrayList<TokenValue> result = new ArrayList<TokenValue>();
    for (Token arg : o) {
      for (TokenValue val : arg.getValues()) {
        result.add(val);
      }
    }
    return new GetResult(result);
  }

  public Set<String> getKeys() {
    return parsed.keySet();
  }

  public Map<String, List<Token>> getMap() {
    return parsed;
  }

}
