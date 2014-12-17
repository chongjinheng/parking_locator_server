package com.jinheng.fyp.DAO;

import com.jinheng.fyp.bean.ForgotPasswordSession;

/**
 * @author chengyang
 */

public interface ForgotPasswordSessionDAO {

	public Long createForgetPasswordSession(String email, String hashedTempPassword);

	public ForgotPasswordSession getForgetPasswordSessionByID(Long ID);

	public void updateForgetPasswordSession(String hashedTempPassword, String userEmail);

	public void deleteForgetPasswordSession(String userEmail);
}
