package com.listatareas.listatareas.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "tareas")
public class Tarea implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "prioridad")
    private String prioridad;
    @Column(name = "fecha_limite")
    private Date fechaLimite;
    @Column(name = "id_usuario")
    private int idUsuario;
    @Column(name = "completada", columnDefinition = "INT DEFAULT 0")
    private int completada;
}
