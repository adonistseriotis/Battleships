package Battleships;

public class Moves {
    public int x;
    public int y;
    public String result;
    public String type;

    public Moves(int x, int y, String result, String type)
    {
        this.x = x;
        this.y = y;
        this.result = result;
        this.type = type;
    }

    public String printMe(){
        return result + " a " + type + " ship at coordinates (" +
                x + ", " + y + ")\n";
    }
}
