package ombrelloniani.controller.exceptions;

public class ClienteNotFoundException extends Exception {

	private static final long serialVersionUID = -952961194318660238L;

	public ClienteNotFoundException() {
		super();
	}

	public ClienteNotFoundException(String message) {
		super("Non è stato trovato nessun cliente associato al documento: " + message);
	}
}
