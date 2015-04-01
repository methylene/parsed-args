package com.github.methylene.args;

import static com.github.methylene.args.ParsedArgs.parse;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;

public class ParsedArgsTest {

  @Test
  public void testGetInt() throws Exception {
    assertThat(parse("-n", "1").getNumber("n"), is(1L));
    assertThat(parse("-n1").getNumber("n"), is(1L));
    assertThat(parse("-m1", "-n1").getNumber("n"), is(1L));
    assertThat(parse("-m1", "-n", "1").getNumber("n"), is(1L));
    assertThat(parse("-m", "-n1", "-c").getNumber("n"), is(1L));
    assertThat(parse("-m", "-n", "1", "-c1").getNumber("n"), is(1L));
    assertThat(parse("-m", "1", "-n1", "-c").getNumber("n"), is(1L));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testDash2() throws Exception {
    parse("-n1", "1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail() throws Exception {
    parse("-n").getNumber("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail2() throws Exception {
    parse("-n1", "1").getNumber("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail3() throws Exception {
    parse("-n", "1", "2").getNumber("n");
  }

  @Test
  public void testDash() throws Exception {
    assertTrue(parse("-").getFlag("-"));
    assertTrue(parse("-1", "-", "-2").getFlag("-"));
    assertEquals("-2", parse("-1", "--", "-2").getObject("--"));
    assertEquals(asList("-2", "-3"), parse("-1", "--", "-2", "-3").getObject("--"));
  }

  @Test
  public void testLong() throws Exception {
    assertThat(parse("--num", "1").getNumber("num"), is(1L));
    assertThat(parse("--num=1").getNumber("num"), is(1L));
    assertThat(parse("-m1", "--num=1").getNumber("num"), is(1L));
    assertThat(parse("-m1", "--num", "1").getNumber("num"), is(1L));
    assertThat(parse("-m", "--num=1", "-c").getNumber("num"), is(1L));
    assertThat(parse("-m", "--num", "1", "-c1").getNumber("num"), is(1L));
    assertThat(parse("-m", "1", "--num=1", "-c").getNumber("num"), is(1L));
  }

  @Test
  public void testGetLong() throws Exception {
    assertEquals("1", parse("--num=1", "-1").getObject("num"));
    assertEquals("1", parse("-m1", "--num=1", "-1").getObject("num"));
  }

  @Test
  public void testPlus() {
    assertThat(parse("+%%").getString("+"), is("%%"));
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