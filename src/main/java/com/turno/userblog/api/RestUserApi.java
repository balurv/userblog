package com.turno.userblog.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.turno.dao.UserDao;
import com.turno.service.UserService;
import com.turno.userblog.kafka.MessageProducer;
import com.turno.userblog.model.User;
import com.turno.userblog.model.UserDetails;

@RestController
@RequestMapping("/user")
public class RestUserApi {
	@Autowired
	UserDao userDao;
	
	@PostMapping(path = "/blog1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> userBlog1(@RequestParam("name") String name,
			@RequestParam(name = "img", required = false) MultipartFile img) {
		
		UserService us = new UserService();
		byte[] bytes = new byte[0];
		if (img != null) {
			try {
				bytes = IOUtils.toByteArray(img.getInputStream());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		User user = us.updateUser(bytes, name, true);
		MessageProducer mp = MessageProducer.getInstance();
		String userStr = new Gson().toJson(user);// object to string
		mp.addMessage("blog", userStr);

		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@PostMapping(path = "/blog", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> userBlog(@RequestParam("name") String name,
			@RequestParam(name = "img", required = false) MultipartFile img) {
		User user = new User();
		if (img != null) {
			try {
				byte[] bytes = IOUtils.toByteArray(img.getInputStream());
				user.setImage(bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		user.setName(name);
		user.setActive(true);
		MessageProducer mp = MessageProducer.getInstance();
		String userStr = new Gson().toJson(user);// object to string
		mp.addMessage("user", userStr);

		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@PostMapping(path = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveUser(@RequestParam("name") String name,
			@RequestParam(name = "img", required = false) MultipartFile img) {
		User user = new User();
		if (img != null) {
			try {
				byte[] bytes = IOUtils.toByteArray(img.getInputStream());
				user.setImage(bytes);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		user.setName(name);
		user.setActive(true);
		userDao.saveOrUpdate(user);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@GetMapping(path = "/all")
	public ResponseEntity<List<UserDetails>> findAllActive() {		
		List<User> userList = userDao.findAllActive();
		List<UserDetails> userDetailsList = new ArrayList<>();
		for(User user: userList) {
			userDetailsList.add(user.getUserDetail());
		}
		return new ResponseEntity<List<UserDetails>>(userDetailsList, HttpStatus.OK);
	}

	@GetMapping(path = "/allIncludingDeleted")
	public ResponseEntity<List<UserDetails>> findAllIncludingSoftDeleted() {
		List<User> userList = userDao.findAllIncludingSoftDeleted();
		List<UserDetails> userDetailsList = new ArrayList<>();
		for(User user: userList) {
			userDetailsList.add(user.getUserDetail());
		}
		return new ResponseEntity<List<UserDetails>>(userDetailsList, HttpStatus.OK);
	}

	@GetMapping(path = "/byId/{id}")
	public ResponseEntity<UserDetails> findById(@PathVariable(value = "id") String id) {
		User user = userDao.findById(id);
		return new ResponseEntity<UserDetails>(user.getUserDetail(), HttpStatus.OK);
	}
	
	@PostMapping(path = "/soft/delete/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> softDeleteUser(@PathVariable(value = "id") String id) {
		User user = userDao.findById(id);
		user.setActive(false);
		userDao.saveOrUpdate(user);
		return new ResponseEntity<String>("Soft Deleted successfully "+user.getName(), HttpStatus.OK);
	}

	@PostMapping(path = "/hard/delete/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> hardDeleteUser(@PathVariable(value = "id") String id) {
		User user = userDao.findById(id);
		userDao.delete(user);
		return new ResponseEntity<String>("Hard Deleted successfully "+user.getName(), HttpStatus.OK);
	}

}
