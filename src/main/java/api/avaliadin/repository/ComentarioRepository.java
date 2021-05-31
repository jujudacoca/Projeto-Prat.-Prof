package api.avaliadin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import api.avaliadin.model.*;

public interface ComentarioRepository extends CrudRepository<Comentario, Integer>  {
	@Query("SELECT c FROM Comentario c WHERE id_avaliacao = :id_avaliacao")
    public Iterable<Comentario> findAllByIdAvaliacao(@Param("id_avaliacao") Integer id);
	
	Comentario findById(int id);
	
	void deleteByIdUsuario(int id);
}
