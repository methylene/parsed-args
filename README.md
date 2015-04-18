# parsed-args

Yet another command line tool for java.
This one makes an attempt to parse the input `String[]` without any prior configuration or parameter declaration.
There is a script `parse.sh` which can be used to explore the default parsing behaviour:

    $ ./parse.sh DEFAULT xJf -a 1 -n1 +%% +%f --day=1 --day 2 - -- -x -y
    flag "xJf" occurs 1 times
    property "-a" has value "1"
    property "-n" has value "1"
    property "+" has multiple values: [%%, %f]
    property "--day" has multiple values: [1, 2]
    flag "-" occurs 1 times
    property "--" has multiple values: [-x, -y]

Some things are not allowed by the default parsing rules:

    $ ./parse.sh DEFAULT -a 1 -a
    == Parsing failed ==
    mixing flags and parameters: -a

The DEFAULT parsing rules recognize tokens like "+%%" and "-n1" as shortcuts. 
This is inspired by the unix `date` and `cut` commands.
BSD parsing rules, as used by the unix `ps` commmand, can also be used:

    $ ./parse.sh BSD auxww
    flag "a" occurs 1 times
    flag "u" occurs 1 times
    flag "x" occurs 1 times
    flag "w" occurs 2 times

If necessary, different parsing rules can be configured in a programmatic way.
Of course, full parameter declaration including rejection of undeclared parameters is also possible.
