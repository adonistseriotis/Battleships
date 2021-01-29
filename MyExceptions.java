package Battleships;
import java.util.*;

class OversizeException extends Exception{
    public OversizeException(String s){
        super(s);
    }
}

class OverlapTilesException extends Exception{
    public OverlapTilesException(String s){
        super(s);
    }
}

class AdjacentTilesException extends Exception{
    public AdjacentTilesException(String s){
        super(s);
    }
}

class InvalidCountException extends Exception{
    public InvalidCountException(String s){
        super(s);
    }
}