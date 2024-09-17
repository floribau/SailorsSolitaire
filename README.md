# Sailor's Solitaire
## Game rules
In this game, the black and white stones (represented as 'B' and 'W') must switch sides with as few moves as possible. Each stone can be moved to an empty adjacent square (two squares are adjacent if they share a common side). Additionally, a stone can jump over an adjacent stone if the target square is empty, but jumping around corners is not allowed. The color of the stones does not matter in the jumping process.
## Implementation details
This AI implements the following search strategies: breadt-first search (BFS), depth-first search with limited depth (DFS), and backtracking with limited depth. BFS always finds the optimal solution of 46 moves, while DFS and backtracking find any existing solution with a depth smaller than the specified depth.
