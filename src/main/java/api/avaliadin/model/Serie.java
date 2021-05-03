package api.avaliadin.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("S")
public class Serie extends Item{
	@Column(name="diretor")
	private String diretor;
	
	@Column(name="elenco")
	private String elenco;
	
	@Column(name="numTemp")
	private Integer numTemp;
	
	
	public Integer getNumTemp() {
		return numTemp;
	}

	public void setNumTemp(Integer numTemp) {
		this.numTemp = numTemp;
	}

	public String getDiretor() {
		return diretor;
	}

	public void setDiretor(String diretor) {
		this.diretor = diretor;
	}

	public String getElenco() {
		return elenco;
	}

	public void setElenco(String elenco) {
		this.elenco = elenco;
	}

	
}
