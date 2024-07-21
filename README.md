# Puzzle Quest - A college project


This is a Java-based game in which players take turns matching pieces on a board. The game features a variety of piece types, each with its own unique effect on the game state.

## Features

- Two-player game with turn-based mechanics.
- Different types of pieces (skull, red, blue, green, yellow, gold, experience) with unique effects.
- Ability to save and load the game state.
- Ability to delete a saved game state.
- The game state is saved after each turn.

## Classes

- `Game`: The main class that controls the flow of the game.
- `Player`: Represents a player in the game.
- `Tile`: Represents a piece on the game board.
- `Board`: Represents the game board.
- `PuzzleQuestMain`: The entry point of the application.

## How to run

To run the game, execute the `PuzzleQuestMain` class.

## How to play

1. start a new game or load a previously saved game.
2. Players take turns swapping adjacent pieces on the board to form a combination of at least three similar pieces.
3. Each type of piece has a unique effect on the game state.
4. The game ends when a player's health reaches zero.
