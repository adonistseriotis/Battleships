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
    Grid myGrid;
    List<Ship> myShips;
    int allShips;
    boolean hasLost;
    int points;
    public Player enemy;
    int shotsLeft;

    abstract boolean placeShip(Ship s);
    abstract int shotsTaken(Coordinates square);
}
