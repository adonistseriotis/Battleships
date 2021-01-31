package Battleships;


import java.io.*;
import java.util.*;

import Battleships.Board.Coordinates;
import Battleships.Board.Grid;
import Battleships.Board.Ship;
import Battleships.Players.Computer;
import Battleships.Players.Human;
import Battleships.Players.Player;

import javafx.geometry.Insets;
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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Battleships extends Application{
    private boolean running = false;
    private Grid computerGrid,humanGrid;
    private Player computer, human;
    private int typeToPlace=1;
    private Random random = new Random();
    private int orientation;
    private Queue<Moves> humanMoves = new LinkedList<>();
    private Queue<Moves> computerMoves = new LinkedList<>();
    private Stack<Ship> humanShips = new Stack<>();
    private Stack<Ship> computerShips = new Stack<>();
    private BorderPane root;
    private HBox topInfo;

    private void popupError(boolean file,boolean human){
        String s,u;
        if(file)
            s="Load new scenarios and start again.";
        else
            s="Try again.";
        if(human)
            u="player's";
        else
            u="computer's";
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Error");
        Text message = new Text("Error in " + u +" ship placement.\n"+s);
        message.setStyle("-fx-font: 20 arial;");
        HBox hbox = new HBox(100, message);
        hbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hbox,500,500);
        popup.setScene(scene);
        popup.show();
    }

    private void parseLine(String line, boolean hooman){
        String[] args = line.split(",");
            Ship s = new Ship(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        if(hooman){
            humanShips.push(s);
        }
        else{
            computerShips.push(s);
        }
    }

    private void parseFile(File scenario, boolean human){
        FileReader fRead=null;
        try {
            fRead = new FileReader(scenario.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(fRead)) {
            String line;
            while((line=reader.readLine()) != null){
                parseLine(line,human);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    private MenuBar createMenu() {
        MenuBar menuBar = new MenuBar();
        /* First menu */
        Menu application = new Menu("Application");
        MenuItem start = new MenuItem("Start");
        start.setOnAction(click->{
            initialize();
            startGameFromSc();
        });

        MenuItem loadP = new MenuItem("Load player scenario");
        loadP.setOnAction(click -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Select scenarios (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(fileExtension);
            File pScenario = fileChooser.showOpenDialog(new Stage());
            if(pScenario != null) {
                parseFile(pScenario,true);
            }
        });

        MenuItem loadC = new MenuItem(("Load computer scenario"));
        loadC.setOnAction(click -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Select scenarios (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(fileExtension);
            File pScenario = fileChooser.showOpenDialog(new Stage());
            if(pScenario != null) {
                parseFile(pScenario,false);
            }
        });

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(click->
                System.exit(0));

        application.getItems().add(start);
        application.getItems().add(loadP);
        application.getItems().add(loadC);
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
            String s = "";
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
            String s = "";
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

    private HBox createInput(){
        HBox inputDialog = new HBox(2);
        inputDialog.setPadding(new Insets(50,50,50,50));
        inputDialog.setAlignment(Pos.CENTER);

        TextField x = new TextField();
        x.setPromptText("Input x coordinate");
        inputDialog.getChildren().add(x);

        TextField y = new TextField();
        y.setPromptText("Input y coordinate");
        inputDialog.getChildren().add(y);

        Button attack = new Button("Attack");
        inputDialog.getChildren().add(attack);

        return inputDialog;
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
        ships.setText(owner+" ships: " + (5 - player.allShips));
        Text points = (Text) playerInfo.getChildren().get(1);
        points.setText(null);
        points.setText(owner + " points: " + (player.points));
        Text shots = (Text) playerInfo.getChildren().get(2);
        shots.setText(null);
        shots.setText(owner + " shots: " + (40-player.shotsLeft));
    }

    private void initialize() {
        human = new Human();
        computer = new Computer();
        human.enemy = computer;
        computer.enemy = human;
        running = false;

        computerGrid = new Grid(computer,
                mouseClickEvent -> {
                    if(!running)
                        return;

                    Coordinates attackSquare = (Coordinates) mouseClickEvent.getSource();
                    if(attackSquare.isShot)
                        return;

                    humanPlay(attackSquare);

                    refreshInfo(topInfo,human);
                    refreshInfo(topInfo,computer);

                    computerPlay();

                    refreshInfo(topInfo,human);
                    refreshInfo(topInfo,computer);
                    //System.out.println("Computer played");

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
            if(!human.placeShip(s)) {
                popupError(false,true);
                return;
            }
            if(human.allShips == 0){
                startGame(topInfo);
            }
        });

        human.myGrid = humanGrid;

        HBox hbox = new HBox(80, humanGrid, computerGrid);
        hbox.setAlignment(Pos.CENTER);
        root.setCenter(hbox);

        topInfo = createInfo();
        MenuBar menuBar = createMenu();
        VBox vbox = new VBox(50,menuBar,topInfo);
        root.setTop(vbox);
    }

    private Parent createBorders(){
        root = new BorderPane();
        root.setPrefSize(1000,1000);

        human = new Human();
        computer = new Computer();
        human.enemy = computer;
        computer.enemy = human;

        computerGrid = new Grid(computer,
                mouseClickEvent -> {
            if(!running)
                return;

            Coordinates attackSquare = (Coordinates) mouseClickEvent.getSource();
            if(attackSquare.isShot)
                return;

            humanPlay(attackSquare);

            refreshInfo(topInfo,human);
            refreshInfo(topInfo,computer);

            computerPlay();

            refreshInfo(topInfo,human);
            refreshInfo(topInfo,computer);
            //System.out.println("Computer played");

        });

        computer.myGrid = computerGrid;

        humanGrid = new Grid(human, mouseClickEvent -> {
            if(running)
                return;

            Coordinates square = (Coordinates) mouseClickEvent.getSource();
            if(!square.isEmpty())
                return;
            orientation = (mouseClickEvent.getButton() == MouseButton.PRIMARY)?1:2;
            Ship s = new Ship((6-human.allShips), square.x, square.y, orientation);
            if(!human.placeShip(s)) {
                popupError(false,true);
                return;
            }
            if(human.allShips == 0){
                startGame(topInfo);
            }
        });

        human.myGrid = humanGrid;

        HBox hbox = new HBox(80, humanGrid, computerGrid);
        hbox.setAlignment(Pos.CENTER);
        root.setCenter(hbox);

        MenuBar menuBar = createMenu();
        topInfo = createInfo();

        VBox vbox = new VBox(50,menuBar,topInfo);
        root.setTop(vbox);

        HBox input = createInput();
        Button attack = (Button) input.getChildren().get(2);

        attack.setOnAction(click->{
            if(!running)
                return;
            TextField xCoord = (TextField) input.getChildren().get(0);
            TextField yCoord = (TextField) input.getChildren().get(1);
            int x = Integer.parseInt(xCoord.getText());
            int y = Integer.parseInt(yCoord.getText());
            if(x < 0 || x > 10 || y < 0 || y > 10){
                xCoord.clear();
                yCoord.clear();
                return;
            }

            Coordinates attackSquare = computerGrid.getSquare(x,y);
            if(attackSquare.isShot)
                return;

            humanPlay(attackSquare);

            refreshInfo(topInfo,human);
            refreshInfo(topInfo,computer);

            computerPlay();

            refreshInfo(topInfo,human);
            refreshInfo(topInfo,computer);
            //System.out.println("Computer played");
            xCoord.clear();
            yCoord.clear();

        });

        root.setBottom(input);

        return root;
    }

    private void humanPlay(Coordinates attackSquare){

        switch (computerGrid.parentPlayer.shotsTaken(attackSquare)) {
            /* No damage */
            case 0:
                if(humanMoves.size() > 5)
                    humanMoves.remove();
                Moves m1 = new Moves(attackSquare.x, attackSquare.y,"Missed", "");
                humanMoves.add(m1);
                //System.out.println("No damage");
                break;
            /* Hit something */
            case 1:
                if(humanMoves.size() > 5)
                    humanMoves.remove();
                Moves m2 = new Moves(attackSquare.x, attackSquare.y,"Hit", attackSquare.ship.TypetoName());
                humanMoves.add(m2);
                //System.out.println("Hit something");
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

    }

    private void computerPlay(){
        if(human.shotsLeft == 0){
            String winner;
            if ((human.points > computer.points))
                winner = "Player";
            else
                winner = "Computer";

            System.out.println(winner + " won");
            System.exit(0);
        }
        Coordinates computerAttackSquare;

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

        if(computer.shotsLeft == 0){
            String winner;
            if ((human.points > computer.points))
                winner = "Player";
            else
                winner = "Computer";

            System.out.println(winner + " won");
            System.exit(0);
        }

    }

    private void startGameFromSc(){
        for(Ship s: humanShips){
            if(!human.placeShip(s)){
                popupError(true,true);
            }
        }

        for(Ship s: computerShips){
            if(!computer.placeShip(s)){
                popupError(true,false);
            }
        }

        int turn = random.nextInt(2);
        switch (turn){
            case 0:
                System.out.println("Computer turn");
                computerPlay();
                refreshInfo(topInfo,computer);
                break;
            case 1:
                System.out.println("Your turn");
                break;
        }
        running = true;
        System.out.println("Game started");
    }

    private void startGame(HBox topInfo){

        while(true) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            int orientation = random.nextInt(2);
            Ship s = new Ship((6-computer.allShips), x, y, orientation+1);
            computer.placeShip(s);
            if(computer.allShips==0)
                break;
        }

        int turn = random.nextInt(2);
        switch (turn){
            case 0:
                System.out.println("Computer turn");
                computerPlay();
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
        launch(args);
    }

}