package com.github.methylene.args;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ExpectationTest {

  @Test
  public void expectFlag() {
    ArgParser parser = ArgParser.builder().expectFlag("1").build();
    ParseResult result = parser.parse("-b", "1");
    assertTrue(result.get().get("-b").isFlag());
    assertTrue(result.get().get("1").isFlag());
  }

}
