package br.com.estoque.estoqueweb.service;
import br.com.estoque.estoqueweb.model.Categoria;
import br.com.estoque.estoqueweb.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class CategoriaService {
 private final CategoriaRepository repository;
 public CategoriaService(CategoriaRepository repository){this.repository=repository;}
 public List<Categoria> listar(){return repository.findAll();}
 public Categoria buscar(Long id){return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Registro não encontrado"));}
 public Categoria salvar(Categoria entidade){return repository.save(entidade);}
 public void excluir(Long id){repository.deleteById(id);}
}
