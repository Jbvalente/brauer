package com.tjgo.brauer.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.StringUtils;

import com.tjgo.brauer.validation.Sku;

@Entity
@Table (name="cerveja")
public class Cerveja implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long codigo;
	
	@Sku
	@NotBlank(message="Sku é obrigatório!")
	private String sku ;  //unidade de manutenção de estoque - Stock Keeping Unit
	
	@NotBlank(message="Nome é obrigatório!")
	private String nome;
	
	@NotBlank(message="A descrição é obrigatória!")
	@Size(min=1, max=4000, message="O tamanho da descrição deve estar entre {min} e {max} caracteres!")
	private String descricao;
	
	private String foto;
	
	@NotNull(message="Valor é obrigatório!")
	@DecimalMin("0.01")
	@DecimalMax(value="9999999.99", message="O valor da cerveja deve ser menor que R$ 9.999.999,99")
	private BigDecimal valor;
	
	@NotNull(message="O teor alcóolico é obrigatório!")
	@DecimalMax(value="100.0", message="O teor alcóolico deve ser menor ou igual a 100%")
	@Column(name="teor_alcoolico")
	private BigDecimal teorAlcoolico;
	
	@NotNull(message="A comissão é obrigatória!")
	@DecimalMax(value="100.0", message="A comissão deve ser menor ou igual a 100%")
	private BigDecimal comissao;
	
	@NotNull(message="A quantidade em estoque é obrigatória!")
	@Max(value=9999, message="A quantidade em estoque deve ser menor que 9.999")
	@Column(name="quantidade_estoque")
	private Integer quantidadeEstoque;
	
	@NotNull(message="A origem é obrigatória!")
	@Enumerated(EnumType.STRING)
	private Origem origem;
	
	@NotNull(message="O sabor é obrigatório!")
	@Enumerated(EnumType.STRING)
	private Sabor sabor;
	
	@NotNull(message="O estilo é obrigatório!")
	@ManyToOne
	@JoinColumn(name="codigo_estilo")
	private Estilo estilo;
	
	@Column(name="tipo_arquivo")
	private String tipoArquivo;
	
	@Transient
	private boolean novaFoto;
	
	@PrePersist
	@PreUpdate
	private void prePersistUdate(){
		sku = sku.toUpperCase();
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getTeorAlcoolico() {
		return teorAlcoolico;
	}

	public void setTeorAlcoolico(BigDecimal teorAlcoolico) {
		this.teorAlcoolico = teorAlcoolico;
	}

	public BigDecimal getComissao() {
		return comissao;
	}

	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}

	public Sabor getSabor() {
		return sabor;
	}

	public void setSabor(Sabor sabor) {
		this.sabor = sabor;
	}

	public Estilo getEstilo() {
		return estilo;
	}

	public void setEstilo(Estilo estilo) {
		this.estilo = estilo;
	}
	
	public String getTipoArquivo() {
		return tipoArquivo;
	}

	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}
	
	public String getFotoOuMock() {
		return !StringUtils.isEmpty(foto) ? foto : "cerveja-mock.png";
	}
	
	public boolean temFoto(){
		return !StringUtils.isEmpty(this.foto);
	}
	
	public boolean isNova(){
		return codigo == null;
	}
	
	public boolean isNovaFoto() {
		return novaFoto;
	}

	public void setNovaFoto(boolean novaFoto) {
		this.novaFoto = novaFoto;
	}

	@Override
	public String toString() {
		return "Cerveja [codigo=" + codigo + ", sku=" + sku + ", nome=" + nome + ", descricao=" + descricao + ", foto="
				+ foto + ", valor=" + valor + ", teorAlcoolico=" + teorAlcoolico + ", comissao=" + comissao
				+ ", quantidadeEstoque=" + quantidadeEstoque + ", origem=" + origem + ", sabor=" + sabor + ", estilo="
				+ estilo + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cerveja other = (Cerveja) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}