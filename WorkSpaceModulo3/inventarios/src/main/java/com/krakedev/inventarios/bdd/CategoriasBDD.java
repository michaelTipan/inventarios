package com.krakedev.inventarios.bdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class CategoriasBDD {
	public void insertar(Categoria categoria) throws KrakeDevException {
		Connection con = null;
		try {
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"insert into categorias (nombre, categoria_padre) values (?,?)");
			ps.setString(1, categoria.getNombre());
			ps.setInt(2, categoria.getCategoriaPadre().getCodigo());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar la CATEGORIA. Detalle:" + e.getMessage());
		} catch (KrakeDevException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void actualizar(Categoria categoria) throws KrakeDevException {
		Connection con = null;
		try {
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"update categorias set nombre=?, categoria_padre=? where codigo_cat=?");
			ps.setString(1, categoria.getNombre());
			ps.setInt(2, categoria.getCategoriaPadre().getCodigo());
			ps.setInt(3, categoria.getCodigo());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al actualizar la CATEGORIA. Detalle:" + e.getMessage());
		} catch (KrakeDevException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public ArrayList<Categoria> recuperar() throws KrakeDevException {
		ArrayList<Categoria> categorias = new ArrayList<Categoria>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Categoria categoria = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					"select codigo_cat, nombre, categoria_padre from categorias");
			rs = ps.executeQuery();

			while (rs.next()) {
				int codigoCat = rs.getInt("codigo_cat");
				String nombre = rs.getString("nombre");
				int categoriaPadre = rs.getInt("categoria_padre");
				
				Categoria padre = new Categoria(categoriaPadre);
				categoria = new Categoria(codigoCat, nombre, padre);
				categorias.add(categoria);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar. Detalle:" + e.getMessage());
		}
		return categorias;
	}
}
