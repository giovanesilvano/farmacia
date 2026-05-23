package br.com.farmacia.usuarios.service;

import br.com.farmacia.usuarios.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "BKQXirOCDcR9TLdo1b87nEJtKePFTnFyoRlvk8tWmpk";

    public String gerarToken(Usuario usuario) {

        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .claim("perfil", usuario.getPerfil().name())
                .claim("id", usuario.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }
}