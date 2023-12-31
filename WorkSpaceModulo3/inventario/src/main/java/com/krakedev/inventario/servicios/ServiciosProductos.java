package com.krakedev.inventario.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.krakedev.inventario.entidades.Categoria;
import com.krakedev.inventario.entidades.Producto;

@Path("productos")
public class ServiciosProductos {
	
	@Path("buscar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Producto buscar() {
		Categoria c1 = new Categoria(1,"Bebidas");
		Producto p1 = new Producto("P1","Coca-cola 1L",c1,1.50,5);
		return p1;
	}
	
	@Path("insertar")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertar(Producto producto) {
		System.out.println("INSERTANDO PRODUCTO >>>" + producto);
	}
	
	@Path("actualizar")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void actualizar(Producto producto) {
		System.out.println("ACTUALIZANDO PRODUCTO >>>" + producto);
	}
	
	@Path("consultar")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Producto> recuperarTodos() {
		Categoria c1 = new Categoria(1,"Bebidas");
		Categoria c2 = new Categoria(2,"Snacks");
		Producto p1 = new Producto("P1","Coca-cola 1L",c1,1.50,5); 
		Producto p2 = new Producto("P2","Gatorade",c1,1.10,11); 
		Producto p3 = new Producto("P3","Doritos",c2,0.75,26); 
		
		ArrayList<Producto> productos = new ArrayList<>();
		productos.add(p1);
		productos.add(p2);
		productos.add(p3);
		return productos;
	}
}
