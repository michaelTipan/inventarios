drop table if exists detalle_ventas;
drop table if exists cabecera_ventas;
drop table if exists historial_stock;
drop table if exists detalle_pedidos;
drop table if exists cabecera_pedidos;
drop table if exists estados_pedidos;
drop table if exists proveedores;
drop table if exists tipo_documentos;
drop table if exists productos;
drop table if exists unidades_medida;
drop table if exists categorias_unidad_medida;
drop table if exists categorias;

create table categorias(
	codigo_cat serial not null,
	nombre varchar(100) not null,
	categoria_padre int,
	constraint categorias_pk primary key (codigo_cat),
	constraint categorias_fk foreign key (categoria_padre) references categorias(codigo_cat)
);

insert into categorias (nombre, categoria_padre)
values
	('Materia Prima', null),
	('Proteina', 1),
	('Salsas', 1),
	('Punto de Venta', null),
	('Bebidas', 4),
	('Con Alcohol', 5),
	('Sin Alcohol', 5);

create table categorias_unidad_medida(
	codigo_cum char(1) not null,
	nombre varchar(100) not null,
	constraint categorias_unidad_medida_pk primary key (codigo_cum)
);

insert into categorias_unidad_medida(codigo_cum, nombre)
values
	('U', 'Unidades'),
	('V', 'Volumen'),
	('P', 'Peso'),
	('L', 'Longitud');
	
create table unidades_medida (
	codigo_um varchar(5) not null,
	descripcion varchar(100) not null,
	categoria_um char(1) not null,
	constraint unidades_medida_pk primary key (codigo_um),
	constraint categorias_unidad_medida_fk foreign key (categoria_um) 
		references categorias_unidad_medida(codigo_cum)
);

insert into unidades_medida(codigo_um, descripcion, categoria_um)
values
	('ml','Mililitros','V'),
	('l','Litros','V'),
	('u','Unidad','U'),
	('d','Docena','U'),
	('g','Gramos','P'),
	('kg','Kilogramos','P'),
	('lb','Libras','P');
	
create table productos(
	codigo_prod int not null,
	nombre varchar(100) not null,
	unidad_medida varchar(5) not null,
	precio_venta money not null,
	tiene_iva boolean not null,
	coste money not null,
	categoria int not null,
	stock int not null,
	constraint productos_pk primary key (codigo_prod),
	constraint unidades_medida_fk foreign key (unidad_medida) 
		references unidades_medida(codigo_um),
	constraint categorias_fk foreign key (categoria) 
		references categorias(codigo_cat)
);

insert into productos
values
	(1,'Coca Cola pequeña','u',0.5804,true,0.3729,7,105),
	(2,'Salsa de Tomate','kg',0.95,true,0.8736,3,0),
	(3,'Mostaza','kg',1.1,true,0.9,3,0),
	(4,'Fuze Tea','u',0.8,true,0.7,7,49);
	
create table tipo_documentos(
	codigo_td char(1) not null,
	descripcion varchar(100) not null,
	constraint tipo_documentos_pk primary key (codigo_td)
);

insert into tipo_documentos
values
	('C','Cedula'),
	('R','Ruc');
	
create table proveedores(
	identificador varchar(20) not null,
	tipo_documento char(1) not null,
	nombre varchar(100) not null,
	telefono varchar(20) not null,
	correo varchar(100),
	direccion varchar(100) not null,
	constraint proveedores_pk primary key (identificador),
	constraint tipo_documentos_fk foreign key (tipo_documento) 
		references tipo_documentos(codigo_td)
);

insert into proveedores
values
	('1727155661','C','Santiago Mosquera','0995365287','zantycd@gmail.com','Calacalí'),
	('17922885747001','R','SNACK SA','0960123456','snacks@gmail.com','Cumbayá');

create table estados_pedidos(
	codigo_ep char(1) not null,
	descripcion varchar(20) not null,
	constraint estados_pedidos_pk primary key (codigo_ep)
);

insert into estados_pedidos
values
	('S','Solicitado'),
	('R','Recibido');

create table cabecera_pedidos(
	numero_cp serial not null,
	proveedor varchar(100) not null,
	fecha date not null,
	estado char(1) not null,
	constraint cabecera_pedidos_pk primary key (numero_cp),
	constraint proveedores_fk foreign key (proveedor) 
		references proveedores(identificador),
	constraint estados_pedidos_fk foreign key (estado) 
		references estados_pedidos(codigo_ep)
);

insert into cabecera_pedidos(proveedor, fecha, estado)
values
	('1727155661','2023-11-20','R'),
	('1727155661','2023-11-21','R');

create table detalle_pedidos(
	codigo_dp serial not null,
	cabecera_pedido int not null,
	producto int not null,
	cantidad_solicitada int not null,
	subtotal money not null,
	cantidad_recibida int not null,
	constraint detalle_pedidos_pk primary key (codigo_dp),
	constraint cabecera_pedidos_fk foreign key (cabecera_pedido) 
		references cabecera_pedidos(numero_cp),
	constraint productos_fk foreign key (producto) 
		references productos(codigo_prod)
);

insert into detalle_pedidos(cabecera_pedido, producto, cantidad_solicitada, subtotal, cantidad_recibida)
values
	(1,1,100,37.29,100),
	(1,4,50,11.80,50),
	(2,1,10,3.73,10);
	
create table historial_stock(
	codigo_hs serial not null,
	fecha timestamp not null,
	referencia varchar(20) not null,
	producto int not null,
	cantidad int not null,
	constraint historial_stock_pk primary key (codigo_hs),
	constraint productos_fk foreign key (producto) 
		references productos(codigo_prod)
);

insert into historial_stock(fecha, referencia, producto, cantidad)
values
	('2023-11-20 19:59:00','PEDIDO 1',1,100),
	('2023-11-21 19:59:00','PEDIDO 1',4,50),
	('2023-11-22 20:00:00','PEDIDO 2',1,10),
	('2023-11-23 20:00:00','VENTA 1',1,-5),
	('2023-11-24 20:00','VENTA 2',4,-1);

create table cabecera_ventas(
	codigo_cv serial not null,
	fecha  timestamp not null,
	total_sin_iva money not null,
	iva money not null,
	total money not null,
	constraint cabecera_ventas_pk primary key (codigo_cv)
);

insert into cabecera_ventas(fecha, total_sin_iva, iva, total)
values
	('2023-11-22 20:00:00',3.36,0.39,3.65);
	
create table detalle_ventas(
	codigo_dv serial not null,
	cabecera_ventas int not null,
	producto int not null,
	cantidad int not null,
	precio_venta money not null,
	subtotal money not null,
	subtotal_con_iva money not null,
	constraint detalle_ventas_pk primary key (codigo_dv),
	constraint cabecera_ventas_fk foreign key (cabecera_ventas) 
		references cabecera_ventas(codigo_cv),
	constraint productos_fk foreign key (producto)
		references productos(codigo_prod)
);

insert into detalle_ventas 
	(cabecera_ventas, producto, cantidad, precio_venta, subtotal, subtotal_con_iva)
values
	(1,1,5,0.58,2.9,3.25),
	(1,4,1,0.36,0.36,0.4);
	
select * from categorias;
select * from categorias_unidad_medida;
select * from unidades_medida;
select * from productos;
select * from proveedores;
select * from estados_pedidos;
select * from cabecera_pedidos;
select * from detalle_pedidos;
select * from historial_stock;
select * from cabecera_ventas;
select * from detalle_ventas;


