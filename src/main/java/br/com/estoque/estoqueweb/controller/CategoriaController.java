package br.com.estoque.estoqueweb.controller;
import br.com.estoque.estoqueweb.model.Categoria;
import br.com.estoque.estoqueweb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
@Controller @RequestMapping("/categorias") public class CategoriaController {
 private final CategoriaService service;
 public CategoriaController(CategoriaService service){this.service=service;}
 @GetMapping public String listar(Model model){model.addAttribute("itens",service.listar());return "categorias/lista";}
 @GetMapping("/novo") public String novo(Model model){model.addAttribute("item",new Categoria());return "categorias/form";}
 @GetMapping("/editar/{id}") public String editar(@PathVariable Long id,Model model){model.addAttribute("item",service.buscar(id));return "categorias/form";}
 @PostMapping("/salvar") public String salvar(@Valid @ModelAttribute("item") Categoria item,BindingResult errors,Model model){if(errors.hasErrors()){return "categorias/form";}service.salvar(item);return "redirect:/categorias";}
 @PostMapping("/excluir/{id}") public String excluir(@PathVariable Long id){service.excluir(id);return "redirect:/categorias";}
}
