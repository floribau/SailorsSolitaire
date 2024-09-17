import java.util.ArrayDeque;
import java.util.Arrays;

public class State {

  public static int HEIGHT = 5;
  public static int WIDTH = 5;
  private final State parent;
  private final char[][] board;
  private int depth = 0;

  public State(char[][] board, State parent) {
    // general constructor
    this.parent = parent;
    this.board = new char[HEIGHT][];
    for (int i = 0; i < HEIGHT; i++) {
      char[] line = Arrays.copyOf(board[i], WIDTH);
      this.board[i] = line;
    }
  }

  public State(char[][] board, State parent, int depth) {
    // constructor with depth argument to limit dfs
    this.parent = parent;
    this.board = new char[HEIGHT][];
    for (int i = 0; i < this.board.length; i++) {
      char[] line = Arrays.copyOf(board[i], WIDTH);
      this.board[i] = line;
    }
    this.depth = depth;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    State state = (State) o;
    return Arrays.deepEquals(board, state.board);
  }

  @Override
  public int hashCode() {
    return Arrays.deepHashCode(board);
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    for (char[] line : board) {
      for (char type : line) {
        str.append("[").append(type).append("]");
      }
      str.append("\n");
    }
    return str.toString();
  }

  public boolean isFinished() {
    // board is finished if flipped compared to starting state
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        if (board[i][j] != SeamanSolitaire.startingState.board[HEIGHT - 1 - i][WIDTH - 1 - j]) {
          return false;
        }
      }
    }
    return true;
  }

  public ArrayDeque<int[]> generateMoves() {
    // generates indices of possible moves
    ArrayDeque<int[]> moves = new ArrayDeque<>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (board[i][j] == 'W' || board[i][j] == 'B') {
          for (int offset : new int[]{-1, 1}) {
            // check for shifts
            if (isEmptySquare(i + offset, j)) {
              moves.add(new int[]{i, j, i + offset, j});
            }
            if (isEmptySquare(i, j + offset)) {
              moves.add(new int[]{i, j, i, j + offset});
            }
            // check for jumps
            if (isEmptySquare(i + 2 * offset, j)) {
              moves.add(new int[]{i, j, i + 2 * offset, j});
            }
            if (isEmptySquare(i, j + 2 * offset)) {
              moves.add(new int[]{i, j, i, j + 2 * offset});
            }
          }
        }
      }
    }
    return moves;
  }

  public void switchSquares(int x1, int y1, int x2, int y2) {
    // switches contents of two squares in-place
    char temp = board[x1][y1];
    board[x1][y1] = board[x2][y2];
    board[x2][y2] = temp;
  }

  public boolean isEmptySquare(int x, int y) {
    // checks if square is on board and is the empty square (symbol 'o')
    return x >= 0 && x < HEIGHT && y >= 0 && y < WIDTH && board[x][y] == 'o';
  }

  public State getParent() {
    return parent;
  }

  public char[][] getBoard() {
    return board;
  }

  public int getDepth() {
    return depth;
  }
}
