package cc.communications.rmi;

public class Mailbox {
	
	private Packet input;

	private Packet output;

	

	public Mailbox(int option){

		input= new Packet(option);

		output= new Packet(option);

	}



	public Packet getInput() {

		return input;

	}



	public void setInput(Packet in) {

		input = in;

	}



	public Packet getOutput() {

		return output;

	}



	public void setOutput(Packet out) {

		output = out;

	}

	

	public String toString() {

		return ("Input MailBox "+ input.toString()+"\n"+"Output MailBox "+ output.toString());	

	}

}
