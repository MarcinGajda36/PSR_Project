package com.PSR_Project;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;

public class CzatImpl extends UnicastRemoteObject implements Czat {

    private Vector<Client> klienci = new Vector<>();
    private CzatServer serwer;

    public CzatImpl(CzatServer serwer) throws RemoteException {
        this.serwer = serwer;
    }

    public synchronized void dolacz(Client n) throws RemoteException {

        klienci.add(n);
        serwer.odswiezListe(klienci);

        serwer.wyswietlKomunikat("Do czatu dołączył/a: " + n.pobierzNicka());


        for (Iterator<Client> i = klienci.iterator(); i.hasNext();) {
            Client klient = i.next();
            klient.bierzacaPunktacja(n.pobierzNicka(), klienci);
        }
    }

//    public synchronized void wiadomosc(Client n, String s) throws RemoteException {
//
//        for (Iterator<Client> i = klienci.iterator(); i.hasNext();) {
//            Client klient = i.next();
//            klient.odswiezPunktacje(n.pobierzNicka(), s);
//        }
//    }

    public synchronized void opusc(Client n) throws RemoteException {

        klienci.remove(n);
        serwer.odswiezListe(klienci);

        serwer.wyswietlKomunikat("Czat opuścił/a: " + n.pobierzNicka());

        for (Iterator<Client> i = klienci.iterator(); i.hasNext();) {
            Client klient = (Client) i.next();
            klient.wiadomoscKonczaca(n.pobierzNicka(), klienci);
        }
    }
}
