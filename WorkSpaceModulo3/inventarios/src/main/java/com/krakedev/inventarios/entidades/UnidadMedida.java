package com.krakedev.inventarios.entidades;

public class UnidadMedida {
	private String codigo;
	private String descripcion;
	private CategoriaUnidadMedida categoria;

	public UnidadMedida() {
	}

	public UnidadMedida(String codigo, String descripcion, CategoriaUnidadMedida categoria) {
		super();
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.categoria = categoria;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public CategoriaUnidadMedida getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaUnidadMedida categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "UnidadMedida [codigo=" + codigo + ", descripcion=" + descripcion + ", categoria=" + categoria + "]";
	}

}
