package com.github.methylene.args;

public interface Predicate<T> {

  boolean matches(T arg);

}
