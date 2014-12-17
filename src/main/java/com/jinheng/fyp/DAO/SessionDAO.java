package com.jinheng.fyp.DAO;

import com.jinheng.fyp.bean.ParkingUserSession;

/**
 * Session DAO Class
 * 
 * @author original author
 * @author chongjinheng
 */
public interface SessionDAO {

	public void updateSession(String sessionKey, String username);

	public ParkingUserSession checkSessionValidity(String sessionKey, String username);

}
