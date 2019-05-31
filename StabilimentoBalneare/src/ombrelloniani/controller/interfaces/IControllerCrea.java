package ombrelloniani.controller.interfaces;

import ombrelloniani.controller.exceptions.ClienteNotFoundException;
import ombrelloniani.controller.exceptions.OmbrelloneNotFoundException;
import ombrelloniani.controller.exceptions.OmbrelloneOccupatoException;

public interface IControllerCrea {
	
	public void cercaCliente() throws ClienteNotFoundException;
	
	public void creaPrenotazione() throws OmbrelloneOccupatoException; 
	
	public void aggiungiOmbrellone() throws OmbrelloneNotFoundException;
	
	public void rimuoviOmbrellone() throws OmbrelloneNotFoundException;
}
