package ombrelloniani.controller.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 6291410278931136902L;

	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String message) {
		super(message);
	}
}
