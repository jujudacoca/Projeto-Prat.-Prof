package api.avaliadin.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Joinha {
	@Id
	@Column(name="idJoinha")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="idAvaliacao")
	private Integer idAvaliacao;
	
	@Column(name="idUsuario")
	private Integer idUsuario;
	
	@Column(name="dtCad")
	private Date dtCad;
}
