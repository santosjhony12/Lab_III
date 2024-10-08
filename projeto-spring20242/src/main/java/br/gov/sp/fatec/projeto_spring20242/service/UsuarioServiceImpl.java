package br.gov.sp.fatec.projeto_spring20242.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.gov.sp.fatec.projeto_spring20242.entity.Anotacao;
import br.gov.sp.fatec.projeto_spring20242.entity.Autorizacao;
import br.gov.sp.fatec.projeto_spring20242.entity.Usuario;
import br.gov.sp.fatec.projeto_spring20242.repository.AnotacaoRepository;
import br.gov.sp.fatec.projeto_spring20242.repository.AutorizacaoRepository;
import br.gov.sp.fatec.projeto_spring20242.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final AutorizacaoRepository autRepo;

    private final AnotacaoRepository anotacaoRepo;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, AutorizacaoRepository autRepo, AnotacaoRepository anotacaoRepo) {
        this.usuarioRepository = usuarioRepository;
        this.autRepo = autRepo;
        this.anotacaoRepo = anotacaoRepo;
    }


    @Transactional // Abre uma transação no banco de dados. Ele não commita até que tudo ocorra bem
    @Override
    public Usuario createUsuario(Usuario usuario) {
        if(usuario == null ||
                usuario.getNome() == null ||
                usuario.getNome().isBlank() ||
                usuario.getSenha() == null ||
                usuario.getSenha().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados inválidos!");
        }
        if(usuario.getAutorizacoes() != null && !usuario.getAutorizacoes().isEmpty()) {
            Set<Autorizacao> autorizacoes = new HashSet<Autorizacao>();
            for(Autorizacao autorizacao: usuario.getAutorizacoes()) {
                if(autorizacao.getNome() != null && !autorizacao.getNome().isBlank()) {
                    Optional<Autorizacao> autOp = autRepo.findByNome(autorizacao.getNome());
                    autorizacoes.add(verificaSeExiste(autOp));
                }
                else {
                    if(autorizacao.getId() != null && autorizacao.getId() > 0) {
                        Optional<Autorizacao> autOp = autRepo.findById(autorizacao.getId());
                        autorizacoes.add(verificaSeExiste(autOp));
                    }
                }
            }
            usuario.setAutorizacoes(autorizacoes);
        }
        usuario = usuarioRepository.save(usuario);
        if(usuario.getAnotacoes() != null && !usuario.getAnotacoes().isEmpty()) {
            for(Anotacao anotacao: usuario.getAnotacoes()) {
                anotacao.setUsuario(usuario);
                anotacaoRepo.save(anotacao);
            }
        }
        return usuario;
    }

    private Autorizacao verificaSeExiste(Optional<Autorizacao> autOp) {
        if(autOp.isPresent()) {
            return autOp.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autorização não existe!");
        }
    }

    @Override
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();        
    }

    @Override
    public Usuario getById(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if(usuarioOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }
        return usuarioOptional.get();
    }
    
}
