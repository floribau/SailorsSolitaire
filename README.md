# Sailor's Solitaire
## Game rules
This is the implementation of Sailor's Solitaire. In this game, the black and white stones must switch sides with as few moves as possible. Each stone can be moved to an empty adjacent space (two spaces are adjacent if they share a common side). Additionally, a stone can jump over an adjacent stone if the target space is empty, but jumping around corners is not allowed. The color of the stones does not matter in this process.
## Implementation details
This AI implements the following search strategies: breadt-first search (BFS), depth-first search with limited depth (DFS), and backtracking with limited depth. BFS always finds the optimal solution of 46 moves, while DFS and backtracking find any existing solution with a depth smaller than the specified depth.
