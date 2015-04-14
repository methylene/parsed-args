package com.github.methylene.args.test;

import com.github.methylene.args.ArgParser;
import com.github.methylene.args.Mapper;

import java.util.Locale;

public class PrintArgs {

  private static String[] shift(String[] args) {
    String[] newArgs = new String[args.length - 1];
    System.arraycopy(args, 1, newArgs, 0, newArgs.length);
    return newArgs;
  }

  public static void main(String[] args) {

    if (args.length == 0)
      return;

    Mapper.Flavour flavour;

    try {
      flavour = Mapper.Flavour.valueOf(args[0].toUpperCase(Locale.US));
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
      return;
    }

    args = shift(args);

    // Create parser
    Mapper mapper = Mapper.builder(flavour).build();
    ArgParser parser = ArgParser.builder(mapper).build();

    // Parse and print
    Util.print(parser.parse(args));

  }

}
