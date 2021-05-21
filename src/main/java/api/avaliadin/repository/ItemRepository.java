package api.avaliadin.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import api.avaliadin.model.Item;

@EntityScan(basePackages="api.avaliadin.model")
public interface ItemRepository extends CrudRepository<Item, Integer>{
	
	@Query("select tipo from Item where id_item=:id")
	String findDtypeById(Integer id);

	Item findById(int id);
	
	Item findByTitulo(String titulo);
	
	@Query("select i from Item i where i.titulo like %:pesquisa% and i.estado=true")
	public Iterable<Item> pesquisa(@Param("pesquisa") String pesquisa);
	
	@Query("select i from Item i where i.estado=false")
	public Iterable<Item> findAllStateFalse();
}
