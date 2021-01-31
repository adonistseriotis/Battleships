package Battleships;

import java.util.*;

public class MyExceptions extends Exception {
    public static class OversizeException extends Exception {
        public OversizeException(String s) {
            super(s);
        }
    }

    public static class OverlapTilesException extends Exception {
        public OverlapTilesException(String s) {
            super(s);
        }
    }

    public static class AdjacentTilesException extends Exception {
        public AdjacentTilesException(String s) {
            super(s);
        }
    }

    public static class InvalidCountException extends Exception {
        public InvalidCountException(String s) {
            super(s);
        }
    }
}