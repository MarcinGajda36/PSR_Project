package com.PSR_Project;

public class BlackJack {

    private Talia talia = new Talia();
    private Reka gracz, dealer;

    private boolean koniecGry = true;

    public BlackJack zacznijGre () {
        koniecGry = false;
//        message.setText("");
//
        talia.przetasuj();

        dealer.resetuj();
        gracz.resetuj();

        dealer.dobierzKarte(talia.dobierzKarte());
        dealer.dobierzKarte(talia.dobierzKarte());
        gracz.dobierzKarte(talia.dobierzKarte());
        gracz.dobierzKarte(talia.dobierzKarte());
        return this;
    }

}
