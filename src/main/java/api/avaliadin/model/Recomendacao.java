package api.avaliadin.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Entity;

@Entity
public class Recomendacao {
	
	@Id
	@Column(name="id_recomendacao")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="id_user")
	private int idUser;
	
	@Column(name="id_user1")
	private int iduser1;
	@Column(name="idUser2")
	private int iduser2;
	@Column(name="idUser3")
	private int iduser3;
	
	@Column(name="idItem1")
	private int idItem1;
	@Column(name="idItem2")
	private int idItem2;
	@Column(name="idItem3")
	private int idItem3;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public int getIduser1() {
		return iduser1;
	}
	public void setIduser1(int iduser1) {
		this.iduser1 = iduser1;
	}
	public int getIduser2() {
		return iduser2;
	}
	public void setIduser2(int iduser2) {
		this.iduser2 = iduser2;
	}
	public int getIduser3() {
		return iduser3;
	}
	public void setIduser3(int iduser3) {
		this.iduser3 = iduser3;
	}
	public int getIdItem1() {
		return idItem1;
	}
	public void setIdItem1(int idItem1) {
		this.idItem1 = idItem1;
	}
	public int getIdItem2() {
		return idItem2;
	}
	public void setIdItem2(int idItem2) {
		this.idItem2 = idItem2;
	}
	public int getIdItem3() {
		return idItem3;
	}
	public void setIdItem3(int idItem3) {
		this.idItem3 = idItem3;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
	
	
}
