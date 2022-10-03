
/**
 * Puzzle maintains the internal representation of a square Slither Link puzzle.
 * 
 * @author Kai Bagley
 * @version 1.0
 */
import java.util.ArrayList;

public class Puzzle
{
    private int[][] puzzle;         // the numbers in the squares, i.e. the puzzle definition
                                    // -1 if the square is empty, 0-3 otherwise
    private boolean[][] horizontal; // the horizontal line segments in the current solution
                                    // true if the segment is on, false otherwise
    private boolean[][] vertical;   // the vertical line segments in the current solution
                                    // true if the segment is on, false otherwise

    /**
     * Creates the puzzle from file filename, and an empty solution.
     * filename is assumed to hold a valid puzzle.
     */
    public Puzzle(String filename)
    {
        FileIO openfile = new FileIO(filename);             //Not allowed to write own file reader so can't figure out how to
        ArrayList<String> lines = openfile.getLines();      //catch and deal with exact exceptions
        int size = lines.size();
        puzzle = new int[size][size];
        horizontal = new boolean[size+1][size]; //since puzzle size 3 has 4x4 dots
        vertical = new boolean[size][size+1];
        parseFile(lines);
    }
    
    /**
     * Creates the puzzle from "eg3_1.txt".
     */
    public Puzzle()
    {
        this("eg3_1.txt");
    }

    /**
     * Returns the size of the puzzle.
     */
    public int size()
    {
        return puzzle.length;
    }

    /**
     * Returns the number layout of the puzzle.
     */
    public int[][] getPuzzle()
    {
        return puzzle;
    }

    /**
     * Returns the state of the current solution, horizontally.
     */
    public boolean[][] getHorizontal()
    {
        return horizontal;
    }

    /**
     * Returns the state of the current solution, vertically.
     */
    public boolean[][] getVertical()
    {
        return vertical;
    }

    /**
     * Turns lines into a Slither Link puzzle.
     * Each String holds one line from the input file. 
     * The first String in the argument goes into puzzle[0], 
     * The second String goes into puzzle[1], etc. 
     * lines is assumed to hold a valid square puzzle; see eg3_1.txt and eg5_1.txt for examples.
     */
    public void parseFile(ArrayList<String> lines)
    {
        String[] line;
        for (int i=0 ; i<lines.size() ; i++) { //Changes all number strings into ints and puts them into puzzle array
            line = lines.get(i).split(" ");
            for (int j=0 ; j<line.length ; j++) {
                puzzle[i][j] = Integer.parseInt(line[j]);
            }
        }
    }
    
    /**
     * Toggles the horizontal line segment to the right of Dot r,c, if the indices are legal.
     * Otherwise do nothing.
     */
    public void horizontalClick(int r, int c)
    {
        if (r < size()+1 && c < size()) {
            horizontal[r][c] = !horizontal[r][c];
        }
    }
    
    /**
     * Toggles the vertical line segment below Dot r,c, if the indices are legal.
     * Otherwise do nothing.
     */
    public void verticalClick(int r, int c)
    {
        if (r < size() && c < size()+1) {
            vertical[r][c] = !vertical[r][c];
        }
    }
    
    /**
     * Clears all line segments out of the current solution.
     */
    public void clear()
    {
        for (int i=0 ; i<size() ; i++){
            for (int j=0 ; j<size() ; j++){
                horizontal[i][j] = false;
                vertical[i][j] = false;
                if (j == size()-1) {  //Needs these two extra parts since one matrix is 4x3 and the other is 3x4
                    vertical[i][j+1] = false;
                }
                if (i == size()-1) {
                    horizontal[i+1][j] = false;   
                }
            }
        }
    }
}
