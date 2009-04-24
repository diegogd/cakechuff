package cc.communications.rmi;

import java.rmi.*;


	public interface ServerInterface extends Remote{


		 public void registerClient(int code, ClientInterface object) throws RemoteException;

		 public void receiveReal(int code, Packet input)throws java.rmi.RemoteException;

			 	 

	}


