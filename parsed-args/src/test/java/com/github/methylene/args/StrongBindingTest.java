package com.github.methylene.args;

import org.junit.Test;
import static com.github.methylene.args.predicate.Predicates.in;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class StrongBindingTest {

  private ArgParser parser = ArgParser.builder(Mapper.builder().setStrongBinding(in("-m")).build()).build();

  @Test
  public void testStrong() {
    assertThat(parser.parse("-m", "--num=1", "-c").get().get("-m").getValue(), is("--num=1"));
    assertThat(parser.parse("-m", "--num", "1").get().get("-m").getValue(), is("--num"));
  }

}
