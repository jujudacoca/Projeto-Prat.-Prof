package api.avaliadin.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import api.avaliadin.model.Amizade;


public interface AmizadeRepository extends CrudRepository<Amizade, Integer>{
	@Query("SELECT a FROM Amizade a WHERE id_user1 = :id_user or id_user2 = :id_user ")
    public Iterable<Amizade> findAllByIdUser(@Param("id_user") Integer id);
	
	@Query("SELECT a FROM Amizade a WHERE (id_user1 = :id_user1 and id_user2 = :id_user2) or (id_user1 = :id_user2 and id_user2 = :id_user1) ")
    public Amizade findByTwoId(@Param("id_user1") Integer id1,@Param("id_user2") Integer id2);

	@Query("SELECT a FROM Amizade a WHERE id_user2 = :id_user and estado = 's' ")
    public Iterable<Amizade> findSolicitacaoById(@Param("id_user") Integer id);
	
	Amizade findById(int id);
}
