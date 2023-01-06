package org.jlereback.knitknot.command;

import org.jlereback.knitknot.Model;
import org.jlereback.knitknot.shapes.shape.FilledCell;

public class EditCommand implements Command {
	FilledCell newCell;
	FilledCell oldCell;
	Model model;
	int index;

	public EditCommand(FilledCell oldCell, FilledCell newCell , Model model, int index) {
		this.oldCell = oldCell;
		this.newCell = newCell;
		this.model = model;
		this.index = index;
	}

	@Override
	public void execute() {
		model.getCellList().set(index, newCell);
	}

	@Override
	public void undo() {
		model.getCellList().set(index, oldCell);
	}
}
