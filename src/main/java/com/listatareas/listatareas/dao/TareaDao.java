package com.listatareas.listatareas.dao;

import com.listatareas.listatareas.models.Tarea;

import java.util.List;

public interface TareaDao {
    List<Tarea> obtenerTareas(String email);
    boolean insertar(Tarea tarea);
    void eliminar(int id);
    boolean modificar(Tarea tarea);
    boolean completarTarea(int id);
    Tarea obtenerTarea(String nombre);
    double obtenerPorcentajeTareasCompetadas(String email);
    int obtenerTareasCompletadas(String email);
    double obtenerPorcentajeTareasPendientes(String email);
    int obtenerTareasPendientes(String email);
    double obtenerPorcentajePorPrioridad(String email, String prioridad);
    long obtenerTareasPorPrioridad(String email, String prioridad);
    public long[] ObtenerDatosGraficoTareas(String email);
}
