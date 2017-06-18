package com.PSR_Project;

public class Karta {

    enum Kolor {
        Kier, Pik, Karo, Trefl
    };

    enum Ranga {
        Dwojka(2), Trojka(3), Czworka(4), Piatka(5), Szostka(6), Siodemka(7), Osemka(8), Dziewiatka(9), Dziesiatka(10),
        Walet(10), Dama(10), Krol(10), As(11);

        final int wartosc;
        Ranga (int value) {
            this.wartosc = value;
        }
    };

    public final Kolor kolor;
    public final Ranga ranga;
    public final int wartosc;

    public Karta (Kolor kolor, Ranga ranga) {
        this.kolor = kolor;
        this.ranga = ranga;
        this.wartosc = ranga.wartosc;
    }
    @Override
    public String toString() {
        return ranga.toString() + " " + kolor.toString();
    }

}
