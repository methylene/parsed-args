package com.github.methylene.args;

public class ArgParser {

  private final Mapper mapper;

  private ArgParser(Mapper mapper) {
    this.mapper = mapper;
  }

  public static ArgParser parser(Mapper mapper) {
    return new ArgParser(mapper);
  }

  public static ArgParser parser() {
    return new ArgParser(Mapper.builder().build());
  }

  public ParsedArgs parse(String... args) {
    return new ParsedArgs(mapper.build(args));
  }

}
