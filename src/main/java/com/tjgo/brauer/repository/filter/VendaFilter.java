package com.tjgo.brauer.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tjgo.brauer.model.StatusVenda;

public class VendaFilter {
	
	private Long codigo;
	private StatusVenda status;
	
	private LocalDate dataCriacaoDe;
	private LocalDate dataCriacaoAte;
	private BigDecimal valorTotalDe;
	private BigDecimal valorTotalAte;
	
	private String nomeCliente;
	private String cpfCnpjCliente;	
		
	/*public VendaFilter() {

	     this.codigo = (Long) 2; // Exemplo para filtragem de código de venda = 2

	     this.valorTotalDe = BigDecimal.valueOf(0.00);
	     this.nomeCliente = "mar";
	     this.status = StatusVenda.ORCAMENTO;
	     this.valorTotalAte = BigDecimal.valueOf(100.00);

	     LocalDate dataManipulacao = LocalDate.now();
	     this.dataCriacaoDe = dataManipulacao.minusDays(30); // 30 dias atrás

	     this.dataCriacaoAte = LocalDate.now();
	}*/
	
	public Long getCodigo() {
		return codigo;
	}
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	public StatusVenda getStatus() {
		return status;
	}
	public void setStatus(StatusVenda status) {
		this.status = status;
	}
	public LocalDate getDataCriacaoDe() {
		return dataCriacaoDe;
	}
	public void setDataCriacaoDe(LocalDate dataCriacaoDe) {
		this.dataCriacaoDe = dataCriacaoDe;
	}
	public LocalDate getDataCriacaoAte() {
		return dataCriacaoAte;
	}
	public void setDataCriacaoAte(LocalDate dataCriacaoAte) {
		this.dataCriacaoAte = dataCriacaoAte;
	}
	public BigDecimal getValorTotalDe() {
		return valorTotalDe;
	}
	public void setValorTotalDe(BigDecimal valorTotalDe) {
		this.valorTotalDe = valorTotalDe;
	}
	public BigDecimal getValorTotalAte() {
		return valorTotalAte;
	}
	public void setValorTotalAte(BigDecimal valorTotalAte) {
		this.valorTotalAte = valorTotalAte;
	}
	public String getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public String getCpfCnpjCliente() {
		return cpfCnpjCliente;
	}
	public void setCpfCnpjCliente(String cpfCnpjCliente) {
		this.cpfCnpjCliente = cpfCnpjCliente;
	}
}
