package com.github.methylene.args;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ParsedArgsTest {

  private ArgParser parser = ArgParser.parser();

  @Test
  public void testGetInt() {
    assertThat(parser.parse("-n", "1").getNumber("-n"), is(1L));
    assertThat(parser.parse("-n1").getNumber("-n"), is(1L));
    assertThat(parser.parse("-m1", "-n1").getNumber("-n"), is(1L));
    assertThat(parser.parse("-m1", "-n", "1").getNumber("-n"), is(1L));
    assertThat(parser.parse("-m", "-n1", "-c").getNumber("-n"), is(1L));
    assertThat(parser.parse("-m", "-n", "1", "-c1").getNumber("-n"), is(1L));
    assertThat(parser.parse("-m", "1", "-n1", "-c").getNumber("-n"), is(1L));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail() {
    parser.parse("-n").getNumber("-n");
  }

  @Test
  public void testFail2() {
    assertThat(parser.parse("-n1", "1").getNumber("-n"), is(1L));
  }

  @Test
  public void testFail3() {
    assertThat(parser.parse("-n", "1").getNumber("-n"), is(1L));
    assertThat(parser.parse("-n", "1", "2").getNumber("-n"), is(1L));
  }

  @Test
  public void testDash() {
    assertThat(parser.parse("-").getFlag("-"), is(1));
    assertThat(parser.parse("-1", "-", "-2").getFlag("-"), is(1));
    assertEquals(singletonList("-2"), parser.parse("-1", "--", "-2").getList("--"));
    assertEquals(asList("-2", "-3"), parser.parse("-1", "--", "-2", "-3").getList("--"));
  }

  @Test
  public void testLong() {
    assertThat(parser.parse("-m", "--num=1", "-c").getNumber("--num"), is(1L));
  }

  @Test
  public void testLong2() {
    assertThat(parser.parse("--num", "1").getNumber("--num"), is(1L));
    assertThat(parser.parse("--num=1").getNumber("--num"), is(1L));
    assertThat(parser.parse("-m1", "--num=1").getNumber("--num"), is(1L));
    assertThat(parser.parse("-m1", "--num", "1").getNumber("--num"), is(1L));
    assertThat(parser.parse("-m", "--num=1", "-c").getNumber("--num"), is(1L));
    assertThat(parser.parse("-m", "--num", "1", "-c1").getNumber("--num"), is(1L));
    assertThat(parser.parse("-m", "1", "--num=1", "-c").getNumber("--num"), is(1L));
  }

  @Test
  public void testFlag() {
    assertThat(parser.parse("-m", "--num=1", "-c").getFlag("-m"), is(1));
  }

  @Test
  public void testGetLong() {
    assertEquals("1", parser.parse("--num=1", "-1").getString("--num"));
    assertEquals("1", parser.parse("-m1", "--num=1", "-1").getString("--num"));
  }

  @Test
  public void testPlus() {
    assertThat(parser.parse("+%%").getString("+"), is("%%"));
  }

  @Test
  public void testLongInvalid() {
    assertThat(parser.parse("--num=1", "1").getNumber("--num"), is(1L));
  }

  @Test
  public void testLongInvalid2() {
    assertThat(parser.parse("-m1", "--num=1", "1").getNumber("--num"), is(1L));
    assertThat(parser.parse("-m1", "--num=1", "1").getNumber("-m"), is(1L));
  }


}
