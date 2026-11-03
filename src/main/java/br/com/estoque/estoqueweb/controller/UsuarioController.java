package br.com.estoque.estoqueweb.controller;
import br.com.estoque.estoqueweb.model.Usuario;
import br.com.estoque.estoqueweb.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
@Controller @RequestMapping("/usuarios") public class UsuarioController {
 private final UsuarioService service;
 public UsuarioController(UsuarioService service){this.service=service;}
 @GetMapping public String listar(Model model){model.addAttribute("itens",service.listar());return "usuarios/lista";}
 @GetMapping("/novo") public String novo(Model model){model.addAttribute("item",new Usuario());return "usuarios/form";}
 @GetMapping("/editar/{id}") public String editar(@PathVariable Long id,Model model){model.addAttribute("item",service.buscar(id));return "usuarios/form";}
 @PostMapping("/salvar") public String salvar(@Valid @ModelAttribute("item") Usuario item,BindingResult errors,Model model){if(errors.hasErrors()){return "usuarios/form";}service.salvar(item);return "redirect:/usuarios";}
 @PostMapping("/excluir/{id}") public String excluir(@PathVariable Long id){service.excluir(id);return "redirect:/usuarios";}
}
