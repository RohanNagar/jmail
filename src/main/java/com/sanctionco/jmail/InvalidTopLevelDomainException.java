package com.sanctionco.jmail;

/**
 * A {@link RuntimeException} that indicates an invalid top level domain was provided when
 * creating a new {@link TopLevelDomain}.
 */
public final class InvalidTopLevelDomainException extends RuntimeException {

  /**
   * Construct a new instance of {@code InvalidTopLevelDomainException}.
   */
  public InvalidTopLevelDomainException() {
  }
}
