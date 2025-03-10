package com.sanctionco.jmail.normalization;

/**
 * <p>This class provides all configurable options to the email address normalization method
 * {@link com.sanctionco.jmail.Email#normalized(NormalizationOptions)} to adjust its behavior.</p>
 *
 * <p>Look at each method in this class to see all of the available options, their default
 * values, and the JVM system property that can be used to adjust the default values on startup.</p>
 *
 * <p>You can build a custom NormalizationOptions object to pass into the normalize method using
 * {@link #builder()}</p>
 */
public class NormalizationOptions {
  private final CaseOption caseOption;
  private final boolean removeDots;
  private final boolean removeSubAddress;
  private final boolean stripQuotes;
  private final String subAddressSeparator;

  NormalizationOptions(NormalizationOptionsBuilder builder) {
    this.caseOption = builder.caseOption;
    this.removeDots = builder.removeDots;
    this.removeSubAddress = builder.removeSubAddress;
    this.stripQuotes = builder.stripQuotes;
    this.subAddressSeparator = builder.subAddressSeparator;
  }

  /**
   * <p>How to adjust the casing of the email address.</p>
   *
   * <p>By default, this is {@link CaseOption#NO_CHANGE}, which does not change the casing
   * of the email address. Use the {@code jmail.normalize.case} JVM system property to adjust
   * this default value or build your own {@code NormalizationOptions} using {@link #builder()}.</p>
   *
   * @return the {@link CaseOption} describing how to adjust the case
   *
   * @see CaseOption
   */
  public CaseOption getCaseOption() {
    return caseOption;
  }

  /**
   * <p>Whether to remove all dots in the local-part of the email address.</p>
   *
   * <p>By default, this is {@code false}. Use the {@code jmail.normalize.remove.dots}
   * JVM system property to adjust this default value or build your own {@code NormalizationOptions}
   * using {@link #builder()}.</p>
   *
   * @return true if dots in the local-part of the email address should be removed, false otherwise
   */
  public boolean shouldRemoveDots() {
    return removeDots;
  }

  /**
   * <p>Whether to remove any sub-addressing (or tagged-addressing) contained in the address.</p>
   *
   * <p>By default, this is {@code false}. Use the {@code jmail.normalize.remove.subaddress}
   * JVM system property to adjust this default value or build your own {@code NormalizationOptions}
   * using {@link #builder()}.</p>
   *
   * @return true if any sub-address in the local-part should be removed, or false otherwise
   */
  public boolean shouldRemoveSubAddress() {
    return removeSubAddress;
  }

  /**
   * <p>Whether to strip all unnecessary quotes contained in the local-part of
   * the email address.</p>
   *
   * <p>By default, this is {@code false}. Use the {@code jmail.normalize.strip.quotes}
   * JVM system property to adjust this default value or build your own {@code NormalizationOptions}
   * using {@link #builder()}</p>
   *
   * @return true if unnecessary quotes should be stripped, or false otherwise
   */
  public boolean shouldStripQuotes() {
    return stripQuotes;
  }

  /**
   * <p>The separator that separates the local-part of an email address from the sub-address.</p>
   *
   * <p>By default, this is {@code "+"}. Use the {@code jmail.normalize.subaddress.separator}
   * JVM system property to adjust this default value or build your own {@code NormalizationOptions}
   * using {@link #builder()}.</p>
   *
   * @return the separator string
   */
  public String getSubAddressSeparator() {
    return subAddressSeparator;
  }

  /**
   * Create a new {@link NormalizationOptionsBuilder} to begin building a new
   * {@link NormalizationOptions} object.
   *
   * @return the new {@link NormalizationOptionsBuilder}
   */
  public static NormalizationOptionsBuilder builder() {
    return new NormalizationOptionsBuilder();
  }
}
