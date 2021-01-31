package Battleships;


import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;
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
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
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
    private Queue<Moves> humanMoves = new LinkedList<>();
    private Queue<Moves> computerMoves = new LinkedList<>();

    private MenuBar createMenu(Grid computerGrid) {
        MenuBar menuBar = new MenuBar();
        /* First menu */
        Menu application = new Menu("Application");
        MenuItem start = new MenuItem("Start");
        //start.setOnAction();
        MenuItem load = new MenuItem("Load");
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(click->{
            System.exit(0);
        });
        application.getItems().add(start);
        application.getItems().add(load);
        application.getItems().add(exit);
        menuBar.getMenus().add(application);

        /* Second menu */
        Menu details = new Menu("Details");
        MenuItem enemyShips = new MenuItem("Enemy Ships");
        enemyShips.setOnAction(click->{
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Enemy ship state");
            Text states = new Text(computerGrid.findStates());
            states.setStyle("-fx-font: 20 arial;");
            HBox hbox = new HBox(100, states);
            hbox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hbox,200,200);
            popup.setScene(scene);
            popup.show();
        });
        MenuItem playerShots = new MenuItem("Player Shots");
        playerShots.setOnAction(click->{
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Player shots");
            String s = new String("");
            for(Moves m : humanMoves){
                s += m.printMe();
            }
            Text states = new Text(s);
            states.setStyle("-fx-font: 20 arial;");
            HBox hbox = new HBox(100, states);
            hbox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hbox,500,500);
            popup.setScene(scene);
            popup.show();
        });
        MenuItem enemyShots = new MenuItem("Computer Shots");
        enemyShots.setOnAction(click->{
            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setTitle("Computer shots");
            String s = new String("");
            for(Moves m : computerMoves){
                s += m.printMe();
            }
            Text states = new Text(s);
            states.setStyle("-fx-font: 20 arial;");
            HBox hbox = new HBox(100, states);
            hbox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hbox,500,500);
            popup.setScene(scene);
            popup.show();
        });
        details.getItems().add(enemyShips);
        details.getItems().add(playerShots);
        details.getItems().add(enemyShots);
        menuBar.getMenus().add(details);

        return menuBar;
    }

    private HBox createInfo(){
        HBox topInfo = new HBox();

        VBox playerInfo = new VBox(5);
        playerInfo.setPrefWidth(500);
        playerInfo.setAlignment(Pos.CENTER);

        Text playerShips = new Text ("Your ships: 5");
        playerShips.setStyle("-fx-font: 20 arial;");
        playerInfo.getChildren().add(playerShips);

        Text playerPoints = new Text("Your Points: 0");
        playerPoints.setStyle("-fx-font: 20 arial;");
        playerInfo.getChildren().add(playerPoints);

        Text PlayerShots = new Text("Your Shots: 0");
        PlayerShots.setStyle("-fx-font: 20 arial;");
        playerInfo.getChildren().add(PlayerShots);
        topInfo.getChildren().add(playerInfo);

        VBox enemyInfo = new VBox();
        enemyInfo.setPrefWidth(500);
        enemyInfo.setAlignment(Pos.CENTER);

        Text EnemyShips = new Text("Computer ships: 5");
        EnemyShips.setStyle("-fx-font: 20 arial;");
        enemyInfo.getChildren().add(EnemyShips);

        Text enemyPoints = new Text("Computer Points: 0");
        enemyPoints.setStyle("-fx-font: 20 arial;");
        enemyInfo.getChildren().add(enemyPoints);

        Text EnemyShots = new Text("Computer shots: 0");
        EnemyShots.setStyle("-fx-font: 20 arial;");
        enemyInfo.getChildren().add(EnemyShots);
        topInfo.getChildren().add(enemyInfo);

        return topInfo;
    }

    private void refreshInfo(HBox topInfo, Player player) {
        VBox playerInfo;
        String owner;
        if(player instanceof Human) {
            playerInfo = (VBox) topInfo.getChildren().get(0);
            owner = "Your";
        }
        else{
            playerInfo = (VBox) topInfo.getChildren().get(1);
            owner = "Computer";
        }
        Text ships = (Text) playerInfo.getChildren().get(0);
        ships.setText(null);
        ships.setText(owner+" ships: " + Integer.toString(5 - player.allShips));
        Text points = (Text) playerInfo.getChildren().get(1);
        points.setText(null);
        points.setText(owner + " points: " + Integer.toString(player.points));
        Text shots = (Text) playerInfo.getChildren().get(2);
        shots.setText(null);
        shots.setText(owner + " shots: " + Integer.toString(40-player.shotsLeft));
    }

    private Parent createBorders(){
        BorderPane root = new BorderPane();
        root.setPrefSize(1000,1000);
        HBox topInfo = createInfo();

        human = new Human();
        computer = new Computer();
        human.enemy = computer;
        computer.enemy = human;

        computerGrid = new Grid(computer,
                mouseClickEvent -> {
            if(!running)
                return;

            Coordinates computerAttackSquare;
            Coordinates attackSquare = (Coordinates) mouseClickEvent.getSource();
            if(attackSquare.isShot)
                return;

            switch (computerGrid.parentPlayer.shotsTaken(attackSquare)) {
                /* No damage */
                case 0:
                    if(humanMoves.size() > 5)
                        humanMoves.remove();
                    Moves m1 = new Moves(attackSquare.x, attackSquare.y,"Missed", "");
                    humanMoves.add(m1);
                    System.out.println("No damage");
                    break;
                /* Hit something */
                case 1:
                    if(humanMoves.size() > 5)
                        humanMoves.remove();
                    Moves m2 = new Moves(attackSquare.x, attackSquare.y,"Hit", attackSquare.ship.TypetoName());
                    humanMoves.add(m2);
                    System.out.println("Hit something");
                    break;
                /* Sank something */
                case 2:
                    if(humanMoves.size() > 5)
                        humanMoves.remove();
                    Moves m3 = new Moves(attackSquare.x, attackSquare.y,"Sank", attackSquare.ship.TypetoName());
                    humanMoves.add(m3);
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
            refreshInfo(topInfo,human);
            refreshInfo(topInfo,computer);

            if((computerAttackSquare = computer.findNextShot())==null)
                computerAttackSquare = computer.findNextShot();

            if(computerMoves.size() > 5)
                computerMoves.remove();

            if(computerAttackSquare.ship == null)
            {
                Moves m = new Moves(computerAttackSquare.x, computerAttackSquare.y, "Missed","");
                computerMoves.add(m);
            }
            else{
                String action = computerAttackSquare.ship.state.name().toLowerCase(Locale.ROOT);
                action = action.substring(0,1).toUpperCase(Locale.ROOT) + action.substring(1);
                Moves m = new Moves((computerAttackSquare.x), computerAttackSquare.y,
                        action,computerAttackSquare.ship.TypetoName());
                computerMoves.add(m);
            }

            if(human.hasLost) {
                System.out.println("Computer won!");
                System.exit(0);
            }

            refreshInfo(topInfo,human);
            refreshInfo(topInfo,computer);
            System.out.println("Computer played");

        });

        computer.myGrid = computerGrid;

        humanGrid = new Grid(human, mouseClickEvent -> {
            if(running)
                return;

            Coordinates square = (Coordinates) mouseClickEvent.getSource();
            if(!square.isEmpty())
                return;
            orientation = (mouseClickEvent.getButton() == MouseButton.PRIMARY)?1:2;
            Ship s = new Ship(typeToPlace++, square.x, square.y, orientation);
            human.placeShip(s);
            if(human.allShips == 0){
                startGame(topInfo);
            }
        });

        human.myGrid = humanGrid;

        HBox hbox = new HBox(80, humanGrid, computerGrid);
        hbox.setAlignment(Pos.CENTER);
        root.setCenter(hbox);

        MenuBar menuBar = createMenu(computerGrid);

        VBox vbox = new VBox(50,menuBar,topInfo);
        root.setTop(vbox);

        return root;
    }

    private void startGame(HBox topInfo){
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
                refreshInfo(topInfo,computer);
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
        System.out.println(args);
        launch(args);
    }

}