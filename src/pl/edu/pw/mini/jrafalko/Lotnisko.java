package pl.edu.pw.mini.jrafalko;

import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

public class Lotnisko {

    private List<Samolot> samoloty;
    static Random rand = new Random();

    //todo interfejs funkcyjny do generacji nazwy + lambda (1.0p)
    @FunctionalInterface
    interface GenerujNazweSamolotu {
        String generuj();
    }
    public Lotnisko(int iloscSamolotow) {
        this.samoloty = new ArrayList<>();
        GenerujNazweSamolotu generator = () -> {
            int dlugosc = rand.nextInt(20) + 1;
            StringBuilder nazwa = new StringBuilder();
            for (int i = 0; i < dlugosc; i++) {
                nazwa.append((char) rand.nextInt(26) + 97);
            }
            return nazwa.toString();
        };
        for (int i = 0; i < iloscSamolotow; i++) {
            String nazwa = generator.generuj();
            int r = rand.nextInt(3);
            switch (r) {
                case 0:
                    //String nazwa = //todo (0.5p)
                    int predkoscMax = 500 + rand.nextInt(500);
                    int maxIloscPasazerow = 100 + rand.nextInt(200);
                    samoloty.add(new SamolotPasazerski(nazwa, predkoscMax, maxIloscPasazerow));
                    break;
                case 1:
                    //nazwa = //todo
                    predkoscMax = 300 + rand.nextInt(400);
                    int maxZaladunek = 10 + rand.nextInt(90);
                    samoloty.add(new SamolotTowarowy(nazwa, predkoscMax, maxZaladunek));
                    break;
                case 2:
                    //nazwa = //todo
                    predkoscMax = 900 + rand.nextInt(2100);
                    samoloty.add(new Mysliwiec(nazwa, predkoscMax));
                    break;
            }
        }
    }//end konstruktor Lotnisko

    public void wypiszSamoloty() {
        for (int i = 0; i < this.samoloty.size(); i++) {
            System.out.println(this.samoloty.get(i));
        }
    }

    public void startSamolotow() {
        for (int i = 0; i < this.samoloty.size(); i++) {
            samoloty.get(i).lec(1 + rand.nextInt(24));
        }
    }

    public void odprawaSamolotow() {
        for (int i = 0; i < this.samoloty.size(); i++) {
            if (samoloty.get(i) instanceof SamolotPasazerski) {
                try {
                    samoloty.get(i).odprawa(rand.nextInt(400));
                } catch (WyjatekLotniczy wyjatekLotniczy) {
                    System.out.println(wyjatekLotniczy.getMessage());
                }
            } else if (samoloty.get(i) instanceof SamolotTowarowy) {
                try {
                    samoloty.get(i).odprawa(rand.nextInt(200));
                } catch (WyjatekLotniczy wyjatekLotniczy) {
                    System.out.println(wyjatekLotniczy.getMessage());
                }
            } else {
                samoloty.get(i).odprawa(rand.nextInt(10));
            }
        }
    }

    public void dzialaniaLotniskowe() {
        //todo 5 konsumerów + wywołanie (3.0p)
        Consumer<Samolot> wyswietl = samolot -> System.out.println(samolot);
        Consumer<Samolot> laduj = Samolot::laduj;
        Consumer<Samolot> odprawa = samolot -> {
            int ile = rand.nextInt(400);
            samolot.odprawa(ile);
        };
        Consumer<Samolot> lec = samolot -> samolot.lec(10);
        Consumer<Samolot> atak = samolot -> {
            if (samolot instanceof Mysliwiec){
               ((Mysliwiec) samolot).atak();
            }
        };
        samoloty.forEach(wyswietl);
        samoloty.forEach(laduj);
        samoloty.forEach(odprawa);
        samoloty.forEach(lec);
        samoloty.forEach(atak);

    }

    public void sortowanieSamolotow() {
        //todo 2 komparatory + wywołanie (1.0p)
        Comparator<Samolot> komparator_po_predkosci = Comparator.comparingInt(samolot -> samolot.predkoscMax);
        Comparator<Samolot> komparator_po_nazwie = Comparator.comparing(samolot -> samolot.nazwa, (nazwa1, nazwa2) -> {
            if(nazwa1.length() > 5 && nazwa2.length() > 5){
                return nazwa1.compareTo(nazwa2);
            }return 0;
        });
        samoloty.sort(komparator_po_predkosci);
        samoloty.sort(komparator_po_nazwie);
    }


    public void sortowanieLosowe() {
        //todo interfejs funkcyjny + lambda + wyołanie (1.5p)
        @FunctionalInterface
        interface GeneratorLosowy {
            Comparator<Samolot> generujKomparator();
        }

        GeneratorLosowy generator = () -> {
            if(rand.nextBoolean()){
                return Comparator.comparingInt(samolot -> samolot.predkoscMax);
            }else{
                return Comparator.comparing(samolot -> samolot.nazwa, (nazwa1, nazwa2) -> {
                    if(nazwa1.length() > 5 && nazwa2.length() > 5){
                        return nazwa1.compareTo(nazwa2);
                    }return 0;
                });
            }
        };
        samoloty.sort(generator.generujKomparator());

    }
//    To jest potrzebne do maina
    public Mysliwiec stworzPrywatnyMysliwiec(String nazwa, int predkosc){
        return new Mysliwiec(nazwa, predkosc){
            @Override
        public String toString() {
            return "To nie obcy, to nowa technologia z przyszłosci";
        }};
    }

    abstract class Samolot {
        protected String nazwa;
        protected int predkoscMax;
        protected int iloscGodzinWPowietrzu;
        protected boolean wPowietrzu;
        protected boolean poOdprawie;

        public Samolot(String nazwa, int predkoscMax) {
            this.nazwa = nazwa;
            this.predkoscMax = predkoscMax;
            wPowietrzu = false;
            poOdprawie = false;
            iloscGodzinWPowietrzu = 0;
        }

        public void lec(int godziny) {
            if (poOdprawie) {
                iloscGodzinWPowietrzu += godziny;
                if (!wPowietrzu) {
                    wPowietrzu = true;
                    System.out.println("Startujemy...");
                } else {
                    System.out.println("Lecimy...");
                }
            } else {
                System.out.println("Nie możemy wystartować");
            }
        }

        public void laduj() {
            if (wPowietrzu) {
                wPowietrzu = false;
                poOdprawie = false;
                System.out.println("Lądujemy...");
            } else {
                System.out.println("I tak jesteśmy na ziemi");
            }
        }

        public abstract void odprawa(int iloscZaladuku) throws WyjatekLotniczy;
    }//end Samolot

    private class SamolotPasazerski extends Samolot {
        //todo (1.0)
        protected int maxIloscPasazerow;
        protected int iloscPasazerow;

        public SamolotPasazerski(String nazwa, int predkoscMax, int max_liczba_pasazerow) {
            super(nazwa, predkoscMax);
            this.maxIloscPasazerow = max_liczba_pasazerow;
            iloscPasazerow = 0;
        }

        @Override
        public void odprawa(int iloscZaladuku) throws WyjatekLotniczy {
            if(iloscZaladuku + iloscPasazerow < maxIloscPasazerow/2){
                throw new WyjatekEkonomiczny("Za mało pasażerów, nie opłaca się lecieć");
            }else if(iloscZaladuku + iloscPasazerow > maxIloscPasazerow){
                iloscPasazerow = maxIloscPasazerow;
                throw new WyjatekPrzeladowania("pasazerow",iloscPasazerow + iloscZaladuku - maxIloscPasazerow);
            }else{
                poOdprawie = true;
                iloscPasazerow += iloscZaladuku;
            }
        }

        @Override
        public String toString() {
            return "Samolot pasażerski " +
                    "o nazwie '" + nazwa + '\'' +
                    ". Predkość maksymalna " + predkoscMax +
                    ", w powietrzu spędził łącznie " + iloscGodzinWPowietrzu + " godzin" +
                    ", moze zabrac na pokład " + maxIloscPasazerow + " pasażerów. " +
                    (wPowietrzu ? "Obecnie leci z " + iloscPasazerow + " pasażerami na pokładzie." :
                            "Aktualnie uziemiony");
        }
    }//end SamolotPasazerski

    private class SamolotTowarowy extends Samolot {
        //todo (0.5p)
        protected int maxZaladunek;
        protected int ladunek;

        public SamolotTowarowy(String nazwa, int predkoscMax, int maxZaladunek) {
            super(nazwa, predkoscMax);
            this.maxZaladunek = maxZaladunek;
            ladunek = 0;
        }

        @Override
        public void odprawa(int iloscZaladuku) throws WyjatekLotniczy {
            if(ladunek + iloscZaladuku < maxZaladunek/2){
                throw new WyjatekEkonomiczny("Zbyt maly ladunek, nie oplaca sie leciec");
            }else if(ladunek + iloscZaladuku > maxZaladunek){
                ladunek = maxZaladunek;
                throw new WyjatekPrzeladowania("ton ladunku", ladunek + iloscZaladuku - maxZaladunek);
            }else{
                poOdprawie = true;
                ladunek += iloscZaladuku;
            }
        }


        @Override
        public String toString() {
            return "Samolot towarowy " +
                    "o nazwie '" + nazwa + '\'' +
                    ". Predkość maksymalna " + predkoscMax +
                    ", w powietrzu spędził łącznie " + iloscGodzinWPowietrzu + " godzin" +
                    ", moze zabrac na pokład " + maxZaladunek + " ton ładunku. " +
                    (wPowietrzu ? "Obecnie leci z " + ladunek + " t. ładunku." :
                            "Aktualnie uziemiony");
        }
    }//end SamolotTowarowy

    private class Mysliwiec extends Samolot {
        //todo (0.5p)
        int iloscRakiet;

        public Mysliwiec(String nazwa, int predkoscMax) {
            super(nazwa, predkoscMax);
            iloscRakiet = 0;
        }

        @Override
        public void odprawa(int iloscZaladuku) throws WyjatekLotniczy {
            iloscRakiet += iloscZaladuku;
            poOdprawie = true;
        }
        public void atak(){
            if(wPowietrzu){
                if(iloscRakiet > 0){
                    System.out.println("Ataaaak");
                    iloscRakiet -= 1;
                }
                if (iloscRakiet == 0){
                    laduj();
                }
            }
        }

        @Override
        public String toString() {
            return "Myśliwiec " +
                    "o nazwie '" + nazwa + '\'' +
                    ". Predkość maksymalna " + predkoscMax +
                    ", w powietrzu spędził łącznie " + iloscGodzinWPowietrzu + " godzin. " +
                    (wPowietrzu ? "Obecnie leci, rakiet: " + iloscRakiet + "." :
                            "Aktualnie uziemiony.");
        }
    }//end Mysliwiec

}//end Lotnisko