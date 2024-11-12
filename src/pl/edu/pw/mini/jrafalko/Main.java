package pl.edu.pw.mini.jrafalko;

import java.util.function.Consumer;

public class Main {

    public static void main(String[] args) {

        Lotnisko lotnisko = new Lotnisko(5);
        System.out.println("Samoloty na lotnisku:");
        System.out.println("---------------------");
        lotnisko.wypiszSamoloty();
        System.out.println("\nPróba odlotu:");
        System.out.println("----------------------");
        lotnisko.startSamolotow();
        System.out.println("\nOdprawa:");
        System.out.println("----------");
        lotnisko.odprawaSamolotow();
        lotnisko.wypiszSamoloty();
        System.out.println("\nOdlot:");
        System.out.println("--------");
        lotnisko.startSamolotow();
        lotnisko.wypiszSamoloty();
        System.out.println("\nDziałania lotniskowe:");
        System.out.println("-----------------------");
        lotnisko.dzialaniaLotniskowe();
        System.out.println("\nSortowanie samolotów:");
        System.out.println("-----------------------");
        lotnisko.sortowanieSamolotow();
        System.out.println("\nSortowanie losowe:");
        System.out.println("--------------------");
        lotnisko.sortowanieLosowe();

//        W klasie lotnisko stworzylem nowa metode i teraz dziala
        Lotnisko.Samolot ufo = lotnisko.stworzPrywatnyMysliwiec("UFO", 100000);

        System.out.println(ufo);
    }
}
