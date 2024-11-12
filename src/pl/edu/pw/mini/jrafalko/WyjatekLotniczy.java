package pl.edu.pw.mini.jrafalko;

public class WyjatekLotniczy extends RuntimeException {
    public WyjatekLotniczy(String message) {
        super(message);
    }
}

class WyjatekEkonomiczny extends WyjatekLotniczy{
    public WyjatekEkonomiczny(String message) {
        super(message);
    }
}

class WyjatekPrzeladowania extends WyjatekLotniczy{
    public WyjatekPrzeladowania(String message, int ilosc) {
        super("Za dużo o " + ilosc + " " +message);
    }
}