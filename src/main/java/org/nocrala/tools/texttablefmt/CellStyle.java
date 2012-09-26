package org.nocrala.tools.texttablefmt;

import org.nocrala.tools.utils.Filler;

/**
 * <p>
 * Defines how the content of a cell is rendered.
 * </p>
 * 
 * </p>It allows to specify the text alignment, the abbreviation mode and
 * rendering of null values.</p>
 * 
 * @author valarcon
 * 
 */

public class CellStyle {

  private static final HorizontalAlign DEFAULT_HORIZONTAL_ALIGN = HorizontalAlign.left;

  private static final AbbreviationStyle DEFAULT_ABBREVIATION_STYLE = AbbreviationStyle.dots;

  private static final NullStyle DEFAULT_NULL_STYLE = NullStyle.emptyString;

  private static final boolean DEFAULT_HANDLE_TERMINAL_FORMATS = true;

  /**
   * This enumeration is used to specify how a text is horizontally aligned in a
   * cell.
   * 
   * @author valarcon
   */
  public enum HorizontalAlign {
    /**
     * Will align to the left.
     */
    left,
    /**
     * Will center the text.
     */
    center,
    /**
     * Will align to the right.
     */
    right
  };

  /**
   * This enumeration is used to specify how to reduce a text to fit it in a
   * small cell.
   * 
   * @author valarcon
   */
  public enum AbbreviationStyle {
    /**
     * Will simply crop the text at the maximum allowed length.
     */
    crop,
    /**
     * Will add three dots at the end of the text to show it has been
     * abbreviated.
     */
    dots
  };

  private static final String NULL_TEXT = "<null>";

  private static final String DOTS_TEXT = "...";

  /**
   * This enumeration is used to specify how to display cell with null values.
   * 
   * @author valarcon
   */
  public enum NullStyle {
    /**
     * Will show a empty string (zero-length).
     */
    emptyString,
    /**
     * Will display the text <code>&lt;null&gt;</code> instead.
     */
    nullText
  };

  private HorizontalAlign horAlign;

  private AbbreviationStyle abbStyle;

  private NullStyle nullStyle;

  private boolean handleTerminalFormats;

  /**
   * <p>
   * Default style that assumes <b>HorizontalAlign.left</b>,
   * <b>AbbreviationStyle.dots</b> and <b>NullStyle.emptyString</b>.
   * </p>
   */

  public CellStyle() {
    initialize(DEFAULT_HORIZONTAL_ALIGN, DEFAULT_ABBREVIATION_STYLE,
        DEFAULT_NULL_STYLE, DEFAULT_HANDLE_TERMINAL_FORMATS);
  }

  /**
   * <p>
   * Style with a specified horizontal alignment, that assumes
   * <b>AbbreviationStyle.dots</b> and <b>NullStyle.emptyString</b>.
   * </p>
   */

  public CellStyle(final HorizontalAlign horAlign) {
    initialize(horAlign, DEFAULT_ABBREVIATION_STYLE, DEFAULT_NULL_STYLE,
        DEFAULT_HANDLE_TERMINAL_FORMATS);
  }

  /**
   * <p>
   * Style with a specified horizontal alignment and abbreviation style, that
   * assumes <b>NullStyle.emptyString</b>.
   * </p>
   */
  public CellStyle(final HorizontalAlign horAlign,
      final AbbreviationStyle abbStyle) {
    initialize(horAlign, abbStyle, DEFAULT_NULL_STYLE,
        DEFAULT_HANDLE_TERMINAL_FORMATS);
  }

  /**
   * Style with a specified horizontal alignment, abbreviation style and null
   * style that assumes no terminal formats.
   * 
   * @param horAlign
   *          Horizontal alignment.
   * @param abbStyle
   *          Abbreviation style.
   * @param nullStyle
   *          Null style.
   */

  public CellStyle(final HorizontalAlign horAlign,
      final AbbreviationStyle abbStyle, final NullStyle nullStyle) {
    initialize(horAlign, abbStyle, nullStyle, DEFAULT_HANDLE_TERMINAL_FORMATS);
  }

  /**
   * Full constructor, that specifies all characteristics.
   * 
   * @param horAlign
   *          Horizontal alignment.
   * @param abbStyle
   *          Abbreviation style.
   * @param nullStyle
   *          Null style.
   * @param handleTerminalFormats
   *          Specifies if terminal format characters should be handled. True by
   *          default. When handled, all Terminal sequences, like ESCAPE[33m,
   *          are considered to have a zero width, and when a cell has them,
   *          it's appended a FORMAT_RESET_SEQUENCE (ESCAPE[0m).
   */

  public CellStyle(final HorizontalAlign horAlign,
      final AbbreviationStyle abbStyle, final NullStyle nullStyle,
      final boolean allowTerminalFormats) {
    initialize(horAlign, abbStyle, nullStyle, allowTerminalFormats);
  }

  private void initialize(final HorizontalAlign horAlign,
      final AbbreviationStyle abbStyle, final NullStyle nullStyle,
      final boolean handleTerminalFormats) {
    this.horAlign = horAlign;
    this.abbStyle = abbStyle;
    this.nullStyle = nullStyle;
    this.handleTerminalFormats = handleTerminalFormats;
  }

  private String renderUncroppedText(final String txt) {
    if (txt == null) {
      if (NullStyle.emptyString.equals(this.nullStyle)) {
        return "";
      }
      return NULL_TEXT;
    }
    return txt;
  }

  /**
   * Returns the width of a rendered text, based on the cell content and style.
   * 
   * @param txt
   *          Text to render.
   * @return width of a rendered text, based on the cell style.
   */
  public int getWidth(final String txt) {
    String content;
    if (this.handleTerminalFormats && txt != null) {
      content = removeTerminalFormats(txt);
    } else {
      content = txt;
    }
    return renderUncroppedText(content).length();
  }

  private String removeTerminalFormats(final String txt) {
    StringBuffer sb = new StringBuffer();
    int i = 0;
    while (i < txt.length()) {
      int esc = txt.indexOf(ESC, i);
      if (esc == -1) {
        sb.append(txt.substring(i));
        return sb.toString();
      }
      int m = txt.indexOf('m', esc);
      if (m == -1) {
        return sb.toString();
      }
      sb.append(txt.substring(i, esc));
      i = m + 1;
    }
    return sb.toString();
  }

  /**
   * Renders a text based on the cell content, style and specified width.
   * 
   * @param txt
   *          Text to render.
   * @param width
   *          Fixed width to accommodate the test to.
   * @return Rendered text based on the cell style and the specified width.
   */
  public String render(final String txt, final int width) {
    String plainText = renderUncroppedText(txt);

    String uc = renderUnclosedContent(txt, width, plainText);
    if (this.handleTerminalFormats && (uc.indexOf("[") != -1)) {
      return uc + FORMAT_RESET_SEQUENCE;
    }
    return uc;
  }

  private String renderUnclosedContent(final String txt, final int width,
      final String plainText) {

    // Text too short.

    int tWidth = getWidth(txt);
    if (tWidth < width) {
      switch (this.horAlign) {
      case left:
        return alignLeft(plainText, width);
      case center:
        return alignCenter(plainText, width);
      default:
        return alignRight(plainText, width);
      }
    }

    // Text that fits perfect.

    if (tWidth == width) {
      return plainText;
    }

    // Text too long.

    switch (this.abbStyle) {
    case crop:
      return abbreviateCrop(plainText, width);
    default:
      return abbreviateDots(plainText, width);
    }
  }

  private String alignLeft(final String txt, final int width) {
    int diff = width - getWidth(txt);
    return txt + Filler.getFiller(diff);
  }

  private String alignCenter(final String txt, final int width) {
    int diff = width - getWidth(txt);
    int diffLeft = diff / 2;
    int diffRight = diff - diffLeft;
    return Filler.getFiller(diffLeft) + txt + Filler.getFiller(diffRight);
  }

  private String alignRight(final String txt, final int width) {
    int diff = width - getWidth(txt);
    return Filler.getFiller(diff) + txt;
  }

  private static final char ESC = 27;
  private static final String FORMAT_RESET_SEQUENCE = ESC + "[0m";

  private String abbreviateCrop(final String txt, final int width) {
    int len = getLength(txt, width);
    // System.out.println(txt + " [" + width + "] -> " + txt.substring(0, i)
    // + " (" + i + ")");
    return txt.substring(0, len);
  }

  private int getLength(final String txt, final int width) {
    int added = 0;
    int i = 0;
    while (i < txt.length() && added <= width) {
      char c = txt.charAt(i);
      if (c == ESC) {
        int m = txt.indexOf('m', i);
        if (m == -1) {
          i = txt.length();
        } else {
          i = m + 1;
        }
      } else if (added < width) {
        i++;
        added++;
      } else {
        return i;
      }
    }
    return i;
  }

  private String abbreviateDots(final String txt, final int width) {
    if (width < 1) {
      return "";
    }
    if (width <= DOTS_TEXT.length()) {
      return DOTS_TEXT.substring(0, width);
    }
    return abbreviateCrop(txt, width - DOTS_TEXT.length()) + DOTS_TEXT;
  }

  static String renderNullCell(final int width) {
    return Filler.getFiller(width);
  }

}
