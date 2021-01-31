package Battleships.Board;

import java.util.*;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import Battleships.Board.Ship;


public class Coordinates extends Rectangle{
    public int x,y;
    public Ship ship=null;
    public boolean isShot=false;

    private Grid parentGrid;

    public Coordinates(int x,int y, Grid grid) {
        super(40, 40);
        this.x = x;
        this.y = y;
        this.parentGrid = grid;
        setFill(Color.WHITE);
        setStroke(Color.BLACK);
    }

    public boolean shotsFired(){
        isShot=true;

        if(!this.isEmpty()){
            setFill(Color.RED);
            return true;
        }
        setFill(Color.GRAY);
        return false;
    }

    /**
     *
     * @return true if there is no other ship
     */

    public boolean isEmpty(){
        return (this.ship == null);
    }
}