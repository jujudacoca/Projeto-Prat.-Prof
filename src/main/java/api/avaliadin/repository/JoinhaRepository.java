package api.avaliadin.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import api.avaliadin.model.*;

public interface JoinhaRepository extends CrudRepository<Joinha, Integer> {
	@Query("select j from Joinha j where id_usuario=:id_usuario and id_avaliacao=:id_avaliacao")
	 public Joinha existeJoinha(@Param("id_usuario")Integer id_usuario,@Param("id_avaliacao")Integer id_avaliacao);
}
