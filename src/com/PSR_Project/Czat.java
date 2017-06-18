package com.PSR_Project;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface Czat extends Remote {

    void dolacz(Client k) throws RemoteException;

    void odswiezPunktacje(Vector<Client> list) throws RemoteException;

    void wiadomosc(String nick, String w) throws RemoteException;

    void opusc(Client k) throws RemoteException;

}
