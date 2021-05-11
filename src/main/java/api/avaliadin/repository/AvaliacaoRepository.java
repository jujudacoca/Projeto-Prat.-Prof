package api.avaliadin.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import api.avaliadin.model.*;

public interface AvaliacaoRepository extends CrudRepository<Avaliacao, Integer> {
	@Query("SELECT a FROM Avaliacao a WHERE id_item = :id_item")
    public Iterable<Avaliacao> findAllByIdItem(@Param("id_item") Integer id);
	
	@Query("SELECT a FROM Avaliacao a WHERE id_usuario = :id_usuario")
    public Iterable<Avaliacao> findAllByIdUser(@Param("id_usuario") Integer id);
	
	Avaliacao findById(int id);
}
