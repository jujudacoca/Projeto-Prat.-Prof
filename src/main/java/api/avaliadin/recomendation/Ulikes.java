package api.avaliadin.recomendation;
import java.util.List;
import java.util.Map;

import api.avaliadin.model.Avaliacao;
public class Ulikes {
	private int iduser;
	private Map<Integer,Float> lista;

	public int getIduser() {
		return iduser;
	}

	public void setIduser(int iduser) {
		this.iduser = iduser;
	}

	public Map<Integer,Float> getLista() {
		return lista;
	}

	public void setLista(Map<Integer,Float> lista) {
		this.lista = lista;
	}

	public Ulikes(int id, Map<Integer,Float> lista) {
		super();
		this.iduser = id;
		this.lista = lista;
	}
	
	public Ulikes() {
	}
	

}
