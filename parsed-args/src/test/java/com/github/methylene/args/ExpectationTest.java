package com.github.methylene.args;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ExpectationTest {

  @Test
  public void expectFlag() {
    ArgParser parser = ArgParser.builder().expectFlag("1").build();
    ParseResult result = parser.parse("-b", "1");
    assertTrue(result.get().getNumber("-b").isFailure());
    assertTrue(result.get().getFlag("-b").get());
    assertTrue(result.get().getFlag("1").get());
  }

  @Test
  public void expectValue() {
    ArgParser parser = ArgParser.builder().expectFlag("1").build();
    ParseResult result = parser.parse("-b", "1");
    assertTrue(result.get().getNumber("-b").isFailure());
    assertTrue(result.get().getFlag("-b").get());
    assertTrue(result.get().getFlag("1").get());
  }

}
