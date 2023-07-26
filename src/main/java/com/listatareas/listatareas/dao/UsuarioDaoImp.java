package com.listatareas.listatareas.dao;

import com.listatareas.listatareas.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UsuarioDaoImp implements UsuarioDao{
    /**
     * Instancia de entityManager que permite la conexion a la base de datos
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Este metodo permite insertar un usuario en la base de datos
     * @param usuario el usuario que vamos a insertar en la base de datos
     */
    @Override
    public void insertar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    /**
     * Este metodo permite obtener un usuario por su email
     * @param email es el email del usuario que queremos obtener
     * @return el usuario si existe, null si no
     */
    @Override
    public Usuario obtenerPorEmail(String email) {
        String query = "FROM Usuario WHERE email = :email";
        List<Usuario> usuarios = entityManager.createQuery(query, Usuario.class)
                .setParameter("email", email)
                .getResultList();

        if (usuarios.isEmpty()) {
            // Manejar el caso en el que no se encontró ningún resultado.
            return null;
        } else {
            return usuarios.get(0);
        }
    }

    /**
     * Este metodo permite obtener un usuario por sus crecenciales
     * @param usuario es el usuario que contiene las credenciales
     * @return el usuario con esas credenciales, null si las credenciales son incorrectas
     */
    @Override
    @SuppressWarnings("All")
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {
        String query = "FROM Usuario WHERE email = :email";
        List<Usuario> usuarios = entityManager.createQuery(query)
                .setParameter("email", usuario.getEmail())
                .getResultList();
        if(usuarios.isEmpty()){
            return null;
        }
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if(argon2.verify(usuarios.get(0).getPassword(),usuario.getPassword())){
            return usuarios.get(0);
        };
        return null;
    }
}
