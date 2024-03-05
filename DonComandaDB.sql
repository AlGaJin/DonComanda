DROP DATABASE IF EXISTS DonComanda;
CREATE DATABASE IF NOT EXISTS DonComanda;
USE DonComanda;

CREATE TABLE mesas(
	num INT PRIMARY KEY,
    ocupada BOOLEAN DEFAULT FALSE,
    numComensales INT DEFAULT 0
);

CREATE TABLE productos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nombre TEXT NOT NULL,
    precio DOUBLE,
    dto INT DEFAULT 0
);

CREATE TABLE facturas(
	id INT PRIMARY KEY AUTO_INCREMENT,
    mesa INT,
    fecha DATE NOT NULL,
    pagada BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (mesa) REFERENCES mesas(num)
);

CREATE TABLE detalle_factura(
	id_factura INT,
    id_producto INT,
    cantidad INT,
    PRIMARY KEY (id_factura, id_producto),
    FOREIGN KEY (id_factura) REFERENCES facturas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id) ON DELETE CASCADE
);

INSERT INTO mesas(num) VALUES(1);
INSERT INTO mesas(num) VALUES(2);
INSERT INTO mesas(num) VALUES(3);
INSERT INTO mesas(num) VALUES(4);

INSERT INTO productos(nombre, precio) VALUES('cafe', 1.2);
INSERT INTO productos(nombre, precio) VALUES('cola cao', 1.5);
INSERT INTO productos(nombre, precio) VALUES('croissant', 0.70);
INSERT INTO productos(nombre, precio) VALUES('infusion', 1.0);
INSERT INTO productos(nombre, precio, dto) VALUES('napolitana', 1.2, 5);
INSERT INTO productos(nombre, precio) VALUES('magdalena', 0.7);

INSERT INTO productos(nombre, precio, dto) VALUES('albondigas', 10.5, 10);
INSERT INTO productos(nombre, precio) VALUES('calamares', 13.2);
INSERT INTO productos(nombre, precio, dto) VALUES('croquetas', 7.3, 5);
INSERT INTO productos(nombre, precio) VALUES('racion jamon', 3.5);
INSERT INTO productos(nombre, precio) VALUES('patatas', 1.85);

INSERT INTO productos(nombre, precio, dto) VALUES('jamon', 5.7, 5);
INSERT INTO productos(nombre, precio, dto) VALUES('lomo', 4.85, 5);
INSERT INTO productos(nombre, precio, dto) VALUES('tortilla', 4.95, 5);
INSERT INTO productos(nombre, precio, dto) VALUES('york', 4.85, 5);

INSERT INTO productos(nombre, precio) VALUES('chocolate', 1.7);
INSERT INTO productos(nombre, precio) VALUES('vainilla', 1.7);
INSERT INTO productos(nombre, precio) VALUES('cocacola', 2.0);
INSERT INTO productos(nombre, precio) VALUES('fanta', 2.0);
INSERT INTO productos(nombre, precio) VALUES('nestea', 2.0);

INSERT INTO productos(nombre, precio) VALUES('cerveza sin', 1.55);
INSERT INTO productos(nombre, precio) VALUES('clara', 1.55);
INSERT INTO productos(nombre, precio) VALUES('coronita', 2.0);
INSERT INTO productos(nombre, precio) VALUES('jarra', 2.5);

INSERT INTO productos(nombre, precio) VALUES('amareto', 4.0);
INSERT INTO productos(nombre, precio) VALUES('anis', 3.0);
INSERT INTO productos(nombre, precio) VALUES('malibu', 3.5);
INSERT INTO productos(nombre, precio) VALUES('vodka', 3.0);