package com.github.methylene.args;

import com.github.methylene.args.predicate.ListPredicates;
import com.github.methylene.args.predicate.Predicates;

import java.util.*;

public class ArgParser {

  public enum StrictnessRule {
    REJECT_UNDECLARED_FLAGS, REJECT_UNDECLARED_PROPERTIES, REJECT_EMPTY_PROPERTY, REJECT_MIXED
  }

  private final Set<StrictnessRule> strictnessRules;

  private static class Expectation {

    private final TokenValue.ValType type;
    private final String field;
    private final Predicate<List<Token>> predicate;
    private final String message;


    private Expectation(TokenValue.ValType type, String field, String message, Predicate<List<Token>> predicate) {
      this.type = type;
      this.field = field;
      this.predicate = predicate;
      this.message = message;
    }

    static Expectation expect(String field, String message, Predicate<List<Token>> predicate) {
      return new Expectation(TokenValue.ValType.VALUE, field, message, predicate);
    }

    static Expectation expectSingleFlag(String field) {
      return new Expectation(TokenValue.ValType.FLAG, field, "single flag expected", ListPredicates.isSingleFlag());
    }

    static Expectation expectFlag(String field, String message, Predicate<List<Token>> predicate) {
      return new Expectation(TokenValue.ValType.FLAG, field, "single flag expected", predicate);
    }

    static Expectation expectMultiFlag(String field) {
      return new Expectation(TokenValue.ValType.FLAG, field, null, Predicates.<List<Token>>anything());
    }

    TokenValue.ValType getType() {
      return type;
    }

    boolean isFlag() {
      return type == TokenValue.ValType.FLAG;
    }

    String getField() {
      return field;
    }

    public static class ExpectationBuilder {

      private final String field;
      private String message = null;
      private final List<Predicate<List<Token>>> predicates;
      private final ParserBuilder parserBuilder;
      private boolean rejectUnknown;

      private ExpectationBuilder(ParserBuilder parserBuilder, String field) {
        this.field = field;
        this.predicates = new ArrayList<Predicate<List<Token>>>();
        this.parserBuilder = parserBuilder;
      }

      public ExpectationBuilder setMessage(String message) {
        this.message = message;
        return this;
      }

      public ExpectationBuilder and(Predicate<List<Token>> predicate) {
        predicates.add(predicate);
        return this;
      }

      public ParserBuilder done() {
        Predicate<List<Token>> predicate = predicates.isEmpty() ? Predicates.<List<Token>>anything() : Predicates.and(predicates);
        parserBuilder.expectations.add(expect(field, message, predicate));
        return parserBuilder;
      }

    }

  }

  public static class ParserBuilder {

    private final Mapper mapper;
    private List<Expectation> expectations = new ArrayList<Expectation>();
    private Set<StrictnessRule> strictnessRules = new HashSet<StrictnessRule>(StrictnessRule.values().length);

    private ParserBuilder(Mapper mapper) {
      this.mapper = mapper;
    }

    public Expectation.ExpectationBuilder expectValue(String field) {
      return new Expectation.ExpectationBuilder(this, field);
    }

    public ParserBuilder expectFlag(String field) {
      expectations.add(Expectation.expectSingleFlag(field));
      return this;
    }

    public ParserBuilder expectFlag(String field, String message, Predicate<List<Token>> predicate) {
      expectations.add(Expectation.expectFlag(field, message, predicate));
      return this;
    }

    public ParserBuilder expectMultiFlag(String field) {
      expectations.add(Expectation.expectMultiFlag(field));
      return this;
    }

    public ParserBuilder addStrictnessRule(StrictnessRule rule) {
      strictnessRules.add(rule);
      return this;
    }

    public ArgParser build() {
      return new ArgParser(modifiedMapper(expectations, mapper), expectations,
          strictnessRules.isEmpty() ? Collections.<StrictnessRule>emptySet() : EnumSet.copyOf(strictnessRules));
    }

  }

  public static ParserBuilder builder() {
    return new ParserBuilder(Mapper.builder().build());
  }

  public static ParserBuilder builder(Mapper mapper) {
    return new ParserBuilder(mapper);
  }

  private final Mapper mapper;
  private final List<Expectation> expectations;

  private ArgParser(Mapper mapper, List<Expectation> expectations, Set<StrictnessRule> strictnessRules) {
    this.mapper = mapper;
    this.strictnessRules = strictnessRules;
    this.expectations = expectations;
  }

  static Mapper modifiedMapper(List<Expectation> expectations, Mapper mapper) {
    if (expectations.isEmpty())
      return mapper;
    Set<String> atoms = new HashSet<String>(expectations.size());
    Set<String> strongBindings = new HashSet<String>(expectations.size());
    for (Expectation expectation : expectations) {
      if (expectation.isFlag())
        atoms.add(expectation.getField());
      else
        strongBindings.add(expectation.getField());
    }
    return Mapper.builder()
        .setExpanders(mapper.getExpanders())
        .setAtomic(Predicates.or(mapper.getTokenizer().getAtomic(), Predicates.in(atoms)))
        .setStrongBinding(Predicates.or(mapper.getTokenizer().getStrongBinding(), Predicates.in(strongBindings)))
        .setWeakBinding(mapper.getTokenizer().getWeakBinding())
        .build();
  }

  public ParseResult parse(String... args) {
    Map<String, List<Token>> map = mapper.build(args);
    List<String> messages = new ArrayList<String>();
    for (Expectation expectation : expectations) {
      if (!expectation.predicate.matches(map.get(expectation.field))) {
        String fieldName = expectation.field.replace("'", "''");
        String msg = expectation.message == null ? "" : expectation.message.replace("'", "''");
        messages.add(String.format("'%s' == '%s' is not ok", fieldName, msg));
      }
    }


    ParsedArgs parsedArgs = new ParsedArgs(map);

    // TODO unit tests for strictness rules, also TODO: how to make a property required?
    if (strictnessRules.contains(StrictnessRule.REJECT_UNDECLARED_FLAGS)) {
      for (String key : parsedArgs.getKeys()) {
        if (parsedArgs.get(key).isFlag()) {
          if (!mapper.getTokenizer().getAtomic().matches(key)) {
            messages.add("unknown flag: " + key);
          }
        }
      }
    }

    if (strictnessRules.contains(StrictnessRule.REJECT_UNDECLARED_PROPERTIES)) {
      for (String key : parsedArgs.getKeys()) {
        if (parsedArgs.get(key).isValues()) {
          if (!mapper.getTokenizer().getStrongBinding().matches(key)) {
            messages.add("unknown property: " + key);
          }
        }
      }
    }

    if (strictnessRules.contains(StrictnessRule.REJECT_MIXED)) {
      for (String key : parsedArgs.getKeys()) {
        if (parsedArgs.get(key).isMixed()) {
          messages.add("mixing flags and values: " + key);
        }
      }
    }

    // this might happen if a strong binding key is the last argument
    if (strictnessRules.contains(StrictnessRule.REJECT_EMPTY_PROPERTY)) {
      for (String key : parsedArgs.getKeys()) {
        if (!mapper.getTokenizer().getStrongBinding().matches(key) &&
            (!parsedArgs.get(key).isValues() || parsedArgs.get(key).getValues().isEmpty())) {
          messages.add("property needs a value: " + key);
        }
      }
    }

    if (!messages.isEmpty())
      return ParseResult.failure(messages);

    return ParseResult.success(parsedArgs);
  }

}
