package com.listatareas.listatareas.controllers;

import com.listatareas.listatareas.dao.TareaDao;
import com.listatareas.listatareas.models.Tarea;
import com.listatareas.listatareas.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TareaController {
    /**
     * Instancia de la interfaz TareaDao, la cual permite el
     * acceso a la base de datos
     */
    @Autowired
    private TareaDao tareaDao;
    /**
     * Instancia de la clase jwtUtil, la cual contiene el mecanismo
     * de autenticacion
     */
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * Esta constante almacena el ResponseEntity el cual se
     * devuelve en caso de que el token de autenticacion
     * sea invalido
     */
    private final ResponseEntity FORBIDDEN = ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .header("X-Error-Message", "Token de autorizacion invalido")
            .body(null);

    /**
     * Esta metodo permite obtener todas las tareas de un determinado usuario
     * @param email es el email del usuario del que queremos obtener la tareas
     * @param token es el token de autenticacion
     * @return un ResponseEntity el cual contiene una lista con todas las preguntas, FORBIDDEN en caso
     * de que el token sea incorrecto
     */
    @RequestMapping(value = "api/tareas/{email}", method = RequestMethod.GET)
    public ResponseEntity<List<Tarea>> obtenerTareas(@PathVariable String email, @RequestHeader(value = "Authorization") String token) {
        if (token == null || !validarToken(token)) {
               return FORBIDDEN;
        }
        if (email == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("X-Error-Message", "Debe especificar el email")
                    .body(null);
        }
        List<Tarea> tareas = tareaDao.obtenerTareas(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tareas);
    }

    /**
     * Este metodo permite insertar una tarea
     * @param tarea es el objeto tarea que contiene los datos que se van a insertar
     * @param token es el token de autenticacion
     * @return un ResponseEntity indicando que se ha insertado la tarea correctamente,
     * o que ha fallado la operacion, FORBIDDEN en caso de que el token de autenticacion
     * sea invalido
     */
    @RequestMapping(value = "api/tareas", method = RequestMethod.POST)
    public ResponseEntity<String> insertarTarea(@RequestBody Tarea tarea, @RequestHeader(value = "Authorization") String token) {
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        String userId = jwtUtil.getKey(token);
        tarea.setIdUsuario(Integer.parseInt(userId));
        if (tareaDao.insertar(tarea)) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Tarea insertada correctamente");
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al insertar la tarea");
        }
    }

    /**
     * Este metodo permite elimina una tarea
     * @param id es el id de la tarea que queremos eliminar
     * @param token es el token de autenticacion
     * @return 204 No Content en caso de que se borre la tarea correctamente,
     * FORBIDDEN en caso de que el token de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> eliminar(@PathVariable int id, @RequestHeader(value = "Authorization") String token) {
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        tareaDao.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * Este metodo permite actualizar una tarea
     * @param tarea es el objeto tarea que contiene los datos de la tarea a modificar
     * @param token es el token de autenticacion
     * @return un ResponseEntity con un String indicando que se ha modificado
     * correctamente la operacion o no, FORBIDDEN en caso de que el token
     * de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas", method = RequestMethod.PUT)
    public ResponseEntity<String> actualizarTarea(@RequestBody Tarea tarea, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        String userId = jwtUtil.getKey(token);
        tarea.setIdUsuario(Integer.parseInt(userId));
        if(tareaDao.modificar(tarea)){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Tarea modificada correctamente");
        }else{
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error al modificar la tarea");
        }
    }

    /**
     * Este metodo permite marcar una tarea como completada
     * @param id es el id de la tarea que vamos a completar
     * @param token es el token de autenticacion
     * @return un ResponseEntity indicando si la tarea se ha completado
     * correctamente o no, FORBIDDEN en caso de que el token de a
     * autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/modificar", method = RequestMethod.PUT)
    public ResponseEntity<String> completarTarea(@RequestBody int id, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        if(tareaDao.completarTarea(id)){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("El estado de la tarea a sido modificado con exito");
        }else{
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Error al modificar el estado de la tarea");
        }
    }

    /**
     * Este metodo permite obtener una tarea por su nombre
     * @param nombre es el nombre de la tarea que queremos obtener
     * @param token es el token de autenticacion
     * @return un ResponseEntity indicando con la tarea obtenida, si existe, null
     * si no, o FORBIDDEN en caso de que el token de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/obtener/{nombre}", method = RequestMethod.GET)
    public ResponseEntity<Tarea> obtenerTareaPorNombre(@PathVariable String nombre, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        Tarea tarea = tareaDao.obtenerTarea(nombre);
        System.out.println(tarea);
        if(tarea == null){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tarea);
    }

    /**
     * Este metodo permite obtener el porcentaje de tareas completadas de un usuario
     * @param email es el email del usuario que queremos obtener el porcentaje
     * @param token es el token de autenticacion
     * @return un ResponseEntity con el porcentaje, FORBIDDEN en caso de el token de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/estadisticas/porcentaje/completadas/{email}", method = RequestMethod.GET)
    public ResponseEntity<Double> obtenerPorcentajeTareasCompletadas(@PathVariable String email, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        double porcentaje = tareaDao.obtenerPorcentajeTareasCompetadas(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(porcentaje);
    }

    /**
     * Este metodo permite obtener el numero de las tareas completadas de un usuario
     * @param email es el email del usuario del cual queremos obtener el numero de tareas completadas
     * @param token es el token de autenticacion
     * @return un ResponseEntity con el numero de tareas completadas, FORBIDDEN en caso de que
     * el token de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/estadisticas/completadas/{email}", method = RequestMethod.GET)
    public ResponseEntity<Integer> obtenerTareasCompletadas(@PathVariable String email, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        Integer porcentaje = tareaDao.obtenerTareasCompletadas(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(porcentaje);
    }

    /**
     * Este metodo permite obtener el porcentaje de tareas pendientes
     * @param email es el email del usuario del que queremos obtener el porcentaje
     * @param token es el token de autenticacion
     * @return un ResponseEntity con el porcentaje de tareas pendientes, FORBIDDEN en
     * caso de que el token de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/estadisticas/porcentaje/pendientes/{email}", method = RequestMethod.GET)
    public ResponseEntity<Double> obtenerPorcentajeTareasPendientes(@PathVariable String email, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        double porcentaje = tareaDao.obtenerPorcentajeTareasPendientes(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(porcentaje);
    }

    /**
     * Esta metodo permite obtener el numero de tareas pendientes
     * @param email es el email del usuario del que queremos obtener el numero de tareas pendientes
     * @param token es el token de autenticacion
     * @return un ResponseEntity con el numero tareas pendientes, FORBIDDEN en caso de que
     * el token de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/estadisticas/pendientes/{email}", method = RequestMethod.GET)
    public ResponseEntity<Integer> obtenerTareasPendientes(@PathVariable String email, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        Integer porcentaje = tareaDao.obtenerTareasPendientes(email);
        return ResponseEntity.status(HttpStatus.OK)
                .body(porcentaje);
    }

    /**
     * Este metodo permite obtener el porcente de tareas completadas de un usuario de una prioridad determinada
     * @param email es el email del usuario del que queremos obtener el poncentaje
     * @param token  es el token de autenticacion
     * @param prioridad es la prioridad de la que queremos obtener el porcentaje
     * @return un ResponseEntity con el porcentaje, FORBIDDEN en caso de el token
     * de autenticacion sea invalido
     */
    @RequestMapping(value = "api/tareas/estadisticas/porcentaje/prioridad/{email}", method = RequestMethod.GET)
    public ResponseEntity<Double> obtenerPorcentajePrioridad(@PathVariable String email, @RequestHeader(value = "Authorization") String token,@RequestHeader(value = "Prioridad") String prioridad){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        double porcentaje = tareaDao.obtenerPorcentajePorPrioridad(email,prioridad);
        return ResponseEntity.status(HttpStatus.OK)
                .body(porcentaje);

    }

    /**
     * Este metodo permite obtener el numero de tareas de cada prioridad de un usuario
     * @param email es el email del usuario del que queremos obtener los datos
     * @param token es el token de autenticacion
     * @return un ResponseEntity con un array de longs con los datos
     */
    @RequestMapping(value = "api/tareas/estadisticas/grafico/{email}", method = RequestMethod.GET)
    public ResponseEntity<long[]> obtenerDatosGrafico(@PathVariable String email, @RequestHeader(value = "Authorization") String token){
        if (token == null || !validarToken(token)) {
            return FORBIDDEN;
        }
        long[] data = tareaDao.ObtenerDatosGraficoTareas(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(data);

    }

    /**
     * Est metodo permite comprobar que un token es correcto
     * @param token es el toke  que queremos comprobar
     * @return true si es correcto, false si no
     */
    public boolean validarToken(String token) {
        String userId = jwtUtil.getKey(token);
        return userId != null;
    }


}
