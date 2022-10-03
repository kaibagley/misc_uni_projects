import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * This class provides unit test cases for Project 2 in 2019 Sem. 1.
 * @author Lyndon While
 * @version 1.0
 */
public class Project2Test
{
    Puzzle[]      ps;
    SlitherLink[] ss;
    int[][] eg2_2  = {{ 3, 2},
                      {-1,-1}};
    int[][] eg3_3  = {{ 2,-1, 3},
                      { 3, 1,-1},
                      { 2,-1, 3}};
    int[][] eg5_2  = {{-1, 2,-1, 2,-1},
                      {-1,-1, 2,-1,-1},
                      { 2, 2,-1, 3,-1},
                      {-1,-1,-1, 0,-1},
                      { 3, 2, 3, 3,-1}};
    int[][] eg10_2 = {{ 2,-1, 2,-1,-1,-1, 3, 3, 2,-1},
                      {-1, 3, 0,-1,-1,-1, 2, 1, 3,-1},
                      { 1,-1, 3,-1,-1, 2, 2,-1,-1,-1},
                      { 2, 1,-1, 1,-1, 3, 3, 2, 2,-1},
                      { 2, 2, 3,-1, 2, 1,-1,-1,-1,-1},
                      { 2, 2, 1,-1,-1, 2,-1,-1, 2, 2},
                      { 2,-1, 2, 1, 2, 0,-1, 2, 1, 2},
                      { 2,-1,-1,-1,-1,-1,-1,-1,-1, 2},
                      { 1,-1, 1, 2,-1,-1,-1,-1, 1, 2},
                      { 3, 3,-1, 2,-1, 3,-1, 2, 1,-1}};
    int[][][] egs   = {null, null, eg2_2, eg3_3, null, eg5_2, null, null, null, null, eg10_2};
    int[] examples  = {2,3,5,10};
    String[] states = {null, "no path", "wrong number", "dangling end", "branching line", "disconnected lines", "finished"};
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
        ps     = new Puzzle[11];
        ps[2]  = new Puzzle("eg2_2.txt");
        ps[3]  = new Puzzle("eg3_3.txt");
        ps[5]  = new Puzzle("eg5_2.txt"); // this is the one from the web page
        ps[10] = new Puzzle("eg10_2.txt");
    }

    @Test
    public void testPuzzle() 
    {
        for (int k : examples)
        {
            assertEquals("", k, ps[k].getPuzzle().length);
            for (int i = 0; i < k; i++)
                assertTrue("", java.util.Arrays.equals(ps[k].getPuzzle()[i],egs[k][i]));
        
            assertEquals("", k+1, ps[k].getHorizontal().length);
            for (int i = 0; i <= k; i++)
            {
                assertEquals("", k, ps[k].getHorizontal()[i].length);
                for (int j = 0; j < k; j++)
                    assertEquals("", false, ps[k].getHorizontal()[i][j]);
            }
        
            assertEquals("", k, ps[k].getVertical().length);
            for (int i = 0; i < k; i++)
            {
                assertEquals("", k+1, ps[k].getVertical()[i].length);
                for (int j = 0; j <= k; j++)
                    assertEquals("", false, ps[k].getVertical()[i][j]);
            }
        }
    }

    @Test
    public void testhorizontalClick() 
    {
        for (int k : examples)
        {
            for (int i = 0; i <= k; i++)
            {
                // paint the next row true
                for (int j = 0; j < k; j++)
                    ps[k].horizontalClick(i, j);
                assertEquals("", k+1, ps[k].getHorizontal().length);
                assertEquals("", k,   ps[k].getHorizontal()[i].length);
                for (int u = 0; u <= k; u++)
                    for (int v = 0; v < k; v++)
                        // all rows up to this one should be true
                        assertEquals("", u <= i, ps[k].getHorizontal()[u][v]);
            }
        
            // these loops include many illegal indices
            for (int i = -2 * k; i <= 2 * k; i++)
            {
                for (int j = -2 * k; j <= 2 * k; j++)
                {
                    // make each element individually false
                    ps[k].horizontalClick(i,j);
                    for (int u = 0; u <= k; u++)
                        for (int v = 0; v < k; v++)
                            assertEquals("", !(u == i && v == j), ps[k].getHorizontal()[u][v]);
                    // and reverse it
                    ps[k].horizontalClick(i,j);
                }
            }
        
            for (int j = 0; j < k; j++)
            {
                // paint the next column false
                for (int i = 0; i <= k; i++)
                    ps[k].horizontalClick(i, j);
                for (int u = 0; u <= k; u++)
                    for (int v = 0; v < k; v++)
                        // all columns beyond this one should be true
                        assertEquals("", v > j, ps[k].getHorizontal()[u][v]);
            }
        
            assertEquals("", k, ps[k].getVertical().length);
            for (int i = 0; i < k; i++)
            {
                assertEquals("", k+1, ps[k].getVertical()[i].length);
                for (int j = 0; j <= k; j++)
                    // should all be false
                    assertEquals("", false, ps[k].getVertical()[i][j]);
            }
            assertEquals("", k, ps[k].getPuzzle().length);
            for (int i = 0; i < k; i++)
                // should be unchanged
                assertTrue("", java.util.Arrays.equals(ps[k].getPuzzle()[i],egs[k][i]));
        }
    }

    @Test
    public void testverticalClick() 
    {
        for (int k : examples)
        {
            for (int i = 0; i < k; i++)
            {
                // paint the next row true
                for (int j = 0; j <= k; j++)
                    ps[k].verticalClick(i, j);
                assertEquals("", k,   ps[k].getVertical().length);
                assertEquals("", k+1, ps[k].getVertical()[i].length);
                for (int u = 0; u < k; u++)
                    for (int v = 0; v <= k; v++)
                        // all rows up to this one should be true
                        assertEquals("", u <= i, ps[k].getVertical()[u][v]);
            }
        
            // these loops include many illegal indices
            for (int i = -2 * k; i <= 2 * k; i++)
            {
                for (int j = -2 * k; j <= 2 * k; j++)
                {
                    // make each element individually false
                    ps[k].verticalClick(i,j);
                    for (int u = 0; u < k; u++)
                        for (int v = 0; v <= k; v++)
                            assertEquals("", !(u == i && v == j), ps[k].getVertical()[u][v]);
                    // and reverse it
                    ps[k].verticalClick(i,j);
                }
            }
        
            for (int j = 0; j <= k; j++)
            {
                // paint the next column false
                for (int i = 0; i < k; i++)
                    ps[k].verticalClick(i, j);
                for (int u = 0; u < k; u++)
                    for (int v = 0; v <= k; v++)
                        // all columns beyond this one should be true
                        assertEquals("", v > j, ps[k].getVertical()[u][v]);
            }
        
            assertEquals("", k+1, ps[k].getHorizontal().length);
            for (int i = 0; i <= k; i++)
            {
                assertEquals("", k, ps[k].getHorizontal()[i].length);
                for (int j = 0; j < k; j++)
                    // should all be false
                    assertEquals("", false, ps[k].getHorizontal()[i][j]);
            }
            assertEquals("", k, ps[k].getPuzzle().length);
            for (int i = 0; i < k; i++)
                // should be unchanged
                assertTrue("", java.util.Arrays.equals(ps[k].getPuzzle()[i],egs[k][i]));
        }
    }

    @Test
    public void testclear() 
    {
        // testclear checks if a puzzle is reset to brand new
        for (int k : examples)
        {
            testPuzzle();
            ps[k].clear();
            testPuzzle();
            for (int i = 0; i <= k; i++)
                for (int j = 0; j <= k; j++)
                {
                    ps[k].horizontalClick(i,j);
                    ps[k].verticalClick(i,j);
                    ps[k].clear();
                    testPuzzle();
                    ps[k].horizontalClick(k-i,j);
                    ps[k].verticalClick(k-i,j);
                    ps[k].clear();
                    testPuzzle();
                    ps[k].horizontalClick(i,k-j);
                    ps[k].verticalClick(i,k-j);
                    ps[k].clear();
                    testPuzzle();
                    ps[k].horizontalClick(k-i,j);
                    ps[k].verticalClick(i,k-j);
                    ps[k].clear();
                    testPuzzle();
                }
            for (int i = 0; i <= k; i++)
                for (int j = 0; j <= k; j++)
                {
                    ps[k].horizontalClick(i,j);
                    ps[k].verticalClick(i,j);
                }
            ps[k].clear();
            testPuzzle();
        }
    }

    @Test
    public void testSlitherLink() 
    {
        // we do this here to minimise window-opening
        ss = new SlitherLink[11];
        for (int k : examples)
            ss[k] = new SlitherLink(ps[k]);
        
        for (int k : examples)
        {
            assertEquals("", k, ss[k].getGame().getPuzzle().length);
            for (int i = 0; i < k; i++)
                assertTrue("", java.util.Arrays.equals(ss[k].getGame().getPuzzle()[i],egs[k][i]));
        
            assertEquals("", k+1, ss[k].getGame().getHorizontal().length);
            for (int i = 0; i <= k; i++)
            {
                assertEquals("", k, ss[k].getGame().getHorizontal()[i].length);
                for (int j = 0; j < k; j++)
                    assertEquals("", false, ss[k].getGame().getHorizontal()[i][j]);
            }
        
            assertEquals("", k, ss[k].getGame().getVertical().length);
            for (int i = 0; i < k; i++)
            {
                assertEquals("", k+1, ss[k].getGame().getVertical()[i].length);
                for (int j = 0; j <= k; j++)
                    assertEquals("", false, ss[k].getGame().getVertical()[i][j]);
            }
        
            // the SimpleCanvas object should have been created
            assertFalse("", ss[k].getCanvas() == null);
        }
    }

    @Test
    public void testSLhorizontalClick() 
    {
        // we do this here to minimise window-opening
        ss = new SlitherLink[11];
        for (int k : examples)
            ss[k] = new SlitherLink(ps[k]);
        
        for (int k : examples)
        {
            for (int i = 0; i <= k; i++)
            {
                // paint the next row true
                for (int j = 0; j < k; j++)
                    ss[k].horizontalClick(i, j);
                assertEquals("", k+1, ss[k].getGame().getHorizontal().length);
                assertEquals("", k,   ss[k].getGame().getHorizontal()[i].length);
                for (int u = 0; u <= k; u++)
                    for (int v = 0; v < k; v++)
                        // all rows up to this one should be true
                        assertEquals("", u <= i, ss[k].getGame().getHorizontal()[u][v]);
            }
        
            // these loops include many illegal indices
            for (int i = -2 * ss.length; i <= 2 * ss.length; i++)
            {
                for (int j = -2 * ss.length; j <= 2 * ss.length; j++)
                {
                    // make each element individually false
                    ss[k].horizontalClick(i,j);
                    for (int u = 0; u <= k; u++)
                        for (int v = 0; v < k; v++)
                            assertEquals("", !(u == i && v == j), ss[k].getGame().getHorizontal()[u][v]);
                    // and reverse it
                    ss[k].horizontalClick(i,j);
                }
            }
        
            for (int j = 0; j < k; j++)
            {
                // paint the next column false
                for (int i = 0; i <= k; i++)
                    ss[k].horizontalClick(i, j);
                for (int u = 0; u <= k; u++)
                    for (int v = 0; v < k; v++)
                        // all columns beyond this one should be true
                        assertEquals("", v > j, ss[k].getGame().getHorizontal()[u][v]);
            }
        
            assertEquals("", k, ss[k].getGame().getVertical().length); 
            for (int i = 0; i < k; i++)
            {
                assertEquals("", k+1, ss[k].getGame().getVertical()[i].length);
                for (int j = 0; j <= k; j++)
                    // should all be false
                    assertEquals("", false, ss[k].getGame().getVertical()[i][j]);
            }
            assertEquals("", k, ss[k].getGame().getPuzzle().length);
            for (int i = 0; i < k; i++)
                // should be unchanged
                assertTrue("", java.util.Arrays.equals(ss[k].getGame().getPuzzle()[i],egs[k][i]));
        }
    }

    @Test
    public void testSLverticalClick() 
    {
        // we do this here to minimise window-opening
        ss = new SlitherLink[11];
        for (int k : examples)
            ss[k] = new SlitherLink(ps[k]);
        
        for (int k : examples)
        {
            for (int i = 0; i < k; i++)
            {
                // paint the next row true
                for (int j = 0; j <= k; j++)
                    ss[k].verticalClick(i, j);
                assertEquals("", k,   ss[k].getGame().getVertical().length);
                assertEquals("", k+1, ss[k].getGame().getVertical()[i].length);
                for (int u = 0; u < k; u++)
                    for (int v = 0; v <= k; v++)
                        // all rows up to this one should be true
                        assertEquals("", u <= i, ss[k].getGame().getVertical()[u][v]);
            }
        
            // these loops include many illegal indices
            for (int i = -2 * ss.length; i <= 2 * ss.length; i++)
            {
                for (int j = -2 * ss.length; j <= 2 * ss.length; j++)
                {
                    // make each element individually false
                    ss[k].verticalClick(i,j);
                    for (int u = 0; u < k; u++)
                        for (int v = 0; v <= k; v++)
                            assertEquals("", !(u == i && v == j), ss[k].getGame().getVertical()[u][v]);
                    // and reverse it
                    ss[k].verticalClick(i,j);
                }
            }
        
            for (int j = 0; j <= k; j++)
            {
                // paint the next column false
                for (int i = 0; i < k; i++)
                    ss[k].verticalClick(i, j);
                for (int u = 0; u < k; u++)
                    for (int v = 0; v <= k; v++)
                        // all columns beyond this one should be true
                        assertEquals("", v > j, ss[k].getGame().getVertical()[u][v]);
            }
        
            assertEquals("", k+1, ss[k].getGame().getHorizontal().length);
            for (int i = 0; i <= k; i++)
            {
                assertEquals("", k, ss[k].getGame().getHorizontal()[i].length);
                for (int j = 0; j < k; j++)
                    // should all be false
                    assertEquals("", false, ss[k].getGame().getHorizontal()[i][j]);
            }
            assertEquals("", k, ss[k].getGame().getPuzzle().length);
            for (int i = 0; i < k; i++)
                // should be unchanged
                assertTrue("", java.util.Arrays.equals(ss[k].getGame().getPuzzle()[i],egs[k][i]));
        }
    }
    
    // Returns "true" iff xs and ys contain the same elements, even in different orders,
    // otherwise returns an error msg.
    // Assumes that all arrays have length 2.
    // A call to sameElements clears its second argument, 
    // so always make that the actual result NOT the expected result. 
    private String sameElements(ArrayList<int[]> xs, ArrayList<int[]> ys)
    {
        if (xs == null) return "The expected result is null - PROBLEM";
        if (ys == null) return "The student's list is null";
        if (xs.size() > ys.size()) return "The student's list is too small by " + (xs.size() - ys.size());
        else
        if (xs.size() < ys.size()) return "The student's list is too big by "   + (ys.size() - xs.size());
        for (int[] x : xs)
        {
            boolean b = false;
            int i = 0;
            while (i < ys.size() && !b)
                if (java.util.Arrays.equals(x, ys.get(i))) {ys.remove(i); b = true;}
                else                                       i++;
            if (!b) return x[0] + "," + x[1] + " not found"; 
        }
        return "true";
    }
    
    // Returns "true" iff ls[0] has the appropriate value and 
    // ls[1],ls2[] denotes one of the locations in zs (if any),
    // otherwise returns an error msg.
    private String checkSegments(ArrayList<int[]> zs, int[] ls)
    {
        if (zs.isEmpty())         
           if (ls[0] == 0 && ls[1] == 0 && ls[2] == 0) return "true";
           else                                        return "Wrong when board empty";
        if (2 * ls[0] != zs.size())                    return "Wrong number of segments";
        for (int[] z : zs) 
            if (z[0] == ls[1] && z[1] == ls[2]) return "true"; 
        return ls[1] + "," + ls[2] + " not found";
    }
    
    @Test
    public void testlinesAroundSquare()
    {
        int z;
        for (int k : examples)
        {
            assertEquals("", k, ps[k].getPuzzle().length);
            for (int i = 0; i < k; i++)
                assertTrue("", java.util.Arrays.equals(ps[k].getPuzzle()[i],egs[k][i]));
            for (int r = -2 * k; r <= 2 * k; r++)
                for (int c = -2 * k; c <= 2 * k; c++)
                    // all 0 initially
                    assertEquals("", 0, AnalyzeSolution.linesAroundSquare(ps[k], r, c));
            // these loops include many illegal indices
            for (int r = -2 * k; r <= 2 * k; r++)
                for (int c = -2 * k; c <= 2 * k; c++)
                {
                    ps[k].horizontalClick(r, c);
                    for (int u = 0; u < k; u++)
                        for (int v = 0; v < k; v++)
                        {
                            if (u <  r - 1)       z = 2; // two rows back
                            else
                            if (u <  r && v <= c) z = 2; // row above, previous squares
                            else
                            if (u <  r && v >  c) z = 1; // row above, later squares
                            else
                            if (u == r && v <= c) z = 1; // current row
                            else 
                            z = 0;
                            assertEquals("", z, AnalyzeSolution.linesAroundSquare(ps[k], u, v));
                        }
                }
            // these loops include many illegal indices
            for (int r = -2 * k; r <= 2 * k; r++)
                for (int c = -2 * k; c <= 2 * k; c++)
                {
                    ps[k].verticalClick(r, c);
                    for (int u = 0; u < k; u++)
                        for (int v = 0; v < k; v++)
                        {
                            if (u <  r)           z = 4; // row above
                            else
                            if (u == r && v <  c) z = 4; // current row, previous squares
                            else
                            if (u == r && v == c) z = 3; // current square
                            else
                            z = 2;
                            assertEquals("", z, AnalyzeSolution.linesAroundSquare(ps[k], u, v));
                        }
                }
            ps[k].clear();
            // round the outside, round the outside
            for (int i = 0; i < k; i++)
            {
                ps[k].horizontalClick(0,i);
                ps[k].horizontalClick(k,i);
                ps[k].verticalClick(i,0);
                ps[k].verticalClick(i,k);
            }
            for (int u = 0; u < k; u++)
                for (int v = 0; v < k; v++)
                {
                    z = 0 ;
                    if (u == 0 || u == k-1) z += 1;
                    if (v == 0 || v == k-1) z += 1;
                    assertEquals("", z, AnalyzeSolution.linesAroundSquare(ps[k], u, v));
                }
        }
    }
    
    // sets up and tests the bad squares for egs[k] with an empty board
    private ArrayList<int[]> testbadSquaresEmpty(int k)
    {
        ArrayList<int[]> zs = new ArrayList<>(); 
        for (int i = 0; i < k; i++)
            for (int j = 0; j < k; j++)
                if (egs[k][i][j] > 0)
                   zs.add(new int[] {i,j});
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[k])));
        return zs;
    }
    
    @Test
    public void testbadSquares2()
    {
        ArrayList<int[]> zs = testbadSquaresEmpty(2);
        ps[2].horizontalClick(0,0);
        ps[2].horizontalClick(0,1);
        ps[2].verticalClick(0,0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[2])));
        
        ps[2].verticalClick(1,1);
        ps[2].verticalClick(1,2);
        ps[2].horizontalClick(2,1);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[2])));
        
        ps[2].horizontalClick(1,0);
        zs.remove(0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[2])));
        
        ps[2].verticalClick(0,2);
        zs.remove(0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[2])));
        
        ps[2].horizontalClick(1,0);
        ps[2].verticalClick(0,2);
        zs.add(new int[] {0,0});
        zs.add(new int[] {0,1});
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[2])));
        
        ps[2].verticalClick(0,1);
        zs.clear();
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[2])));
    }
    
    @Test
    public void testbadSquares3()
    {
        ArrayList<int[]> zs = testbadSquaresEmpty(3);
        ps[3].horizontalClick(1,1);
        zs.remove(3);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        
        ps[3].horizontalClick(1,0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        ps[3].verticalClick(0,1);
        zs.remove(0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        
        ps[3].verticalClick(1,0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        ps[3].horizontalClick(2,0);
        zs.remove(1);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        
        ps[3].verticalClick(0,3);
        ps[3].horizontalClick(0,2);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        ps[3].horizontalClick(1,2);
        zs.remove(0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        
        ps[3].verticalClick(1,2);
        ps[3].horizontalClick(2,2);
        ps[3].verticalClick(2,3);
        ps[3].horizontalClick(3,2);
        ps[3].horizontalClick(1,1);
        zs.remove(1);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
        
        ps[3].horizontalClick(3,0);
        zs.remove(0);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[3])));
    }
    
    @Test
    public void testbadSquares5()
    {
        ArrayList<int[]> zs = testbadSquaresEmpty(5);
        // each of these lines makes the 0 at 3,3 go bad
        for (int[] k : new int[][] {{3,3},{4,3}})
        {
            ps[5].horizontalClick(k[0],k[1]);
            zs.add(new int[] {3,3});
            assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
            ps[5].horizontalClick(k[0],k[1]);
            zs.remove(10);
            assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
            ps[5].verticalClick(k[1],k[0]);
            zs.add(new int[] {3,3});
            assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
            ps[5].verticalClick(k[1],k[0]);
            zs.remove(10);
            assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
        }
        // these four lines make the 3 at 4,2 stay bad, but removing any one of them makes it go good
        ps[5].horizontalClick(4,2);
        ps[5].horizontalClick(5,2);
        ps[5].verticalClick(4,2);
        ps[5].verticalClick(4,3);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
        zs.remove(8);
        ps[5].horizontalClick(4,2);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
        ps[5].horizontalClick(4,2);
        ps[5].horizontalClick(5,2);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
        ps[5].horizontalClick(5,2);
        ps[5].verticalClick(4,2);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
        ps[5].verticalClick(4,2);
        ps[5].verticalClick(4,3);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[5])));
        ps[5].verticalClick(4,3);
    }

    @Test
    public void testbadSquares10()
    {
        ArrayList<int[]> zs = testbadSquaresEmpty(10);
        ps[10].horizontalClick(9,8);
        zs.remove(50);
        zs.remove(43);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[10])));
        ps[10].verticalClick(2,1);
        ps[10].verticalClick(3,1);
        zs.remove(14);
        zs.remove(9);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[10])));
        ps[10].horizontalClick(4,0);
        ps[10].verticalClick(4,1);
        zs.remove(18);
        zs.remove(12);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[10])));
        ps[10].horizontalClick(5,1);
        ps[10].verticalClick(5,1);
        zs.remove(22);
        zs.remove(17);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[10])));
        ps[10].horizontalClick(6,2);
        ps[10].verticalClick(6,3);
        zs.remove(27);
        zs.remove(26);
        zs.remove(21);
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[10])));
        ps[10].verticalClick(1,3);
        zs.add(new int[] {1,2});
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[10])));
        ps[10].verticalClick(6,5);
        zs.add(new int[] {6,5});
        assertEquals("", "true", sameElements(zs, AnalyzeSolution.badSquares(ps[10])));
    }

    @Test
    public void testgetConnections()
    {
        ArrayList<int[]> zs = new ArrayList<>();
        
        for (int k : examples)
        {
            // lots of illegal indices
            for (int i = -2 * k; i <= 2 * k; i++)
                for (int j = -2 * k; j < 0; j++)
                    assertEquals("", null, AnalyzeSolution.getConnections(ps[k], i, j));
            for (int i = -2 * k; i <= 2 * k; i++)
                for (int j = k + 1; j <= 2 * k; j++)
                    assertEquals("", null, AnalyzeSolution.getConnections(ps[k], i, j));
            for (int i = -2 * k; i < 0; i++)
                for (int j = -2 * k; j <= 2 * k; j++)
                    assertEquals("", null, AnalyzeSolution.getConnections(ps[k], i, j));
            for (int i = k + 1; i <= 2 * k; i++)
                for (int j = -2 * k; j <= 2 * k; j++)
                    assertEquals("", null, AnalyzeSolution.getConnections(ps[k], i, j));
            // empty board
            for (int i = 0; i <= k; i++)
                for (int j = 0; j <= k; j++)
                    assertEquals("", "true", sameElements(zs, AnalyzeSolution.getConnections(ps[k], i, j)));
            // build up all horizontal lines
            for (int i = 0; i <= k; i++)
                for (int j = 0; j < k; j++)
                {
                    ps[k].horizontalClick(i,j);
                    for (int u = 0; u <= k; u++)
                        for (int v = 0; v <= k; v++)
                        {
                            if (u < i || u == i && v <= j) 
                            {
                                if (v > 0) zs.add(new int[] {u,v-1});
                                if (v < k) zs.add(new int[] {u,v+1});
                            }
                            else
                            if (u == i && v == j + 1) 
                                zs.add(new int[] {u,v-1});
                            assertEquals("", "true", sameElements(zs, AnalyzeSolution.getConnections(ps[k], u, v)));
                            zs.clear();
                        }
                }
            // now add all vertical lines
            for (int i = 0; i < k; i++)
                for (int j = 0; j <= k; j++)
                {
                    ps[k].verticalClick(i,j);
                    for (int u = 0; u <= k; u++)
                        for (int v = 0; v <= k; v++)
                        {
                            if (v > 0) zs.add(new int[] {u,v-1});
                            if (v < k) zs.add(new int[] {u,v+1});
                            if (u < i || u == i && v <= j) 
                            {
                                if (u > 0) zs.add(new int[] {u-1,v});
                                if (u < k) zs.add(new int[] {u+1,v}); 
                            }
                            else
                            if (u == i || u == i+1 && v <= j) 
                                if (u > 0) zs.add(new int[] {u-1,v});
                            assertEquals("", "true", sameElements(zs, AnalyzeSolution.getConnections(ps[k], u, v)));
                            zs.clear();
                        }
                }
            ps[k].clear();
            // every horizontal line individually
            for (int i = 0; i <= k; i++)
                for (int j = 0; j < k; j++)
                {
                    ps[k].horizontalClick(i,j);
                    for (int u = 0; u <= k; u++)
                        for (int v = 0; v <= k; v++)
                        {
                            if (u == i && v == j)   zs.add(new int[] {i,j+1});
                            if (u == i && v == j+1) zs.add(new int[] {i,j});
                            assertEquals("", "true", sameElements(zs, AnalyzeSolution.getConnections(ps[k], u, v)));
                            zs.clear();
                        }
                    ps[k].horizontalClick(i,j);
                }
            // every vertical line individually
            for (int i = 0; i < k; i++)
                for (int j = 0; j <= k; j++)
                {
                    ps[k].verticalClick(i,j);
                    for (int u = 0; u <= k; u++)
                        for (int v = 0; v <= k; v++)
                        {
                            if (u == i   && v == j) zs.add(new int[] {i+1,j});
                            if (u == i+1 && v == j) zs.add(new int[] {i,j});
                            assertEquals("", "true", sameElements(zs, AnalyzeSolution.getConnections(ps[k], u, v)));
                            zs.clear();
                        }
                    ps[k].verticalClick(i,j);
                }
            // round the outside, round the outside
            for (int i = 0; i < k; i++)
            {
                ps[k].horizontalClick(0,i);
                ps[k].horizontalClick(k,i);
                ps[k].verticalClick(i,0);
                ps[k].verticalClick(i,k);
            }
            for (int u = 0; u <= k; u++)
                for (int v = 0; v <= k; v++)
                {
                    if (u == 0 || u == k)
                    {
                        if (v > 0) zs.add(new int[] {u,v-1});
                        if (v < k) zs.add(new int[] {u,v+1});
                    }
                    if (v == 0 || v == k)
                    {
                        if (u > 0) zs.add(new int[] {u-1,v});
                        if (u < k) zs.add(new int[] {u+1,v});
                    }
                    assertEquals("", "true", sameElements(zs, AnalyzeSolution.getConnections(ps[k], u, v)));
                    zs.clear();
                }
        } 
    }
    
    // this allows for multiple error conditions
    // the digits of x > 0 are used to indicate correct responses from the instance variable states
    // only the prefix of s is checked,and case is irrelevant 
    private void assertOneOf(int x, String s)
    {
        boolean b = false;
        do
        {
            if (s.toLowerCase().startsWith(states[x % 10])) b = true;
            x /= 10;
        }
        while (!b && x > 0);
        assertTrue("'" + s + "' is wrong", b);
    }
        
    @Test
    public void testtracePath2()
    {
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j <= 2; j++)
                assertOneOf(1, AnalyzeSolution.tracePath(ps[2], i, j));
        
        ps[2].horizontalClick(0, 0);
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j <= 2; j++)
                if (i == 0 && j < 2)
                   assertOneOf(3, AnalyzeSolution.tracePath(ps[2], i, j));
                else
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[2], i, j));
        
        ps[2].horizontalClick(0, 1);
        ps[2].verticalClick(0, 0);
        ps[2].verticalClick(1, 0);
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j <= 2; j++)
                if (i == 0 || j == 0)
                   assertOneOf(3, AnalyzeSolution.tracePath(ps[2], i, j));
                else
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[2], i, j));
        
        ps[2].horizontalClick(1, 0);
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j <= 2; j++)
                if (i + j <= 2)
                   assertOneOf(34, AnalyzeSolution.tracePath(ps[2], i, j));
                else
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[2], i, j));
        
        ps[2].horizontalClick(2, 1);
        ps[2].verticalClick(1, 1);
        ps[2].verticalClick(0, 2);
        ps[2].verticalClick(1, 2);
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j <= 2; j++)
                assertOneOf(34, AnalyzeSolution.tracePath(ps[2], i, j));
        
        ps[2].horizontalClick(2, 0);
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j <= 2; j++)
                assertOneOf(4, AnalyzeSolution.tracePath(ps[2], i, j));
        
        ps[2].horizontalClick(2, 0);
        ps[2].verticalClick(1, 0);
        for (int i = 0; i <= 2; i++)
            for (int j = 0; j <= 2; j++)
                if (i == 2 && j == 0)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[2], i, j));
                else
                   assertEquals("", "8", AnalyzeSolution.tracePath(ps[2], i, j));
    }
    
    @Test
    public void testtracePath3()
    {
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
        
        for (int c : new int[] {1,2}) ps[3].horizontalClick(0, c);
        for (int c : new int[] {0,2}) ps[3].horizontalClick(1, c);
        for (int c : new int[] {0,2}) ps[3].horizontalClick(2, c);
        for (int c : new int[] {1,2}) ps[3].horizontalClick(3, c);
        for (int r : new int[] {1})   ps[3].verticalClick(r, 0);
        for (int r : new int[] {0,2}) ps[3].verticalClick(r, 1);
        for (int r : new int[] {1})   ps[3].verticalClick(r, 2);
        for (int r : new int[] {0,2}) ps[3].verticalClick(r, 3);
        
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                if (i % 3 == 0 && j == 0)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                   assertEquals("", "14", AnalyzeSolution.tracePath(ps[3], i, j));
        
        ps[3].verticalClick(1,3);
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                if (i % 3 == 0 && j == 0)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                   assertOneOf(4, AnalyzeSolution.tracePath(ps[3], i, j));
        
        ps[3].clear();
        for (int r = 0; r <= 3; r += 3) 
            ps[3].horizontalClick(r, 0);
        for (int r = 1; r <= 2; r++) 
            ps[3].horizontalClick(r, 2);
        for (int c = 0; c <= 3; c++) 
            ps[3].verticalClick(1, c);
        for (int r = 0; r <= 2; r += 2) 
            for (int c = 0; c <= 1; c++) 
                ps[3].verticalClick(r, c);
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                if (i % 3 == 0 && j > 1)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                if (j <= 1)
                   assertEquals("", "8", AnalyzeSolution.tracePath(ps[3], i, j));
                else
                   assertEquals("", "4", AnalyzeSolution.tracePath(ps[3], i, j));
        
        ps[3].horizontalClick(1,0);
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                if (i % 3 == 0 && j > 1)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                if (j <= 1)
                   assertOneOf(4, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                   assertEquals("", "4", AnalyzeSolution.tracePath(ps[3], i, j));
        
        ps[3].verticalClick(1,0);
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                if (i % 3 == 0 && j > 1)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                if (j <= 1)
                   assertOneOf(34, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                   assertEquals("", "4", AnalyzeSolution.tracePath(ps[3], i, j));
        
        ps[3].horizontalClick(2,0);
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                if (i % 3 == 0 && j > 1)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                if (j <= 1)
                   assertOneOf(4, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                   assertEquals("", "4", AnalyzeSolution.tracePath(ps[3], i, j));
        
        ps[3].verticalClick(1,3);
        for (int i = 0; i <= 3; i++)
            for (int j = 0; j <= 3; j++)
                if (i % 3 == 0 && j > 1)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                if (j <= 1)
                   assertOneOf(4, AnalyzeSolution.tracePath(ps[3], i, j));
                else
                   assertOneOf(3, AnalyzeSolution.tracePath(ps[3], i, j));
    }
    
    @Test
    public void testtracePath5()
    {
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++)
                assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j));
        
        for (int c : new int[] {1,2,3,4}) ps[5].horizontalClick(0, c);
        for (int c : new int[] {0,2,3,4}) ps[5].horizontalClick(1, c);
        for (int c : new int[] {0,3})     ps[5].horizontalClick(2, c);
        for (int c : new int[] {2,4})     ps[5].horizontalClick(3, c);
        for (int c : new int[] {0,2,4})   ps[5].horizontalClick(4, c);
        for (int c : new int[] {0,1,3})   ps[5].horizontalClick(5, c);
        for (int r : new int[] {1,4})     ps[5].verticalClick(r, 0);
        for (int r : new int[] {0,2,3})   ps[5].verticalClick(r, 1);
        for (int r : new int[] {1,2,4})   ps[5].verticalClick(r, 2);
        for (int r : new int[] {2,4})     ps[5].verticalClick(r, 3);
        for (int r : new int[] {2,4})     ps[5].verticalClick(r, 4);
        for (int r : new int[] {0,3})     ps[5].verticalClick(r, 5);
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++)
                if (j == 0 && i % 3 == 0 || j == 5 && i % 3 == 2)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j)); 
                else
                   assertEquals("", "32", AnalyzeSolution.tracePath(ps[5], i, j)); 
        
        ps[5].horizontalClick(0, 4);
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++)
                if (j == 0 && i % 3 == 0 || j == 5 && i % 3 == 2)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j)); 
                else
                   assertOneOf(3, AnalyzeSolution.tracePath(ps[5], i, j));
        
        ps[5].horizontalClick(4, 3);
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++)
                if (j == 0 && i % 3 == 0 || j == 5 && i % 3 == 2)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j)); 
                else
                   assertOneOf(34, AnalyzeSolution.tracePath(ps[5], i, j));
                   
        ps[5].horizontalClick(0, 4);
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++)
                if (j == 0 && i % 3 == 0 || j == 5 && i % 3 == 2)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j)); 
                else
                   assertOneOf(4, AnalyzeSolution.tracePath(ps[5], i, j));
        
        ps[5].horizontalClick(4, 3);
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++)
                if (j == 0 && i % 3 == 0 || j == 5 && i % 3 == 2)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j)); 
                else
                   assertEquals("", "32", AnalyzeSolution.tracePath(ps[5], i, j)); 
                   
        for (int r : new int[] {1,2,4,5}) ps[5].horizontalClick(r, 0);
        for (int r : new int[] {1,4}) for (int c : new int[] {0,1}) ps[5].verticalClick(r, c);
        for (int i = 0; i <= 5; i++)
            for (int j = 0; j <= 5; j++)
                if (j == 0 || j == 5 && i % 3 == 2)
                   assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j)); 
                else
                   assertEquals("", "28", AnalyzeSolution.tracePath(ps[5], i, j)); 
        
        for (int r : new int[] {0,2,4})
        {
            ps[5].verticalClick(r, 0);
            for (int i = 0; i <= 5; i++)
                for (int j = 0; j <= 5; j++)
                    if (j == 0 && i > r+1 || j == 5 && i % 3 == 2)
                       assertOneOf(1, AnalyzeSolution.tracePath(ps[5], i, j)); 
                    else
                    if (j == 0)
                       assertOneOf(3, AnalyzeSolution.tracePath(ps[5], i, j)); 
                    else
                       assertEquals("", "28", AnalyzeSolution.tracePath(ps[5], i, j));
        }
    }
    
    @Test
    public void testlineSegments()
    {
        ArrayList<int[]> zs = new ArrayList<>();
        for (int k : examples)
        {
            assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
            // add all horizontal lines
            for (int i = 0; i <= k; i++)
                for (int j = 0; j < k; j++)
                {
                    ps[k].horizontalClick(i,j);
                    zs.add(new int[] {i,j});
                    zs.add(new int[] {i,j+1});
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                }
            // and remove them
            for (int i = 0; i <= k; i++)
                for (int j = 0; j < k; j++)
                {
                    ps[k].horizontalClick(i,j);
                    zs.remove(0);
                    zs.remove(0);
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                }
            // add all vertical lines 
            for (int i = 0; i < k; i++)
                for (int j = 0; j <= k; j++)
                {
                    ps[k].verticalClick(i,j);
                    zs.add(new int[] {i,j});
                    zs.add(new int[] {i+1,j});
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                }
            // and remove them
            for (int i = 0; i < k; i++)
                for (int j = 0; j <= k; j++)
                {
                    ps[k].verticalClick(i,j);
                    zs.remove(0);
                    zs.remove(0);
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                }
            // add each horizontal line alone, and immediately remove it
            for (int i = 0; i <= k; i++)
                for (int j = 0; j < k; j++)
                {
                    ps[k].horizontalClick(i,j);
                    zs.add(new int[] {i,j});
                    zs.add(new int[] {i,j+1});
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                    ps[k].horizontalClick(i,j);
                    zs.clear();
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                }
            // add each vertical line alone, and immediately remove it
            for (int i = 0; i < k; i++)
                for (int j = 0; j <= k; j++)
                {
                    ps[k].verticalClick(i,j);
                    zs.add(new int[] {i,j});
                    zs.add(new int[] {i+1,j});
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                    ps[k].verticalClick(i,j);
                    zs.clear();
                    assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
                }
            ps[k].clear();
            // round the outside, round the outside
            for (int i = 0; i < k; i++)
            {
                ps[k].horizontalClick(0,i);
                ps[k].horizontalClick(k,i);
                ps[k].verticalClick(i,0);
                ps[k].verticalClick(i,k);
                for (int x = 0; x <=1; x++)
                {
                    zs.add(new int[] {0,i+x});
                    zs.add(new int[] {k,i+x});
                    zs.add(new int[] {i+x,0});
                    zs.add(new int[] {i+x,k});
                }
            }
            assertEquals("", "true", checkSegments(zs, AnalyzeSolution.lineSegments(ps[k])));
            zs.clear();
        }
    }
    
    @Test
    public void testfinished2()
    {
        assertOneOf(2, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(0, 0);
        assertOneOf(23, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(0, 1);
        ps[2].verticalClick(0, 0);
        assertOneOf(23, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(1, 0);
        ps[2].verticalClick(0, 2);
        assertOneOf(3, AnalyzeSolution.finished(ps[2]));
        
        ps[2].verticalClick(1, 0);
        ps[2].verticalClick(0, 2);
        assertOneOf(234, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(2, 1);
        ps[2].verticalClick(1, 1);
        ps[2].verticalClick(0, 2);
        ps[2].verticalClick(1, 2);
        assertOneOf(34, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(2, 0);
        assertOneOf(4, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(1, 0);
        ps[2].verticalClick(1, 1);
        assertOneOf(2, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(1, 1);
        ps[2].verticalClick(1, 1);
        assertOneOf(24, AnalyzeSolution.finished(ps[2]));
        
        ps[2].horizontalClick(1, 1);
        ps[2].verticalClick(1, 0);
        ps[2].horizontalClick(2, 0);
        ps[2].horizontalClick(1, 0);
        assertOneOf(6, AnalyzeSolution.finished(ps[2]));
        
        ps[2].clear();
        assertOneOf(2, AnalyzeSolution.finished(ps[2]));
    }
    
    @Test
    public void testfinished3()
    {
        assertOneOf(2, AnalyzeSolution.finished(ps[3]));
        
        for (int c = 0; c <= 3; c++)
            for (int r = 0; r <= 3; r++)
            {
                ps[3].horizontalClick(r, c);
                ps[3].verticalClick(r, c);
            }
        assertOneOf(24, AnalyzeSolution.finished(ps[3]));
        
        for (int r = 0; r <= 3; r++)
            ps[3].horizontalClick(r, 1);
        assertOneOf(24, AnalyzeSolution.finished(ps[3]));
        
        for (int r = 1; r <= 2; r++)
            for (int c = 0; c <= 2; c += 2)
                ps[3].horizontalClick(r, c);
        assertOneOf(25, AnalyzeSolution.finished(ps[3]));
        
        for (int r = 0; r <= 3; r++)
            ps[3].horizontalClick(r, 0);
        ps[3].verticalClick(0, 0);
        ps[3].verticalClick(2, 0);
        ps[3].verticalClick(1, 1);
        assertOneOf(35, AnalyzeSolution.finished(ps[3]));
        
        for (int r = 0; r <= 3; r += 3)
            ps[3].horizontalClick(r, 1);
        assertOneOf(4, AnalyzeSolution.finished(ps[3]));
        
        for (int r = 0; r <= 3; r += 2)
            ps[3].verticalClick(r, 2);
        for (int r = 1; r <= 2; r++)
            ps[3].horizontalClick(r, 2);
        ps[3].verticalClick(1, 3);
        assertOneOf(6, AnalyzeSolution.finished(ps[3]));
        
        ps[3].verticalClick(2, 0);
        assertOneOf(234, AnalyzeSolution.finished(ps[3]));
        
        ps[3].horizontalClick(3, 0);
        assertOneOf(24, AnalyzeSolution.finished(ps[3]));
        
        ps[3].verticalClick(2, 0);
        ps[3].verticalClick(2, 1);
        assertOneOf(3, AnalyzeSolution.finished(ps[3]));
    }
    
    @Test
    public void testfinished5()
    {
        assertOneOf(2, AnalyzeSolution.finished(ps[5]));
        
        for (int c : new int[] {1,2,3,4}) ps[5].horizontalClick(0, c);
        for (int c : new int[] {0,2,3,4}) ps[5].horizontalClick(1, c);
        for (int c : new int[] {0,3})     ps[5].horizontalClick(2, c);
        for (int c : new int[] {2,4})     ps[5].horizontalClick(3, c);
        for (int c : new int[] {0,2,4})   ps[5].horizontalClick(4, c);
        for (int c : new int[] {0,1,3})   ps[5].horizontalClick(5, c);
        for (int r : new int[] {1,4})     ps[5].verticalClick(r, 0);
        for (int r : new int[] {0,2,3})   ps[5].verticalClick(r, 1);
        for (int r : new int[] {1,2,4})   ps[5].verticalClick(r, 2);
        for (int r : new int[] {2,4})     ps[5].verticalClick(r, 3);
        for (int r : new int[] {2,4})     ps[5].verticalClick(r, 4);
        for (int r : new int[] {0,3})     ps[5].verticalClick(r, 5);
        assertOneOf(6, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(0, 4);
        ps[5].horizontalClick(1, 4);
        ps[5].verticalClick(0, 4);
        ps[5].verticalClick(0, 5);
        assertOneOf(2, AnalyzeSolution.finished(ps[5]));
        
        ps[5].verticalClick(0, 4);
        assertOneOf(3, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(0, 4);
        ps[5].horizontalClick(1, 4);
        ps[5].verticalClick(0, 5);
        assertOneOf(6, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(0, 0);
        ps[5].verticalClick(0, 0);
        ps[5].verticalClick(0, 1);
        ps[5].horizontalClick(1, 1);
        assertOneOf(4, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(0, 0);
        ps[5].verticalClick(0, 0);
        ps[5].verticalClick(0, 1);
        ps[5].horizontalClick(1, 1);
        assertOneOf(6, AnalyzeSolution.finished(ps[5])); 
        
        ps[5].horizontalClick(0, 3);
        ps[5].horizontalClick(1, 3);
        ps[5].verticalClick(0, 3);
        ps[5].verticalClick(0, 4);
        assertOneOf(5, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(0, 0);
        assertOneOf(345, AnalyzeSolution.finished(ps[5]));
        ps[5].horizontalClick(0, 0);
        assertOneOf(5, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(4, 4);
        ps[5].verticalClick(4, 4);
        ps[5].horizontalClick(5, 4);
        ps[5].verticalClick(4, 5);
        assertOneOf(25, AnalyzeSolution.finished(ps[5]));
        ps[5].horizontalClick(0, 0);
        assertOneOf(2345, AnalyzeSolution.finished(ps[5]));
        ps[5].horizontalClick(0, 0);
        assertOneOf(25, AnalyzeSolution.finished(ps[5]));
        ps[5].horizontalClick(4, 4);
        ps[5].verticalClick(4, 4);
        ps[5].horizontalClick(5, 4);
        ps[5].verticalClick(4, 5);
        assertOneOf(5, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(0, 2);
        assertOneOf(35, AnalyzeSolution.finished(ps[5])); 
        ps[5].horizontalClick(0, 2);
        assertOneOf(5, AnalyzeSolution.finished(ps[5]));
        
        ps[5].verticalClick(0, 2);
        assertOneOf(245, AnalyzeSolution.finished(ps[5]));
        ps[5].verticalClick(0, 2);
        assertOneOf(5, AnalyzeSolution.finished(ps[5]));
        
        ps[5].verticalClick(0, 4);
        assertOneOf(235, AnalyzeSolution.finished(ps[5]));
        ps[5].verticalClick(0, 4);
        assertOneOf(5, AnalyzeSolution.finished(ps[5]));
        
        ps[5].verticalClick(1, 1);
        assertOneOf(45, AnalyzeSolution.finished(ps[5]));
        ps[5].verticalClick(1, 1);
        assertOneOf(5, AnalyzeSolution.finished(ps[5]));
        
        ps[5].horizontalClick(0, 3);
        ps[5].horizontalClick(1, 3);
        ps[5].verticalClick(0, 3);
        ps[5].verticalClick(0, 4);
        assertOneOf(6, AnalyzeSolution.finished(ps[5]));
        
        ps[5].clear();
        assertOneOf(2, AnalyzeSolution.finished(ps[5]));
    }
}
