# parsed-args

Very simple and opinionated command line argument parser in Java.
No need for annotations and xml, prefer convention over configuration.
In fact, there aren't currently any configuration options.

Try it out using `parse.sh`:

    $ ./parse.sh xzf -a 1 -n100 --day 01 --day 02 --include=**/* - -- -x -y -z
    -x -> true (Boolean)
    -z -> true (Boolean)
    -f -> true (Boolean)
    -a -> 1 (String)
    -n -> 100 (String)
    --day -> [01, 02] (ArrayList)
    --include -> **/* (String)
    - -> true (Boolean)
    -- -> [-x, -y, -z] (ArrayList)
