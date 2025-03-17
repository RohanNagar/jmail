package com.sanctionco.jmail.normalization;

import java.text.Normalizer;

/**
 * The builder class used to build a {@link NormalizationOptions} object that can be provided
 * to the email address normalization method
 * {@link com.sanctionco.jmail.Email#normalized(NormalizationOptions)} to adjust its behavior.
 */
public class NormalizationOptionsBuilder {
  CaseOption caseOption = CaseOption.LOWERCASE;
  boolean stripQuotes = true;
  boolean removeDots = false;
  boolean removeSubAddress = false;
  String subAddressSeparator = "+";
  boolean performUnicodeNormalization = false;
  Normalizer.Form unicodeNormalizationForm = Normalizer.Form.NFKC;

  NormalizationOptionsBuilder() {
  }

  /**
   * By default, normalization strips all unnecessary quotes contained in the local-part
   * of the email address. This method will tell normalization to <strong>keep</strong> those
   * extraneous quotes.
   *
   * @return this
   */
  public NormalizationOptionsBuilder keepQuotes() {
    this.stripQuotes = false;
    return this;
  }

  /**
   * <p>Adjust the case of the email address. See {@link CaseOption} to see all available casing
   * adjustments you can choose from.</p>
   *
   * <p>The default normalization behavior uses {@link CaseOption#LOWERCASE}.</p>
   *
   * @param caseOption the {@link CaseOption} that should be used when adjusting the case
   * @return this
   */
  public NormalizationOptionsBuilder adjustCase(CaseOption caseOption) {
    this.caseOption = caseOption;
    return this;
  }

  /**
   * <p>Remove all dot ('.') characters contained in the local-part of the email address.</p>
   *
   * <p>This can be useful since some email providers (such as GMail) compare addresses without
   * dots. For example, {@code my.test.address@gmail.com} and {@code mytestaddress@gmail.com}
   * route to the same mailbox.</p>
   *
   * <p>The default normalization behavior does <strong>not</strong> remove dots.</p>
   *
   * @return this
   */
  public NormalizationOptionsBuilder removeDots() {
    this.removeDots = true;
    return this;
  }

  /**
   * <p>Remove any sub-addressing (or tagged-addressing) contained in the email address.</p>
   *
   * <p>Many mail servers support adding to the end of the local-part a {@code +} character
   * (or in rare cases a {@code -} character, or even more rare an arbitrary character),
   * followed by addtional characters, and the mail will be sent to the same address
   * as if the sub-address was never added.</p>
   *
   * <p>For example, {@code test+subaddress@gmail.com} and {@code test+additional@gmail.com}
   * route to the same mailbox.</p>
   *
   * <p>It can be beneficial to remove these sub-addresses as part of normalization
   * since they ultimately route to the same address.</p>
   *
   * <p>The default normalization behavior does <strong>not</strong> remove sub-addressing.
   * If sub-addressing removal is enabled via this method, then the default sub-address separator
   * is the {@code +} "plus" character. You can change the separator by specifying it using the
   * {@link #removeSubAddress(String)} method instead of this one.</p>
   *
   * @return this
   */
  public NormalizationOptionsBuilder removeSubAddress() {
    this.removeSubAddress = true;
    return this;
  }

  /**
   * <p>Remove any sub-addressing (or tagged-addressing) contained in the email address, and
   * supply a custom separator.</p>
   *
   * <p>Many mail servers support adding to the end of the local-part a {@code +} character
   * (or in rare cases a {@code -} character, or even more rare an arbitrary character),
   * followed by addtional characters, and the mail will be sent to the same address
   * as if the sub-address was never added.</p>
   *
   * <p>For example, {@code test+subaddress@gmail.com} and {@code test+additional@gmail.com}
   * route to the same mailbox.</p>
   *
   * <p>It can be beneficial to remove these sub-addresses as part of normalization
   * since they ultimately route to the same address.</p>
   *
   * <p>The default normalization behavior does <strong>not</strong> remove sub-addressing.</p>
   *
   * @param separator the separator string or character that separates the local-part from the
   *                  sub-address
   * @return this
   */
  public NormalizationOptionsBuilder removeSubAddress(String separator) {
    this.removeSubAddress = true;
    this.subAddressSeparator = separator;
    return this;
  }

  /**
   * <p>Perform unicode normalization on the local-part of the email address.</p>
   *
   * <p><a href="https://datatracker.ietf.org/doc/html/rfc6530#section-10.1">RFC 6530</a> Section 10.1
   * describes unicode normalization on the local-part of email addresses in the context
   * of internationalized addresses. Of particular note is the following:</p>
   *
   * <pre>
   * Unnormalized strings are valid, but sufficiently bad practice
   * that they may not work reliably on a global basis.
   * </pre>
   *
   * <p>With this in mind, it may be beneficial in some situations to be able to normalize
   * the local-part of the email address in an application.</p>
   *
   * <p>The default normalization behavior does <strong>not</strong> perform unicode normalization.
   * If unicode normalization is enabled via this method, then the default normalization form used
   * is {@link Normalizer.Form#NFKC}, as recommended in RFC 6530. To use a different form, use the
   * method {@link #performUnicodeNormalization(Normalizer.Form)}.</p>
   *
   * @return this
   */
  public NormalizationOptionsBuilder performUnicodeNormalization() {
    this.performUnicodeNormalization = true;
    return this;
  }

  /**
   * <p>Perform unicode normalization on the local-part of the email address, and supply a
   * custom {@link Normalizer.Form}.</p>
   *
   * <p><a href="https://datatracker.ietf.org/doc/html/rfc6530#section-10.1">RFC 6530</a> Section 10.1
   * describes unicode normalization on the local-part of email addresses in the context
   * of internationalized addresses. Of particular note is the following:</p>
   *
   * <pre>
   * Unnormalized strings are valid, but sufficiently bad practice
   * that they may not work reliably on a global basis.
   * </pre>
   *
   * <p>With this in mind, it may be beneficial in some situations to be able to normalize
   * the local-part of the email address in an application.</p>
   *
   * <p>The default normalization behavior does <strong>not</strong> perform unicode
   * normalization.</p>
   *
   * @param form the {@link Normalizer.Form} to use when performing unicode normalization
   * @return this
   */
  public NormalizationOptionsBuilder performUnicodeNormalization(Normalizer.Form form) {
    this.performUnicodeNormalization = true;
    this.unicodeNormalizationForm = form;
    return this;
  }

  /**
   * Build the new {@code NormalizationOptions} instance.
   *
   * @return the new {@link NormalizationOptions} instance
   */
  public NormalizationOptions build() {
    return new NormalizationOptions(this);
  }
}
