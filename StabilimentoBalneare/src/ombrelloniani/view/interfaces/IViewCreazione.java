package ombrelloniani.view.interfaces;

import java.time.LocalDate;

public interface IViewCreazione {
	
	public String getIdDocumento();
	public String getNome();
	public String getCognome();
	public String getTelefono();
	public String getEmail();
	public LocalDate getDataInizio();
	public LocalDate getDataFine();
	public int getNumeroLettini();
	public String getInputOmbrellone();
	public String getOmbrelloneSelezionato();
	public void setNome(String nome);
	public void setCognome(String cognome);
	public void setTelefono(String telefono);
	public void setEmail(String email);
	public void addOmbrelloneToList(String idOmbrellone);
	public void removeOmbrelloneFromList(String idOmbrellone);
	public void showError(String titolo, String descrizione);
	public void confermaCreazione(int idPrenotazioneCreata);
}
