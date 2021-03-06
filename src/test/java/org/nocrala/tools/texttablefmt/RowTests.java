package org.nocrala.tools.texttablefmt;

import junit.framework.TestCase;

import org.nocrala.tools.texttablefmt.CellStyle.AbbreviationStyle;
import org.nocrala.tools.texttablefmt.CellStyle.HorizontalAlign;
import org.nocrala.tools.texttablefmt.CellStyle.NullStyle;
import org.nocrala.tools.utils.Log;

public class RowTests extends TestCase {

  public RowTests(final String txt) {
    super(txt);
  }

  public void testSeparators() {
    Log.debug("Separators");
    CellStyle cs = new CellStyle(HorizontalAlign.left, AbbreviationStyle.crop,
        NullStyle.emptyString);
    Row r = new Row();
    r.addCell("abc", cs, 1);
    r.addCell("def", cs, 1);
    r.addCell("ghi", cs, 2);
    r.addCell("jkl", cs, 1);
    r.addCell("mno", cs, 1);

    assertEquals(5, r.getSize());

    assertEquals(true, r.hasSeparator(0));
    assertEquals(true, r.hasSeparator(1));
    assertEquals(true, r.hasSeparator(2));
    assertEquals(false, r.hasSeparator(3));
    assertEquals(true, r.hasSeparator(4));
    assertEquals(true, r.hasSeparator(5));

  }

}
