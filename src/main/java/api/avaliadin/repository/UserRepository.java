package api.avaliadin.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import api.avaliadin.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	
	User findByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
	
	User findById(int id);
}