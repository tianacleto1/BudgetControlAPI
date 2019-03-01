package com.anacleto.budgetcontrol.api.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.anacleto.budgetcontrol.api.model.Categoria;
import com.anacleto.budgetcontrol.api.repository.CategoriaRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoriaResource.class)
public class CategoriaResourceTest {
	
	@MockBean
	private CategoriaRepository mockRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private CategoriaResource categoriaResource;
	
	private Categoria categoriaMock = criaObjCategoria();
	
	@Test
	public void getAllCategoriasTest() throws Exception {
		List<Categoria> categorias = Arrays.asList(categoriaMock);

		when(mockRepository.findAll()).thenReturn(categorias);
		Categoria categoria = categoriaResource.listarCategorias().get(0);
		
		assertEquals("NomeTest", categoria.getNome());
	}

	@Test
	public void whenGetCategoriaByCodigoExistShouldReturnCategoryJsonTest() throws Exception {
		when(mockRepository.findById(1L)).thenReturn(Optional.of(categoriaMock));
		
		this.mockMvc.perform(get("/categorias/{codigo}", "1")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.codigo", is(1)))
						.andExpect(jsonPath("$.nome", is(categoriaMock.getNome())));
	}
	
	@Test
	public void whenGetCategoriaByCodigoDoesntExistShouldReturnNoContentTest() throws Exception {
		when(mockRepository.findById(1L)).thenReturn(Optional.empty());
		
		this.mockMvc.perform(get("/categorias/{codigo}", "0")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isNotFound());
	}
	
	@Test
	public void criarCategoriaTest() throws Exception {
		when(mockRepository.save(new Categoria())).thenReturn(categoriaMock);
		
		this.mockMvc.perform(post("/categorias")
						.content("{\"nome\" : \"NomeTest\"}")
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isCreated())
						.andExpect(jsonPath("$.codigo", is(1)))
						.andExpect(jsonPath("$.nome", is(categoriaMock.getNome())));
	}
	
	private Categoria criaObjCategoria() {
		Categoria categoriaMock = new Categoria();
		categoriaMock.setCodigo(1L);
		categoriaMock.setNome("NomeTest");
		
		return categoriaMock;
	}
}
