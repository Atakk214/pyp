-- ============================================================================
-- SISTEMA DE PROMOCIÓN Y PREVENCIÓN EN SALUD (SISTEMA PyP)
-- DATOS INICIALES DE PRUEBA Y DEMOSTRACIÓN (data.sql)
-- EVIDENCIA: GA7-AA3-EV01 - SENA ADSO FICHA 3336045
-- AUTORES: Angie Garzón & Linyermar Altuzarra
-- ============================================================================

INSERT IGNORE INTO usuario (id_usuario, nombre, sexo, edad, telefono, pais, tipo_usuario) 
VALUES ('1001', 'Angie Garzon', 'FEMENINO', 22, '3001234567', 'Colombia', 'PACIENTE');

INSERT IGNORE INTO usuario (id_usuario, nombre, sexo, edad, telefono, pais, tipo_usuario) 
VALUES ('1002', 'Linyermar Altuzarra', 'FEMENINO', 21, '3109876543', 'Colombia', 'PACIENTE');

INSERT IGNORE INTO usuario (id_usuario, nombre, sexo, edad, telefono, pais, tipo_usuario) 
VALUES ('2001', 'Dr. Carlos Mendoza', 'MASCULINO', 45, '3205557788', 'Colombia', 'PERSONAL_SALUD');

INSERT IGNORE INTO usuario (id_usuario, nombre, sexo, edad, telefono, pais, tipo_usuario) 
VALUES ('1003', 'Valentina Gomez', 'FEMENINO', 29, '3158889900', 'Colombia', 'PACIENTE');

INSERT IGNORE INTO usuario (id_usuario, nombre, sexo, edad, telefono, pais, tipo_usuario) 
VALUES ('2002', 'Dra. Patricia Silva', 'FEMENINO', 38, '3114443322', 'Colombia', 'PERSONAL_SALUD');
