package com.github.methylene.args;

import org.junit.Test;

import java.util.regex.Matcher;

import static com.github.methylene.args.Util.EQUALS_TOKEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TokenizerTest {

  @Test
  public void testGnuPattern() {
    Matcher matcher = EQUALS_TOKEN.matcher("--a=b");
    assertTrue(matcher.matches());
    assertEquals(2, matcher.groupCount());
    assertEquals("--a", matcher.group(1));
    assertEquals("b", matcher.group(2));
  }

  @Test
  public void testUnixPattern() {
    Matcher matcher = EQUALS_TOKEN.matcher("-a=b");
    assertTrue(matcher.matches());
    assertEquals(2, matcher.groupCount());
    assertEquals("-a", matcher.group(1));
    assertEquals("b", matcher.group(2));
  }

}
