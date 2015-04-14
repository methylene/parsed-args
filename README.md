# parsed-args

Yet another command line tool for java.

There is a script `parse.sh` which reads any number of arguments and prints their map interpretation. An example invocation might look like this:

    $ ./parse.sh DEFAULT xJf -a 1 -n1 +%% +%f --day=1 --day 2 - -- -x -y
    flag "xJf" occurs 1 times
    parameter "-a" has value "1"
    parameter "-n" has value "1"
    parameter "+" has multiple values: [%%, %f]
    parameter "--day" has multiple values: [1, 2]
    flag "-" occurs 1 times
    parameter "--" has multiple values: [-x, -y]

The DEFAULT parsing rules recognize tokens like "+%%" and "-n1" as shortcuts. 
This is inspired by the unix `date` and `cut` commands.
BSD style options are available:

    $ ./parse.sh BSD auxww
    flag "a" occurs 1 times
    flag "u" occurs 1 times
    flag "x" occurs 1 times
    flag "w" occurs 2 times

Other parsing rules can be configured.
