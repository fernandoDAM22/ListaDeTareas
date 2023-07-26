package com.listatareas.listatareas.controllers;

import com.listatareas.listatareas.dao.UsuarioDao;
import com.listatareas.listatareas.models.Usuario;
import com.listatareas.listatareas.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

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
     * Este metodo permite a un usuario logearse en el sistema
     * @param usuario es el usuario que contiene las credenciales
     * @return el token de autenticacion en caso de que las credenciales
     * sean validas, FAIL si no
     */
    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public String login(@RequestBody Usuario usuario) {
        Usuario authUser = usuarioDao.obtenerUsuarioPorCredenciales(usuario);
        System.out.println(authUser);
        if (authUser != null) {
            return jwtUtil.create(String.valueOf(authUser.getId()),authUser.getEmail());
        }else{
            return "FAIL";
        }
    }
}
