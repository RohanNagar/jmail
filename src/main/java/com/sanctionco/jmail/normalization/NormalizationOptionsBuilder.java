package com.sanctionco.jmail.normalization;

/**
 * The builder class used to build a {@link NormalizationOptions} object that can be provided
 * to the email address normalization method
 * {@link com.sanctionco.jmail.Email#normalized(NormalizationOptions)} to adjust its behavior.
 */
public class NormalizationOptionsBuilder {
  CaseOption caseOption = CaseOption.NO_CHANGE;
  boolean stripQuotes = false;
  boolean removeDots = false;
  boolean removeSubAddress = false;
  String subAddressSeparator = "+";

  NormalizationOptionsBuilder() {
  }

  /**
   * <p>Strip all unnecessary quotes contained in the local-part of the email address.</p>
   *
   * <p>The default normalization behavior does <strong>not</strong> strip quotes. You can change
   * the default by using the JVM system property {@code jmail.normalize.strip.quotes}. If you set
   * {@code -Djmail.normalize.strip.quotes=true} on your application startup, then it is not
   * necessary to use this method.</p>
   *
   * @return this
   */
  public NormalizationOptionsBuilder stripQuotes() {
    this.stripQuotes = true;
    return this;
  }

  /**
   * <p>Adjust the case of the email address. See {@link CaseOption} to see all available casing
   * adjustments you can choose from.</p>
   *
   * <p>The default normalization behavior does <strong>not</strong> adjust the case. You can change
   * the default by using the JVM system property {@code jmail.normalize.case}. If you set
   * {@code -Djmail.normalize.case=xxx} on your application startup, then it is not
   * necessary to use this method.</p>
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
   * <p>The default normalization behavior does <strong>not</strong> remove dots. You can change
   * the default by using the JVM system property {@code jmail.normalize.remove.dots}. If you set
   * {@code -Djmail.normalize.remove.dots=true} on your application startup, then it is not
   * necessary to use this method.</p>
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
   * You can change the default by using the JVM system property
   * {@code jmail.normalize.remove.subaddress}.</p>
   *
   * <p>Additionally, the default sub-address separator is the {@code +} "plus" character. You
   * can change the default by using the JVM system property
   * {@code jmail.normalize.subaddress.separator} or by specifying it using the
   * {@link #removeSubAddress(String)} method instead of this one.</p>
   *
   * @return this
   */
  public NormalizationOptionsBuilder removeSubAddress() {
    this.removeSubAddress = true;
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
   * You can change the default by using the JVM system property
   * {@code jmail.normalize.remove.subaddress}.</p>
   *
   * <p>Additionally, the default sub-address separator is the {@code +} "plus" character. You
   * can change the default by using the JVM system property
   * {@code jmail.normalize.subaddress.separator}.</p>
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
   * Build the new {@code NormalizationOptions} instance.
   *
   * @return the new {@link NormalizationOptions} instance
   */
  public NormalizationOptions build() {
    return new NormalizationOptions(this);
  }
}
