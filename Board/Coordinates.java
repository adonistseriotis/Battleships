package Battleships.Board;
import java.util.*;

public class Coordinates{
    private int x,y;

    public Coordinates(int x,int y){
        this.x=x;
        this.y=y;
    }

    /*public Coordinates(){
        Random r = new Random();
        this.x = r.nextInt(10);
        this.y = r.nextInt(10);
    }*/

    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}