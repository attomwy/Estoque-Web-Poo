package br.com.estoque.estoqueweb.service;
import br.com.estoque.estoqueweb.model.Fornecedor;
import br.com.estoque.estoqueweb.repository.FornecedorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class FornecedorService {
 private final FornecedorRepository repository;
 public FornecedorService(FornecedorRepository repository){this.repository=repository;}
 public List<Fornecedor> listar(){return repository.findAll();}
 public Fornecedor buscar(Long id){return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Registro não encontrado"));}
 public Fornecedor salvar(Fornecedor entidade){return repository.save(entidade);}
 public void excluir(Long id){repository.deleteById(id);}
}
