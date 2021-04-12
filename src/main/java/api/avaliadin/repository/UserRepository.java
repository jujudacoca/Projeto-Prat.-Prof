package api.avaliadin.repository;

import org.springframework.data.repository.CrudRepository;

import api.avaliadin.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{
	
	User findByUsername(String username);

}