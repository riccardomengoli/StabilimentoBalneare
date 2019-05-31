package ombrelloniani.controller.exceptions;

public class OmbrelloneOccupatoException extends Exception {

	private static final long serialVersionUID = 6291410278931136902L;

	public OmbrelloneOccupatoException() {
		super();
	}

	public OmbrelloneOccupatoException(String message) {
		super("L'ombrellone (ID = " + message + ") non è disponibile nei giorni selezionati");
	}
}
