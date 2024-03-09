DROP DATABASE IF EXISTS DonComanda;
CREATE DATABASE IF NOT EXISTS DonComanda;
USE DonComanda;

CREATE TABLE mesas(
	num INT PRIMARY KEY,
    ocupada BOOLEAN DEFAULT FALSE
);

CREATE TABLE productos(
	id INT PRIMARY KEY AUTO_INCREMENT,
    nombre TEXT NOT NULL,
    precio DOUBLE,
    precio_dto DECIMAL(6,2) AS (precio-(precio*(dto/100))),
    dto INT DEFAULT 0
);

CREATE TABLE facturas(
	id INT PRIMARY KEY AUTO_INCREMENT,
    mesa INT NULL,
    fecha TIMESTAMP NOT NULL,
    pagada BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (mesa) REFERENCES mesas(num)
);

CREATE TABLE detalle_factura(
	id_factura INT,
    id_producto INT,
    cantidad INT DEFAULT 1,
    PRIMARY KEY (id_factura, id_producto),
    FOREIGN KEY (id_factura) REFERENCES facturas(id) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id) ON DELETE CASCADE
);

INSERT INTO mesas(num) VALUES (1),(2),(3),(4),(5),(6);

INSERT INTO productos(nombre, precio) VALUES
	('cafe', 1.2),
	('cola cao', 1.5),
	('croissant', 0.70),
	('infusion', 1.0),
    ('magdalena', 0.7);
INSERT INTO productos(nombre, precio, dto) VALUES('napolitana', 1.2, 5);

INSERT INTO productos(nombre, precio) VALUES	
	('calamares', 13.2),
    ('racion jamon', 3.5),
    ('patatas', 1.85);
INSERT INTO productos(nombre, precio, dto) VALUES
	('croquetas', 7.3, 5),
    ('albondigas', 10.5, 10);

INSERT INTO productos(nombre, precio, dto) VALUES
	('jamon', 5.7, 5),
	('lomo', 4.85, 5),
	('tortilla', 4.95, 5),
	('york', 4.85, 5);

INSERT INTO productos(nombre, precio) VALUES
	('chocolate', 1.7),
	('nestea', 2.0),
    ('vainilla', 1.7),
    ('cocacola', 2.0),
    ('fanta', 2.0);

INSERT INTO productos(nombre, precio) VALUES
	('cerveza sin', 1.55),
    ('clara', 1.55),
    ('coronita', 2.0),
    ('jarra', 2.5);

INSERT INTO productos(nombre, precio) VALUES
	('amareto', 4.0),
	('anis', 3.0),
    ('malibu', 3.5),
    ('vodka', 3.0);

SELECT * FROM mesas;
SELECT * FROM facturas;
SELECT * FROM detalle_factura;
SELECT * FROM productos;

SELECT * FROM detalle_factura WHERE id_factura = 1;

-- Query utilizada en JasperSoft para generar la tabla de factuas simplificadas
SELECT p.precio as "Precio", df.cantidad as "Uds.", p.nombre as "Producto",
	p.dto as "Dto. %", p.precio_dto*df.cantidad as "Importe"
		FROM detalle_factura df
			INNER JOIN productos p ON df.id_producto = p.id
			WHERE id_factura = 1;
            
-- Query utilizada en JasperSoft para generar la tabla del histórico
SELECT p.precio_dto as "Precio venta", SUM(df.cantidad) as "Uds.", p.nombre as "Producto", SUM(p.precio_dto*df.cantidad) as "Importe"
FROM detalle_factura df
	INNER JOIN productos p ON df.id_producto = p.id
    INNER JOIN facturas f ON df.id_factura = f.id
    WHERE DATE(f.fecha) = CURDATE()
    GROUP BY p.nombre, p.precio_dto;