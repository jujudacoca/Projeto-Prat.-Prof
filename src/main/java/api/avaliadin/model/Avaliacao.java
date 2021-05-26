package api.avaliadin.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;




@Entity
public class Avaliacao {
	@Id
	@Column(name="idAvaliacao")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="descricao", length=1024)
	private String descricao;
	
	@Column(name="nota")
	private float nota;
	
	@Column(name="idItem")
	private int idItem;
	
	@Column(name="idUsuario")
	private int idUsuario;
	
	@Column(name="dtCad")
	private Date dtCad;
	
	@Column(name="numJoinha")
	private int numJoinha;

	@Column(name="username")
	private String username;
	
	@Column(name="titulo")
	private String titulo;
	
	@OneToMany(mappedBy="avaliacao")
	private List<Comentario> comentarios;
	
	
	public List<Comentario> getComentarios() {
		return comentarios;
	}

	public void setComentarios(List<Comentario> comentarios) {
		this.comentarios = comentarios;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

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

	public float getNota() {
		return nota;
	}

	public void setNota(float nota) {
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
