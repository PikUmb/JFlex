/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex Unicode Properties                                                *
 * Copyright (c) 2008 Steve Rowe <steve_rowe@users.sf.net>                 *
 *                                                                         *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.unicode;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jflex.IntCharSet;
import jflex.Interval;
import jflex.unicode.data.*;



/**
 * This class was automatically generated by jflex-unicode-maven-plugin based
 * on data files downloaded from unicode.org on 2010-10-11.
 */
public class UnicodeProperties {

  //////////////////////////////////////////////////////////////////////////////
  // From <http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4313773>:       //
  //                                                                          //
  //    JDK 1.0.x -> Unicode 1.1.5                                            //
  //    JDK 1.1 to JDK 1.1.6 -> Unicode 2.0                                   //
  //    JDK 1.1.7 and up, Java 2 SE 1.2.x and Java 2 SE 1.3.x -> Unicode 2.1  //
  //                                                                          //
  // From <http://java.sun.com/j2se/1.4.2/docs/api/java/lang/Character.html>: //
  //                                                                          //
  //    Java 2 SE 1.4.2 -> Unicode 3.0                                        //
  //                                                                          //
  // (Inspection of downloaded docs for 1.4.0 and 1.4.1, both end-of-life'd,  //
  // from java.sun.com confirms that they, too, were based on Unicode 3.0.)   //
  //                                                                          //
  // From <http://java.sun.com/j2se/1.5.0/docs/api/java/lang/Character.html>: //
  //                                                                          //
  //    Java 2 SE 5.0 -> Unicode 4.0                                          //
  //                                                                          //
  // From <http://java.sun.com/javase/6/docs/api/java/lang/Character.html>:   //
  //                                                                          //
  //    Java SE 6 -> Unicode 4.0                                              //
  //                                                                          //
  // NOTE: The output of this Maven plugin is intended for incorporation in   //
  //       JFlex verion 1.5, which requires Java 2 SE 5.0+.                   //
  //////////////////////////////////////////////////////////////////////////////

  // Both Java5 and Java6 JREs are based on Unicode 4.0
  private static final String DEFAULT_UNICODE_VERSION = "4.0";

  private static final Pattern WORD_SEP_PATTERN = Pattern.compile("[-_\\s()]");

  private int maximumCodePoint;
  private Map<String,IntCharSet> propertyValueIntervals
    = new HashMap<String,IntCharSet>();
  private String caselessMatchPartitions;
  private int caselessMatchPartitionSize;
  private IntCharSet caselessMatches[];

  /**
   * Unpacks the Unicode data corresponding to the default Unicode version:
   * "{@value #DEFAULT_UNICODE_VERSION}".
   *
   * @throws UnsupportedUnicodeVersionException if the default version is not
   *  supported.
   */
  public UnicodeProperties() throws UnsupportedUnicodeVersionException {
    init(DEFAULT_UNICODE_VERSION);
  }

  /**
   * Unpacks the Unicode data corresponding to the given version.
   *
   * @param version The Unicode version for which to unpack data
   * @throws UnsupportedUnicodeVersionException if the given version is not
   *  supported.
   */
  public UnicodeProperties(String version)
    throws UnsupportedUnicodeVersionException {
    init(version);
  }

  /**
   * Returns the maximum code point for the selected Unicode version.
   *
   * @return the maximum code point for the selected Unicode version.
   */
  public int getMaximumCodePoint() {
    return maximumCodePoint;
  }

  /**
   * Returns the character interval set associated with the given property value
   * for the selected Unicode version.
   *
   * @param propertyValue The Unicode property or property value (or alias for
   *  one of these) for which to return the corresponding character intervals.
   * @return The character interval set corresponding to the given property
   *  value, if a match exists, and null otherwise.
   */
  public IntCharSet getIntCharSet(String propertyValue) {
    return propertyValueIntervals.get(normalize(propertyValue));
  }

  /**
   * Returns the set of all properties, property values, and their aliases
   * supported by the specified Unicode version.
   *
   * @return The set of all properties supported by the specified Unicode
   *  version
   */
  public Set<String> getPropertyValues() {
    return propertyValueIntervals.keySet();
  }

  /**
   * Returns a set of character intervals representing all characters
   * that are case-insensitively equivalent to the given character,
   * including the given character itself.
   * <p/>
   * The first call to this method lazily initializes the backing data.
   *
   * @param c The character for which to return case-insensitive equivalents.
   * @return All case-insensitively equivalent characters, or null
   *  if the given character is case-insensitively equivalent only to itself.
   */
  public IntCharSet getCaselessMatches(char c) {
    if (null == caselessMatches)
      initCaselessMatches();
    return caselessMatches[c];
  }

  /**
   * Unpacks the caseless match data. Called from
   * {@link #getCaselessMatches(char)} to lazily initialize.
   */
  private void initCaselessMatches() {
    caselessMatches = new IntCharSet[maximumCodePoint + 1];
    int[] members = new int[caselessMatchPartitionSize];
    for (int index = 0 ; index < caselessMatchPartitions.length() ; ) {
      IntCharSet partition = new IntCharSet();
      for (int n = 0 ; n < caselessMatchPartitionSize ; ++n) {
        int c = caselessMatchPartitions.codePointAt(index);
        index += (c <= 0xFFFF ? 1 : 2);
        members[n] = c;
        //TODO: Remove BMP boundary condition
        if (c > 0 && c <= 0xFFFF)
          //TODO: Change the character type from char to int
          partition.add((char)c);
      }
      if (partition.containsElements()) {
        for (int n = 0 ; n < caselessMatchPartitionSize ; ++n) {
          if (members[n] > 0)
            caselessMatches[members[n]] = partition;
        }
      }
    }
  }

  /**
   * Based on the given version, selects and binds the corresponding Unicode
   * data to facilitate mappings from property values to character intervals.
   *
   * @param version The Unicode version for which to bind data
   * @throws UnsupportedUnicodeVersionException if the given version is not
   *  supported.
   */
  private void init(String version) throws UnsupportedUnicodeVersionException {

    if (version.equals("1.1") || version.equals("1.1.5")) {
      bind(Unicode_1_1.propertyValues, Unicode_1_1.intervals, Unicode_1_1.propertyValueAliases,
         Unicode_1_1.maximumCodePoint, Unicode_1_1.caselessMatchPartitions, Unicode_1_1.caselessMatchPartitionSize);
    } else if (version.equals("2") || version.equals("2.0") || version.equals("2.0.14")) {
      bind(Unicode_2_0.propertyValues, Unicode_2_0.intervals, Unicode_2_0.propertyValueAliases,
         Unicode_2_0.maximumCodePoint, Unicode_2_0.caselessMatchPartitions, Unicode_2_0.caselessMatchPartitionSize);
    } else if (version.equals("2.1") || version.equals("2.1.9")) {
      bind(Unicode_2_1.propertyValues, Unicode_2_1.intervals, Unicode_2_1.propertyValueAliases,
         Unicode_2_1.maximumCodePoint, Unicode_2_1.caselessMatchPartitions, Unicode_2_1.caselessMatchPartitionSize);
    } else if (version.equals("3") || version.equals("3.0") || version.equals("3.0.1")) {
      bind(Unicode_3_0.propertyValues, Unicode_3_0.intervals, Unicode_3_0.propertyValueAliases,
         Unicode_3_0.maximumCodePoint, Unicode_3_0.caselessMatchPartitions, Unicode_3_0.caselessMatchPartitionSize);
    } else if (version.equals("3.1") || version.equals("3.1.0")) {
      bind(Unicode_3_1.propertyValues, Unicode_3_1.intervals, Unicode_3_1.propertyValueAliases,
         Unicode_3_1.maximumCodePoint, Unicode_3_1.caselessMatchPartitions, Unicode_3_1.caselessMatchPartitionSize);
    } else if (version.equals("3.2") || version.equals("3.2.0")) {
      bind(Unicode_3_2.propertyValues, Unicode_3_2.intervals, Unicode_3_2.propertyValueAliases,
         Unicode_3_2.maximumCodePoint, Unicode_3_2.caselessMatchPartitions, Unicode_3_2.caselessMatchPartitionSize);
    } else if (version.equals("4") || version.equals("4.0") || version.equals("4.0.1")) {
      bind(Unicode_4_0.propertyValues, Unicode_4_0.intervals, Unicode_4_0.propertyValueAliases,
         Unicode_4_0.maximumCodePoint, Unicode_4_0.caselessMatchPartitions, Unicode_4_0.caselessMatchPartitionSize);
    } else if (version.equals("4.1") || version.equals("4.1.0")) {
      bind(Unicode_4_1.propertyValues, Unicode_4_1.intervals, Unicode_4_1.propertyValueAliases,
         Unicode_4_1.maximumCodePoint, Unicode_4_1.caselessMatchPartitions, Unicode_4_1.caselessMatchPartitionSize);
    } else if (version.equals("5") || version.equals("5.0") || version.equals("5.0.0")) {
      bind(Unicode_5_0.propertyValues, Unicode_5_0.intervals, Unicode_5_0.propertyValueAliases,
         Unicode_5_0.maximumCodePoint, Unicode_5_0.caselessMatchPartitions, Unicode_5_0.caselessMatchPartitionSize);
    } else if (version.equals("5.1") || version.equals("5.1.0")) {
      bind(Unicode_5_1.propertyValues, Unicode_5_1.intervals, Unicode_5_1.propertyValueAliases,
         Unicode_5_1.maximumCodePoint, Unicode_5_1.caselessMatchPartitions, Unicode_5_1.caselessMatchPartitionSize);
    } else if (version.equals("5.2") || version.equals("5.2.0")) {
      bind(Unicode_5_2.propertyValues, Unicode_5_2.intervals, Unicode_5_2.propertyValueAliases,
         Unicode_5_2.maximumCodePoint, Unicode_5_2.caselessMatchPartitions, Unicode_5_2.caselessMatchPartitionSize);
    } else if (version.equals("6") || version.equals("6.0") || version.equals("6.0.0")) {
      bind(Unicode_6_0.propertyValues, Unicode_6_0.intervals, Unicode_6_0.propertyValueAliases,
         Unicode_6_0.maximumCodePoint, Unicode_6_0.caselessMatchPartitions, Unicode_6_0.caselessMatchPartitionSize);
    } else {
      throw new UnsupportedUnicodeVersionException();
    }

  }

  /**
   * Unpacks data for the selected Unicode version, populating
   * {@link #propertyValueIntervals}.
   *
   * @param propertyValues The list of property values, in same order as the
   *  packed data corresponding to them, in the given intervals, for the
   *  selected Unicode version.
   * @param intervals The packed character intervals corresponding to and in the
   *  same order as the given propertyValues, for the selected Unicode version.
   * @param propertyValueAliases Key/value pairs mapping property value aliases
   *  to property values, for the selected Unicode version.
   * @param maximumCodePoint The maximum code point for the selected Unicode
   *  version.
   * @param caselessMatchPartitions The packed caseless match partition data for
   *  the selected Unicode version
   * @param caselessMatchPartitionSize The partition data record length (the
   *  maximum number of elements in a caseless match partition) for the selected
   *  Unicode version.
   */
  private void bind(String[] propertyValues, String[] intervals,
                    String[] propertyValueAliases, int maximumCodePoint,
                    String caselessMatchPartitions, int caselessMatchPartitionSize) {
    // IntCharSet caselessMatches[] is lazily initialized - don't unpack here
    this.caselessMatchPartitions = caselessMatchPartitions;
    this.caselessMatchPartitionSize = caselessMatchPartitionSize;
    this.maximumCodePoint = maximumCodePoint;
    for (int n = 0 ; n < propertyValues.length ; ++n) {
      String propertyValue = propertyValues[n];
      String propertyIntervals = intervals[n];
      IntCharSet set = new IntCharSet();
      for (int index = 0 ; index < propertyIntervals.length() ; ) {
        int start = propertyIntervals.codePointAt(index);
        index += (start <= 0xFFFF ? 1 : 2);
        int end = propertyIntervals.codePointAt(index);
        index += (end <= 0xFFFF ? 1 : 2);
        //TODO: Remove BMP boundary condition
        if (start <= 0xFFFF) {
          //TODO: Change the character type from char to int and remove boundary condition
          set.add(new Interval((char)start, (char)Math.min(end, 0xFFFF)));
        }
      }
      propertyValueIntervals.put(propertyValue, set);
      if (2 == propertyValue.length()) {
        String singleLetter = propertyValue.substring(0, 1);
        IntCharSet singleLetterPropValueSet
          = propertyValueIntervals.get(singleLetter);
        if (null == singleLetterPropValueSet) {
          singleLetterPropValueSet = new IntCharSet();
          propertyValueIntervals.put(singleLetter, singleLetterPropValueSet);
        }
        singleLetterPropValueSet.add(set);
      }
    }
    for (int n = 0 ; n < propertyValueAliases.length ; n += 2) {
      String alias = propertyValueAliases[n];
      String propertyValue = propertyValueAliases[n + 1];
      IntCharSet targetSet = propertyValueIntervals.get(propertyValue);
      if (null != targetSet) {
        propertyValueIntervals.put(alias, targetSet);
      }
    }
    bindInvariantIntervals();
  }

  /**
   * Adds intervals for \p{ASCII} and \p{Any} to {@link #propertyValueIntervals}.
   */
  private void bindInvariantIntervals() {
    //TODO: Change the character type from char to int
    IntCharSet asciiSet = new IntCharSet(new Interval('\000', '\u007F'));
    propertyValueIntervals.put(normalize("ASCII"), asciiSet);

    //TODO: Change the character type from char to int
    //TODO: End of interval should be maximumCodePoint instead of '\uFFFF'
    IntCharSet anySet = new IntCharSet(new Interval('\000', '\uFFFF'));
    propertyValueIntervals.put(normalize("Any"), anySet);
  }

  /**
   * Normalizes the given identifier, by: downcasing; removing whitespace,
   * underscores, hyphens, and parentheses; and substituting '=' for every ':'.
   *
   * @param identifier The identifier to normalize
   * @return The normalized identifier
   */
  private String normalize(String identifier) {
    if (null == identifier)
      return identifier;
    Matcher matcher = WORD_SEP_PATTERN.matcher(identifier.toLowerCase());
    return matcher.replaceAll("").replace(':', '=');
  }

  public class UnsupportedUnicodeVersionException extends Exception {
	private static final long serialVersionUID = -1718158223161422981L;

    public UnsupportedUnicodeVersionException() {
      super("Supported versions: " +

              "1.1, 1.1.5, 2, 2.0, 2.0.14, 2.1, 2.1.9, 3, 3.0, 3.0.1, 3.1, 3.1.0, 3.2, 3.2.0, 4, 4.0, 4.0.1, 4.1, 4.1.0, 5, 5.0, 5.0.0, 5.1, 5.1.0, 5.2, 5.2.0, 6, 6.0, 6.0.0"
      );
    }
  }
}
