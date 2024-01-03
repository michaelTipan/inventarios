package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.CategoriasBDD;
import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("categorias")
public class ServiciosCategorias {
	
	@Path("insertar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response crear(Categoria categoria) {
		CategoriasBDD categoriaBDD = new CategoriasBDD();
		try {
			categoriaBDD.insertar(categoria);
			return Response.ok().build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("actualizar")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response actualizar(Categoria categoria) {
		CategoriasBDD categoriaBDD = new CategoriasBDD();
		try {
			categoriaBDD.actualizar(categoria);
			return Response.ok().build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("recuperar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response recuperar (){
		CategoriasBDD categoriaBDD = new CategoriasBDD();
		ArrayList<Categoria> categorias = null;
		try {
			categorias = categoriaBDD.recuperar();
			return Response.ok(categorias).build();
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
