package com.java.mystuff.mavenExample;

import java.util.List; 
import javax.ws.rs.GET; 
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("/UserService") 
public class UserService {  
	UserDao userDao = new UserDao();  
	@GET 
	@Path("/users")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}) 
	public Response getUsers(@Context UriInfo uriInfo){
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(userDao.getAllUsers()){};
		String format = uriInfo.getQueryParameters().getFirst("format");
		if (format == null){
			System.out.println("format is null");
		}
		return Response.ok(entity, format==null? MediaType.APPLICATION_XML : MediaType.APPLICATION_JSON)
				.build();
	}
}