# parsed-args

Very simple command line argument parser in Java.
It just parses the arguments into a map, or throws an exception.

It is not possible to specify mandatory arguments, or register converters and validators.
This is outside the scope of this library, because validation can be done after parsing.

Try it out using `parse.sh`:

    $ ./parse.sh xJf -a 1 -n+2 --day 01 --day 02 +%% --include=**/* - -- -x -y
    -x -> true (Boolean)
    -J -> true (Boolean)
    -f -> true (Boolean)
    -a -> 1 (String)
    -n -> +2 (String)
    --day -> [01, 02] (ArrayList)
    + -> %% (String)
    --include -> **/* (String)
    - -> true (Boolean)
    -- -> [-x, -y] (ArrayList)
