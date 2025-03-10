package com.sanctionco.jmail.normalization;

import java.util.function.Function;

/**
 * This enum provides all possible options for adjusting the case of an email address
 * during normalization.
 *
 * @see NormalizationOptions#getCaseOption()
 */
public enum CaseOption {
  /**
   * Do not change the case of any characters within the email address.
   */
  NO_CHANGE(String::toString, String::toString),

  /**
   * Lowercase the entire email address.
   */
  LOWERCASE(String::toLowerCase, String::toLowerCase),

  /**
   * Lowercase the domain part of the email address only.
   */
  LOWERCASE_DOMAIN_ONLY(String::toString, String::toLowerCase),

  /**
   * Lowercase the local-part of the email address only.
   */
  LOWERCASE_LOCAL_PART_ONLY(String::toLowerCase, String::toString),

  /**
   * Uppercase the entire email address.
   */
  UPPERCASE(String::toUpperCase, String::toUpperCase),

  /**
   * Uppercase the domain part of the email address only.
   */
  UPPERCASE_DOMAIN_ONLY(String::toString, String::toUpperCase),

  /**
   * Uppercase the local-part of the email address only.
   */
  UPPERCASE_LOCAL_PART_ONLY(String::toUpperCase, String::toString);

  private final Function<String, String> localPartAdjuster;
  private final Function<String, String> domainAdjuster;

  CaseOption(Function<String, String> localPartAdjuster,
             Function<String, String> domainAdjuster) {
    this.localPartAdjuster = localPartAdjuster;
    this.domainAdjuster = domainAdjuster;
  }

  /**
   * Adjust the provided local-part based on the current CaseOption.
   *
   * @param localPart the string to adjust casing for
   * @return the adjusted case string
   */
  public String adjustLocalPart(String localPart) {
    return this.localPartAdjuster.apply(localPart);
  }

  /**
   * Adjust the provided domain based on the current CaseOption.
   *
   * @param domain the string to adjust casing for
   * @return the adjusted case string
   */
  public String adjustDomain(String domain) {
    return this.domainAdjuster.apply(domain);
  }
}
