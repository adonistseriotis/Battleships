package Battleships.Board;

import java.util.*;
import Battleships.Board.Ship;
import Battleships.Board.Coordinates;
/**
 * @author Adonis Tseriotis
 *
 * Class to represent the board.
 * One for each player.
 */
public class Grid{
    grid = new int[10][10];
    private Player belongsTo;

    /**
     *
     * @param belongsTo: player to which this grid belongs to
     */
    public Grid(Player belongsTo){
        this.belongsTo = belongsTo;
        Arrays.fill(this.grid,0);
    }

    /**
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if there is no other ship
     */
    public boolean isEmpty(int x, int y){
        return (grid[x][y] == 0)? true:false;
    }

    /**
     *
     * @param s Ship to add
     * @param x X coordinate ship start position
     * @param y Y coordinate ship start position
     */
    public void markShipPos(Ship s){
        Coordinates c = s.position.get(0);
        int x = c.getX();
        int y = c.getY();
        for(int i=0; i<s.TypetoSize(s.type); i++)
        {
            if(x<0 || x>9 || y<0 || y<9) {
                OversizeException OutOfBounds =
                        new OversizeException("Out of bounds");
                throw OutOfBounds;
            }

            if(this.grid[x][y] != 0){
                OverlapTilesException Overlap =
                        new OverlapTilesException("There is another ship here");
                throw Overlap;
            }

            this.grid[x][y] = s.type;
            Coordinates pos = new Coordinates(x,y);
            s.position.add(pos);

            switch(s.orientation)
            {
                case 1:
                    x += 1;
                    break;

                case 2:
                    y += 1;
                    break;
            }
        }
    }

}