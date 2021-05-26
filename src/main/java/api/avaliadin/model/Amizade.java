package api.avaliadin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Amizade {
	
	@Id
	@Column(name="idAmizade")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="dtCad")
	private Date dtCad;
	
	@Column(name="id_user1")
	private int idUser1;
	
	@Column(name="id_user2")
	private int idUser2;
	
	@Column(name="username1")
	private String username1;

	@Column(name="username2")
	private String username2;
	
	@Column(name="estado")
	private String estado;
	
	@Column(name="nome1")
	private String nome1;

	@Column(name="nome2")
	private String nome2;

	public String getNome1() {
		return nome1;
	}

	public void setNome1(String nome1) {
		this.nome1 = nome1;
	}

	public String getNome2() {
		return nome2;
	}

	public void setNome2(String nome2) {
		this.nome2 = nome2;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDtCad() {
		return dtCad;
	}

	public void setDtCad(Date dtCad) {
		this.dtCad = dtCad;
	}

	public int getIdUser1() {
		return idUser1;
	}

	public void setIdUser1(int idUser1) {
		this.idUser1 = idUser1;
	}

	public int getIdUser2() {
		return idUser2;
	}

	public void setIdUser2(int idUser2) {
		this.idUser2 = idUser2;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getUsername1() {
		return username1;
	}

	public void setUsername1(String username1) {
		this.username1 = username1;
	}

	public String getUsername2() {
		return username2;
	}

	public void setUsername2(String username2) {
		this.username2 = username2;
	}
}
