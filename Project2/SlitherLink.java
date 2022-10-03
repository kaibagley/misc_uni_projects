
/**
 * SlitherLink does the user interaction for a square Slither Link puzzle.
 * 
 * @author Kai Bagley
 * @version 1.0
 */
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

public class SlitherLink implements MouseListener
{    
    private Puzzle game;     // internal representation of the game
    private SimpleCanvas sc; // the display window
    private int size;
    private int canvasSize;
    
    private final static int DOTDIST=60; //Defined a bunch of my own final static ints, hope this is okay
    private final static int DOTSIZE=6;
    private final static Color DOTCOL=Color.black;
    private final static Color BACKCOL=Color.white;
    private final static Color NUMCOL=Color.red;

    /**
     * Creates a display for playing the puzzle p.
     */
    public SlitherLink(Puzzle p)
    {
        game = p;
        size = game.size();
        canvasSize = (size+1)*DOTDIST;
        sc = new SimpleCanvas("Slither Link",
                              canvasSize, //Tried to make the canvas a square, but keeps coming up rectangular if its smaller
                              canvasSize, //than 3x3 or 4x4
                              BACKCOL);
        sc.addMouseListener(this);
        displayPuzzle();
    }
    
    /**
     * Returns the current state of the game.
     */
    public Puzzle getGame()
    {
        return game;
    }

    /**
     * Returns the current state of the canvas.
     */
    public SimpleCanvas getCanvas()
    {
        return sc;
    }

    /**
     * Displays the initial puzzle on sc. 
     * Have a look at puzzle-loop.com for a basic display, or use your imagination. 
     */
    public void displayPuzzle()
    {
        sc.drawRectangle(0,0,canvasSize,canvasSize,BACKCOL);
        if (AnalyzeSolution.finished(game) == "Finished") { //For when game is finished, displays a completion message
            sc.drawString("Finished - Click to restart",canvasSize/4,canvasSize/2,Color.black);
        } else {
            for (int i=0 ; i<=size ; i++) {
                for (int j=0 ; j<=size ; j++) {
                    sc.drawDisc((i)*DOTDIST+DOTDIST/2,
                                (j)*DOTDIST+DOTDIST/2,
                                DOTSIZE,
                                DOTCOL);
                    if ((i != size && j != size) && (game.getPuzzle()[i][j] != -1)) {
                        sc.drawString(game.getPuzzle()[i][j],
                                      (j+1)*DOTDIST,
                                      (i+1)*DOTDIST,
                                      NUMCOL);
                    }
                    if (j != size && game.getHorizontal()[i][j]) { //Once again, these two ifs are because horizontal and
                        sc.drawLine(DOTDIST/2+j*DOTDIST,           //vertical matrices are 4x3 and 3x4
                                    DOTDIST/2+i*DOTDIST,
                                        DOTDIST/2+(j+1)*DOTDIST,
                                        DOTDIST/2+i*DOTDIST,
                                        DOTCOL);
                        }
                    if (i != size && game.getVertical()[i][j]) {
                        sc.drawLine(DOTDIST/2+j*DOTDIST,
                                    DOTDIST/2+i*DOTDIST,
                                    DOTDIST/2+j*DOTDIST,
                                    DOTDIST/2+(i+1)*DOTDIST,
                                    DOTCOL);
                    }
                }
            }
        }
    }
    
    /**
     * Makes a horizontal click to the right of Dot r,c.
     * Update game and the display, if the indices are legal; otherwise do nothing.
     */
    public void horizontalClick(int r, int c)
    {
        game.horizontalClick(r,c);
        displayPuzzle();
    }
    
    /**
     * Makes a vertical click below Dot r,c. 
     * Update game and the display, if the indices are legal; otherwise do nothing. 
     */
    public void verticalClick(int r, int c)
    {
        game.verticalClick(r,c);
        displayPuzzle();
    }
    
    /**
     * Actions for a mouse press.
     */
    public void mousePressed(MouseEvent e) 
    {
        int horidist,vertdist;
        int xv,yv,xh,yh;
        int xpos = e.getX();
        int ypos = e.getY();
        boolean done = false;
        if (AnalyzeSolution.finished(game) == "Finished") { //Allows for the game to reset after completion
            game.clear();
            displayPuzzle();
        } else {
            for (int i=0 ; i<=size && !done; i++) {         //Goes through all lines and finds the one with the shortest
                for (int j=0 ; j<=size && !done; j++) {     //Euclidean distance to the click position
                    if (i != size) { //Two ifs because 4x3 and 3x4 matrices again
                        xv = j*DOTDIST+DOTDIST/2;
                        yv = (i+1)*DOTDIST;
                        vertdist = (int)(Math.sqrt(Math.pow(xv-xpos,2)+Math.pow(yv-ypos,2)));
                        if (vertdist < DOTDIST/2-1) { //These ifs are here since a distance of DOTDIST/2-1 or less
                            verticalClick(i,j);       //corresponds to one point garuanteed
                            done = true;
                        }
                    }
                    if (j != size) {
                        xh = (j+1)*DOTDIST;
                        yh = i*DOTDIST+DOTDIST/2;
                        horidist = (int)(Math.sqrt(Math.pow(xh-xpos,2)+Math.pow(yh-ypos,2)));
                        if (horidist < DOTDIST/2-1 && !done) {
                            horizontalClick(i,j);
                            done = true;
                        }
                    }
                }
            }
        }
    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
