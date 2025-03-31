package com.sanctionco.jmail.normalization;

import java.text.Normalizer;

/**
 * <p>This class provides all configurable options to the email address normalization method
 * {@link com.sanctionco.jmail.Email#normalized(NormalizationOptions)} to adjust its behavior.</p>
 *
 * <p>Look at each method in this class to see all of the available options and their default
 * values.</p>
 *
 * <p>You can build a custom NormalizationOptions object to pass into the normalize method using
 * {@link #builder()}</p>
 */
public class NormalizationOptions {
  /**
   * A default {@code NormalizationOptions} object.
   */
  public static final NormalizationOptions DEFAULT_OPTIONS = builder().build();

  private final CaseOption caseOption;
  private final boolean removeDots;
  private final boolean removeSubAddress;
  private final boolean stripQuotes;
  private final String subAddressSeparator;
  private final boolean performUnicodeNormalization;
  private final Normalizer.Form unicodeNormalizationForm;

  NormalizationOptions(NormalizationOptionsBuilder builder) {
    this.caseOption = builder.caseOption;
    this.removeDots = builder.removeDots;
    this.removeSubAddress = builder.removeSubAddress;
    this.stripQuotes = builder.stripQuotes;
    this.subAddressSeparator = builder.subAddressSeparator;
    this.performUnicodeNormalization = builder.performUnicodeNormalization;
    this.unicodeNormalizationForm = builder.unicodeNormalizationForm;
  }

  /**
   * <p>How to adjust the casing of the email address.</p>
   *
   * <p>By default, this is {@link CaseOption#NO_CHANGE}, which does not change the casing
   * of the email address.</p>
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
   * <p>By default, this is {@code false}.</p>
   *
   * @return true if dots in the local-part of the email address should be removed, false otherwise
   */
  public boolean shouldRemoveDots() {
    return removeDots;
  }

  /**
   * <p>Whether to remove any sub-addressing (or tagged-addressing) contained in the address.</p>
   *
   * <p>By default, this is {@code false}.</p>
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
   * <p>By default, this is {@code false}.</p>
   *
   * @return true if unnecessary quotes should be stripped, or false otherwise
   */
  public boolean shouldStripQuotes() {
    return stripQuotes;
  }

  /**
   * <p>The separator that separates the local-part of an email address from the sub-address.</p>
   *
   * <p>By default, this is {@code "+"}.</p>
   *
   * @return the separator string
   */
  public String getSubAddressSeparator() {
    return subAddressSeparator;
  }

  /**
   * <p>Whether to perform unicode normalization on the local-part of the email address.</p>
   *
   * <p>By default, this is {@code false}.</p>
   *
   * @return true if unicode normalization should be performed, or false otherwise
   */
  public boolean shouldPerformUnicodeNormalization() {
    return performUnicodeNormalization;
  }

  /**
   * <p>The {@link Normalizer.Form} to use if performing unicode normalization on the
   * local-part of the email address.</p>
   *
   * <p>By default, this is {@link Normalizer.Form#NFKC}.</p>
   *
   * @return the unicode normalization form to use
   */
  public Normalizer.Form getUnicodeNormalizationForm() {
    return unicodeNormalizationForm;
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
