package com.java.mystuff.mavenExample;

import java.util.ArrayList; 
import java.util.List;  

public class UserDao { 
   public List<User> getAllUsers(){ 
      
		List<User> userList = null; 
		User user = new User(1, "Name01", "Teacher01"); 
		User user2 = new User(2, null, "Teacher02"); 
		userList = new ArrayList<User>(); 
		userList.add(user); 
		userList.add(user2); 
		return userList; 
   } 
}