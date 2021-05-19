package api.avaliadin.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import api.avaliadin.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	
	User findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
	
	User findById(int id);
	
	@Query("SELECT u FROM User u WHERE u.username like %:pesquisa% or u.nome like %:pesquisa%")
    public Iterable<User> pesquisa(@Param("pesquisa") String pesquisa);
	
	@Query("SELECT count(u) FROM User u ")
    public int findSomTotalUser();
	
	@Query("SELECT sum(u.numAmigos) FROM User u ")
    public int findSomTotalAmigos();

	List<User> findByOrderByNumAmigosDesc(Pageable pageable);
	
	@Query("SELECT u FROM User u WHERE u.id=:id")
	public User getUserById(@Param("id") Integer id);
}