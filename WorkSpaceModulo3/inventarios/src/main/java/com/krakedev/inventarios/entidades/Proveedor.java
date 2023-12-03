package com.krakedev.inventarios.entidades;

public class Proveedor {
	private String identificador;
	private String tipoDocumeto;
	private String nombre;
	private String telefono;
	private String correo;
	private String direccion;

	public Proveedor() {
	}

	public Proveedor(String identificador, String tipoDocumeto, String nombre, String telefono, String correo,
			String direccion) {
		super();
		this.identificador = identificador;
		this.tipoDocumeto = tipoDocumeto;
		this.nombre = nombre;
		this.telefono = telefono;
		this.correo = correo;
		this.direccion = direccion;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getTipoDocumeto() {
		return tipoDocumeto;
	}

	public void setTipoDocumeto(String tipoDocumeto) {
		this.tipoDocumeto = tipoDocumeto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	@Override
	public String toString() {
		return "Proveedor [identificador=" + identificador + ", tipoDocumeto=" + tipoDocumeto + ", nombre=" + nombre
				+ ", telefono=" + telefono + ", correo=" + correo + ", direccion=" + direccion + "]";
	}

}
