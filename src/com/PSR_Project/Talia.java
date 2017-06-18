package com.PSR_Project;

import com.PSR_Project.Karta.Kolor;
import com.PSR_Project.Karta.Ranga;

public class Talia {

    private Karta[] talia = new Karta[52];
//    private File[] pngTalia;

    public Talia () {
        przetasuj();
    }
    public final void przetasuj () {
        int i = 0;
        for (Kolor kolor : Kolor.values())
            for (Ranga ranga : Ranga.values())
                talia[i++] = new Karta(kolor,ranga);
    }
//    public final void przetasuj () {
//        File path = new File("PSR_Project\\KartyPNG");
//        pngTalia = path.listFiles();
//    }

    public Karta losujKarte() {
        Karta karta = null;
        while (karta == null) {
            int losowyIdx = (int) (Math.random()*talia.length);
            karta = talia[losowyIdx];
            talia[losowyIdx] = null;
        }
        return karta;
    }
//    public File losujKarte () {
//        File karta = null;
//        while (karta == null) {
//            int losowyIdx = (int) (Math.random()*pngTalia.length);
//            karta = pngTalia[losowyIdx];
//            pngTalia[losowyIdx] = null;
//        }
//        return karta;
//    }

}
