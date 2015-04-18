package com.github.methylene.args;

import com.github.methylene.args.predicate.IntegerPredicates;
import com.github.methylene.args.predicate.ListPredicates;
import com.github.methylene.args.predicate.Predicates;

import java.util.*;

public class ArgParser {

  public enum RejectionCondition {
    UNDECLARED_FLAG, UNDECLARED_PARAMETER
  }

  public enum ArityRule {
    ZERO_OR_MORE(ListPredicates.hasCardinality(IntegerPredicates.geq(0))),
    ZERO_OR_ONE(Predicates.or(
        ListPredicates.hasCardinality(IntegerPredicates.exactly(0)),
        ListPredicates.hasCardinality(IntegerPredicates.exactly(1)))),
    ONE_OR_MORE(ListPredicates.hasCardinality(IntegerPredicates.geq(1))),
    EXACTLY_ONE(ListPredicates.hasCardinality(IntegerPredicates.exactly(1)));
    private final Predicate<List<Token>> predicate;

    ArityRule(Predicate<List<Token>> predicate) {this.predicate = predicate;}

    public Predicate<List<Token>> getPredicate() {
      return predicate;
    }
  }

  private final Set<RejectionCondition> rejectionConditions;

  private static final class Expectation {

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

    private static Expectation parameter(String field, ArityRule mult) {
      return new Expectation(TokenValue.ValType.PARAMETER, field, "expected parameter with arity " + mult,
          Predicates.and(TokenValue.ValType.PARAMETER.getPredicate(), mult.getPredicate()));
    }

    private static Expectation flag(String field, ArityRule mult) {
      return new Expectation(TokenValue.ValType.FLAG, field, "expected flag with arity " + mult,
          Predicates.and(TokenValue.ValType.FLAG.getPredicate(), mult.getPredicate()));
    }

    private boolean isFlag() {
      return type == TokenValue.ValType.FLAG;
    }

    private String getField() {
      return field;
    }

  }

  public static class ParserBuilder {

    private final Mapper mapper;
    private final List<Expectation> expectations = new ArrayList<Expectation>();
    private final Set<RejectionCondition> rejectionConditions = new HashSet<RejectionCondition>(RejectionCondition.values().length);

    private ParserBuilder(Mapper mapper) {
      this.mapper = mapper;
    }

    private ParserBuilder parameter(String field, ArityRule mult) {
      expectations.add(Expectation.parameter(field, mult));
      return this;
    }

    public ParserBuilder required(String field) {
      return parameter(field, ArityRule.EXACTLY_ONE);
    }

    public ParserBuilder optional(String field) {
      return parameter(field, ArityRule.ZERO_OR_ONE);
    }

    public ParserBuilder list(String field) {
      return parameter(field, ArityRule.ZERO_OR_MORE);
    }

    public ParserBuilder flag(String field, ArityRule mult) {
      expectations.add(Expectation.flag(field, mult));
      return this;
    }

    public ParserBuilder flag(String field) {
      return flag(field, ArityRule.ZERO_OR_ONE);
    }

    public ParserBuilder rejectUndeclared() {
      rejectionConditions.addAll(Arrays.asList(RejectionCondition.UNDECLARED_FLAG, RejectionCondition.UNDECLARED_PARAMETER));
      return this;
    }

    public ArgParser build() {
      return new ArgParser(modifiedMapper(expectations, mapper), expectations,
          rejectionConditions.isEmpty() ? Collections.<RejectionCondition>emptySet() : EnumSet.copyOf(rejectionConditions));
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

  private ArgParser(Mapper mapper, List<Expectation> expectations, Set<RejectionCondition> rejectionConditions) {
    this.mapper = mapper;
    this.rejectionConditions = rejectionConditions;
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

  private static boolean isMixed(List<Token> tokens) {
    if (tokens.isEmpty())
      return false;
    TokenValue.ValType type = tokens.get(0).getToken().getValue().getType();
    for (Token val : tokens)
      if (val.getType() != type)
        return true;
    return false;
  }


  public ParseResult parse(String... args) {

    Map<String, List<Token>> map = mapper.build(args);
    List<String> messages = new ArrayList<String>();

    for (Expectation expectation : expectations) {
      if (!expectation.predicate.matches(map.get(expectation.field))) {
        messages.add(String.format("%s: %s", expectation.field, expectation.message));
      }
    }

    if (!messages.isEmpty())
      return ParseResult.failure(messages);

    for (String key : map.keySet()) {
      if (isMixed(map.get(key))) {
        messages.add("mixing flags and parameters: " + key);
      }
    }

    if (!messages.isEmpty())
      return ParseResult.failure(messages);

    ParsedArgs parsedArgs = new ParsedArgs(mapper.build(args));

    Map<String, TokenValue.ValType> declared = new HashMap<String, TokenValue.ValType>(expectations.size());

    for (Expectation expectation : expectations) {
      declared.put(expectation.getField(), expectation.type);
    }

    if (rejectionConditions.contains(RejectionCondition.UNDECLARED_FLAG)) {
      for (String key : parsedArgs.getKeys()) {
        if (parsedArgs.get(key).isFlag()) {
          if (declared.get(key) == null || declared.get(key) != TokenValue.ValType.FLAG) {
            messages.add("undeclared flag: " + key);
          }
        }
      }
    }

    if (rejectionConditions.contains(RejectionCondition.UNDECLARED_PARAMETER)) {
      for (String key : parsedArgs.getKeys()) {
        if (parsedArgs.get(key).isParameter()) {
          if (declared.get(key) == null || declared.get(key) != TokenValue.ValType.PARAMETER) {
            messages.add("undeclared parameter: " + key + " = " + parsedArgs.get(key).getValues());
          }
        }
      }
    }

    if (!messages.isEmpty())
      return ParseResult.failure(messages);

    return ParseResult.success(parsedArgs);

  }

}
