package com.github.methylene.args;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ParsedArgsTest {

  private ArgParser parser = ArgParser.builder().build();

  @Test
  public void testGetInt() {
    assertThat(parser.parse("-n", "1").get().getNumber("-n").get(), is(1L));
    assertThat(parser.parse("-n1").get().getNumber("-n").get(), is(1L));
    assertThat(parser.parse("-m1", "-n1").get().getNumber("-n").get(), is(1L));
    assertThat(parser.parse("-m1", "-n", "1").get().getNumber("-n").get(), is(1L));
    assertThat(parser.parse("-m", "-n1", "-c").get().getNumber("-n").get(), is(1L));
    assertThat(parser.parse("-m", "-n", "1", "-c1").get().getNumber("-n").get(), is(1L));
    assertThat(parser.parse("-m", "1", "-n1", "-c").get().getNumber("-n").get(), is(1L));
  }

  @Test
  public void testFail() {
    assertTrue(parser.parse("-n").get().getNumber("-n").isFailure());
  }

  @Test
  public void testFail2() {
    assertThat(parser.parse("-n1", "1").get().getNumber("-n").get(), is(1L));
  }

  @Test
  public void testFail3() {
    assertThat(parser.parse("-n", "1").get().getNumber("-n").get(), is(1L));
    assertThat(parser.parse("-n", "1", "2").get().getNumber("-n").get(), is(1L));
  }

  @Test
  public void testDash() {
    assertThat(parser.parse("-").get().getFlagCount("-").get(), is(1));
    assertThat(parser.parse("-1", "-", "-2").get().getFlagCount("-").get(), is(1));
    assertEquals(singletonList("-2"), parser.parse("-1", "--", "-2").get().getValues("--").get());
    assertEquals(asList("-2", "-3"), parser.parse("-1", "--", "-2", "-3").get().getValues("--").get());
  }

  @Test
  public void testLong() {
    assertThat(parser.parse("-m", "--num=1", "-c").get().getNumber("--num").get(), is(1L));
  }

  @Test
  public void testLong2() {
    assertThat(parser.parse("--num", "1").get().getNumber("--num").get(), is(1L));
    assertThat(parser.parse("--num=1").get().getNumber("--num").get(), is(1L));
    assertThat(parser.parse("-m1", "--num=1").get().getNumber("--num").get(), is(1L));
    assertThat(parser.parse("-m1", "--num", "1").get().getNumber("--num").get(), is(1L));
    assertThat(parser.parse("-m", "--num=1", "-c").get().getNumber("--num").get(), is(1L));
    assertThat(parser.parse("-m", "--num", "1", "-c1").get().getNumber("--num").get(), is(1L));
    assertThat(parser.parse("-m", "1", "--num=1", "-c").get().getNumber("--num").get(), is(1L));
  }

  @Test
  public void testFlag() {
    assertTrue(parser.parse("-m", "--num=1", "-c").get().getFlag("-m").get());
  }

  @Test
  public void testGetLong() {
    assertEquals("1", parser.parse("--num=1", "-1").get().getString("--num").get());
    assertEquals("1", parser.parse("-m1", "--num=1", "-1").get().getString("--num").get());
  }

  @Test
  public void testPlus() {
    assertThat(parser.parse("+%%").get().getString("+").get(), is("%%"));
  }

  @Test
  public void testLongInvalid() {
    assertThat(parser.parse("--num=1", "1").get().getNumber("--num").get(), is(1L));
  }

  @Test
  public void testLongInvalid2() {
    assertThat(parser.parse("-m1", "--num=1", "1").get().getNumber("--num").get(), is(1L));
    assertThat(parser.parse("-m1", "--num=1", "1").get().getNumber("-m").get(), is(1L));
  }


}
