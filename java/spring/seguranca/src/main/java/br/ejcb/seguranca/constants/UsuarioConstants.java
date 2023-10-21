package br.ejcb.seguranca.constants;

public class UsuarioConstants {

	public static final String CONSTRAINT_FIELD_ID_NOT_NULL = "Campo 'id' do usuário deve ser informado";
	public static final String CONSTRAINT_FIELD_NOME_SIZE = "Campo 'nome' do usuário de ser informado com tamanho entre 5 e 100 caracteres";
	public static final String CONSTRAINT_FIELD_LOGIN_SIZE = "Campo 'login' do usuário de ser informado com tamanho entre 2 e 50 caracteres";
	public static final String CONSTRAINT_FIELD_ATIVO_NOT_NULL = "Campo 'ativo' do usuário deve ser informado";
	public static final String CONSTRAINT_FIELD_PERFIL_NOT_NULL = "Campo 'perfil' do usuário deve ser informado";
	
	private UsuarioConstants() {
	}

}
