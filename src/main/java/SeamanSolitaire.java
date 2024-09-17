import java.time.Duration;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class SeamanSolitaire {

  public static final int DEPTH = 46;

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
  public static HashSet<State> visited = new HashSet<>();
  public static HashMap<State, Integer> visitedDepth = new HashMap<>();

  public static void main(String[] args) {
    System.out.println("Starting State:\n" + startingState);

    // run search algorithm
    long startTime = System.currentTimeMillis();
    State solution = bfs(startingState);
    // State solution = dfs(startingState);
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
      // duplicate elimination
      if (!visited.add(currentState)) {
        continue;
      }
      // target test
      if (currentState.isFinished()) {
        return currentState;
      }
      // node expansion
      for (int[] move : currentState.generateMoves()) {
        currentState.switchSquares(move[0], move[1], move[2], move[3]);
        todo.offer(new State(currentState.getBoard(), currentState));
        currentState.switchSquares(move[0], move[1], move[2], move[3]);
      }
    }
    return null;
  }

  public static State dfs(State startingState) {
    // depth first search with limited depth
    ArrayDeque<State> todo = new ArrayDeque<>();
    todo.push(startingState);

    while (!todo.isEmpty()) {
      State currentState = todo.pop();
      if (visitedDepth.putIfAbsent(currentState, currentState.getDepth()) != null) {
        // state has been visited
        if (visitedDepth.get(currentState) > currentState.getDepth()) {
          // state has been visited in deeper depth => still has to be expanded in this shallower depth
          visitedDepth.replace(currentState, currentState.getDepth());
        } else {
          // duplicate elimination
          continue;
        }
      }
      // target test
      if (currentState.isFinished()) {
        return currentState;
      }
      // node expansion
      if (currentState.getDepth() < DEPTH) {
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
    if (depth > DEPTH) {
      return null;
    }

    if (visitedDepth.putIfAbsent(currentState, depth) != null) {
      // state has been visited
      if (visitedDepth.get(currentState) > depth) {
        // state has been visited in deeper depth => still has to be expanded in this shallower depth
        visitedDepth.replace(currentState, depth);
      } else {
        // duplicate elimination
        return null;
      }
    }
    // target test
    if (currentState.isFinished()) {
      return currentState;
    }
    // node expansion
    for (int[] move : currentState.generateMoves()) {
      currentState.switchSquares(move[0], move[1], move[2], move[3]);
      State newState = new State(currentState.getBoard(), currentState);
      currentState.switchSquares(move[0], move[1], move[2], move[3]);
      // recursively search deeper
      State result = backtracking(newState, depth + 1);
      if (result != null) {
        // return solution
        return result;
      }
    }
    return null;
  }

}
