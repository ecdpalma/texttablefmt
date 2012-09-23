package org.nocrala.tools.texttablefmt;

import java.util.ArrayList;
import java.util.List;

import org.nocrala.tools.utils.Log;

class Row {

  private List<Cell> cells;

  Row() {
    this.cells = new ArrayList<Cell>();
  }

  public void addCell(final String content, final CellStyle style,
      final int colSpan) {
    this.cells.add(new Cell(content, style, colSpan));
  }

  public boolean hasSeparator(final int pos) {
    Log.debug("----> pos=" + pos);
    if (pos == 0) {
      return true;
    }
    int i = 0;
    for (Cell cell : this.cells) {
      Log.debug("i=" + i);
      if (i < pos) {
        if (i + cell.getColSpan() > pos) {
          Log.debug("TRUE");
          return false;
        }
      } else {
        return true;
      }
      i = i + cell.getColSpan();
    }
    return true;
  }

  int getSize() {
    return this.cells.size();
  }

  Cell get(final int index) {
    return this.cells.get(index);
  }

  List<Cell> getCells() {
    return this.cells;
  }

}
