package com.krakedev.inventario.servicios;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.krakedev.inventario.entidades.Categoria;

@Path("categorias")
public class ServiciosCategoria {
	
	@Path("buscar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Categoria buscar() {
		Categoria c1 = new Categoria(1,"Bebidas");
		return c1;
	}
	
	@Path("insertar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertar(Categoria categoria) {
		System.out.println("INSERTANDO CATEGORIA >>>" + categoria);
	}
}
