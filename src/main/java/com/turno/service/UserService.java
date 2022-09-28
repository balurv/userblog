package com.turno.service;

import java.io.IOException;

import org.apache.commons.io.IOUtils;


import com.turno.userblog.model.User;

public class UserService {

	
	public User updateUser(byte[] img, String name, boolean flag) {
		User user = new User();
		user.setName(name);
		user.setActive(true);
		user.setImage(img);
		return user;

	}
}
