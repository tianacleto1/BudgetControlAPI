package com.anacleto.budgetcontrol.api.repository.lancamento;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.anacleto.budgetcontrol.api.model.Lancamento;
import com.anacleto.budgetcontrol.api.model.LancamentoMock;
import com.anacleto.budgetcontrol.api.repository.filter.LancamentoFilter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LancamentoRepositoryImplTest {
	
	@MockBean
	private CriteriaBuilder builder;
	
	@MockBean
	private CriteriaQuery<Lancamento> criteria;
	
	@MockBean
	private Root<Lancamento> root;
	
	@MockBean
	private TypedQuery<Lancamento> query;
	
	@MockBean
	private EntityManager em;

	@Autowired
	private LancamentoRepositoryImpl repository;
	
	private Lancamento lancamentoMock = LancamentoMock.criaMockLancamento();
	
	@Test
	public void whenSearchIsFilteredByDescriptionThatExistsItShouldReturnOkTest() {
		when(em.getCriteriaBuilder()).thenReturn(builder);
		when(builder.createQuery(Lancamento.class)).thenReturn(criteria);
		when(criteria.from(Lancamento.class)).thenReturn(root);
		when(em.createQuery(criteria)).thenReturn(query);
		when(query.getResultList()).thenReturn(Arrays.asList(lancamentoMock));
		
		LancamentoFilter filter = new LancamentoFilter();
		filter.setDescricao("descricaoTest");

		Lancamento lancamento = repository.filtrar(filter).get(0);
		
		assertEquals("descricaoTest", lancamento.getDescricao());
	}
	
	@Test
	public void whenSearchIsFilteredByDataVencimentoDeItShouldReturnAllLancamentosFromThatDateAheadTest() {
		when(em.getCriteriaBuilder()).thenReturn(builder);
		when(builder.createQuery(Lancamento.class)).thenReturn(criteria);
		when(criteria.from(Lancamento.class)).thenReturn(root);
		when(em.createQuery(criteria)).thenReturn(query);
		when(query.getResultList()).thenReturn(Arrays.asList(lancamentoMock));
		
		LancamentoFilter filter = new LancamentoFilter();
		filter.setDataVencimentoDe(LocalDate.of(2017, 01, 01));

		Lancamento lancamento = repository.filtrar(filter).get(0);
		
		assertEquals("Outros", lancamento.getCategoria().getNome());
	}
	
	@Test
	public void whenSearchIsFilteredByDataVencimentoDeAndDataVencimentoAteItShouldReturnAllLancamentosOnThisDateRangeTest() {
		when(em.getCriteriaBuilder()).thenReturn(builder);
		when(builder.createQuery(Lancamento.class)).thenReturn(criteria);
		when(criteria.from(Lancamento.class)).thenReturn(root);
		when(em.createQuery(criteria)).thenReturn(query);
		when(query.getResultList()).thenReturn(Arrays.asList(lancamentoMock));
		
		LancamentoFilter filter = new LancamentoFilter();
		filter.setDataVencimentoDe(LocalDate.of(2017, 02, 10));
		filter.setDataVencimentoAte(LocalDate.of(2017, 06, 15));

		Lancamento lancamento = repository.filtrar(filter).get(0);
		
		assertEquals("Outros", lancamento.getCategoria().getNome());
	}
}
