package gdrc.sports.gui.es;

import static gdrc.sports.util.propriedades.Propriedade.NOME_PROGRAMA;
import static javax.swing.JOptionPane.*;

/**
 * Contém métodos para exibir janelas informativas para o usuário.
 * Pode ser utilizada para exibir ao usuário mensagens de 
 * ERRO, INFORMATIVA, ou de CONFIRMAÇÃO, em que usuário tem que 
 * confirmar ou não o que for exibido na mensagem.  
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class MensagemUsuario {
	
	/**
	 * Exibe uma caixa de diálogo com a mensagem passada parâmetro 
	 * informando um ERRO ao usuário. 
	 * 
	 * @param mensagem O objeto ou mensagem que será exibido na 
	 * caixa de diálogo.
	 */
	public static void msgErro(Object mensagem) {
		showMessageDialog(null, mensagem, NOME_PROGRAMA, ERROR_MESSAGE);
	}
	
	/**
	 * Exibe uma caixa de diálogo com a mensagem passada parâmetro 
	 * apenas como um conteúdo INFORMATIVO para usuário. 
	 * 
	 * @param mensagem O objeto ou mensagem que será exibido na 
	 * caixa de diálogo.
	 */
	public static void msgInfo(Object mensagem) {
		showMessageDialog(null, mensagem, NOME_PROGRAMA, INFORMATION_MESSAGE);
	}
	
	/**
	 * Exibe uma caixa de diálogo com a mensagem passada parâmetro 
	 * solicitando uma CONFIRMAÇÂO do que a mensagem propõe.
	 * A confirmação pelo usuário é feita selecionando SIM, NÃO ou
	 * CANCELAR. 
	 * 
	 * @param mensagem O objeto ou mensagem que será exibido na 
	 * caixa de diálogo.
	 * 
	 * @return Retorna <code>true</code> se o usuário confirmar a 
	 * mensagem com SIM. Retorna <code>false</code> se o usuário 
	 * selecionou NÃO. E caso selecionou CANCELAR, retorna 
	 * <code>null</code>.  
	 */
	public static Boolean msgConfirma(String mensagem) {
		int opcao = showConfirmDialog(null, mensagem, NOME_PROGRAMA, YES_NO_OPTION);
		if(opcao == YES_OPTION) return true;
		else if(opcao == NO_OPTION) return false;
		else return null;
	}
	
}
