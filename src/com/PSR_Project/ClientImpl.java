package com.PSR_Project;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

public class ClientImpl extends UnicastRemoteObject implements Client {

    private static final long serialVersionUID = 98L;

    private CzatClient klient;
    private String nick;

    public ClientImpl(CzatClient klient, String nick) throws RemoteException {
        this.klient = klient;
        this.nick = nick;
    }

    public void wiadomoscPowitalna(String nick, Vector<Client> lista) throws RemoteException {
        klient.wyswietlKomunikat("Do czatu dołączył/a: " + nick);
        klient.odswiezListe(lista);
    }

    public void wiadomosc(String nick, String wiadomosc) throws RemoteException {
        klient.wyswietlKomunikat("<" + nick + ">" + wiadomosc);
    }

    public void wiadomoscKonczaca(String nick, Vector<Client> lista) throws RemoteException {
        klient.wyswietlKomunikat("Czat opuścił/a: " + nick);
        klient.odswiezListe(lista);
    }

    public String pobierzNicka() throws RemoteException {
        return nick;
    }

    public void ustawNicka(String nick) throws RemoteException {
        this.nick = nick;
    }
}

