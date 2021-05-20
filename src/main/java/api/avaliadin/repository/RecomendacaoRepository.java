package api.avaliadin.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import api.avaliadin.model.Recomendacao;

public interface RecomendacaoRepository extends CrudRepository<Recomendacao, Integer>  {
	@Query("select r from Recomendacao r where id_user = :id_usuario")
	public Recomendacao recomendadoById(@Param("id_usuario") Integer id);
}
