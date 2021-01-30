package Battleships;

import java.util.Random;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import Battleships.Board.Coordinates;
import Battleships.Board.Grid;
import Battleships.Board.Ship;


public class Battleships extends Application{
    private boolean running = false;
    private Grid computerBoard,humanBoard;
    private Player computer, human;

    private Parent createBorders(){
        BorderPane root = new BorderPane();
        ToolBar toolbar = new ToolBar();
        root.setPrefSize(500,400);
        root.setTop(toolbar);
        computerBoard = new Grid(computer);
        humanBoard = new Grid(human);
        human = new Human(humanBoard);

        VBox vbox = new VBox(50, computerBoard, humanBoard);
        vbox.setAlignment(Pos.CENTER);

        root.setCenter(vbox);

        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
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