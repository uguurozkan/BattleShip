package decoration_Package;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ship_Package.Ship;
import battleField_Package.Cell;

/*** Display Class */

public class Display implements DisplayListener, Serializable {
	private JPanel gridPanel;
	private JPanel mainPanel;
	private JPanel shipListPanel;
	private JPanel contentPanel;
	private JButton[][] buttons;
	private JLabel[] shipNameLabels;
	private Player player;
	private boolean isEnabled = false;
	private GameWindow gWindow;
	private DisplayListener displayListener = null;
	private Display display = this;
	private ImageIcon[] images;

	public Display(Player player, GameWindow gw) {
		this.player = player;
		this.gWindow = gw;
		constructPreview();
	}

	public Player getPlayer() {
		return this.player;
	}

	public JPanel getContentPanel() {
		return this.contentPanel;
	}

	public void constructPreview() {

		// BattleField
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 2, 2, 2));

		images = Images.images; // Order: AC, BS, SM, D, PB
		images[0] = Images.shipAC;
		images[1] = Images.shipBS;
		images[2] = Images.shipSM;
		images[3] = Images.shipD;
		images[4] = Images.shipPB;
		// images[5]= new ImageIcon("src\\ac5h.png");
		images[6] = Images.ship;
		images[7] = Images.sea;
		images[8] = Images.hit;
		images[9] = Images.sunk;
		images[10] = Images.explode;
		// Storages ships
		shipListPanel = new JPanel(); // TODO add icon to each label. DONE!!!
		Ship[] ships = getPlayer().getShips();
		shipListPanel.setLayout(new GridLayout(2 * ships.length, 1));
		// shipListPanel.setBackground(Color.cyan);
		shipNameLabels = new JLabel[ships.length];
		for (int i = 0; i < shipNameLabels.length; i++) {
			shipNameLabels[i] = new JLabel(ships[i].getName());
			// JLabel shipLabel = shipNameLabels[i];
			shipNameLabels[i].setBorder(new EmptyBorder(2, 2, 2, 2));
			shipListPanel.add(shipNameLabels[i]);
			shipListPanel.add(new JLabel(images[i]));
		}

		// Storages the grid
		gridPanel = new JPanel();
		buttons = new JButton[Game.GRID_SIZE][Game.GRID_SIZE];
		gridPanel.setLayout(new GridLayout(Game.GRID_SIZE, Game.GRID_SIZE));

		for (int i = 0; i < Game.GRID_SIZE; i++) {
			for (int j = 0; j < Game.GRID_SIZE; j++) {
				JButton button = createButton(i, j); // TODO Add mouse listener.
														// DONE!!!
				buttons[i][j] = button;
				// buttons[i][j].add(seaLabel);
				gridPanel.add(button);
			}
		}

		// Composition of grid and ship
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
		// contentPanel.add(new JLabel((player.getName() + "'s Grid")),
		// BorderLayout.NORTH);
		contentPanel.add(gridPanel, BorderLayout.CENTER);

		String allignment;
		if (player == gWindow.getCPU()) {
			allignment = BorderLayout.EAST;
		} else {
			allignment = BorderLayout.WEST;
		}
		contentPanel.add(shipListPanel, allignment);

	}

	private JButton createButton(final int i, final int j) {
		JButton button = new JButton(" ");
		button.addMouseListener(new buttonHandler(i, j));
		return button;
	}

	// Arrange buttons accessibility
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		for (int i = 0; i < Game.GRID_SIZE; i++) {
			for (int j = 0; j < Game.GRID_SIZE; j++) {
				buttons[i][j].setEnabled(this.isEnabled);
			}
		}
	}

	// Paints cells
	public void paintCell(int x, int y, Color color) {
		this.buttons[x][y].setBackground(color);
		if (Cell.DISPLAY_OCCUPIED == getPlayer().getGrid().getCell(x, y)
				.displayState(gWindow.getHumanPlayer())) {
			this.buttons[x][y].setIcon(images[6]);
		}

		if (Cell.DISPLAY_MISS == getPlayer().getGrid().getCell(x, y)
				.displayState(gWindow.getCPU())) {
			this.buttons[x][y].setIcon(images[7]);
		}

		if (Cell.DISPLAY_HIT == getPlayer().getGrid().getCell(x, y)
				.displayState(gWindow.getCPU())) {
			this.buttons[x][y].setIcon(images[8]);
		}

		if (Cell.DISPLAY_SUNK == getPlayer().getGrid().getCell(x, y)
				.displayState(gWindow.getCPU())) {
			this.buttons[x][y].setIcon(images[9]);
		}
	}

	// Updates the game
	public void repaint() {
		repaintShipNameLabels();
		repaintGrid();
	}

	// Updates the labels
	private void repaintShipNameLabels() {
		Ship[] ships = getPlayer().getShips();
		Color color;
		for (int i = 0; i < ships.length; i++) {
			if (ships[i].isSunk()) {
				color = Color.RED;
				shipNameLabels[i].setText("<html><strike>"
						+ shipNameLabels[i].getText() + "</strike></html>");
				// images[i].paintIcon(null, null, 0, 0);
			} else {
				color = Color.GREEN;
			}

			shipNameLabels[i].setForeground(color);
		}
	}

	// Updates Grid
	private void repaintGrid() {
		for (int i = 0; i < Game.GRID_SIZE; i++) {
			for (int j = 0; j < Game.GRID_SIZE; j++) {
				repaintCell(i, j);
			}
		}
	}

	// Updates Cell
	private void repaintCell(int x, int y) {
		getPlayer().getGrid().sunkShipCellStatus();
		int cellStatus = getPlayer().getGrid().getCell(x, y)
				.displayState(gWindow.getHumanPlayer());
		Color color = colorForCell(cellStatus);
		if (cellStatus != 0)
			paintCell(x, y, color); // TODO There is a bug here. DONE!!!
	}

	class buttonHandler extends MouseAdapter implements Serializable {
		int i;
		int j;

		public buttonHandler(int i, int j) {
			this.i = i;
			this.j = j;
		}

		public void mousePressed(MouseEvent e) {
			onGridClicked(display, e, i, j);
		}

		public void mouseEntered(MouseEvent e) {
			onGridEntered(display, e, i, j);
		}

		public void mouseExited(MouseEvent e) {
			onGridExited(display, e, i, j);
		}
	}

	// Determines Cell's colors
	private Color colorForCell(int cellStatus) {
		switch (cellStatus) {
		case Cell.DISPLAY_SUNK:
			return Color.BLACK;
		case Cell.DISPLAY_OCCUPIED:
			return Color.GREEN;
		case Cell.DISPLAY_MISS:
			return Color.BLUE;
		case Cell.DISPLAY_HIT:
			return Color.RED;
		default:
			if (isEnabled)
				return Color.WHITE;
			else
				return Color.LIGHT_GRAY;
		}
	}

	public void setListener(DisplayListener displayListener) {
		this.displayListener = displayListener;
	}

	@Override
	public void onGridClicked(Display disp, MouseEvent e, int x, int y) {
		if (displayListener == null && !isEnabled)
			return;
		else
			displayListener.onGridClicked(disp, e, x, y);
	}

	@Override
	public void onGridEntered(Display disp, MouseEvent e, int x, int y) {
		if (displayListener == null && !isEnabled)
			return;
		else
			displayListener.onGridEntered(disp, e, x, y);
	}

	@Override
	public void onGridExited(Display disp, MouseEvent e, int x, int y) {
		if (displayListener == null && !isEnabled)
			return;
		else
			displayListener.onGridExited(disp, e, x, y);
	}

}
