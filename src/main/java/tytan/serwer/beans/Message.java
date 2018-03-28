package tytan.serwer.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {

	String nickTo;
	String nickFrom;
	Object message;

	public Message(String nickTo, String nickFrom, Object message) {
		this.nickTo = nickTo;
		this.nickFrom = nickFrom;
		this.message = message;
	}

	public String getNickTo() {
		return nickTo;
	}

	public String getNickFrom() {
		return nickFrom;
	}

	public Object getMessage() {
		return message;
	}

	public void setNickTo(String nickTo) {
		this.nickTo = nickTo;
	}

	public void setNickFrom(String nickFrom) {
		this.nickFrom = nickFrom;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return nickFrom;
	}
}
