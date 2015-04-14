package com.github.methylene.args.test;

import com.github.methylene.args.ArgParser;
import com.github.methylene.args.Mapper;

import java.util.Arrays;
import java.util.Locale;

public class PrintArgs {

  private static String[] shift(String[] args) {
    String[] newArgs = new String[args.length - 1];
    System.arraycopy(args, 1, newArgs, 0, newArgs.length);
    return newArgs;
  }

  private static void synopsis() {
    System.out.println("Arguments: [flavour [more_args]*] ");
    System.out.println("Available flavours: " + Arrays.asList(Mapper.Flavour.values()));
    System.exit(1);
  }

  public static void main(String[] args) {

    if (args.length == 0)
      synopsis();

    Mapper.Flavour flavour;

    try {
      flavour = Mapper.Flavour.valueOf(args[0].toUpperCase(Locale.US));
    } catch (RuntimeException e) {
      e.printStackTrace();
      synopsis();
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
