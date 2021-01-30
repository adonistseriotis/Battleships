package Battleships.Board;


import java.util.*;
import java.util.HashMap;
import Battleships.Board.Coordinates;

/**
 * @author Adonis Tseriotis
 *
 * Class to represent the ships of game
 *
 */

public class Ship {
    private static Map<Integer, Integer> Size = new HashMap<>();
    static{
        Size.put(1,5);
        Size.put(2,4);
        Size.put(3,3);
        Size.put(4,3);
        Size.put(5,2);
    }

    public static Map<Integer, Integer> HitPoints = new HashMap<>();
    static{
            HitPoints.put(1,350);
            HitPoints.put(2,250);
            HitPoints.put(3,100);
            HitPoints.put(4,100);
            HitPoints.put(5,50);
    }

    public static Map<Integer, Integer> SinkPoints = new HashMap<>();
    static {
            SinkPoints.put(1,1000);
            SinkPoints.put(2,500);
            SinkPoints.put(3,250);
            SinkPoints.put(4,0);
            SinkPoints.put(5,0);
    }

    public enum State{
        INTACT,
        HIT,
        SANK
    }

    public int type;
    public int orientation;
    public int initX;
    public int initY;
    public State state;
    public List<Coordinates> position;
    public int hitpoints;

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
        this.initX = x;
        this.initY = y;
        this.state = State.INTACT;
        this.orientation = orientation;
        this.hitpoints = this.TypetoSize();
    }

    public int TypetoSize(){
        return Size.get(this.type);
    }

    public int TypetoHitpoints(){
        return HitPoints.get(this.type);
    }

    public int TypetoSinkpoints(){
        return SinkPoints.get(this.type);
    }

    public boolean hit(){
        this.state = State.HIT;
        hitpoints--;
        if(hitpoints == 0){
            this.state = State.SANK;
            return true;
        }
        return false;

    }
}