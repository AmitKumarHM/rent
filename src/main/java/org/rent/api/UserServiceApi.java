package org.rent.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.rent.base.BaseController;
import org.rent.model.AccessToken;
import org.rent.model.User;
import org.rent.service.Services;
import org.rent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
 
@Path("/user")
@Component
public class UserServiceApi extends BaseController{

	@Autowired
    private UserService	userService;
	
	@Autowired
    private Services<User> services;
	
	@GET
	@Path("/{id}")
	@Produces({"application/json"})
	public User getUser(@PathParam("id") int id) {
		return userService.getUser(id);		
	}
	
	@GET
	@Path("/all")
	@Produces({"application/json"})
	public List<User> getUserlist() {
		return userService.getUserList();
	}
	
	@POST
	@Path("/login")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public AccessToken login(User user) {
		User persistedUser=userService.getByEmailAndPwd(user);
		if(persistedUser!=null){
			return createAccsessToken(persistedUser);
		}
		return null;
	}
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Boolean update(@HeaderParam("Authorization") String authorization, @HeaderParam("userId") Integer userId ,User user) {
		AccessToken accessToken=new AccessToken();
		accessToken.setAccessToken(getAccsessToken(authorization));
		accessToken.setUser(new User(userId));
		accessToken=validateAccessToken(accessToken);
		if(accessToken!=null)
			return true;
		
		return false;
     }
	
	@PUT
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public AccessToken register(User user) {
		 User persistedUser=services.save(user);
		 if(persistedUser!=null){
				return createAccsessToken(persistedUser);
		}
		return null;
	}
	
	@DELETE
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON})
	public Boolean remove(@HeaderParam("Authorization") String authorization , @HeaderParam("userId") String userId ,@PathParam("id") int id) {
		return services.remove(id,User.class);
     }
	
}
