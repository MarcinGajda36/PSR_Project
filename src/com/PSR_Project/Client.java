package com.PSR_Project;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Client extends Remote {

    void bierzacaPunktacja(String nick, Vector<Client> lista) throws RemoteException;

    void odswiezPunktacje(Vector<Client> list) throws RemoteException;

    void dodajZwycieztwo (int z) throws RemoteException;

    void odswiezListe (Vector<Client> list) throws RemoteException;

    String pobierzNicka() throws RemoteException;

}
