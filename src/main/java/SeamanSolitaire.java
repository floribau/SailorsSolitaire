import java.time.Duration;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class SeamanSolitaire {
  public static char[][] startingBoard = {
      {'W', 'W', 'W', ' ', ' '},
      {'W', 'W', 'W', ' ', ' '},
      {'W', 'W', 'o', 'B', 'B'},
      {' ', ' ', 'B', 'B', 'B'},
      {' ', ' ', 'B', 'B', 'B'}};

/*
  // smaller board for testing purposes
  public static char[][] startingBoard = {
      {'W', 'W', ' '},
      {'W', 'o', 'B'},
      {' ', 'B', 'B'}};

 */


  public static State startingState = new State(startingBoard, null);
  public static final int DEPTH = 47;
  public static HashSet<State> visited = new HashSet<>();
  public static HashMap<State, Integer> visitedDepth = new HashMap<>();

  public static void main(String[] args) {
    System.out.println("Starting State:\n" + startingState);

    // run search algorithm
    long startTime = System.currentTimeMillis();
    // State solution = bfs(startingState);
    State solution = dfs(startingState, DEPTH);
    // State solution = backtracking(startingState, 0);
    long finishTime = System.currentTimeMillis();
    System.out.println(
        "Time elapsed: " + Duration.ofMillis(finishTime - startTime).toSeconds() + " seconds.");

    // print solution if solution was found
    if (solution != null) {
      LinkedList<State> solutionPath = new LinkedList<>();
      while (solution.getParent() != null) {
        solutionPath.addFirst(solution);
        solution = solution.getParent();
      }
      System.out.println("Solution found in " + solutionPath.size() + " steps :)\n");
      int count = 1;
      for (State s : solutionPath) {
        System.out.println("After " + count++ + ". step:");
        System.out.println(s);
      }
    } else {
      System.out.println("No solution found :(");
    }
  }

  public static State bfs(State startingState) {
    // breadth first search
    ArrayDeque<State> todo = new ArrayDeque<>();
    todo.offer(startingState);

    while (!todo.isEmpty()) {
      State currentState = todo.poll();
      if (!visited.add(currentState)) {
        continue;
      }
      if (currentState.isFinished()) {
        return currentState;
      }
      for (int[] move : currentState.generateMoves()) {
        currentState.switchSquares(move[0], move[1], move[2], move[3]);
        todo.offer(new State(currentState.getBoard(), currentState));
        currentState.switchSquares(move[0], move[1], move[2], move[3]);
      }
    }
    return null;
  }

  public static State dfs(State startingState, int depth) {
    // limited depth first search
    ArrayDeque<State> todo = new ArrayDeque<>();
    todo.push(startingState);

    while (!todo.isEmpty()) {
      State currentState = todo.pop();
      if (visitedDepth.putIfAbsent(currentState, currentState.getDepth()) != null) {
        if (visitedDepth.get(currentState) > currentState.getDepth()) {
          visitedDepth.replace(currentState, currentState.getDepth());
        } else {
          continue;
        }
      }
      if (currentState.isFinished()) {
        return currentState;
      }
      if (currentState.getDepth() < depth) {
        for (int[] move : currentState.generateMoves()) {
          currentState.switchSquares(move[0], move[1], move[2], move[3]);
          todo.push(
              new State(currentState.getBoard(), currentState, currentState.getDepth() + 1));
          currentState.switchSquares(move[0], move[1], move[2], move[3]);
        }
      }
    }
    return null;
  }

  public static State backtracking(State currentState, int depth) {
    // calls itself recursively
    // doesn't work properly (likely due to pruning of visited state nodes)
    if (depth >= DEPTH) {
     return null;
    }
    if (visitedDepth.putIfAbsent(currentState, currentState.getDepth()) != null) {
      // state has been visited
      if (visitedDepth.get(currentState) > currentState.getDepth()) {
        // state has been visited in larger depth => still has to be expanded in this depth
        visitedDepth.replace(currentState, currentState.getDepth());
      } else {
        // eliminate duplicate
        return null;
      }
    }
    if (currentState.isFinished()) {
      return currentState;
    }
    for (int[] move : currentState.generateMoves()) {
      currentState.switchSquares(move[0], move[1], move[2], move[3]);
      State newState = new State(currentState.getBoard(), currentState);
      currentState.switchSquares(move[0], move[1], move[2], move[3]);
      State result = backtracking(newState, depth + 1);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

}
