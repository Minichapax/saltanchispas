create table usuarios(
	nickname varchar not null unique,
	nombre varchar not null,
	apellidos varchar,
	password varchar not null,
	correo varchar not null,
	id SERIAL primary key
);