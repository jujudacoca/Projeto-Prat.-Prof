package api.avaliadin.repository;


import org.springframework.data.repository.CrudRepository;
import api.avaliadin.model.Filme;

public interface FilmeRepository extends CrudRepository<Filme, Integer>{
	Filme findByTitulo(String titulo);
	Filme findById(int id);
}
