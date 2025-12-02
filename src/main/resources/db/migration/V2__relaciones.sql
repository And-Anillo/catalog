-- V2__relaciones.sql - Add advanced relationships and constraints
-- This script adds additional indexes and constraints for optimized queries

ALTER TABLE usuario_categoria 
ADD CONSTRAINT fk_usuario_categoria_usuario 
FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE;

ALTER TABLE usuario_categoria 
ADD CONSTRAINT fk_usuario_categoria_categoria 
FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE CASCADE;

-- Create composite indexes for common query patterns
CREATE INDEX idx_tarea_usuario_estado ON tarea(usuario_id, estado);
CREATE INDEX idx_tarea_usuario_fecha ON tarea(usuario_id, fecha_vencimiento);
CREATE INDEX idx_tarea_estado_prioridad ON tarea(estado, prioridad);

-- Add check constraints for data integrity
ALTER TABLE tarea 
ADD CONSTRAINT chk_tarea_estado 
CHECK (estado IN ('PENDIENTE', 'EN_PROGRESO', 'COMPLETADA', 'CANCELADA'));

ALTER TABLE tarea 
ADD CONSTRAINT chk_tarea_prioridad 
CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'URGENTE'));

-- Comment on tables for documentation
COMMENT ON TABLE usuario IS 'Entidad principal que representa los usuarios del sistema';
COMMENT ON TABLE tarea IS 'Entidades de tareas asignadas a usuarios';
COMMENT ON TABLE categoria IS 'Categorías para clasificar tareas';
COMMENT ON TABLE usuario_categoria IS 'Tabla de relación muchos-a-muchos entre usuarios y categorías';

-- Commit Transaction
COMMIT;
