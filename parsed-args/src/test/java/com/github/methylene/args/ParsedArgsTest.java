package com.github.methylene.args;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParsedArgsTest {

  @Test
  public void testGetInt() throws Exception {
    assertEquals(1, ParsedArgs.parse(new String[]{"-n", "1"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-n1"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m1", "-n1"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m1", "-n", "1"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m1", "1", "-n1"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m", "1", "1,", "-n", "1"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m", "-n1", "-c"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m", "-n", "1", "-c1"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m", "1", "-n1", "-c"}).getInt("n"));
    assertEquals(1, ParsedArgs.parse(new String[]{"-m", "1", "1,", "-n", "1", "-c", "1"}).getInt("n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail() throws Exception {
    ParsedArgs.parse(new String[]{"-n"}).getInt("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail2() throws Exception {
    ParsedArgs.parse(new String[]{"-n1", "1"}).getInt("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail3() throws Exception {
    ParsedArgs.parse(new String[]{"-n", "1", "2"}).getInt("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail4() throws Exception {
    ParsedArgs.parse(new String[]{"-"});
  }

}