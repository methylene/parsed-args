# parsed-args

Very simple and opinionated command line argument parser in Java.
No need for annotations and xml, prefer convention over configuration.
In fact, there aren't currently any configuration options.

Try it out using parse.sh:

    $ ./parse.sh xzf --day 2015-03-01 --day 2015-03-02 --include=**/* - -a -- -x -y -z
    [xzf, --day, 2015-03-01, --day, 2015-03-02, --include=**/*, -, -a, --, -x, -y, -z]
    -x -> true (Boolean)
    -z -> true (Boolean)
    -f -> true (Boolean)
    --day -> [2015-03-01, 2015-03-02] (ArrayList)
    --include -> **/* (String)
    - -> true (Boolean)
    -a -> true (Boolean)
    -- -> [-x, -y, -z] (ArrayList)
