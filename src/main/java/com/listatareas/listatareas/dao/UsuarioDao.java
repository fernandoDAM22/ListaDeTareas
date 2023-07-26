package com.listatareas.listatareas.dao;

import com.listatareas.listatareas.models.Usuario;

public interface UsuarioDao {
     void insertar(Usuario usuario);
     Usuario obtenerPorEmail(String email);

     Usuario obtenerUsuarioPorCredenciales(Usuario usuario);
}
