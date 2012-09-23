package org.nocrala.tools.texttablefmt;

import junit.framework.TestCase;

import org.nocrala.tools.texttablefmt.CellStyle.AbbreviationStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.CellStyle.NullStyle;
import org.nocrala.tools.utils.Log;

public class TableTests extends TestCase {

  public TableTests(final String txt) {
    super(txt);
  }

  public void testEmpty() {
    Log.debug("Empty");
    Table t = new Table(10, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    assertEquals("", t.render());
  }

  public void testOneCell() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(1, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.addCell("abcdef", cs);
    assertEquals("" //
        + "+------+\n" //
        + "|abcdef|\n" //
        + "+------+", t.render());
  }

  public void testNullCell() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(1, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.addCell(null, cs);
    assertEquals("" //
        + "++\n" //
        + "||\n" //
        + "++", t.render());
  }

  public void testEmptyCell() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(1, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.addCell("", cs);
    assertEquals("" //
        + "++\n" //
        + "||\n" //
        + "++", t.render());
  }

  public void testTwoCellsHorizontal() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(2, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.addCell("abcdef", cs);
    t.addCell("123456", cs);
    // logger.debug("t=" + t.render());
    assertEquals("" //
        + "+------+------+\n" //
        + "|abcdef|123456|\n" //
        + "+------+------+", t.render());
  }

  public void testTwoCellsVertical() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(1, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.addCell("abcdef", cs);
    t.addCell("123456", cs);
    // logger.debug("t=" + t.render());
    assertEquals("" //
        + "+------+\n" //
        + "|abcdef|\n" //
        + "+------+\n" //
        + "|123456|\n" //
        + "+------+", t.render());
  }

  public void testMarginPrompt() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(1, BorderStyle.CLASSIC, ShownBorders.ALL, false,
        "prompt");
    t.addCell("abcdef", cs);
    t.addCell("123456", cs);
    // logger.debug("t=" + t.render());
    assertEquals(""//
        + "prompt+------+\n" //
        + "prompt|abcdef|\n" //
        + "prompt+------+\n" //
        + "prompt|123456|\n" //
        + "prompt+------+", t.render());
  }

  public void testMarginSpaces() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(1, BorderStyle.CLASSIC, ShownBorders.ALL, false, 4);
    t.addCell("abcdef", cs);
    t.addCell("123456", cs);
    // logger.debug("t=" + t.render());
    assertEquals(""//
        + "    +------+\n" //
        + "    |abcdef|\n" //
        + "    +------+\n" //
        + "    |123456|\n" //
        + "    +------+", t.render());
  }

  public void testAutomaticWidth() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(2, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.addCell("abcdef", cs);
    t.addCell("123456", cs);
    t.addCell("mno", cs);
    t.addCell("45689", cs);
    t.addCell("xyztuvw", cs);
    t.addCell("01234567", cs);
    // logger.debug("t=" + t.render());
    assertEquals("" //
        + "+-------+--------+\n" //
        + "|abcdef |123456  |\n" //
        + "+-------+--------+\n" //
        + "|mno    |45689   |\n" //
        + "+-------+--------+\n" //
        + "|xyztuvw|01234567|\n" //
        + "+-------+--------+", t.render());
  }

  public void testSetWidth() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(2, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.setColumnWidth(0, 6, 10);
    t.setColumnWidth(1, 2, 7);

    t.addCell("abcd", cs);
    t.addCell("123456", cs);
    t.addCell("mno", cs);
    t.addCell("45689", cs);
    t.addCell("xyztu", cs);
    t.addCell("01234567", cs);
    // logger.debug("t=" + t.render());
    assertEquals(""//
        + "+------+-------+\n" //
        + "|abcd  |123456 |\n" //
        + "+------+-------+\n" //
        + "|mno   |45689  |\n" //
        + "+------+-------+\n" //
        + "|xyztu |0123456|\n" //
        + "+------+-------+", t.render());
  }

  public void testMissingCell() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Table t = new Table(2, BorderStyle.CLASSIC, ShownBorders.ALL, false, "");
    t.setColumnWidth(0, 6, 10);
    t.setColumnWidth(1, 2, 7);

    t.addCell("abcd", cs);
    t.addCell("123456", cs);
    t.addCell("mno", cs);
    t.addCell("45689", cs);
    t.addCell("xyztu", cs);
    // logger.debug("t=\n" + t.render());
    assertEquals(""//
        + "+------+------+\n" //
        + "|abcd  |123456|\n" //
        + "+------+------+\n" //
        + "|mno   |45689 |\n" //
        + "+------+------+\n" //
        + "|xyztu |      |\n" //
        + "+------+------+", t.render());
  }

}
