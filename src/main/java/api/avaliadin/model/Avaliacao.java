package api.avaliadin.model;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




@Entity
public class Avaliacao {
	@Id
	@Column(name="idAvaliacao")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="descricao", length=300)
	private String descricao;
	
	@Column(name="nota")
	private int nota;
	
	@Column(name="idItem")
	private int idItem;
	
	@Column(name="idUsuario")
	private int idUsuario;
	
	@Column(name="dtCad")
	private Date dtCad;
	
	@Column(name="numJoinha")
	private int numJoinha;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	public int getIdItem() {
		return idItem;
	}

	public void setIdItem(int idItem) {
		this.idItem = idItem;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getDtCad() {
		return dtCad;
	}

	public void setDtCad(Date dtCad) {
		this.dtCad = dtCad;
	}

	public int getNumJoinha() {
		return numJoinha;
	}

	public void setNumJoinha(int numJoinha) {
		this.numJoinha = numJoinha;
	}
	
	
}
