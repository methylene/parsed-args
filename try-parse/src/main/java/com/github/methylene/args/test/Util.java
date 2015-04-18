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
      if (value.isFlag()) {
        System.out.format("flag \"%s\" occurs %d times%n", key, value.count());
      } else if (value.isParameter() && value.count() == 1) {
        System.out.format("property \"%s\" has value \"%s\"%n", key, value.getValue());
      } else {
        System.out.format("property \"%s\" has multiple values: %s%n", key, value.getValues());
      }
    }

  }


}
