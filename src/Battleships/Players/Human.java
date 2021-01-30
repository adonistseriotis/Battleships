package Battleships.Players;

import Battleships.Board.Coordinates;
import Battleships.Board.Grid;
import Battleships.Board.Ship;

/**
 *
 * @author Adonis Tseriotis
 *
 * Class to represent Human player
 */
public class Human extends Player{

    public Human(Grid grid){
        this.myGrid = grid;
        this.allShips = 5;
        this.hasLost = false;
        this.points = 0;
        this.shotsLeft = 40;
    }

    /**
     *
     * @param s Ship to place
     * @return False if player placed all ships True elsewhere
     */
    @Override
    public boolean placeShip(Ship s) {
        if(allShips == 0)
            return false;
        myGrid.markShipPos(s);
        allShips--;
        return true;
    }

    /**
     * Function to be inserted in event handling
     * @param square Coordination in which enemy fires
     * @return  0 If none damage, 1 if yes damage, 2 if ship sank
     */
    @Override
    public int shotsTaken(Coordinates square){
        enemy.shotsLeft--;
        if(square.shotsFired()){
            points += square.ship.TypetoHitpoints();
            if(square.ship.hit()){
                points += square.ship.TypetoSinkpoints();
                allShips++;
                if(allShips==5)
                    hasLost = true;
                return 2;
            }
            return 1;
        }
        return 0;
    }

    /*
    Human fire is handled by mouse handler
     */
}
