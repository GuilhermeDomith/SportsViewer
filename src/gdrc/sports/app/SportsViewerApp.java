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
 * Classe que mantém os métodos necessários para a inicialização da
 * aplicação <i>Sports Viewer</i>. <br><br>
 * 
 * A estrutura da aplicação pode ser compreendida a partir dos diagramas 
 * documentados em {@link gdrc.sports.tipo} que apresenta a estrutura das 
 * classes base utilizadas e {@link gdrc.sports.io.bd.dao} que apresenta a estrutura 
 * das tabelas utilizadas para fazer a persistênca dos dados manipulados 
 * pela aplicação.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class SportsViewerApp {
	
	/** 
	 * Inicia o programa abrindo a conexão com as dependências 
	 * da aplicação.
	 *   
	 * @param args Os argumentos de linha de comando não são 
	 * utilizados por esta aplicação. 
	 */
	public static void main(String[] args) {
		alterarLookAndFeel();
		
		if(abrirConexaoBD()) {
			JFrame janelaPrincipal = new IgSportViewer();
			janelaPrincipal.addWindowListener(new TratarEventoJanela());
		}
	}
	
	/**
	 * Abre a conexão com o banco de dados da aplicação.
	 * @return Retorna <code>true</code> se foi aberto a conexão, 
	 * se não retorna <code>false</code>.
	 */
	public static boolean abrirConexaoBD() {
		try{ DataBaseDAO.abrirConexao();
		} catch (SQLException e) {
			MensagemUsuario.msgErro("Ocorreu um erro ao conectar com o "
					+ "Banco de Dados.\nContate o suporte técnico.");
			return false;
		}
		return true;
	}
	
	/**
	 * Fecha a conexão com o banco de dados da aplicação.
	 * @return Retorna <code>true</code> se foi fechado a conexão, 
	 * se não retorna <code>false</code>.
	 */
	public static boolean fecharConexaoBD() {
		try { DataBaseDAO.fecharConexao();
		} catch (SQLException e) {
			MensagemUsuario.msgErro("Ocorreu um erro ao fechar a conexão "
					+ "com o Banco de Dados.\nContate o suporte técnico.");
			return false;
		}
		return true;
	}
	
	/**
	 * Altera o look and feel da aplicação.
	 * @return Retorna <code>true</code> se for alterado com sucesso,
	 * se não retorna false. 
	 */
	public static boolean alterarLookAndFeel() {
		try { UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Trata o evento de fechar a janela principal da aplicação. 
	 * Ao fechar a janela será feito o encerramento das conexões 
	 * feitas pela aplicação. <br><br>
	 * 
	 * Todas as dependências que precisam ser encerradas 
	 * ao término da aplicação devem ser implementadas 
	 * neste método. 
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
