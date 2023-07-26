package com.listatareas.listatareas.controllers;

import com.listatareas.listatareas.dao.UsuarioDao;
import com.listatareas.listatareas.models.Usuario;
import com.listatareas.listatareas.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioController {
    /**
     * Instancia de la interfaz UsuarioDao, la cual permite el
     * acceso a la base de datos
     */
    @Autowired
    private UsuarioDao usuarioDao;
    /**
     * Instancia de la clase jwtUtil, la cual contiene el mecanismo
     * de autenticacion
     */
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Este metodo permite insertar un usuario en la base de datos
     * @param usuario es el usuario que vamos a insertar en el base de datos
     * @return un ResponseEntity indicando si el usuario se ha insertado correctamente,
     * o un mensaje de error en caso de que ya exista un usuario con ese email
     */
    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody Usuario usuario){
        Usuario targerUser = usuarioDao.obtenerPorEmail(usuario.getEmail());
        System.out.println(targerUser);
        if(targerUser != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un usuario con ese email");
        }
        //cifrado de la contrasena
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        char[] password = usuario.getPassword().toCharArray();
        String hash = argon2.hash(1,1024,1,password);
        usuario.setPassword(hash);
        usuarioDao.insertar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario insertado correctamente");
    }

}
