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
	private Integer idItem;
	
	@Column(name="idUsuario")
	private Integer idUsuario;
	
	
	@Column(name="dtCad")
	private Date dtCad;
}
