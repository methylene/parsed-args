package com.github.methylene.args;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ParsedArgsTest {

  private ArgParser parser = ArgParser.builder().build();

  @Test
  public void testGetInt() {
    assertThat(parser.parse("-n", "1").get().get("-n").parseLong(), is(1L));
    assertThat(parser.parse("-n1").get().get("-n").parseLong(), is(1L));
    assertThat(parser.parse("-m1", "-n1").get().get("-n").parseLong(), is(1L));
    assertThat(parser.parse("-m1", "-n", "1").get().get("-n").parseLong(), is(1L));
    assertThat(parser.parse("-m", "-n1", "-c").get().get("-n").parseLong(), is(1L));
    assertThat(parser.parse("-m", "-n", "1", "-c1").get().get("-n").parseLong(), is(1L));
    assertThat(parser.parse("-m", "1", "-n1", "-c").get().get("-n").parseLong(), is(1L));
  }

  @Test
  public void testFlagCount() {
    assertThat(parser.parse("-n").get().get("-n").count(), is(1));
    assertTrue(parser.parse("-n").get().get("-n").isFlag());
  }

  @Test
  public void testFail2() {
    assertThat(parser.parse("-n1", "1").get().get("-n").parseLong(), is(1L));
  }

  @Test
  public void testFail3() {
    assertThat(parser.parse("-n", "1").get().get("-n").parseLong(), is(1L));
    assertThat(parser.parse("-n", "1", "2").get().get("-n").parseLong(), is(1L));
  }

  @Test
  public void testDash() {
    assertThat(parser.parse("-").get().get("-").count(), is(1));
    assertThat(parser.parse("-1", "-", "-2").get().get("-").count(), is(1));
    assertEquals(singletonList("-2"), parser.parse("-1", "--", "-2").get().get("--").getValues());
    assertEquals(asList("-2", "-3"), parser.parse("-1", "--", "-2", "-3").get().get("--").getValues());
  }

  @Test
  public void testLong() {
    assertThat(parser.parse("-m", "--num=1", "-c").get().get("--num").parseLong(), is(1L));
  }

  @Test
  public void testLong2() {
    assertThat(parser.parse("--num", "1").get().get("--num").parseLong(), is(1L));
    assertThat(parser.parse("--num=1").get().get("--num").parseLong(), is(1L));
    assertThat(parser.parse("-m1", "--num=1").get().get("--num").parseLong(), is(1L));
    assertThat(parser.parse("-m1", "--num", "1").get().get("--num").parseLong(), is(1L));
    assertThat(parser.parse("-m", "--num=1", "-c").get().get("--num").parseLong(), is(1L));
    assertThat(parser.parse("-m", "--num", "1", "-c1").get().get("--num").parseLong(), is(1L));
    assertThat(parser.parse("-m", "1", "--num=1", "-c").get().get("--num").parseLong(), is(1L));
  }

  @Test
  public void testFlag() {
    assertTrue(parser.parse("-m", "--num=1", "-c").get().get("-m").isPresent());
  }

  @Test
  public void testGetLong() {
    assertEquals("1", parser.parse("--num=1", "-1").get().get("--num").getValue());
    assertEquals("1", parser.parse("-m1", "--num=1", "-1").get().get("--num").getValue());
  }

  @Test
  public void testPlus() {
    assertThat(parser.parse("+%%").get().get("+").getValue(), is("%%"));
  }

  @Test
  public void testLongInvalid() {
    assertThat(parser.parse("--num=1", "1").get().get("--num").parseLong(), is(1L));
  }

  @Test
  public void testLongInvalid2() {
    assertThat(parser.parse("-m1", "--num=1", "1").get().get("--num").parseLong(), is(1L));
    assertThat(parser.parse("-m1", "--num=1", "1").get().get("-m").parseLong(), is(1L));
  }


}
