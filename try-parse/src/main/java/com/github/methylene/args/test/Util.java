package com.github.methylene.args.test;

import com.github.methylene.args.GetResult;
import com.github.methylene.args.ParseResult;
import com.github.methylene.args.ParsedArgs;

class Util {

  static void print(ParseResult parseResult) {

    if (parseResult.isFailure()) {
      System.out.println("== Parsing failed ==");
      for (String message: parseResult.getMessages()) {
        System.out.println(message);
      }
      return;
    }

    ParsedArgs parsedArgs = parseResult.get();
    for (String key : parsedArgs.getKeys()) {
      GetResult value = parsedArgs.get(key);
      if (value.isMixed()) {
        System.out.format("%s: not sure if flag or value%n", key);
      } else if (value.isFlag()) {
        System.out.format("flag \"%s\" occurs %d times%n", key, value.getFlagCount());
      } else if (value.isSingleValue()) {
        System.out.format("parameter \"%s\" has value \"%s\"%n", key, value.getString());
      } else {
        System.out.format("parameter \"%s\" has multiple values: %s%n", key, value.getValues());
      }
    }

  }


}
