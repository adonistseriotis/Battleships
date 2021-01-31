package Battleships.Board;

import Battleships.MyExceptions.*;
import Battleships.Players.Human;
import Battleships.Players.Player;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Stack;

/**
 * @author Adonis Tseriotis
 *
 * Class to represent the board.
 * One for each player.
 */
public class Grid extends Parent implements Cloneable{
    public Player parentPlayer;
    private Stack<Coordinates> toBePlaced;
    private Ship toPlace;
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
     *
     */
    public void markShipPos(){
        for(Coordinates c: toBePlaced) {
            if (parentPlayer instanceof Human)
                c.setFill(Color.GREEN);
            parentPlayer.myShips[toPlace.type-1] = true;
            c.ship = toPlace;
        }
    }

    public String canPlaceShip(Ship s){
        int x = s.initX;
        int y = s.initY;
        toBePlaced = new Stack<>();
        toPlace = s;

        for(int i=0; i<s.TypetoSize(); i++)
        {
            try {
                if (x < 0 || x > 9 || y < 0 || y > 9) {
                    throw new OversizeException("Out of bounds");
                }
            }
            catch (OversizeException e){
                return e.getMessage();
            }

            Coordinates c = getSquare(x,y);

            try{
                if(c.ship != null){
                    throw new OverlapTilesException("There is another ship here");
                }
            }
            catch (OverlapTilesException e){
                return e.getMessage();
            }

            try{
                Coordinates n1 = getSquare(x-1,y);
                Coordinates n2 = getSquare(x+1,y);
                Coordinates n3 = getSquare(x,y-1);
                Coordinates n4 = getSquare(x,y+1);

                if(s.orientation == 2){
                    if((n1!=null) && (toBePlaced.search(n1) == -1) && (!n1.isEmpty())) {
                        throw new AdjacentTilesException("The ships are touching");
                    }

                    if(i==(s.TypetoSize()-1)){
                        if(n2 != null && !n2.isEmpty())
                            throw new AdjacentTilesException("The ships are touching");
                    }

                    if(((n3!=null)&&(!n3.isEmpty())) || ((n4!=null) && (!n4.isEmpty()))){
                        throw new AdjacentTilesException("The ships are touching");
                    }
                }
                else{
                    if(((n1!=null)&&(!n1.isEmpty())) || ((n2!=null)&&(!n2.isEmpty()))){
                        throw new AdjacentTilesException("The ships are touching");
                    }

                    if((n3!=null) && (toBePlaced.search(n3) == -1) && (!n3.isEmpty())){
                        throw new AdjacentTilesException("The ships are touching");
                    }

                    if(i==(s.TypetoSize()-1)){
                        if((n4!=null) && (!n4.isEmpty())){
                            throw new AdjacentTilesException("The ships are touching");
                        }
                    }
                }
            }
            catch (AdjacentTilesException e){
                return e.getMessage();
            }

            try{
                if(this.parentPlayer.myShips[s.type-1]){
                    throw new InvalidCountException("There is only one ship of each type");
                }
            }
            catch (InvalidCountException e){
                return e.getMessage();
            }

            toBePlaced.push(c);

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
        return "OK";
    }



    /**
     *
     * @param x X coordinate of square in grid
     * @param y Y coordinate of square in grid
     * @return Coordinate instance with coordinates (x,y)
     */
    public Coordinates getSquare(int x, int y){
        if (x < 0 || x > 9 || y < 0 || y > 9) {
            return null;
        }
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