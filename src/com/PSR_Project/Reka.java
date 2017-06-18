package com.PSR_Project;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class Reka {

    private List<Karta> karty = new ArrayList<>();
    private SimpleIntegerProperty punkty = new SimpleIntegerProperty(0);
    private int asy = 0;

    public Reka () {
        karty.clear();
        asy = 0;
        punkty.set(0);
    }
    public void dobierzKarte (Karta karta) {
        karty.add(karta);

        if (karta.ranga == Karta.Ranga.As) {
            asy++;
        }

        if (punkty.get() + karta.wartosc > 21 && asy > 0) {
            punkty.set(punkty.get() + karta.wartosc - 10);
            asy--;
        }
        else {
            punkty.set(punkty.get() + karta.wartosc);
        }
    }
    public void resetuj () {
        karty.clear();
        asy = 0;
        punkty.set(0);
    }
    public SimpleIntegerProperty  pobierzPunkty () {
        return punkty;
    }
    public int liczbaKart () {
        return karty.size();
    }

}
