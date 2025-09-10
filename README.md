# JPGN Parser

PGN (Portable Game Notation) parser written in Java. I made this as a project to learn how stuff works in this language.
The bare minimum works, parses PGN and displays its output to the terminal. Doesn't fully cover all parts of it, but can
parse valid PGNs and moves just fine, and the of course the metadata too. Some headers are grouped, some are treated
as "extras."

## Getting Started

Clone the repository.

```shell
$ git clone https://github.com/Dev-Siri/jpgn-parser
```

Let your IDE (Intellij or whatever you use) take care of the rest of the stuff in setting up the project. Then run the
project and pass in one command-line argument to it, which should be the PGN source file's path.

```shell
$ java Main.class ./opera-game.pgn
```

In your IDE, you'll probably need to pass it in through the debug options.

## Quirks

Note that this parser counts individual SAN moves of a single move, or what you'd call a ply, as separate `Move`
objects. So you'd notice the comments for one move (If written for the entire move) printed twice. Can't really say
whether that's intentional or an accident.

Pawns appear as `P` because the structured `Move` object is printed in place of the SAN. So just a purposed symbol used
for just pawn moves.

The game may end in the form of `Move(18, NULL, -0)` or something similar due to it parsing the result (`1-0`, `0-1`,
`1/2-1/2` or `*`) as a separate `Move`. However, the result in the metadata is used instead to show the result clearly
with an English statement.

## License

This project is MIT Licensed, see [LICENSE](LICENSE).