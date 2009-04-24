package cc.communications.rmi;

public interface CommunicationInterface {

	public abstract Mailbox getMailbox(int code);

	public abstract boolean getSent(int code);

	public abstract boolean getReceived(int code);

	public abstract void receive(int code);

	public abstract void send(int code,Packet output);

	public abstract void sendReal(int code);

}
