package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_BattleInterface extends Remote {
    void attackMonster() throws RemoteException;
    void flee() throws RemoteException;
    void getPlayerHP() throws RemoteException;
    boolean isGameOver() throws RemoteException;
    void resetGame() throws RemoteException;
}