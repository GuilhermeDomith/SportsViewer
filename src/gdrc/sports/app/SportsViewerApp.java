package gdrc.sports.app;

import gdrc.sports.gui.es.MensagemUsuario;
import gdrc.sports.gui.janela.IgSportViewer;
import gdrc.sports.io.bd.dao.DataBaseDAO;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

/**
 * Classe que mant�m os m�todos necess�rios para a inicializa��o da
 * aplica��o <i>Sports Viewer</i>. <br><br>
 * 
 * A estrutura da aplica��o pode ser compreendida a partir dos diagramas 
 * documentados em {@link gdrc.sports.tipo} que apresenta a estrutura das 
 * classes base utilizadas e {@link gdrc.sports.io.bd.dao} que apresenta a estrutura 
 * das tabelas utilizadas para fazer a persist�nca dos dados manipulados 
 * pela aplica��o.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class SportsViewerApp {
	
	/** 
	 * Inicia o programa abrindo a conex�o com as depend�ncias 
	 * da aplica��o.
	 *   
	 * @param args Os argumentos de linha de comando n�o s�o 
	 * utilizados por esta aplica��o. 
	 */
	public static void main(String[] args) {
		alterarLookAndFeel();
		
		if(abrirConexaoBD()) {
			JFrame janelaPrincipal = new IgSportViewer();
			janelaPrincipal.addWindowListener(new TratarEventoJanela());
		}
	}
	
	/**
	 * Abre a conex�o com o banco de dados da aplica��o.
	 * @return Retorna <code>true</code> se foi aberto a conex�o, 
	 * se n�o retorna <code>false</code>.
	 */
	public static boolean abrirConexaoBD() {
		try{ DataBaseDAO.abrirConexao();
		} catch (SQLException e) {
			MensagemUsuario.msgErro("Ocorreu um erro ao conectar com o "
					+ "Banco de Dados.\nContate o suporte t�cnico.");
			return false;
		}
		return true;
	}
	
	/**
	 * Fecha a conex�o com o banco de dados da aplica��o.
	 * @return Retorna <code>true</code> se foi fechado a conex�o, 
	 * se n�o retorna <code>false</code>.
	 */
	public static boolean fecharConexaoBD() {
		try { DataBaseDAO.fecharConexao();
		} catch (SQLException e) {
			MensagemUsuario.msgErro("Ocorreu um erro ao fechar a conex�o "
					+ "com o Banco de Dados.\nContate o suporte t�cnico.");
			return false;
		}
		return true;
	}
	
	/**
	 * Altera o look and feel da aplica��o.
	 * @return Retorna <code>true</code> se for alterado com sucesso,
	 * se n�o retorna false. 
	 */
	public static boolean alterarLookAndFeel() {
		try { UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Trata o evento de fechar a janela principal da aplica��o. 
	 * Ao fechar a janela ser� feito o encerramento das conex�es 
	 * feitas pela aplica��o. <br><br>
	 * 
	 * Todas as depend�ncias que precisam ser encerradas 
	 * ao t�rmino da aplica��o devem ser implementadas 
	 * neste m�todo. 
	 *  
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	private static class TratarEventoJanela extends WindowAdapter{

		@Override
		public void windowClosing(WindowEvent e) {
			fecharConexaoBD();
			System.exit(0);
		}
	}

}
