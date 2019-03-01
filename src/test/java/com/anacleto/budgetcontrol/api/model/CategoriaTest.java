package com.anacleto.budgetcontrol.api.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CategoriaTest {

	@Test
	public void categoriaTest() {
		Categoria categoria = new Categoria();
		categoria.setCodigo(01L);
		categoria.setNome("CategoriaTest");

		assertEquals(new Long(01), categoria.getCodigo());
		assertEquals("CategoriaTest", categoria.getNome());
		assertEquals(32, categoria.hashCode());
		assertFalse(categoria.equals(new Categoria()));
	}
}
