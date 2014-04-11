package decoration_Package;

import java.awt.event.MouseEvent;

/*** DisplayListener interface */

interface DisplayListener {
	public void onGridClicked(Display disp, MouseEvent e, int x, int y);

	public void onGridEntered(Display disp, MouseEvent e, int x, int y);

	public void onGridExited(Display disp, MouseEvent e, int x, int y);

}