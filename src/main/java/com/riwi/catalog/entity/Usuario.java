package com.riwi.catalog.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario", indexes = {
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_nombre", columnList = "nombre")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @OneToMany(
        mappedBy = "usuario",
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @Builder.Default
    private Set<Tarea> tareas = new HashSet<>();

    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "usuario_categoria",
        joinColumns = @JoinColumn(name = "usuario_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "categoria_id", nullable = false),
        indexes = {
            @Index(name = "idx_usuario_categoria", columnList = "usuario_id, categoria_id")
        }
    )
    @ToString.Exclude
    @Builder.Default
    private Set<Categoria> categorias = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void addTarea(Tarea tarea) {
        this.tareas.add(tarea);
        tarea.setUsuario(this);
    }

    public void removeTarea(Tarea tarea) {
        this.tareas.remove(tarea);
        tarea.setUsuario(null);
    }

    public void addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getUsuarios().add(this);
    }

    public void removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.getUsuarios().remove(this);
    }
}
