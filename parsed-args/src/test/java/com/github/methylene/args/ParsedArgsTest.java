package com.github.methylene.args;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.methylene.args.ParsedArgs.parse;
import static java.util.Arrays.asList;

public class ParsedArgsTest {

  @Test
  public void testGetInt() throws Exception {
    assertEquals(1, parse("-n", "1").getInt("n"));
    assertEquals(1, parse("-n1").getInt("n"));
    assertEquals(1, parse("-m1", "-n1").getInt("n"));
    assertEquals(1, parse("-m1", "-n", "1").getInt("n"));
    assertEquals(1, parse("-m1", "1", "-n1").getInt("n"));
    assertEquals(1, parse("-m", "1", "1,", "-n", "1").getInt("n"));
    assertEquals(1, parse("-m", "-n1", "-c").getInt("n"));
    assertEquals(1, parse("-m", "-n", "1", "-c1").getInt("n"));
    assertEquals(1, parse("-m", "1", "-n1", "-c").getInt("n"));
    assertEquals(1, parse("-m", "1", "1,", "-n", "1", "-c", "1").getInt("n"));
  }

  @Test
  public void testGet() throws Exception {
    assertEquals(asList("1", "1"), parse("-n", "1", "1").get("n"));
    assertEquals(asList("1", "1"), parse("-n1", "1").get("n"));
    assertEquals(asList("1", "1"), parse("-m1", "-n1", "1").get("n"));
    assertEquals(asList("1", "1"), parse("-m1", "-n", "1", "1").get("n"));
    assertEquals(asList("1", "1"), parse("-m", "1", "-n1", "1", "-c").get("n"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail() throws Exception {
    parse("-n").getInt("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail2() throws Exception {
    parse("-n1", "1").getInt("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail3() throws Exception {
    parse("-n", "1", "2").getInt("n");
  }

  @Test
  public void testDash() throws Exception {
    assertTrue(parse("-").contains("-"));
    assertTrue(parse("-1", "-", "-2").contains("-"));
    assertEquals(asList("-2"), parse("-1", "--", "-2").get("--"));
  }

  @Test
  public void testLong() throws Exception {
    assertEquals(1, parse("--num", "1").getInt("num"));
    assertEquals(1, parse("--num=1").getInt("num"));
    assertEquals(1, parse("-m1", "--num=1").getInt("num"));
    assertEquals(1, parse("-m1", "--num", "1").getInt("num"));
    assertEquals(1, parse("-m1", "1", "--num=1").getInt("num"));
    assertEquals(1, parse("-m", "1", "1,", "--num", "1").getInt("num"));
    assertEquals(1, parse("-m", "--num=1", "-c").getInt("num"));
    assertEquals(1, parse("-m", "--num", "1", "-c1").getInt("num"));
    assertEquals(1, parse("-m", "1", "--num=1", "-c").getInt("num"));
    assertEquals(1, parse("-m", "1", "1,", "--num", "1", "-c", "1").getInt("num"));
  }

  @Test
  public void testGetLong() throws Exception {
    assertEquals(asList("1", "1"), parse("--num", "1", "1").get("num"));
    assertEquals(asList("1"), parse("--num=1", "-1").get("num"));
    assertEquals(asList("1"), parse("-m1", "--num=1", "-1").get("num"));
    assertEquals(asList("1", "1"), parse("-m1", "--num", "1", "1").get("num"));
    assertEquals(asList("1", "1"), parse("-m", "1", "--num=1", "-c", "1", "--num=1").get("num"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLongInvalid() {
    parse("--num=1", "1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLongInvalid2() {
    parse("-m1", "--num=1", "1");
  }


}