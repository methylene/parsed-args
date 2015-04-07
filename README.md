# parsed-args

Yet another command line tool for java.

There is a script `parse.sh` which reads any number of arguments and prints their map interpretation. An example invocation might look like this:

    $ ./parse.sh xJf -a 1 -n+2-3 +%% +%f --day=1 --day 2 - -- -x -y

    xJf occurs 1 times
    -a -> 1
    -n -> +2-3
    + -> [%%, %f]
    --day -> [1, 2]
    - occurs 1 times
    -- -> [-x, -y
