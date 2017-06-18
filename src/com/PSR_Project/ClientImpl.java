package com.PSR_Project;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class ClientImpl extends UnicastRemoteObject implements Client {

    private GraClient klient;
    private String nick;
    private Gra server;

    private int punktacja = 0;

    private Vector<Client> list;

    public ClientImpl(Gra server , GraClient klient, String nick, int punktacja) throws RemoteException {
        this.server = server;
        this.klient = klient;
        this.nick = nick;
        this.punktacja = punktacja;
    }

    public void bierzacaPunktacja(String nick, Vector<Client> lista) throws RemoteException {
        klient.odswiezListe(lista);
        list = lista;
    }
    public void odswiezPunktacje(Vector<Client> list) throws RemoteException {
        server.odswiezPunktacje(list);
    }
    public void odswiezListe (Vector<Client> list) throws RemoteException {
        klient.odswiezListe(list);
    }

    public String pobierzNicka() throws RemoteException {
        return nick + "(" + punktacja + ")";
    }

    public void dodajZwycieztwo(int z) throws RemoteException {
        punktacja = z;
        odswiezPunktacje(list);
        server.wiadomosc(pobierzNicka(), "Wygral rozdanie");
    }
}

