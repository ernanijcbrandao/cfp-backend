package br.ejcb.cfp.seguranca.ms.domain.model;

//@Entity
//@Table(name = "seg_senha",
//		indexes = @Index(name = "iuk_senha_chave_senha", columnList = "chave,senha", unique = true))
//@Getter
//@Setter
//@With
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
public class Senha { // extends PanacheEntityBase {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//	
//	@Column(name = "chave", length = 1000, nullable = false, insertable = true, updatable = false)
//	private String chave;
//	
//	@Column(name = "senha", length = 1000, nullable = false, insertable = true, updatable = false)
//	private String senha;
//	
//	@Column(name = "dt_hr_expiracao", nullable = true, insertable = true, updatable = false)
//	private LocalDateTime dataExpiracao;
//	
//	@Column(name = "dt_hr_criacao", nullable = false, insertable = true, updatable = false)
//	private LocalDateTime dataHoraCriacao;
//	
//	@Column(name = "ativo", nullable = false, insertable = true, updatable = true)
//	private Boolean ativo;
	
	public static synchronized Senha create() {
		return new Senha();
	}

}
