package com.mypojo;

import lombok.Getter;
import lombok.Setter;

/**
 * UserData class to define a POJO payload for User details.
 */
@Getter
@Setter
public class UserPojo {

    private String name;
    private String jobTitle;
    
    public UserPojo Userlog(UserPojo name ) {
    	
    	return name;
    }

	public UserPojo Userlog(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
