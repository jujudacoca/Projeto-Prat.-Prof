package api.avaliadin.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import api.avaliadin.model.Item;

@EntityScan(basePackages="api.avaliadin.model")
public interface ItemRepository extends CrudRepository<Item, Integer>{
	
	@Query("select tipo from Item where id_item=:id")
	String findDtypeById(Integer id);

	Item findById(int id);
	
	Item findByTitulo(String titulo);
}
