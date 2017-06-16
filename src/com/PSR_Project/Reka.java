package com.PSR_Project;

import java.util.List;

public class Reka {

    private List<Karta> karty;
    private int punkty = 0;
    private int asy = 0;

    public Reka (List<Karta> karty) {
        this.karty = karty;
    }
    public void dobierzKarte (Karta karta) {
        karty.add(karta);

        if (karta.ranga == Karta.Ranga.As) {
            ++asy;
        }
        if (punkty + karta.wartosc > 21 && asy > 0) {
            punkty -= 10; //As w zasadach blackJacka moze byc 11 lub 1
            --asy;
        } else {
            punkty += karta.wartosc;
        }
    }
    public void resetuj () {
        karty.clear();
        asy = 0;
        punkty = 0;
    }
    public int pobierzPunkty () {
        return punkty;
    }

}
