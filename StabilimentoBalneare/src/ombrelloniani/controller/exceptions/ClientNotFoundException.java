package ombrelloniani.controller.exceptions;

public class ClientNotFoundException extends Exception {

	private static final long serialVersionUID = -952961194318660238L;

	public ClientNotFoundException() {
		super();
	}

	public ClientNotFoundException(String message) {
		super("Non è stato trovato nessun cliente associato al documento: " + message);
	}
}
