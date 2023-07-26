package com.listatareas.listatareas.dao;

import com.listatareas.listatareas.models.Tarea;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TareaDaoImp implements TareaDao {
    /**
     * Instancia de entityManager que permite la conexion a la base de datos
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Este metodo permite obtener todas las tareas de un usuario
     * @param email es el email del usuario del que queremos obtener las tareas
     * @return una lista con todas las tareas
     */
    @Override
    public List<Tarea> obtenerTareas(String email) {
        String query = "Select t From Tarea t inner join Usuario u on t.idUsuario = u.id where u.email = :email";
        return entityManager.createQuery(query, Tarea.class)
                .setParameter("email", email)
                .getResultList();
    }

    /**
     * Este metodo permite insertar una tarea
     * @param tarea es la tarea que se va a insertar
     * @return true si se inserta, false si no
     */
    @Override
    public boolean insertar(Tarea tarea) {
        Tarea target = entityManager.merge(tarea);
        return target != null;
    }

    /**
     * Este metodo permite eliminar una tarea
     * @param id es el id de la tarea que se va a eliminar
     */
    @Override
    public void eliminar(int id) {
        Tarea tarea = entityManager.find(Tarea.class, id);
        entityManager.remove(tarea);
    }

    /**
     * Este metodo permite modificar una tarea
     * @param tarea es la tarea con los datos a modificar
     * @return true si se modifica, false si no
     */
    public boolean modificar(Tarea tarea) {
        Tarea target = entityManager.merge(tarea);
        return target != null;
    }

    /**
     * Este metodo permite completar una tarea
     * @param id es el id de la tarea que vamos a completar
     * @return true si se completa correctamente, false si no
     */
    @Override
    public boolean completarTarea(int id) {
        Tarea tarea = entityManager.find(Tarea.class, id);
        if (tarea == null) {
            return false;
        }
        if (tarea.getCompletada() == 0) {
            tarea.setCompletada(1);
        } else {
            tarea.setCompletada(0);
        }
        return true;
    }

    /**
     * Este metodo permite obtener una tarea a partir de su nombre
     * @param nombre es el nombre de la tarea que queremos obtener
     * @return la tarea obtenida si existe, null si no
     */
    @Override
    public Tarea obtenerTarea(String nombre) {
        String query = "From Tarea where nombre like :nombre order by id desc";
        List<Tarea> tareas = entityManager.createQuery(query,Tarea.class)
                .setParameter("nombre",nombre)
                .getResultList();
        if(tareas.isEmpty()){
            return null;
        }
        return tareas.get(0);
    }

    /**
     * Este metodo permite obtener el porcentaje de tareas completadas de un usuario
     * @param email es el email del usuario del que queremos obtener el porcentaje
     * @return el porcentaje
     */
    @Override
    public double obtenerPorcentajeTareasCompetadas(String email) {
        // Obtener las tareas totales
        String queryTareasTotales = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE u.email = :email";
        Query totalTareas = entityManager.createQuery(queryTareasTotales, Long.class)
                .setParameter("email", email);
        long cuentaTotalTareas = (long) totalTareas.getSingleResult();

        // Obtener las tareas completadas
        String queryTareasCompletadas = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE t.completada = 1 AND u.email = :email";
        Query totalTareasCompletadas = entityManager.createQuery(queryTareasCompletadas, Long.class)
                .setParameter("email", email);
        long cuentaTareasCompletas = (long) totalTareasCompletadas.getSingleResult();

        // Calcular el porcentaje de tareas completadas
        return (double) cuentaTareasCompletas / cuentaTotalTareas * 100;

    }

    /**
     * Este metodo permite obtener el numero de tareas completadas
     * @param email es el email del usuario del que queremos obtener el numero de tareas completadas
     * @return el numero de tareas completadas
     */
    @Override
    public int obtenerTareasCompletadas(String email) {
        String queryTareasCompletadas = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE t.completada = 1 AND u.email = :email";
        Query totalTareasCompletadas = entityManager.createQuery(queryTareasCompletadas, Long.class)
                .setParameter("email", email);
        long cuentaTareasCompletas = (long) totalTareasCompletadas.getSingleResult();
        return (int) cuentaTareasCompletas;
    }

    /**
     * Este metodo permite obtener el porcentaje de taras pendientes de un usuario
     * @param email es el email del usuario del que queremos obtener el porcentaje de tareas pendientes
     * @return el porcentaje de tareas pendientes
     */
    @Override
    public double obtenerPorcentajeTareasPendientes(String email) {
        // Obtener las tareas totales
        String queryTareasTotales = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE u.email = :email";
        Query totalTareas = entityManager.createQuery(queryTareasTotales, Long.class)
                .setParameter("email", email);
        long cuentaTotalTareas = (long) totalTareas.getSingleResult();

        // Obtener las tareas sin completar
        String queryTareasSinCompletar = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE t.completada = 0 AND u.email = :email";
        Query totalTareasSinCompletar = entityManager.createQuery(queryTareasSinCompletar, Long.class)
                .setParameter("email", email);
        long cuentaTareasSinCompletar = (long) totalTareasSinCompletar.getSingleResult();

        // Calcular el porcentaje de tareas sin completar
        return (double) cuentaTareasSinCompletar / cuentaTotalTareas * 100;
    }

    /**
     * Este metodo permite obtener el numero de tareas pendientes de un usuario
     * @param email es el email del usuario del que queremos obtener el numero de tareas pendientes
     * @return el numero de tareas pendientes
     */
    @Override
    public int obtenerTareasPendientes(String email) {
        String queryTareasCompletadas = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE t.completada = 0 AND u.email = :email";
        Query totalTareasCompletadas = entityManager.createQuery(queryTareasCompletadas, Long.class)
                .setParameter("email", email);
        long cuentaTareasCompletas = (long) totalTareasCompletadas.getSingleResult();
        return (int) cuentaTareasCompletas;
    }

    /**
     * Este metodo permite obtener el porcentaje de tareas completadas de un usuario de una
     * determinada prioridad
     * @param email es el email del usuario del que queremos obtener el porcentaje
     * @param prioridad es la prioridad de la que queremos obtener el porcentaje
     * @return el porcentaje de las taras pendientes
     */
    @Override
    public double obtenerPorcentajePorPrioridad(String email, String prioridad) {
        // Obtener las tareas totales
        String queryTareasTotales = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE u.email = :email and prioridad like :prioridad";
        Query totalTareas = entityManager.createQuery(queryTareasTotales, Long.class)
                .setParameter("email", email)
                .setParameter("prioridad",prioridad);
        long cuentaTotalTareas = (long) totalTareas.getSingleResult();

        // Obtener las tareas completadas
        String queryTareasCompletadas = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE t.completada = 1 AND u.email = :email and prioridad like :prioridad";
        Query totalTareasCompletadas = entityManager.createQuery(queryTareasCompletadas, Long.class)
                .setParameter("email", email)
                .setParameter("prioridad",prioridad);
        long cuentaTareasCompletas = (long) totalTareasCompletadas.getSingleResult();

        // Calcular el porcentaje de tareas completadas
        return (double) cuentaTareasCompletas / cuentaTotalTareas * 100;
    }

    /**
     * Esta metodo permite obtener el numero de tareas pendientes de un usuario y de una prioridad determinada
     * @param email el email del usuario del que queremos obtener el numero de tareas
     * @param prioridad es la prioridad del que queremos obtener el numero de tareas
     * @return el numero de tareas de la priodidad indicada
     */
    @Override
    public long obtenerTareasPorPrioridad(String email, String prioridad) {
        // Obtener las tareas totales
        String query = "SELECT COUNT(*) FROM Tarea t INNER JOIN Usuario u ON t.idUsuario = u.id WHERE u.email = :email and prioridad like :prioridad";
        Query totalTareas = entityManager.createQuery(query, Long.class)
                .setParameter("email", email)
                .setParameter("prioridad",prioridad);
        return (long) totalTareas.getSingleResult();
    }

    /**
     * Este metodo permite obtener el recuento de tareas por prioridad de un usuario
     * @param email es el email del usuario del que queremos obtener el recuento
     * @return un array con el recuento de tareas, ordenado de mayor a menor prioridad
     */
    @Override
    public long[] ObtenerDatosGraficoTareas(String email) {
        long tareasExtremas = obtenerTareasPorPrioridad(email,"Extrema");
        long tareasAltas = obtenerTareasPorPrioridad(email,"Alta");
        long tareasMedias = obtenerTareasPorPrioridad(email,"Media");
        long tareasBajas = obtenerTareasPorPrioridad(email,"Baja");

        return new long[]{
          tareasExtremas,tareasAltas,tareasMedias,tareasBajas
        };
    }


}
