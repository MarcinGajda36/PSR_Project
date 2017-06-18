package com.PSR_Project;

import java.io.File;

public class BlackJack {

    private GraClient graClient;
    private Talia talia = new Talia();
    private Reka gracz, dealer;
    private int nrParti = 0;

    private final File PATH = new File("KartyPNG\\");
    private File[] pngTalia;

    private boolean graWToku = false;

    public BlackJack(GraClient graClient) {
        this.graClient = graClient;
        dealer = new Reka();
        gracz = new Reka();

        pngTalia = PATH.listFiles();

        gracz.pobierzPunkty().addListener((obs, old, newValue) -> {
            graClient.setPunktacjaClientaLabel("Punktacja Klienta: " + newValue);
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

        dealer.pobierzPunkty().addListener((obs, old, newValue) -> {
            graClient.setPunktacjaDealeraLabel("Punktacja dealera: " + newValue);
            if (newValue.intValue() >= 21) {
                endGame();
            }
        });

    }
    public synchronized void zacznijGre () {
        graClient.noweRozdanie();

        graWToku = true;
        ++nrParti;
        graClient.setClientHand("numer parti: " + nrParti + "\n");
        graClient.setDealerHand("numer parti: " + nrParti + "\n");

        talia.przetasuj();

        dealer.resetuj();
        gracz.resetuj();

        graClient.wlaczSterowanie();

        kolejnaKartaGracza();
        kolejnaKartaDealera();

        kolejnaKartaGracza();
        kolejnaKartaDealera();

    }

    public void kolejnaKartaGracza () {
        Karta karta = talia.losujKarte();
        graClient.setClientHand(karta.toString()+"\n");
        gracz.dobierzKarte(karta);

        for (int i = 0; i < pngTalia.length; i++)
            if (pngTalia[i].toString().contains(karta.toString())) {
                graClient.dodajKarteGracz(pngTalia[i]);
            }
    }
    public void kolejnaKartaDealera () {
        Karta karta = talia.losujKarte();
        graClient.setDealerHand(karta.toString()+"\n");
        dealer.dobierzKarte(karta);

        for (int i = 0; i < pngTalia.length; i++)
            if (pngTalia[i].toString().contains(karta.toString())) {
                graClient.dodajKarteDealera(pngTalia[i]);
            }
    }
    public int punktyDealera () {
        return dealer.pobierzPunkty().get();
    }
    public void endGame() {
        if (!graWToku())
            return;

            graWToku = false;

            int punktyDealera = dealer.pobierzPunkty().get();
            int punktyGracza = gracz.pobierzPunkty().get();
            String zwyciezca = String.format("Cos poszlo nie tak punkty dealera: %d , Gracza: %d", punktyDealera, punktyGracza);

            if (punktyDealera == 21 || punktyGracza > 21 || punktyDealera == punktyGracza
                    || (punktyDealera < 21 && punktyDealera > punktyGracza)) {
                zwyciezca = "Kasyno";
            }
            else if (punktyGracza == 21 || punktyDealera > 21 || punktyGracza > punktyDealera) {
                zwyciezca = "Gracz";
                graClient.wygranaPartia();
            }

            graClient.setDealerHand("koniec rundy: "+ zwyciezca + " wygrywa!" + "\n");
            graClient.setClientHand("koniec rundy: "+ zwyciezca + " wygrywa!" + "\n");

            graClient.wylaczSterowanie();

    }

    public boolean graWToku() {
        return graWToku;
    }
}
