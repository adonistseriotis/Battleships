package Battleships.Board;

import java.util.*;

import Battleships.Players.Human;
import Battleships.Players.Player;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Battleships.Board.Ship;
import Battleships.Board.Coordinates;
import javafx.scene.paint.Color;

/**
 * @author Adonis Tseriotis
 *
 * Class to represent the board.
 * One for each player.
 */
public class Grid extends Parent{
    public Player parentPlayer;
    private VBox rows = new VBox();

    /**
     *
     * @param belongsTo player to which this grid belongs to
     */
    public Grid(Player belongsTo, EventHandler<? super MouseEvent> handler){
        this.parentPlayer = belongsTo;

        for(int y=0; y<10; y++){
            HBox row = new HBox();
            for(int x=0; x<10; x++){
                Coordinates square = new Coordinates(y,x,this);
                square.setOnMouseClicked((handler));
                row.getChildren().add(square);
            }

            rows.getChildren().add(row);
        }
        getChildren().add(rows);
    }

    /**
     *
     * @param s Ship to add
     *
     */
    public void markShipPos(Ship s){
        int x = s.initX;
        int y = s.initY;
        System.out.print(x);
        System.out.println(y);
        for(int i=0; i<s.TypetoSize(); i++)
        {
            /*if(x<0 || x>9 || y<0 || y<9) {
                OversizeException OutOfBounds =
                        new OversizeException("Out of bounds");
                throw OutOfBounds;
            }*/
            Coordinates c = getSquare(x,y);

            //if(parentPlayer instanceof Human)
            c.setFill(Color.GREEN);
            /*if(c.ship != null){
                OverlapTilesException Overlap =
                        new OverlapTilesException("There is another ship here");
                throw Overlap;
            }*/

            c.ship = s;

            switch(s.orientation)
            {
                case 1:
                    y += 1;
                    break;

                case 2:
                    x += 1;
                    break;
            }
        }
    }



    /**
     *
     * @param x X coordinate of square in grid
     * @param y Y coordinate of square in grid
     * @return Coordinate instance with coordinates (x,y)
     */
    public Coordinates getSquare(int x, int y){
        return (Coordinates)((HBox)rows.getChildren().get(x)).getChildren().get(y);
    }

}