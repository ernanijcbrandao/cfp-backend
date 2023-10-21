package br.ejcb.cfp.financeiro.ms.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Table(name = "fin_conta",
		indexes = {@Index(name="idx_conta_dono_conta", columnList = "chave_usuario", unique = false)},
		uniqueConstraints = @UniqueConstraint(columnNames = "nome", name = "uk_conta_nome"))
@Getter
@Setter
@With
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Conta extends PanacheEntityBase {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "chave_usuario", length = 1000, nullable = false, insertable = true, updatable = false)
	private String chaveDonoConta;

	@Column(name = "nome", length = 50, nullable = false, insertable = true, updatable = true)
	private String nome;
	
	@Column(name = "descricao", length = 200, nullable = true, insertable = true, updatable = true)
	private String descricao;
	
	@Column(name = "dt_criacao", nullable = false, insertable = true, updatable = false)
	private LocalDate dataCriacao;
	
	@Column(name = "sd_inicial", nullable = false, insertable = true, updatable = false, precision = 15, scale = 2)
	private BigDecimal saldoInicial;
	
	@Column(name = "ativo", nullable = false, insertable = true, updatable = true)
	private Boolean ativo;
	
	public static synchronized Conta create() {
		return new Conta();
	}

}
