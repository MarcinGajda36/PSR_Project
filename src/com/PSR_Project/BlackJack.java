package com.PSR_Project;

import java.io.File;

public class BlackJack {

    private CzatClient czatClient;
    private Talia talia = new Talia();
    private Reka gracz, dealer;
    private int nrParti = 0;

    private final File PATH = new File("KartyPNG\\");
    private File[] pngTalia;

    private boolean graWToku = false;

    public BlackJack(CzatClient czatClient) {
        this.czatClient = czatClient;
        dealer = new Reka();
        gracz = new Reka();

        pngTalia = PATH.listFiles();

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
        Karta karta = talia.losujKarte();
        czatClient.setClientHand(karta.toString()+"\n");
        gracz.dobierzKarte(karta);

        for (int i = 0; i < pngTalia.length; i++)
            if (pngTalia[i].toString().contains(karta.toString())) {
                czatClient.dodajKarteGracz(pngTalia[i]);
            }
    }
    public void kolejnaKartaDealera () {
        Karta karta = talia.losujKarte();
        czatClient.setDealerHand(karta.toString()+"\n");
        dealer.dobierzKarte(karta);

        for (int i = 0; i < pngTalia.length; i++)
            if (pngTalia[i].toString().contains(karta.toString())) {
                czatClient.dodajKarteDealera(pngTalia[i]);
            }
    }
    public int punktyDealera () {
        return dealer.pobierzPunkty().get();
    }
    public void endGame() {
        if (graWToku && dealer.liczbaKart() > 1) {
            graWToku = false;

            int punktyDealera = dealer.pobierzPunkty().get();
            int punktyGracza = gracz.pobierzPunkty().get();
            String zwyciezca = String.format("Cos poszlo nie tak punkty dealera: %d , Gracza: %d", punktyDealera, punktyGracza);

            if (punktyDealera == 21 || punktyGracza > 21 || punktyDealera == punktyGracza
                    || (punktyDealera < 21 && punktyDealera > punktyGracza)) {
                zwyciezca = "Kasyno";
            }
            else if (punktyGracza == 21 || punktyDealera > 21 || punktyGracza > punktyDealera) {
                zwyciezca = "Gra";
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
