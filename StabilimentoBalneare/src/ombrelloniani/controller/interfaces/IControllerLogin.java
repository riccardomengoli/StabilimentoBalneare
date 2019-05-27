package ombrelloniani.controller.interfaces;

import ombrelloniani.controller.exceptions.UserNotFoundException;

public interface IControllerLogin {
	
	public void verificaCredenziali(String username,String password) throws IllegalArgumentException, UserNotFoundException;
	
}
