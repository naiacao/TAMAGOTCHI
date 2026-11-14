package tamagotchi;

import javax.swing.table.DefaultTableModel;

public class ModeloTablaRanking extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] jugadores = {"Jugador 1", "Jugador 2", "Jugador 3", "Jugador 4", "Jugador 5", "Jugador 6"}; //Esto cambiar en BD
	private String[] columnas = {"POS.", "JUGADOR", "PUNTUACIÓN"};
	public ModeloTablaRanking() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		if (jugadores !=null) {
			return jugadores.length;
		}
		return 0;
		 //Cuando creemos la base de datos dependiendo de los jugadores tendremos que cambiar esto!!!!!!
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnas.length;
	}

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return columnas[column];
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getValueAt(int row, int column) {
		// TODO Auto-generated method stub
		if (column == 0) {
			return row+1;
		} else if (column== 1) {
			return jugadores[row]; //Por ahora
		} else {
			return "0 pts"; //Por ahora
		}
	}

}
