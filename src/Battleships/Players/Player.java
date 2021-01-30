package Battleships.Players;

import Battleships.Board.Coordinates;
import Battleships.Board.Grid;
import Battleships.Board.Ship;

import java.util.List;

/**
 *
 * @author Adonis Tseriotis
 *
 * Abstract class to represent Player
 */
public abstract class Player {
    public Grid myGrid;
    public List<Ship> myShips;
    public int allShips;
    public boolean hasLost;
    public int points;
    public Player enemy;
    public int shotsLeft;

    abstract public boolean placeShip(Ship s);
    abstract public int shotsTaken(Coordinates square);
    abstract public void findNextShot();
}
