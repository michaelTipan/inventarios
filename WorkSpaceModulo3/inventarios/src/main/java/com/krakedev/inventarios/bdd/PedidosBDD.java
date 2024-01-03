package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidades.DetallePedido;
import com.krakedev.inventarios.entidades.EstadoPedido;
import com.krakedev.inventarios.entidades.Pedido;
import com.krakedev.inventarios.entidades.Producto;
import com.krakedev.inventarios.entidades.Proveedor;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidosBDD {
	public void insertar(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		ResultSet rsClave = null;
		int codigoCabecera = 0;

		Date fechaActual = new Date();
		java.sql.Date fechaSQL = new java.sql.Date(fechaActual.getTime());
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("insert into cabecera_pedidos (proveedor, fecha, estado)values (?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSQL);
			ps.setString(3, "S");

			ps.executeUpdate();

			rsClave = ps.getGeneratedKeys();

			if (rsClave.next()) {
				codigoCabecera = rsClave.getInt(1);
			}

			ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
			for (DetallePedido det : detallesPedido) {
				psDet = con.prepareStatement(
						"insert into detalle_pedidos (cabecera_pedido, producto, cantidad_solicitada, subtotal, cantidad_recibida) values(?,?,?,?,?)");
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidadSolicitada());
				
				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadSolicitada());
				BigDecimal subtotal = pv.multiply(cantidad);
				
				psDet.setBigDecimal(4, subtotal);
				psDet.setInt(5, 0);
				
				psDet.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar al insertar. Detalle:" + e.getMessage());
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
	
	public void recibir(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		PreparedStatement psHis = null;
		
		Date fechaActual = new Date();
		Timestamp fechaHoraActual = new Timestamp(fechaActual.getTime());
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement("update cabecera_pedidos set estado=? where numero_cp=?");
			ps.setString(1, "R");
			ps.setInt(2, pedido.getCodigo());
			ps.executeUpdate();
			ArrayList<DetallePedido> detallesPedido = pedido.getDetalles();
			for (DetallePedido det : detallesPedido) {
				psDet = con.prepareStatement(
						"update detalle_pedidos set subtotal=?, cantidad_recibida=? where codigo_dp=?");

				BigDecimal pv = det.getProducto().getPrecioVenta();
				BigDecimal cantidad = new BigDecimal(det.getCantidadRecibida());
				BigDecimal subtotal = pv.multiply(cantidad);
				
				psDet.setBigDecimal(1, subtotal);
				psDet.setInt(2, det.getCantidadRecibida());
				psDet.setInt(3, det.getCodigo());
				
				psDet.executeUpdate();
				
				psHis = con.prepareStatement(
						"insert into historial_stock (fecha, referencia, producto, cantidad) values (?,?,?,?)");
				psHis.setTimestamp(1, fechaHoraActual);
				
				String referencia = "PEDIDO " + pedido.getCodigo();
				psHis.setString(2, referencia);
				psHis.setInt(3,det.getProducto().getCodigo());
				psHis.setInt(4, det.getCantidadRecibida());
				
				psHis.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar recibir. Detalle:" + e.getMessage());
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
	
	public ArrayList<Pedido> buscarPorProveedor(Proveedor proveedor) throws KrakeDevException {
		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement psDet = null;
		ResultSet rs = null;
		ResultSet rsDet = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					"select numero_cp, proveedor, fecha, estado "
					+ "from cabecera_pedidos "
					+ "where proveedor = ?");
			ps.setString(1, proveedor.getIdentificador());
			rs = ps.executeQuery();

			while (rs.next()) {
				int codigoCat = rs.getInt("numero_cp");
				String codProv = rs.getString("proveedor");
				Proveedor prov = new Proveedor(codProv);
				Date fecha = rs.getDate("fecha");
				String estado = rs.getString("estado");
				EstadoPedido estPedido = new EstadoPedido(estado);
				
				Pedido pedido = new Pedido(codigoCat, prov, fecha, estPedido);
				pedidos.add(pedido);
			}
			
			psDet = con.prepareStatement("select dp.codigo_dp, dp.cabecera_pedido, dp.producto, dp.cantidad_solicitada, cast(dp.subtotal as decimal(6,2)), dp.cantidad_recibida "
					+ "from cabecera_pedidos cp, detalle_pedidos dp "
					+ "where cp.numero_cp = dp.cabecera_pedido "
					+ "and proveedor = ? ");
			psDet.setString(1, proveedor.getIdentificador());
			rsDet = psDet.executeQuery();
			
			while (rsDet.next()) {
				int codigoDp = rsDet.getInt("codigo_dp");
				int codPedido = rsDet.getInt("cabecera_pedido");
				Pedido cabeceraPedido = new Pedido(codPedido);
				int codigoProd = rsDet.getInt("producto");
				Producto producto = new Producto(codigoProd);
				int cantidadSolicitada = rsDet.getInt("cantidad_solicitada");
				BigDecimal subtotal = rsDet.getBigDecimal("subtotal");
				int cantidadRecibida = rsDet.getInt("cantidad_recibida");
				
				DetallePedido detPedido = new DetallePedido(codigoDp, cabeceraPedido, producto, cantidadSolicitada, subtotal, cantidadRecibida);
				
				for(Pedido pedidoRecuperado: pedidos) {
					if(pedidoRecuperado.getCodigo() == detPedido.getCabecera().getCodigo()) {
						pedidoRecuperado.getDetalles().add(detPedido);
					}
				}
			}
		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar. Detalle:" + e.getMessage());
		}
		return pedidos;
	}
}
