package com.PSR_Project;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Czat extends Remote {

    public void dolacz(Client k) throws RemoteException;

//    public void wiadomosc(Client k, String s) throws RemoteException;

    public void opusc(Client k) throws RemoteException;

}
