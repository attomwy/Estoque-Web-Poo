package br.com.estoque.estoqueweb.service;
import br.com.estoque.estoqueweb.model.Usuario;
import br.com.estoque.estoqueweb.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class UsuarioService {
 private final UsuarioRepository repository;
 public UsuarioService(UsuarioRepository repository){this.repository=repository;}
 public List<Usuario> listar(){return repository.findAll();}
 public Usuario buscar(Long id){return repository.findById(id).orElseThrow(()->new IllegalArgumentException("Registro não encontrado"));}
 public Usuario salvar(Usuario entidade){return repository.save(entidade);}
 public void excluir(Long id){repository.deleteById(id);}
}
