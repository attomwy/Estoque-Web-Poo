package br.com.estoque.estoqueweb.controller;
import br.com.estoque.estoqueweb.model.Produto;
import br.com.estoque.estoqueweb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
@Controller @RequestMapping("/produtos") public class ProdutoController {
 private final ProdutoService service;private final CategoriaService categorias;private final FornecedorService fornecedores;
 public ProdutoController(ProdutoService service,CategoriaService categorias,FornecedorService fornecedores){this.service=service;this.categorias=categorias;this.fornecedores=fornecedores;}
 @GetMapping public String listar(Model model){model.addAttribute("itens",service.listar());return "produtos/lista";}
 @GetMapping("/novo") public String novo(Model model){model.addAttribute("item",new Produto());model.addAttribute("categorias",categorias.listar());model.addAttribute("fornecedores",fornecedores.listar());return "produtos/form";}
 @GetMapping("/editar/{id}") public String editar(@PathVariable Long id,Model model){model.addAttribute("item",service.buscar(id));model.addAttribute("categorias",categorias.listar());model.addAttribute("fornecedores",fornecedores.listar());return "produtos/form";}
 @PostMapping("/salvar") public String salvar(@Valid @ModelAttribute("item") Produto item,BindingResult errors,Model model){if(errors.hasErrors()){model.addAttribute("categorias",categorias.listar());model.addAttribute("fornecedores",fornecedores.listar());return "produtos/form";}service.salvar(item);return "redirect:/produtos";}
 @PostMapping("/excluir/{id}") public String excluir(@PathVariable Long id){service.excluir(id);return "redirect:/produtos";}
}
