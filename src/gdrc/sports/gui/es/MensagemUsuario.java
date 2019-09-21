package gdrc.sports.gui.es;

import static gdrc.sports.util.propriedades.Propriedade.NOME_PROGRAMA;
import static javax.swing.JOptionPane.*;

/**
 * Cont�m m�todos para exibir janelas informativas para o usu�rio.
 * Pode ser utilizada para exibir ao usu�rio mensagens de 
 * ERRO, INFORMATIVA, ou de CONFIRMA��O, em que usu�rio tem que 
 * confirmar ou n�o o que for exibido na mensagem.  
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class MensagemUsuario {
	
	/**
	 * Exibe uma caixa de di�logo com a mensagem passada par�metro 
	 * informando um ERRO ao usu�rio. 
	 * 
	 * @param mensagem O objeto ou mensagem que ser� exibido na 
	 * caixa de di�logo.
	 */
	public static void msgErro(Object mensagem) {
		showMessageDialog(null, mensagem, NOME_PROGRAMA, ERROR_MESSAGE);
	}
	
	/**
	 * Exibe uma caixa de di�logo com a mensagem passada par�metro 
	 * apenas como um conte�do INFORMATIVO para usu�rio. 
	 * 
	 * @param mensagem O objeto ou mensagem que ser� exibido na 
	 * caixa de di�logo.
	 */
	public static void msgInfo(Object mensagem) {
		showMessageDialog(null, mensagem, NOME_PROGRAMA, INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibe uma caixa de di�logo com a mensagem passada par�metro 
	 * solicitando uma CONFIRMA��O do que a mensagem prop�e.
	 * A confirma��o pelo usu�rio � feita selecionando SIM, N�O ou
	 * CANCELAR. 
	 * 
	 * @param mensagem O objeto ou mensagem que ser� exibido na 
	 * caixa de di�logo.
	 * 
	 * @return Retorna <code>true</code> se o usu�rio confirmar a 
	 * mensagem com SIM. Retorna <code>false</code> se o usu�rio 
	 * selecionou N�O. E caso selecionou CANCELAR, retorna 
	 * <code>null</code>.  
	 */
	public static Boolean msgConfirma(String mensagem) {
		int opcao = showConfirmDialog(null, mensagem, NOME_PROGRAMA, YES_NO_OPTION);
		if(opcao == YES_OPTION) return true;
		else if(opcao == NO_OPTION) return false;
		else return null;
	}
	
}
