# parsed-args

Very simple command line argument parser in Java.
Its goal is to parse the arguments into a map, or throw an exception.

Please note that it is not possible to specify mandatory arguments,
or register converters and validators.
These things are outside the scope of this library.

There is a script `parse.sh` which reads any number of arguments and prints their map interpretation:

    $ ./parse.sh xJf -a 1 -n+2-3 +%% +%f --day=1 --day 2 - -- -x -y
    -x -> true (Boolean)
    -J -> true (Boolean)
    -f -> true (Boolean)
    -a -> 1 (String)
    -n -> +2-3 (String)
    + -> [%%, %f] (ArrayList)
    --day -> [1, 2] (ArrayList)
    - -> true (Boolean)
    -- -> [-x, -y] (ArrayList)

The example above passes the 13 tokens `xJf`, `-a`, `1`, `-n+2-3`, `+%%`, `+%f`, `--day=1`,
`--day`, `2`, `-`, `--`, `-x` and `-y` to `parse.sh`. Some of these have a special meaning.


### `-`

This is a self contained literal token that does not put the reader in literal mode.
It can appear at most once, otherwise parsing fails.

### `--`

All remaining tokens after this one are treated as arguments

### Tokens that begin with `-` or `--` but are



### BSD style flags

TODO allow bsd style flags everywhere

The very first argument 

Example:
