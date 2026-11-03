package br.com.estoque.estoqueweb.service;
import br.com.estoque.estoqueweb.model.Produto;
import br.com.estoque.estoqueweb.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class ProdutoService {
 private final ProdutoRepository repository;
 public ProdutoService(ProdutoRepository repository){this.repository=repository;}
 public List<Produto> listar(){return repository.findAll();}
 public Produto buscar(Long id){return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Registro não encontrado"));}
 public Produto salvar(Produto entidade){return repository.save(entidade);}
 public void excluir(Long id){repository.deleteById(id);}
}
