package br.com.estoque.estoqueweb.controller;
import br.com.estoque.estoqueweb.service.*;
import br.com.estoque.estoqueweb.model.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Comparator;
import java.util.List;
@Controller public class HomeController {
 private final ProdutoService produtos; private final CategoriaService categorias; private final FornecedorService fornecedores; private final MovimentacaoService movimentacoes;
 public HomeController(ProdutoService produtos,CategoriaService categorias,FornecedorService fornecedores,MovimentacaoService movimentacoes){this.produtos=produtos;this.categorias=categorias;this.fornecedores=fornecedores;this.movimentacoes=movimentacoes;}
 @GetMapping("/") public String home(Model model){
  List<Produto> lista=produtos.listar(); List<Movimentacao> movs=movimentacoes.listar();
  model.addAttribute("totalProdutos",lista.size());model.addAttribute("totalCategorias",categorias.listar().size());model.addAttribute("totalFornecedores",fornecedores.listar().size());model.addAttribute("totalMovimentacoes",movs.size());
  model.addAttribute("baixoEstoque",lista.stream().filter(x->x.getQuantidade()!=null && x.getQuantidade()<=10).limit(8).toList());
  model.addAttribute("recentes",movs.stream().sorted(Comparator.comparing(Movimentacao::getData,Comparator.nullsLast(Comparator.naturalOrder())).reversed()).limit(8).toList());
  return "dashboard";
 }
}