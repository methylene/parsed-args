# parsed-args

Yet another command line tool for java.
This one makes an attempt to parse the input `String[]` without any prior configuration or parameter declaration.
There is a script `parse.sh` which can be used to explore the default parsing behaviour:

    $ ./parse.sh DEFAULT xJf -a 1 -n1 +%% +%f --day=1 --day 2 - -- -x -y
    flag "xJf" occurs 1 times
    parameter "-a" has value "1"
    parameter "-n" has value "1"
    parameter "+" has multiple values: [%%, %f]
    parameter "--day" has multiple values: [1, 2]
    flag "-" occurs 1 times
    parameter "--" has multiple values: [-x, -y]

The first argument to the script, `DEFAULT` in this case, tells it which of the built-in parsing behaviours to use.
The parser's job is to tell <b>parameters</b> and <b>flags</b> apart.
The core algorithm can be found in [Tokenizer.java](parsed-args/src/main/java/com/github/methylene/args/Tokenizer.java)
and implements roughly this rule:

* <i>token starts with a dash AND next next token exists AND looks like a number</i> `=>` <i>it's a parameter<i>
* <i>token starts with a dash AND (this is the last token OR next token starts with a dash)</i> `=>` <i>it's a flag<i>

By default, a flag or parameter can appear any number of times. One thing however, is never allowed:

    $ ./parse.sh DEFAULT -a 1 -a
    == Parsing failed ==
    mixing flags and parameters: -a

The DEFAULT parsing rules recognize tokens like `+%%` and `-n1` as shortcuts. 
This is similar to the unix `date` and `cut` commands.
A double dash will by default cause all remaining arguments to be read as a list of literal strings,
similar to various unix commmands like `rm` or `touch`.

BSD parsing rules, as recognized by the unix `ps` commmand, can also be used:

    $ ./parse.sh BSD auxww
    flag "a" occurs 1 times
    flag "u" occurs 1 times
    flag "x" occurs 1 times
    flag "w" occurs 2 times

If necessary, different parsing rules can be configured in a programmatic and very flexible way.
Have a look at the source of `Mapper#builder` to get an idea.
Of course, full parameter declaration and rejection of undeclared parameters or flags, is also possible:

````java
ArgParser parser = ArgParser.builder().required("-a").rejectUndeclared().build();
ParseResult result = parser.parse("-a", "1", "-b");
// -b is not declared, and rejectUndeclared is set, therefore parsing fails
assertTrue(result.isFailure());                
````
