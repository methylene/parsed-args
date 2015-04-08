# parsed-args

Yet another command line tool for java.

There is a script `parse.sh` which reads any number of arguments and prints their map interpretation. An example invocation might look like this:

    $ ./parse.sh xJf -a 1 -n+2-3 +%% +%f --day=1 --day 2 - -- -x -y

    flag "xJf" occurs 1 times
    parameter "-a" has value "1"
    parameter "-n" has value "+2-3"
    parameter "+" has multiple values: [%%, %f]
    parameter "--day" has multiple values: [1, 2]
    flag "-" occurs 1 times
    parameter "--" has multiple values: [-x, -y]
