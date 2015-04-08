package com.github.methylene.args.test;

import com.github.methylene.args.ArgParser;

public class PrintArgs {

  public static void main(String[] args) {

    // Create default parser
    ArgParser parser = ArgParser.builder().build();

    // Parse and print
    Util.print(parser.parse(args));

  }

}
