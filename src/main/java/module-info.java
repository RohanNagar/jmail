/**
 * A modern and lightweight library for working with email addresses in Java.
 *
 * @author Rohan Nagar (rohannagar11@gmail.com)
 */
open module com.sanctionco.jmail {
  requires java.naming;

  exports com.sanctionco.jmail;
  exports com.sanctionco.jmail.disposable;
  exports com.sanctionco.jmail.dns;
  exports com.sanctionco.jmail.net;
  exports com.sanctionco.jmail.normalization;
}
