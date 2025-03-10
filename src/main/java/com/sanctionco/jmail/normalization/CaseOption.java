package com.sanctionco.jmail.normalization;

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
    NO_CHANGE,

    /**
     * Lowercase the entire email address.
     */
    LOWERCASE,

    /**
     * Lowercase the domain part of the email address only.
     */
    LOWERCASE_DOMAIN_ONLY,

    /**
     * Lowercase the local-part of the email address only.
     */
    LOWERCASE_LOCAL_PART_ONLY
}
