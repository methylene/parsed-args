package com.github.methylene.args;

import static com.github.methylene.args.ParsedArgs.factory;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ParsedArgsTest {

  @Test
  public void testGetInt() throws Exception {
    assertEquals(1, factory().parse("-n", "1").getInt("n"));
    assertEquals(1, factory().parse("-n1").getInt("n"));
    assertEquals(1, factory().parse("-m1", "-n1").getInt("n"));
    assertEquals(1, factory().parse("-m1", "-n", "1").getInt("n"));
    assertEquals(1, factory().parse("-m", "-n1", "-c").getInt("n"));
    assertEquals(1, factory().parse("-m", "-n", "1", "-c1").getInt("n"));
    assertEquals(1, factory().parse("-m", "1", "-n1", "-c").getInt("n"));
  }


  @Test(expected = IllegalArgumentException.class)
  public void testDash2() throws Exception {
    factory().parse("-n1", "1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail() throws Exception {
    factory().parse("-n").getInt("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail2() throws Exception {
    factory().parse("-n1", "1").getInt("n");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFail3() throws Exception {
    factory().parse("-n", "1", "2").getInt("n");
  }

  @Test
  public void testDash() throws Exception {
    assertTrue(factory().parse("-").contains("-"));
    assertTrue(factory().parse("-1", "-", "-2").contains("-"));
    assertEquals(asList("-2"), factory().parse("-1", "--", "-2").get("--"));
  }

  @Test
  public void testLong() throws Exception {
    assertEquals(1, factory().parse("--num", "1").getInt("num"));
    assertEquals(1, factory().parse("--num=1").getInt("num"));
    assertEquals(1, factory().parse("-m1", "--num=1").getInt("num"));
    assertEquals(1, factory().parse("-m1", "--num", "1").getInt("num"));
    assertEquals(1, factory().parse("-m", "--num=1", "-c").getInt("num"));
    assertEquals(1, factory().parse("-m", "--num", "1", "-c1").getInt("num"));
    assertEquals(1, factory().parse("-m", "1", "--num=1", "-c").getInt("num"));
  }

  @Test
  public void testGetLong() throws Exception {
    assertEquals(asList("1"), factory().parse("--num=1", "-1").get("num"));
    assertEquals(asList("1"), factory().parse("-m1", "--num=1", "-1").get("num"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLongInvalid() {
    factory().parse("--num=1", "1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLongInvalid2() {
    factory().parse("-m1", "--num=1", "1");
  }


}