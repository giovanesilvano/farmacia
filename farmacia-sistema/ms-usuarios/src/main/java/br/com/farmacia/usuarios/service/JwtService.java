package br.com.farmacia.usuarios.service;

import br.com.farmacia.usuarios.model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;

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

    public boolean tokenValido(String token) {
        try {
            extrairClaims(token);
            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String extrairLogin(String token) {

        return extrairClaims(token).getSubject();
    }

    public Claims extrairClaims(String token) {

        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}