package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidades.Categoria;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.UnidadMedida;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProductosBDD {
	public ArrayList<Producto> buscar(String subcadena) throws KrakeDevException {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto p = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					"select prod.codigo_prod, prod.nombre, um.codigo_um, um.descripcion as descripcion_um, "
					+ "cast(prod.precio_venta as decimal(6,2)), prod.tiene_iva, cast(prod.coste as decimal(6,2)), cat.codigo_cat, cat.nombre as nombre_cat, prod.stock "
					+ "from productos prod, unidades_medida um, categorias cat "
					+ "where prod.unidad_medida = um.codigo_um "
					+ "and prod.categoria = cat.codigo_cat "
					+ "and upper(prod.nombre) like ?");
			ps.setString(1, "%" + subcadena.toUpperCase() + "%");
			rs = ps.executeQuery();

			while (rs.next()) {
				int codigoProd = rs.getInt("codigo_prod");
				String nombre = rs.getString("nombre");
				String codigoUM = rs.getString("codigo_um");
				String descripcionUM = rs.getString("descripcion_um");
				BigDecimal precioVenta = rs.getBigDecimal("precio_venta");
				boolean tieneIva = rs.getBoolean("tiene_iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				int codigoCat = rs.getInt("codigo_cat");
				String nombreCat = rs.getString("nombre_cat");
				int stock = rs.getInt("stock");
				UnidadMedida um = new UnidadMedida();
				um.setCodigo(codigoUM);
				um.setDescripcion(descripcionUM);
				
				Categoria categoria = new Categoria();
				categoria.setCodigo(codigoCat);
				categoria.setNombre(nombreCat);
				
				p = new Producto();
				p.setCodigo(codigoProd);
				p.setNombre(nombre);
				p.setUnidadMedida(um);
				p.setPrecioVenta(precioVenta);
				p.setTieneIva(tieneIva);
				p.setCoste(coste);
				p.setCategoria(categoria);
				p.setStock(stock);
				productos.add(p);
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar. Detalle:" + e.getMessage());
		}
		return productos;
	}
	
	public void insertar(Producto producto) throws KrakeDevException {
		Connection con = null;
		try {
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"insert into productos (codigo_prod, nombre, unidad_medida, precio_venta, tiene_iva, coste, categoria, stock) values (?,?,?,?,?,?,?,?)");
			ps.setInt(1, producto.getCodigo());
			ps.setString(2, producto.getNombre());
			ps.setString(3, producto.getUnidadMedida().getCodigo());
			ps.setBigDecimal(4, producto.getPrecioVenta());
			ps.setBoolean(5, producto.isTieneIva());
			ps.setBigDecimal(6, producto.getCoste());
			ps.setInt(7, producto.getCategoria().getCodigo());
			ps.setInt(8, producto.getStock());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar el PRODUCTO. Detalle:" + e.getMessage());
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
}
