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

    @Override
    public synchronized void wiadomosc(String nick, String w) throws RemoteException {
        serwer.wyswietlKomunikat("Klient: " + nick + " " + w);
    }

    public synchronized void dolacz(String command,Client n) throws RemoteException {
        if (!command.equals(Czat.DOLACZ_COMMAND)){
            serwer.wyswietlKomunikat("Proba polaczenia z blednym protokolem! " + n.pobierzNicka());
            return;
        }
        klienci.add(n);
        serwer.odswiezListe(klienci);
        serwer.wyswietlKomunikat("Do czatu dołączył/a: " + n.pobierzNicka());

        for (Iterator<Client> i = klienci.iterator(); i.hasNext();) {
            Client klient = i.next();
            klient.bierzacaPunktacja(n.pobierzNicka(), klienci);
        }
    }
    public synchronized void odswiezPunktacje(Vector<Client> list) throws RemoteException {
        serwer.odswiezListe(list);
        for (Iterator<Client> i = list.iterator(); i.hasNext();) {
            Client klient = i.next();
            klient.odswiezListe(list);
        }
    }

    public synchronized void opusc(Client n) throws RemoteException {

        klienci.remove(n);
        serwer.odswiezListe(klienci);

        serwer.wyswietlKomunikat("Czat opuścił/a: " + n.pobierzNicka());
    }
}
