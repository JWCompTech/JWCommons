package com.jwcomptech.commons.validators;

/*-
 * #%L
 * JWCT Commons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

/*
 * Copyright 2008 Les Hazlewood
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import lombok.*;

import java.util.regex.Pattern;

/**
 * An email address represents the textual string of an
 * RFC 2822 email address and other corresponding
 * information of interest.
 *
 * @author Les Hazlewood (www.leshazlewood.com)
 * @since 1.0.0-alpha
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings({"unused", "HardcodedFileSeparator"})
@ToString
@Builder
public final class EmailValidator {
    /**
     * This constant states that domain literals are allowed in the email address, e.g.:
     * someone@[192.168.1.100] or
     * john.doe@[23:33:A2:22:16:1F] or
     * me@[my computer]

     * The RFC says these are valid email addresses, but most people don't like allowing them.
     * If you don't want to allow them, and only want to allow valid domain names
     * (RFC 1035, x.y.z.com, etc.),
     * change this constant to false.

     *Its default value is true to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */
    private final boolean allowDomainLiterals = true;

    /**
     * This constant states that quoted identifiers are allowed
     * (using quotes and angle brackets around the raw address) are allowed, e.g.:
     * "John Smith" <john.smith@somewhere.com>

     * The RFC says this is a valid mailbox.  If you don't want to
     * allow this, because for example, you only want users to provide
     * a raw address (john.smith@somewhere.com - no quotes or angle
     * brackets), then change this constant to false.
     * Its default value is true to remain RFC 2822 compliant, but
     * you should set it depending on what you need for your application.
     */

    private final boolean allowQuotedIdentifiers = true;

    // RFC 2822 2.2.2 Structured Header Field Bodies

    private static final String wsp = "[ \\t]"; //space or tab

    private static final String fwsp = wsp + '*'; //empty string, space or tab

    //RFC 2822 3.2.1 Primitive tokens

    private static final String quote = "\"";

    //ASCII Control characters excluding white space:

    private static final String noWsCtl = "\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F";

    //all ASCII characters except CR and LF:

    private static final String asciiText = "[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F]";

    // RFC 2822 3.2.2 Quoted characters:

    private static final String quotedPair = "(\\\\" + asciiText + ')'; //single backslash followed by a text char

    //RFC 2822 3.2.4 Atom:

    private static final String atomText = "[a-zA-Z0-9!#$%&'*+\\-/=?^_`{|}~]";

    private static final String atom = fwsp + atomText + '+' + fwsp;

    private static final String dotAtomText = atomText + '+' + '(' + "\\." + atomText + "+)*";

    private static final String dotAtom = fwsp + '(' + dotAtomText + ')' + fwsp;

    //RFC 2822 3.2.5 Quoted strings:

    //noWsCtl and the rest of ASCII except the double quote and backslash characters:

    private static final String quotedText = '[' + noWsCtl + "\\x21\\x23-\\x5B\\x5D-\\x7E]";

    private static final String quotedContent = '(' + quotedText + '|' + quotedPair + ')';

    private static final String quotedString = quote + '(' + fwsp + quotedContent + ")*" + fwsp + quote;

    //RFC 2822 3.2.6 Miscellaneous tokens

    private static final String word = "((" + atom + ")|(" + quotedString + "))";

    private static final String phrase = word + '+'; //one or more words.

    //RFC 1035 tokens for domain names:

    private static final String letter = "[a-zA-Z]";

    private static final String letterNumber = "[a-zA-Z0-9]";

    private static final String letterNumberHyphen = "[a-zA-Z0-9-]";

    private static final String rfcLabel = letterNumber + '(' + letterNumberHyphen + "{0,61}" + letterNumber + ")?";

    private static final String rfc1035DomainName = rfcLabel + "(\\." + rfcLabel + ")*\\." + letter + "{2,6}";

    //RFC 2822 3.4 Address specification

    //domain text - non-white space controls and the rest of ASCII chars not including [, ], or \:

    private static final String domainText = '[' + noWsCtl + "\\x21-\\x5A\\x5E-\\x7E]";

    private static final String domainContent = domainText + '|' + quotedPair;

    private static final String domainLiteral = "\\[" + '(' + fwsp + domainContent + "+)*" + fwsp + ']';

    private static final String rfc2822Domain = '(' + dotAtom + '|' + domainLiteral + ')';

    private final String domain = allowDomainLiterals ? rfc2822Domain : rfc1035DomainName;

    private static final String localPart = "((" + dotAtom + ")|(" + quotedString + "))";

    private final String addressSpec = localPart + '@' + domain;

    private final String angleAddress = '<' + addressSpec + '>';

    private final String nameAddress = '(' + phrase + ")?" + fwsp + angleAddress;

    private final String mailbox = nameAddress + '|' + addressSpec;

    public final Pattern validPattern = Pattern.compile(allowQuotedIdentifiers ? mailbox : addressSpec);

    public boolean isValid(final CharSequence email) {
        return validPattern.matcher(email).matches();
    }
}
