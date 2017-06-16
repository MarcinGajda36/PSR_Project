package com.PSR_Project;
import java.rmi.*;
import java.util.*;

public interface Client extends Remote {

    public void wiadomoscPowitalna(String nick, Vector<Client> lista) throws RemoteException;

    public void wiadomosc(String nick, String wiadomosc) throws RemoteException;

    public void wiadomoscKonczaca(String nick, Vector<Client> lista) throws RemoteException;

    public String pobierzNicka() throws RemoteException;

    public void ustawNicka(String nick) throws RemoteException;
}
