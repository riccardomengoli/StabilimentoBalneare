package ombrelloniani.controller.interfaces;

import ombrelloniani.controller.exceptions.ClientNotFoundException;
import ombrelloniani.controller.exceptions.OmbrelloneNotFoundException;
import ombrelloniani.controller.exceptions.OmbrelloneOccupatoException;

public interface IControllerCrea {
	
	public void cercaCliente() throws ClientNotFoundException;
	
	public void creaPrenotazione() throws OmbrelloneOccupatoException; 
	
	public void aggiungiOmbrellone() throws OmbrelloneNotFoundException;
	
	public void rimuoviOmbrellone() throws OmbrelloneNotFoundException;
}
