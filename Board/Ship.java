package Battleships.Board;

import java.util.*;
import Battleships.Board.Coordinates;

/**
 * @author Adonis Tseriotis
 *
 * Class to represent the ships of game
 *
 */

public class Ship {
    public static Map<Integer, Integer> TypetoSize
            = new HashMap<>() {
            TypetoSize.put(1,5);
            TypetoSize.put(2,4);
            TypetoSize.put(3,3);
            TypetoSize.put(4,3);
            TypetoSize.put(5,2);
    }

    public static Map<Integer, Integer> TypetoHitPoints
            = new HashMap<>(){
            TypetoHitPoints.put(1,350);
            TypetoHitPoints.put(2,250);
            TypetoHitPoints.put(3,100);
            TypetoHitPoints.put(4,100);
            TypetoHitPoints.put(5,50);
    }

    public static Map<Integer, Integer> TypetoSinkPoints
            = new HashMap<>(){
            TypetoSinkPoints.put(1,1000);
            TypetoSinkPoints.put(2,500);
            TypetoSinkPoints.put(3,250);
            TypetoSinkPoints.put(4,0);
            TypetoSinkPoints.put(5,0);
    }

    public enum State{
        INTACT,
        HIT,
        SANK
    }

    public int type;
    public int orientation;
    public State state;
    public List<Coordinates> position;
    public List<Coordinates> hitpoints;

    /**
     *
     * @param type Type of ship
     * @param x X coordinate of start position
     * @param y Y coordinate of start position
     * @param orientation Horizontal(1) or Vertical(2)
     */
    public Ship(int type,int x, int y, int orientation)
    {
        this.type = type;
        Coordinates startpos = new Coordinates(x,y);
        this.position.add(startpos);
        this.orientation = orientation;
    }
}