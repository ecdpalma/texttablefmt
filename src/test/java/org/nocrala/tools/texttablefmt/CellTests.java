package org.nocrala.tools.texttablefmt;

import junit.framework.TestCase;

import org.nocrala.tools.texttablefmt.CellStyle.AbbreviationStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.CellStyle.NullStyle;
import org.nocrala.tools.utils.Log;

public class CellTests extends TestCase {

  public CellTests(final String txt) {
    super(txt);
  }

  public void testAlignLeft() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Cell c = new Cell("abcdef", cs, 1);
    assertEquals("", c.render(0));
    assertEquals("a", c.render(1));
    assertEquals("ab", c.render(2));
    assertEquals("abc", c.render(3));
    assertEquals("abcd", c.render(4));
    assertEquals("abcde", c.render(5));
    assertEquals("abcdef", c.render(6));
    assertEquals("abcdef ", c.render(7));
    assertEquals("abcdef  ", c.render(8));
    assertEquals("abcdef   ", c.render(9));
    assertEquals("abcdef    ", c.render(10));
    assertEquals("abcdef     ", c.render(11));
  }

  public void testAlignCenter() {
    CellStyle cs = new CellStyle(HorizontalAlign.center,
        AbbreviationStyle.crop, NullStyle.emptyString);
    Cell c = new Cell("abcdef", cs, 1);
    assertEquals("", c.render(0));
    assertEquals("a", c.render(1));
    assertEquals("ab", c.render(2));
    assertEquals("abc", c.render(3));
    assertEquals("abcd", c.render(4));
    assertEquals("abcde", c.render(5));
    assertEquals("abcdef", c.render(6));
    assertEquals("abcdef ", c.render(7));
    assertEquals(" abcdef ", c.render(8));
    assertEquals(" abcdef  ", c.render(9));
    assertEquals("  abcdef  ", c.render(10));
    assertEquals("  abcdef   ", c.render(11));
  }

  public void testAlignRight() {
    CellStyle cs = new CellStyle(HorizontalAlign.right, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Cell c = new Cell("abcdef", cs, 1);
    assertEquals("", c.render(0));
    assertEquals("a", c.render(1));
    assertEquals("ab", c.render(2));
    assertEquals("abc", c.render(3));
    assertEquals("abcd", c.render(4));
    assertEquals("abcde", c.render(5));
    assertEquals("abcdef", c.render(6));
    assertEquals(" abcdef", c.render(7));
    assertEquals("  abcdef", c.render(8));
    assertEquals("   abcdef", c.render(9));
    assertEquals("    abcdef", c.render(10));
    assertEquals("     abcdef", c.render(11));
  }

  public void testAbbreviateCrop() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Cell c = new Cell("abcdef", cs, 1);
    assertEquals("", c.render(0));
    assertEquals("a", c.render(1));
    assertEquals("ab", c.render(2));
    assertEquals("abc", c.render(3));
    assertEquals("abcd", c.render(4));
    assertEquals("abcde", c.render(5));
    assertEquals("abcdef", c.render(6));
    assertEquals("abcdef ", c.render(7));
  }

  public void testAbbreviateDots() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.dots,
        NullStyle.emptyString);
    Cell c = new Cell("abcdef", cs, 1);
    assertEquals("", c.render(0));
    assertEquals(".", c.render(1));
    assertEquals("..", c.render(2));
    assertEquals("...", c.render(3));
    assertEquals("a...", c.render(4));
    assertEquals("ab...", c.render(5));
    assertEquals("abcdef", c.render(6));
    assertEquals("abcdef ", c.render(7));
  }

  public void testNullEmpty() {
    Log.debug("Null");
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Cell c = new Cell(null, cs, 1);
    assertEquals("", c.render(0));
    assertEquals(" ", c.render(1));
    assertEquals("  ", c.render(2));
    assertEquals("   ", c.render(3));
    assertEquals("    ", c.render(4));
    assertEquals("          ", c.render(10));
    assertEquals("           ", c.render(11));
  }

  public void testNullText() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.nullText);
    Cell c = new Cell(null, cs, 1);
    assertEquals("", c.render(0));
    assertEquals("<", c.render(1));
    assertEquals("<n", c.render(2));
    assertEquals("<nu", c.render(3));
    assertEquals("<nul", c.render(4));
    assertEquals("<null", c.render(5));
    assertEquals("<null>", c.render(6));
    assertEquals("<null> ", c.render(7));
    assertEquals("<null>     ", c.render(11));
  }

  private static final String FRS = ((char) 27) + "[0m"; // Format reset
                                                         // sequence

  public void testTerminalFormats() {
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop);
    char esc = 27;
    Cell c;

    c = new Cell("abc" + esc + "[23;45mdef", cs, 1);
    assertEquals("a", c.render(1));
    assertEquals("ab", c.render(2));
    assertEquals("abc" + esc + "[23;45m" + FRS, c.render(3));
    assertEquals("abc" + esc + "[23;45md" + FRS, c.render(4));
    assertEquals("abc" + esc + "[23;45mdef" + FRS, c.render(6));
    assertEquals("abc" + esc + "[23;45mdef " + FRS, c.render(7));

    c = new Cell(esc + "[23;45mdef", cs, 1);
    assertEquals(esc + "[23;45md" + FRS, c.render(1));
    assertEquals(esc + "[23;45mde" + FRS, c.render(2));
    assertEquals(esc + "[23;45mdef" + FRS, c.render(3));
    assertEquals(esc + "[23;45mdef " + FRS, c.render(4));

    c = new Cell("abc" + esc + "[23;45m", cs, 1);
    assertEquals("a", c.render(1));
    assertEquals("ab", c.render(2));
    assertEquals("abc" + esc + "[23;45m" + FRS, c.render(3));
    assertEquals("abc" + esc + "[23;45m " + FRS, c.render(4));

    c = new Cell("abc" + esc + "[23;45mdef" + esc + "[0mghi", cs, 1);
    assertEquals("a", c.render(1));
    assertEquals("ab", c.render(2));
    assertEquals("abc" + esc + "[23;45m" + FRS, c.render(3));
    assertEquals("abc" + esc + "[23;45md" + FRS, c.render(4));
    assertEquals("abc" + esc + "[23;45mde" + FRS, c.render(5));
    assertEquals("abc" + esc + "[23;45mdef" + esc + "[0m" + FRS, c.render(6));
    assertEquals("abc" + esc + "[23;45mdef" + esc + "[0mg" + FRS, c.render(7));
    assertEquals("abc" + esc + "[23;45mdef" + esc + "[0mgh" + FRS, c.render(8));
    assertEquals("abc" + esc + "[23;45mdef" + esc + "[0mghi" + FRS, c.render(9));
    assertEquals("abc" + esc + "[23;45mdef" + esc + "[0mghi " + FRS,
        c.render(10));

  }

}
