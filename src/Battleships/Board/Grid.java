package Battleships.Board;

import java.util.*;

import Battleships.Players.Human;
import Battleships.Players.Player;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Battleships.Board.Ship;
import Battleships.Board.Coordinates;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
/**
 * @author Adonis Tseriotis
 *
 * Class to represent the board.
 * One for each player.
 */
public class Grid extends Parent implements Cloneable{
    public Player parentPlayer;
    private VBox rows = new VBox();

    /**
     *
     * @param belongsTo player to which this grid belongs to
     */
    public Grid(Player belongsTo, EventHandler<? super MouseEvent> handler){
        this.parentPlayer = belongsTo;

        HBox firstRow = new HBox();
        VBox space = new VBox();
        space.setPrefWidth(15);
        firstRow.getChildren().add(space);
        for(int i=0; i<10; i++){
            VBox frame = new VBox();
            frame.setPrefWidth(40);
            Text Column = new Text(Integer.toString(i));
            Column.setStyle("-fx-font: 16 arial;");
            frame.setAlignment(Pos.CENTER);
            frame.getChildren().add(Column);
            firstRow.getChildren().add(frame);
        }
        rows.getChildren().add(firstRow);

        for(int y=0; y<10; y++){
            HBox row = new HBox();
            Text Row = new Text(Integer.toString(y));
            Row.setStyle("-fx-font: 16 arial;");
            row.setAlignment(Pos.CENTER_LEFT);
            row.getChildren().add(Row);
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

            if(parentPlayer instanceof Human)
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
        return (Coordinates)((HBox)rows.getChildren().get(x+1)).getChildren().get(y+1);
    }

    public String findStates(){
        String states[] = {"Ship 1 is ", "Ship 2 is ","Ship 3 is ","Ship 4 is ","Ship 5 is "};
        String returnable = new String("");
        boolean visited[] = {false,false,false,false,false};
        for(int x=0; x<10; x++){
            for(int y=0; y<10; y++){
                Coordinates cell = getSquare(x,y);
                if(!cell.isEmpty() && !visited[cell.ship.type-1]) {
                    states[cell.ship.type - 1] += cell.ship.state.name();
                    visited[cell.ship.type-1] = true;
                }
            }
        }
        for(String i: states){
            returnable += (i+"\n");
        }
        return returnable;
    }

}