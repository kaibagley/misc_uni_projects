
/**
 * AnalyzeSolution methods are used to analyze the state of a Slither Link puzzle, 
 * to determine if the puzzle is finished. 
 * 
 * @author Kai Bagley
 * @version v1.0
 */
import java.util.*;

public class AnalyzeSolution
{
    /**
     * We don't need to create any objects of class AnalyzeSolution; all of its methods are static.
     */
    private AnalyzeSolution() {}

    /**
     * Returns the number of line segments surrounding Square r,c in p.
     * Returns 0 if the indices are illegal.
     */
    public static int linesAroundSquare(Puzzle p, int r, int c)
    {
        int linecount = 0;
        boolean[][] hori = p.getHorizontal();
        boolean[][] vert = p.getVertical();
        if ((r>=p.size() || c>=p.size()) || (r<0 || c<0)) {
            return 0; //Catches bad indices
        }
        if (hori[r][c]) {
            linecount++;
        }
        if (vert[r][c]) {
            linecount++;
        }
        if (r != p.size() && hori[r+1][c]) {
            linecount++;
        }
        if (c != p.size() && vert[r][c+1]) {
            linecount++;
        }
        return linecount;
    }
    
    /**
     * Returns all squares in p that are surrounded by the wrong number of line segments.
     * Each item on the result will be an int[2] containing the indices of a square.
     * The order of the items on the result is unimportant.
     */
    public static ArrayList<int[]> badSquares(Puzzle p)
    {
        ArrayList<int[]> badlist = new ArrayList<>();
        for (int i=0 ; i<p.size() ; i++) {
            for (int j=0 ; j<p.size() ; j++) {
                if ((p.getPuzzle()[i][j] != linesAroundSquare(p,i,j)) && p.getPuzzle()[i][j] != -1) {
                    badlist.add(new int[] {i,j});
                }
            }
        }
        return badlist;
    }

    /**
     * Returns all dots connected by a single line segment to Dot r,c in p.
     * Each item on the result will be an int[2] containing the indices of a dot.
     * The order of the items on the result is unimportant.
     * Returns null if the indices are illegal.
     */
    public static ArrayList<int[]> getConnections(Puzzle p, int r, int c)
    {
        ArrayList<int[]> linelist = new ArrayList<>();
        boolean[][] hori = p.getHorizontal();
        boolean[][] vert = p.getVertical();
        if ((r>p.size() || c>p.size()) || (r<0 || c<0)) {
            return null; //Bad indices
        }
        if (c != p.size() && hori[r][c]) {
            linelist.add(new int[] {r,c+1});  //Checks the only two - four possible connections a dot can have
        }
        if (r != p.size() && vert[r][c]) {
            linelist.add(new int[] {r+1,c});
        }
        if (c != 0 && hori[r][c-1]) {
            linelist.add(new int[] {r,c-1});
        }
        if (r != 0 && vert[r-1][c]) {
            linelist.add(new int[] {r-1,c});
        }
        return linelist;
    }

    /**
     * Returns an array of length 3 whose first element is the number of line segments in the puzzle p, 
     * and whose other elements are the indices of a dot on any one of those segments. 
     * Returns {0,0,0} if there are no line segments on the board. 
     */
    public static int[] lineSegments(Puzzle p)
    {
        int[] seg = new int[3];
        int connections = 0;
        boolean found = false;
        for (int i=0 ; i<=p.size() ; i++) {
            for (int j=0 ; j<=p.size() ; j++) {
                if (getConnections(p,i,j).size()>0 && !found) { //Finds the first point with a line and records its position,
                    seg[1]=i;                                   //since all that matters is that the point has a line
                    seg[2]=j;
                    found = true;
                }
                if (j != p.size() && p.getHorizontal()[i][j]) { //Counts all lines in horizontal and vertical matrices
                    connections++;
                }
                if (i != p.size() && p.getVertical()[i][j]) {
                    connections++;
                }
            }
        }
        seg[0]=connections;
        return seg;
    }
    
    /**
     * Tries to trace a closed loop starting from Dot r,c in p. 
     * Returns either an appropriate error message, or 
     * the number of steps in the closed loop (as a String). 
     * See the project page and the JUnit for a description of the messages expected. 
     */
    public static String tracePath(Puzzle p, int r, int c)
    {
        int noconn = getConnections(p,r,c).size();
        int prevr,prevc,newr,newc,tempr;
        int steps = 0;
        if (noconn == 0) {          //This if else section is outside of while loop since we need a new and prev position to
            return "No path";       //start while loop, and we wouldnt have both otherwise
        } else if (noconn == 1) {
            return "Dangling end";
        } else if (noconn == 3 || noconn == 4) {
            return "Branching line";
        } else {
            prevr = r;
            prevc = c;
            newr = getConnections(p,r,c).get(0)[0];
            newc = getConnections(p,r,c).get(0)[1];
            steps++;
            while (!((newr==r) && (newc==c))) {
                noconn = getConnections(p,newr,newc).size();
                if (noconn == 0) {
                    return "No path";
                } else if (noconn == 1) {
                    return "Dangling end";
                } else if (noconn == 3 || noconn == 4) {
                    return "Branching line";
                } else {
                    for (int i=0 ; i<2 ; i++) { //Makes sure its assigning the new pos to the value that ISN'T the previous one
                        if ((getConnections(p,newr,newc).get(i)[0] != prevr) || (getConnections(p,newr,newc).get(i)[1] != prevc)) {
                            prevr = newr;
                            prevc = newc;
                            tempr = newr;
                            newr = getConnections(p,newr,newc).get(i)[0];
                            newc = getConnections(p,tempr,newc).get(i)[1];
                            steps++;
                            break;
                        }
                    }
                }
            }
            return ""+steps;
        }
    }
    
    /**
     * Returns a message on whether the puzzle p is finished. 
     * p is finished iff all squares are good, and all line segments form a single closed loop. 
     * An algorithm is given on the project page. 
     * See the project page and the JUnit for a description of the messages expected.
     */
    public static String finished(Puzzle p)
    {
        int passcount = 0;
        int steps,lines;
        if (!(badSquares(p).isEmpty())) {
            return "Wrong number";
        }
        if (lineSegments(p)[0] != 0) {
            lines = lineSegments(p)[0];
            if (tracePath(p,lineSegments(p)[1],lineSegments(p)[2]).length() < 7) {              //7 is the length of shortest
                steps = Integer.parseInt(tracePath(p,lineSegments(p)[1],lineSegments(p)[2]));   //statement in tracePath,
            } else {                                                                            //therefore <7 will be the
                return tracePath(p,lineSegments(p)[1],lineSegments(p)[2]);                      //number of steps
            }
        } else {
            return "No lines on board";
        }
        if (lines > steps) {
            return "Disconnected lines";
        }
        return "Finished";
    }
}
