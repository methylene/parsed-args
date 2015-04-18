package com.github.methylene.args;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class ExpectationTest {

  @Test
  public void expectFlag() {
    ArgParser parser = ArgParser.builder().flag("1").build();
    ParseResult result = parser.parse("-b", "1");
    assertTrue(result.get().get("-b").isFlag());
    assertTrue(result.get().get("1").isFlag());
  }

  @Test
  public void requiredFailure() {
    ArgParser parser = ArgParser.builder().required("-a").build();
    ParseResult result = parser.parse("-b", "1");
    assertTrue(result.isFailure());
  }

  @Test
  public void mixing() {
    ArgParser parser = ArgParser.builder().required("-a").build();
    ParseResult result = parser.parse("-a", "1", "-a");
    assertTrue(result.isFailure());
  }

  @Test
  public void listFailure() {
    ArgParser parser = ArgParser.builder().list("-a").build();
    ParseResult result = parser.parse("-a", "1", "-a");
    assertTrue(result.isFailure());
  }

  @Test
  public void strangeList() {
    ArgParser parser = ArgParser.builder().list("-a").build();
    ParseResult result = parser.parse("-a", "-a", "-1");
    assertTrue(result.isSuccess());
    assertThat(result.get().get("-a").getStrings(), is(Collections.singletonList("-a")));
  }

  @Test
  public void list() {
    ArgParser parser = ArgParser.builder().list("-a").build();
    ParseResult result = parser.parse("-a", "1", "-a", "2");
    assertTrue(result.isSuccess());
    assertThat(result.get().get("-a").getStrings(), is(Arrays.asList("1", "2")));
  }

  @Test
  public void requiredSuccess() {
    ArgParser parser = ArgParser.builder().required("-a").build();
    ParseResult result = parser.parse("-a", "1", "-b1");
    assertTrue(result.isSuccess());
    assertTrue(result.get().get("-a").isProperty());
    assertTrue(result.get().get("-b").isProperty());
    assertFalse(result.get().get("-a").isFlag());
    assertFalse(result.get().get("-b").isFlag());
    assertThat(result.get().get("-a").getString(), is("1"));
    assertThat(result.get().get("-b").getString(), is("1"));
  }

  @Test
  public void undeclaredProperty() {
    ArgParser parser = ArgParser.builder().required("-a").rejectUndeclared().build();
    ParseResult result = parser.parse("-a", "1", "-b", "1");
    assertTrue(result.isFailure());
  }

  @Test
  public void undeclaredFlag() {
    ArgParser parser = ArgParser.builder().required("-a").rejectUndeclared().build();
    ParseResult result = parser.parse("-a", "1", "-b");
    assertTrue(result.isFailure());
  }

}
