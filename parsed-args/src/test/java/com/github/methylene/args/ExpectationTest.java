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
    // 1 is declared as a flag, therefore -b is also a flag
    ParseResult result = parser.parse("-b", "1");
    assertTrue(result.get().get("-b").isFlag());
    assertTrue(result.get().get("1").isFlag());
  }

  @Test
  public void requiredFailure() {
    ArgParser parser = ArgParser.builder().required("-a").build();
    // required parameter -a is missing
    ParseResult result = parser.parse("-b", "1");
    assertTrue(result.isFailure());
  }

  @Test
  public void requiredShortNumeric() {
    ArgParser parser = ArgParser.builder()
        .required("-n")
        .flag("-v")
        .build();
    // using short numeric form for required parameter -n
    ParseResult result = parser.parse("-n1");
    assertTrue(result.isSuccess());
  }

  @Test
  public void mixing() {
    ArgParser parser = ArgParser.builder().build();
    // missing value for second -a
    ParseResult result = parser.parse("-a", "1", "-a");
    assertTrue(result.isFailure());
  }

  @Test
  public void validBecauseDeclared() {
    ArgParser parser = ArgParser.builder().list("-a").build();
    // no mixing: -a is used as a value
    ParseResult result = parser.parse("-a", "-a", "-1");
    assertTrue(result.isSuccess());
    assertThat(result.get().get("-a").getValues(), is(Collections.singletonList("-a")));
  }

  @Test
  public void list() {
    ArgParser parser = ArgParser.builder().list("-a").build();
    ParseResult result = parser.parse("-a", "1", "-a", "2");
    assertTrue(result.isSuccess());
    assertThat(result.get().get("-a").getValues(), is(Arrays.asList("1", "2")));
  }

  @Test
  public void mixed() {
    ArgParser parser = ArgParser.builder().build();
    // -a is first used as a parameter and then as a flag
    ParseResult result = parser.parse("-a", "1", "-a", "-b4");
    assertTrue(result.isFailure());
  }

  @Test
  public void notMixed() {
    ArgParser parser = ArgParser.builder().list("-a").build();
    // -a is declared, so -b4 is treated as a value
    ParseResult result = parser.parse("-a", "1", "-a", "-b4");
    assertTrue(result.isSuccess());
    assertThat(result.get().get("-a").getValues(), is(Arrays.asList("1", "-b4")));
  }

  @Test
  public void requiredSuccess() {
    ArgParser parser = ArgParser.builder().required("-a").build();
    ParseResult result = parser.parse("-a", "1", "-b1");
    assertTrue(result.isSuccess());
    assertTrue(result.get().get("-a").isParameter());
    assertTrue(result.get().get("-b").isParameter());
    assertFalse(result.get().get("-a").isFlag());
    assertFalse(result.get().get("-b").isFlag());
    assertThat(result.get().get("-a").getValue(), is("1"));
    assertThat(result.get().get("-b").getValue(), is("1"));
  }

  @Test
  public void undeclaredParameter() {
    ArgParser parser = ArgParser.builder().required("-a").rejectUndeclared().build();
    // -b is not declared
    ParseResult result = parser.parse("-a", "1", "-b", "1");
    assertTrue(result.isFailure());
  }

  @Test
  public void undeclaredFlag() {
    ArgParser parser = ArgParser.builder().required("-a").rejectUndeclared().build();
    // -b is not declared
    ParseResult result = parser.parse("-a", "1", "-b");
    assertTrue(result.isFailure());
  }

}
