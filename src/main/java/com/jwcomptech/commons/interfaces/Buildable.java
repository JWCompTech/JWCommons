package com.jwcomptech.commons.interfaces;

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

/**
 * <p>
 * The Buildable interface is designed to designate a class as a <em>builder</em>
 * object in the Builder design pattern. Builders are capable of creating and
 * configuring objects or results that normally take multiple steps to construct
 * or are very complex to derive.
 * </p>
 *
 * <p>
 * The buildable interface defines a single method, {@link #build()}, that
 * classes must implement. The result of this method should be the final
 * configured object or result after all building operations are performed.
 * </p>
 *
 * <p>
 * It is a recommended practice that the methods supplied to configure the
 * object or result being built return a reference to {@code this} so that
 * method calls can be chained together.
 * </p>
 *
 * <p>
 * Example Builder:
 * <pre>{@code
 * class FontBuilder implements Buildable<Font> {
 *     private Font font;
 *
 *     public FontBuilder(String fontName) {
 *         this.font = new Font(fontName, Font.PLAIN, 12);
 *     }
 *
 *     public FontBuilder bold() {
 *         this.font = this.font.deriveFont(Font.BOLD);
 *         return this; // Reference returned so calls can be chained
 *     }
 *
 *     public FontBuilder size(float pointSize) {
 *         this.font = this.font.deriveFont(pointSize);
 *         return this; // Reference returned so calls can be chained
 *     }
 *
 *     // Other Font construction methods
 *
 *     public Font build() {
 *         return this.font;
 *     }
 * }
 * }</pre>
 *
 * Example Buildable Usage:
 * <pre>{@code
 * Font bold14ptSansSerifFont = new FontBuilder(Font.SANS_SERIF).bold()
 *                                                              .size(14.0f)
 *                                                              .build();
 * }</pre>
 *
 *
 * @param <T> the type of object that the builder will construct or compute.
 * @since 1.0.0-alpha
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface Buildable<T> {

    /**
     * Returns a reference to the object being constructed or result being
     * calculated by the builder.
     *
     * @return the object constructed or result calculated by the builder.
     */
    T build();
}
