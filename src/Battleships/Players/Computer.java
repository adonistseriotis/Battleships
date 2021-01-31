package Battleships.Players;

import Battleships.Board.Coordinates;
import Battleships.Board.Grid;
import Battleships.Board.Ship;

import java.util.Stack;
import java.util.Random;

/**
 *
 * @author Adonis Tseriotis
 *
 * Class to represent computer player
 */
public class Computer extends Player{

    private Stack<Coordinates> nextShots;
    private Random random = new Random();

    public Computer(){
        this.allShips = 5;
        this.hasLost = false;
        this.points = 0;
        this.shotsLeft = 40;
        this.nextShots = new Stack<Coordinates>();
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
            enemy.points += square.ship.TypetoHitpoints();
            if(square.ship.hit()){
                enemy.points += square.ship.TypetoSinkpoints();
                allShips++;
                if(allShips==5)
                    hasLost = true;
                return 2;
            }
            return 1;
        }
        return 0;
    }

    /**
     * Function to find next shot of computer
     */
    public Coordinates findNextShot(){
        int x,y;
        if(nextShots.isEmpty()){
            while(true) {
                x = random.nextInt(10);
                y = random.nextInt(10);
                Coordinates attackSquare = enemy.myGrid.getSquare(x, y);
                if (!attackSquare.isShot) {
                    if(enemy.shotsTaken(attackSquare) > 0) {
                        /* Push next possible hits */
                        if (x - 1 > 0)
                            nextShots.push(enemy.myGrid.getSquare(x - 1, y));
                        if (x + 1 < 10)
                            nextShots.push(enemy.myGrid.getSquare(x + 1, y));
                        if (y - 1 > 0)
                            nextShots.push(enemy.myGrid.getSquare(x, y - 1));
                        if (y + 1 < 10)
                            nextShots.push(enemy.myGrid.getSquare(x, y + 1));
                    }
                    return attackSquare;
                }
            }
        }

        else{
            Coordinates nextAttackSquare;
            while(true){
                if(nextShots.isEmpty()) {
                    return null;
                }
                nextAttackSquare = nextShots.pop();
                if(nextAttackSquare.isShot)
                    continue;
                else
                    break;

            }

            if(enemy.shotsTaken(nextAttackSquare) > 0){
                x = nextAttackSquare.x;
                y = nextAttackSquare.y;
                /* Push next possible hits */
                if (x - 1 > 0)
                    nextShots.push(enemy.myGrid.getSquare(x - 1, y));
                if (x + 1 < 10)
                    nextShots.push(enemy.myGrid.getSquare(x + 1, y));
                if (y - 1 > 0)
                    nextShots.push(enemy.myGrid.getSquare(x, y - 1));
                if (y + 1 < 10)
                    nextShots.push(enemy.myGrid.getSquare(x, y + 1));
            }
            return nextAttackSquare;
        }
    }
}
