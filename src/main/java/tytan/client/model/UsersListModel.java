package tytan.client.model;

import java.util.HashSet;
import java.util.Set;

import client.beans.Message;

public class UsersListModel extends AbstractModel{
	private Set<String> usersList;
	public UsersListModel() {
		usersList = new HashSet<String>();
	}
	
	public void addNewUser(Message message) {
		String user = message.getNickFrom();
		usersList.add(user);
		this.firePropertyChange("addNewUserToJList", null, user);
	}
	
	public void removeUser(Message message) {
		String user = message.getNickFrom();
		usersList.remove(user);
		this.firePropertyChange("removeUserFromJList", null, user);
	}
	
	public void isUserAtTheList(String userName){                                   
		if (!usersList.contains(userName))
			this.firePropertyChange("", userName, null);
	}
}
