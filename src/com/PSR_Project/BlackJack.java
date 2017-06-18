package com.PSR_Project;

import java.io.File;

public class BlackJack {

    private CzatClient czatClient;
    private Talia talia = new Talia();
    private Reka gracz, dealer;
    private int nrParti = 0;

    private int koordynatyKartyX;
    private int koordynatyKartyY;

    File path;
    private File[] pngTalia;

    private boolean graWToku = false;

    public BlackJack(CzatClient czatClient) {
        this.czatClient = czatClient;
        dealer = new Reka();
        gracz = new Reka();

        koordynatyKartyX = 10;
        koordynatyKartyY = 400;

        path = new File("KartyPNG\\");
        pngTalia = path.listFiles();

        gracz.pobierzPunkty().addListener((obs, old, newValue) -> {
            czatClient.setPunktacjaClientaLabel("Punktacja Klienta: " + newValue);
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

        dealer.pobierzPunkty().addListener((obs, old, newValue) -> {
            czatClient.setPunktacjaDealeraLabel("Punktacja dealera: " + newValue);
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

    }
    public synchronized void zacznijGre () {
        czatClient.noweRozdanie();
        koordynatyKartyX = 10;

        graWToku = true;
        ++nrParti;
        czatClient.setClientHand("numer parti: " + nrParti + "\n");
        czatClient.setDealerHand("numer parti: " + nrParti + "\n");

        talia.przetasuj();

        dealer.resetuj();
        gracz.resetuj();

        czatClient.wlaczSterowanie();

        kolejnaKartaGracza();
        kolejnaKartaDealera();

        kolejnaKartaGracza();
        kolejnaKartaDealera();

    }

    public void kolejnaKartaGracza () {
        System.out.println("kolejnaKartaGracza");
        Karta karta = talia.losujKarte();
        czatClient.setClientHand(karta.toString()+"\n");
        gracz.dobierzKarte(karta);

        for (int i = 0; i < pngTalia.length; i++)
            if (pngTalia[i].toString().contains(karta.toString())) {
                czatClient.dodajKarte(pngTalia[i], koordynatyKartyX, koordynatyKartyY);
                koordynatyKartyX += 50;
            }

        koordynatyKartyX += 75;
    }

    public void kolejnaKartaDealera () {
        System.out.println("kolejnaKartaDealera");
        Karta karta = talia.losujKarte();
        czatClient.setDealerHand(karta.toString()+"\n");
        dealer.dobierzKarte(karta);
    }
    public int punktyDealera () {
        return dealer.pobierzPunkty().get();
    }
    public void endGame() {
        if (graWToku && dealer.liczbaKart() > 1) {
            graWToku = false;

            int punktyDealera = dealer.pobierzPunkty().get();
            System.out.println("punktyDealera: " + punktyDealera);
            int punktyGracza = gracz.pobierzPunkty().get();
            System.out.println("punktyGracza: " + punktyGracza);
            String zwyciezca = String.format("Cos poszlo nie tak punkty dealera: %d , Gracza: %d", punktyDealera, punktyGracza);

            // the order of checking is important
            if (punktyDealera == 21 || punktyGracza > 21 || punktyDealera == punktyGracza
                    || (punktyDealera < 21 && punktyDealera > punktyGracza)) {
                zwyciezca = "Kasyno";
            }
            else if (punktyGracza == 21 || punktyDealera > 21 || punktyGracza > punktyDealera) {
                zwyciezca = "Gracz";
                czatClient.wygranaPartia();
            }

            czatClient.setDealerHand("koniec rundy: "+ zwyciezca + " wygrywa!" + "\n");
            czatClient.setClientHand("koniec rundy: "+ zwyciezca + " wygrywa!" + "\n");

            czatClient.wylaczSterowanie();
        }
    }

    public boolean graWToku() {
        return graWToku;
    }
}
