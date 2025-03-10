package com.sanctionco.jmail.normalization;

/**
 * The builder class used to build a {@link NormalizationOptions} object that can be provided
 * to the email address normalization method
 * {@link com.sanctionco.jmail.Email#normalized(NormalizationOptions)} to adjust its behavior.
 */
public class NormalizationOptionsBuilder {
  boolean stripQuotes = NormalizationProperties.stripQuotes();
  CaseOption caseOption = NormalizationProperties.caseOption();
  boolean removeDots = NormalizationProperties.removeDots();

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

  public NormalizationOptions build() {
    return new NormalizationOptions(this);
  }
}
