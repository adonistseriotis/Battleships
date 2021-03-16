package Battleships.Board;


/**
 * @author Adonis Tseriotis
 *
 * Class to represent the ships of game
 *
 */

public class Ship {
    /*private static Map<Integer, Integer> Size = new HashMap<>();
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
    }*/

    private static int size[] = {5,4,3,3,2};
    private static int hitPoints[] = {350,250,100,100,50};
    private static int sinkPoints[] = {1000,500,250,0,0};
    private static String name[] = {"Carrier","Battleship","Cruiser","Submarine","Destroyer"};
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

    /**
     * getter method for size array
     * @return size of this.type
     */
    public int TypetoSize(){
        return size[this.type-1];
    }

    /**
     * getter method for hitPoints array
     * @return hitPoints of this.type
     */
    public int TypetoHitpoints(){
        return hitPoints[this.type-1];
    }

    /**
     * getter method for sinkPoints array
     * @return sinkPoints of this.type
     */
    public int TypetoSinkpoints(){
        return sinkPoints[this.type-1];
    }

    /**
     * getter method for name array
     * @return Name of this.type
     */
    public String TypetoName(){
        return name[this.type-1];
    }

    /**
     * Mark ship as hit
     * @return true if ship is sank false otherwise
     */
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