package br.gov.sp.fatec.springbootlab4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import br.gov.sp.fatec.springbootlab4.entity.Usuario;
import br.gov.sp.fatec.springbootlab4.entity.View;
import br.gov.sp.fatec.springbootlab4.service.UsuarioService;

@RestController
@RequestMapping(value = "/usuario")
@CrossOrigin
public class UsuarioController {
    
    @Autowired
    private UsuarioService service;

    @GetMapping
    @JsonView(View.ViewUsuario.class)
    public List<Usuario> todosUsuarios() {
        return service.todosUsuarios();
    }

    @PostMapping
    @JsonView(View.ViewUsuario.class)
    public Usuario novoUsuario(@RequestBody Usuario usuario) {
        return service.novoUsuario(usuario);
    }

    @GetMapping(value = "/{id}")
    @JsonView(View.ViewUsuarioCompleto.class)
    public Usuario buscarPeloId(@PathVariable("id") Long id) {
        return service.buscarPeloId(id);
    }

}
