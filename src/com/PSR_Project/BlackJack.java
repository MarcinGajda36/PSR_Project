package com.PSR_Project;

public class BlackJack {

    private CzatClient czatClient;
    private Talia talia = new Talia();
    private Reka gracz, dealer;
    private int nrParti = 0;

    private boolean graWToku = false;

    public BlackJack(CzatClient czatClient) {
        this.czatClient = czatClient;
        dealer = new Reka();
        gracz = new Reka();

//        gracz.pobierzPunkty().addListener((obs, old, newValue) -> {
//            if (newValue.intValue() >= 21) {
//                endGame();
//            }
//        });
//
//        dealer.pobierzPunkty().addListener((obs, old, newValue) -> {
//            if (newValue.intValue() >= 21) {
//                endGame();
//            }
//        });

    }
    public void zacznijGre () {
        graWToku = true;
        ++nrParti;
        czatClient.setClientHand("numer parti: " + nrParti + "\n");
        czatClient.setDealerHand("numer parti: " + nrParti + "\n");

        talia.przetasuj();

        dealer.resetuj();
        gracz.resetuj();

        kolejnaKartaGracza();
        kolejnaKartaGracza();

        kolejnaKartaDealera();
        kolejnaKartaDealera();

    }

    public void kolejnaKartaGracza () {
        Karta karta = talia.losujKarte();
        gracz.dobierzKarte(karta);
        czatClient.setClientHand(karta.toString()+"\n");
    }

    public void kolejnaKartaDealera () {
        Karta karta = talia.losujKarte();
        dealer.dobierzKarte(karta);
        czatClient.setDealerHand(karta.toString()+"\n");
    }
    public int punktyDealera () {
        return dealer.pobierzPunkty().get();
    }
    public void endGame() {
        graWToku = false;

        int punktyDealera = dealer.pobierzPunkty().get();
        int punktyGracza = gracz.pobierzPunkty().get();
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

        czatClient.setDealerHand("\n\n");
        czatClient.setClientHand("\n\n");
    }
    public boolean graWToku() {
        return graWToku;
    }


}
