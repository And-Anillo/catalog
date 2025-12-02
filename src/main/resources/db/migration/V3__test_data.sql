-- V3__test_data.sql - Insert initial test data
-- This script populates the database with sample data for testing and validation

-- Insert test users
INSERT INTO usuario (nombre, email, descripcion, fecha_creacion, activo) VALUES
('Juan Pérez', 'juan.perez@example.com', 'Desarrollador Senior', NOW(), true),
('María García', 'maria.garcia@example.com', 'Diseñadora UX', NOW(), true),
('Carlos López', 'carlos.lopez@example.com', 'Product Manager', NOW(), true),
('Ana Martínez', 'ana.martinez@example.com', 'QA Engineer', NOW(), false);

-- Insert test categories
INSERT INTO categoria (nombre, descripcion, color, fecha_creacion, activo) VALUES
('Desarrollo', 'Tareas relacionadas con desarrollo de software', '#0052CC', NOW(), true),
('Diseño', 'Tareas de diseño gráfico e interfaz', '#FF6B6B', NOW(), true),
('Testing', 'Tareas de pruebas y control de calidad', '#4ECDC4', NOW(), true),
('Documentación', 'Tareas relacionadas con documentación', '#FFE66D', NOW(), true),
('Operaciones', 'Tareas operacionales del proyecto', '#95E1D3', NOW(), true);

-- Insert test tasks for Juan Pérez (usuario_id = 1)
INSERT INTO tarea (titulo, descripcion, estado, prioridad, usuario_id, categoria_id, fecha_vencimiento, fecha_creacion) VALUES
('Implementar API REST', 'Crear endpoints para gestión de tareas', 'PENDIENTE', 'URGENTE', 1, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), NOW()),
('Revisar código', 'Code review del módulo de autenticación', 'EN_PROGRESO', 'ALTA', 1, 1, DATE_ADD(NOW(), INTERVAL 5 DAY), NOW()),
('Escribir tests unitarios', 'Tests para funciones de validación', 'PENDIENTE', 'MEDIA', 1, 3, DATE_ADD(NOW(), INTERVAL 10 DAY), NOW()),
('Documentar API', 'Crear documentación Swagger', 'PENDIENTE', 'MEDIA', 1, 4, DATE_ADD(NOW(), INTERVAL 15 DAY), NOW()),
('Tarea completada', 'Esta tarea ya fue completada', 'COMPLETADA', 'BAJA', 1, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 20 DAY)),
('Tarea cancelada', 'Esta tarea fue cancelada', 'CANCELADA', 'BAJA', 1, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 15 DAY));

-- Insert test tasks for María García (usuario_id = 2)
INSERT INTO tarea (titulo, descripcion, estado, prioridad, usuario_id, categoria_id, fecha_vencimiento, fecha_creacion) VALUES
('Diseñar mockups', 'Crear mockups del nuevo dashboard', 'EN_PROGRESO', 'ALTA', 2, 2, DATE_ADD(NOW(), INTERVAL 3 DAY), NOW()),
('Revisar componentes UI', 'Revisar consistencia de componentes', 'PENDIENTE', 'MEDIA', 2, 2, DATE_ADD(NOW(), INTERVAL 8 DAY), NOW()),
('Prototipar interacciones', 'Crear prototipo en Figma', 'PENDIENTE', 'ALTA', 2, 2, DATE_ADD(NOW(), INTERVAL 7 DAY), NOW());

-- Insert test tasks for Carlos López (usuario_id = 3)
INSERT INTO tarea (titulo, descripcion, estado, prioridad, usuario_id, categoria_id, fecha_vencimiento, fecha_creacion) VALUES
('Planificar sprint', 'Definir backlog del próximo sprint', 'PENDIENTE', 'URGENTE', 3, 5, DATE_ADD(NOW(), INTERVAL 1 DAY), NOW()),
('Reunión stakeholders', 'Reunión con cliente para requerimientos', 'PENDIENTE', 'ALTA', 3, 5, DATE_ADD(NOW(), INTERVAL 2 DAY), NOW()),
('Seguimiento de tareas', 'Revisar estado de todas las tareas del equipo', 'EN_PROGRESO', 'MEDIA', 3, 5, DATE_ADD(NOW(), INTERVAL 1 DAY), NOW()),
('Tarea sin categoría', 'Esta tarea no tiene categoría asignada', 'PENDIENTE', 'BAJA', 3, NULL, DATE_ADD(NOW(), INTERVAL 14 DAY), NOW());

-- Insert user-category relationships
INSERT INTO usuario_categoria (usuario_id, categoria_id) VALUES
(1, 1), (1, 3), (1, 4),
(2, 2), (2, 4),
(3, 5), (3, 1), (3, 2);

-- Verify data insertion
SELECT COUNT(*) as total_usuarios FROM usuario;
SELECT COUNT(*) as total_categorias FROM categoria;
SELECT COUNT(*) as total_tareas FROM tarea;
SELECT COUNT(*) as total_relaciones FROM usuario_categoria;
