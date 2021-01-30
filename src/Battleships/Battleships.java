package Battleships;


import java.util.Random;

import Battleships.Players.Computer;
import Battleships.Players.Human;
import Battleships.Players.Player;
import javafx.scene.control.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import Battleships.Board.Coordinates;
import Battleships.Board.Grid;
import Battleships.Board.Ship;


public class Battleships extends Application{
    private boolean running = false;
    private Grid computerGrid,humanGrid;
    private Player computer, human;
    private int typeToPlace=1;
    private Random random = new Random();
    private int orientation;

    private Parent createBorders(){
        BorderPane root = new BorderPane();
        ToolBar toolbar = new ToolBar();
        root.setPrefSize(500,400);
        root.setTop(toolbar);
        human = new Human();
        computer = new Computer();
        human.enemy = computer;
        computer.enemy = human;

        computerGrid = new Grid(computer,mouseEvent -> {
            if(!running)
                return;

            Coordinates attackSquare = (Coordinates) mouseEvent.getSource();
            if(attackSquare.isShot)
                return;

            switch (computerGrid.parentPlayer.shotsTaken(attackSquare)) {
                /* No damage */
                case 0:
                    System.out.println("No damage");
                    break;
                /* Hit something */
                case 1:
                    System.out.println("Hit something");
                    break;
                /* Sank something */
                case 2:
                    if (computerGrid.parentPlayer.enemy.allShips == 5) {
                        /* Computer Lost */
                        System.out.println("You won!");
                    }
                    break;
            }

            if(computer.hasLost){
                System.out.println("You won!");
                System.exit(0);
            }
            computer.findNextShot();

            if(human.hasLost) {
                System.out.println("Computer won!");
                System.exit(0);
            }
            
            System.out.println("Computer played");
        });

        computer.myGrid = computerGrid;

        humanGrid = new Grid(human, mouseEvent -> {
            if(running)
                return;

            Coordinates square = (Coordinates) mouseEvent.getSource();
            orientation = (mouseEvent.getButton() == MouseButton.PRIMARY)?1:2;
            Ship s = new Ship(typeToPlace++, square.x, square.y, orientation);
            human.placeShip(s);
            if(human.allShips == 0){
                startGame();
            }
        });

        human.myGrid = humanGrid;

        VBox vbox = new VBox(50, computerGrid, humanGrid);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    private void startGame(){
        Ship s1 = new Ship(1,8,4,1);
        computer.placeShip(s1);
        Ship s2 = new Ship(2,4,2,2);
        computer.placeShip(s2);
        Ship s3 = new Ship(3,3,8,2);
        computer.placeShip(s3);
        Ship s4 = new Ship(4,1,6,2);
        computer.placeShip(s4);
        Ship s5 = new Ship(5,4,4,2);
        computer.placeShip(s5);

        int turn = random.nextInt(2);
        switch (turn){
            case 0:
                System.out.println("Computer turn");
                computer.findNextShot();
                break;
            case 1:
                System.out.println("Your turn");
                break;
        }
        running = true;
        System.out.println("Game started");

    }

    @Override
    public void start(Stage primaryStage){
        Scene scene = new Scene(createBorders());
        primaryStage.setTitle("MediaLab Battleship");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}