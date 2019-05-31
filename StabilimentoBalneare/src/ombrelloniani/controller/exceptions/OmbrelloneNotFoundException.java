package ombrelloniani.controller.exceptions;

public class OmbrelloneNotFoundException extends Exception {

	private static final long serialVersionUID = -952961194318660238L;

	public OmbrelloneNotFoundException() {
		super();
	}

	public OmbrelloneNotFoundException(String message) {
		super("Non è stato trovato nessun ombrellone con ID: " + message);
	}
}
