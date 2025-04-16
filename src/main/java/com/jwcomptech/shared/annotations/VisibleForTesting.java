package com.jwcomptech.shared.annotations;

/**
 * Annotates a program element that exists, or is more widely visible than otherwise necessary, only
 * for use in test code.
 *
 * <p><b>Do not use this interface</b> for public or protected declarations: it is a fig leaf for
 * bad design, and it does not prevent anyone from using the declaration---and experience has shown
 * that they will. If the method breaks the encapsulation of its class, then its internal
 * representation will be hard to change. Instead, use <a
 * href="http://errorprone.info/bugpattern/RestrictedApi">RestrictedApiChecker</a>, which enforces
 * fine-grained visibility policies.
 *
 * @author Johannes Henkel
 */
public @interface VisibleForTesting { }
