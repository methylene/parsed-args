package com.github.methylene.args.test;

import com.github.methylene.args.ParsedArgs;

public class PrintArgs {

  public static void main(String[] args) {
    try {
      ParsedArgs parsedArgs = ParsedArgs.parse(args);
      for (String key : parsedArgs.getKeys()) {
        Object val = parsedArgs.get(key);
        System.out.format("%s -> %s (%s)%n", key, val, val.getClass().getSimpleName());
      }
    } catch (RuntimeException e) {
      e.printStackTrace();
    }
  }

}
