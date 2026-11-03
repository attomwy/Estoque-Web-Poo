package br.com.estoque.estoqueweb.controller;

import br.com.estoque.estoqueweb.model.*;
import br.com.estoque.estoqueweb.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/movimentacoes")
public class MovimentacaoController {
    private final MovimentacaoService service;
    private final ProdutoService produtos;
    private final UsuarioService usuarios;

    public MovimentacaoController(MovimentacaoService service, ProdutoService produtos, UsuarioService usuarios) {
        this.service = service;
        this.produtos = produtos;
        this.usuarios = usuarios;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("itens", service.listar());
        return "movimentacoes/lista";
    }

    private void carregarOpcoes(Model model) {
        model.addAttribute("produtos", produtos.listar());
        model.addAttribute("usuarios", usuarios.listar());
        model.addAttribute("tipos", TipoMovimentacao.values());
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("item", new Movimentacao());
        carregarOpcoes(model);
        return "movimentacoes/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("item", service.buscar(id));
        carregarOpcoes(model);
        return "movimentacoes/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("item") Movimentacao item, BindingResult erros, Model model) {
        if (erros.hasErrors()) {
            carregarOpcoes(model);
            return "movimentacoes/form";
        }
        try {
            service.salvar(item);
            return "redirect:/movimentacoes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            carregarOpcoes(model);
            return "movimentacoes/form";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes mensagem) {
        try { service.excluir(id); }
        catch (IllegalArgumentException e) { mensagem.addFlashAttribute("erro", e.getMessage()); }
        return "redirect:/movimentacoes";
    }
}
