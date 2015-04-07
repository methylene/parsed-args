package com.github.methylene.args.test;

import com.github.methylene.args.ArgParser;
import com.github.methylene.args.ParseResult;
import com.github.methylene.args.ParsedArgs;

import java.util.List;

public class PrintArgs {

  public static final ArgParser ARG_PARSER = ArgParser.builder().build();

  public static void main(String[] args) {

    ParseResult parseResult = ARG_PARSER.parse(args);

    if (parseResult.isFailure()) {
      System.out.println("== Parsing failed ==");
      for (String message: parseResult.getMessages()) {
        System.out.println(message);
      }
      return;
    }

    ParsedArgs parsedArgs = parseResult.get();
    for (String key : parsedArgs.getKeys()) {
      ParsedArgs.ArgType type = parsedArgs.getType(key);
      switch (type) {
        case VALUE: {
          List<String> values = parsedArgs.getValues(key).get();
          System.out.format("%s -> %s%n", key, values.size() == 1 ? values.get(0) : values);
          break;
        }
        case FLAG: {
          Integer flagCount = parsedArgs.getFlagCount(key).get();
          System.out.format("%s occurs %d times%n", key, flagCount);
          break;
        }
        case MIXED: {
          System.out.format("%s: not sure if flag or value%n", key);
          break;
        }
        case NOTHING: {
          System.out.format("%s: nothing%n", key);
          break;
        }
      }

    }

  }

}