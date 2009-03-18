package cc.communications;
import java.rmi.*;

public interface ClientInterface extends java.rmi.Remote{

	
	public void receiveReal(int code,Packet input)throws java.rmi.RemoteException;

	public void pingClient() throws java.lang.Exception;



}


