CREATE DATABASE IF NOT EXISTS doncomanda;
USE doncomanda;

CREATE TABLE mesas(
	num INT PRIMARY KEY,
    ocupada BOOLEAN DEFAULT FALSE,
    numComnsales INT
);

CREATE TABLE productos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nombre TEXT NOT NULL,
    precio DOUBLE,
    dto INT DEFAULT 0
);

CREATE TABLE facturas(
	id INT PRIMARY KEY AUTO_INCREMENT,
    mesa INT NULL,
    fecha DATE NOT NULL,
    FOREIGN KEY (mesa) REFERENCES mesas(num)
);

CREATE TABLE detalle_factura(
	id_factura INT,
    id_producto INT,
    cantidad INT,
    FOREIGN KEY (id_factura) REFERENCES facturas(id),
    FOREIGN KEY (id_producto) REFERENCES productos(id)
);