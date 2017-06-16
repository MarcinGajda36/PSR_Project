package com.PSR_Project;

import com.PSR_Project.Karta.Kolor;
import com.PSR_Project.Karta.Ranga;

public class Talia {

    private Karta[] talia = new Karta[52];

    public Talia () {
        przetasuj();
    }
    public final void przetasuj () {
        int i = 0;
        for (Kolor kolor : Kolor.values())
            for (Ranga ranga : Ranga.values())
                talia[i++] = new Karta(kolor,ranga);
    }

    public Karta dobierzKarte () {
        Karta karta = null;
        while (karta == null) {
            int losowyIdx = (int) (Math.random()*talia.length);
            karta = talia[losowyIdx];
            talia[losowyIdx] = null;
        }
        return karta;
    }

}
