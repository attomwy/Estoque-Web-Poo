package br.com.estoque.estoqueweb.controller;
import br.com.estoque.estoqueweb.model.Fornecedor;
import br.com.estoque.estoqueweb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
@Controller @RequestMapping("/fornecedores") public class FornecedorController {
 private final FornecedorService service;
 public FornecedorController(FornecedorService service){this.service=service;}
 @GetMapping public String listar(Model model){model.addAttribute("itens",service.listar());return "fornecedores/lista";}
 @GetMapping("/novo") public String novo(Model model){model.addAttribute("item",new Fornecedor());return "fornecedores/form";}
 @GetMapping("/editar/{id}") public String editar(@PathVariable Long id,Model model){model.addAttribute("item",service.buscar(id));return "fornecedores/form";}
 @PostMapping("/salvar") public String salvar(@Valid @ModelAttribute("item") Fornecedor item,BindingResult errors,Model model){if(errors.hasErrors()){return "fornecedores/form";}service.salvar(item);return "redirect:/fornecedores";}
 @PostMapping("/excluir/{id}") public String excluir(@PathVariable Long id){service.excluir(id);return "redirect:/fornecedores";}
}
