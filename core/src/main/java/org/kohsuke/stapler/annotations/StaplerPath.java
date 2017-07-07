/*
 * Copyright (c) 2017, Stephen Connolly, CloudBees, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice, this list of
 *       conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.kohsuke.stapler.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks methods and fields as being navigable by Stapler.
 *
 * @since TODO
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Documented
@Repeatable(StaplerPaths.class)
public @interface StaplerPath {
    /**
     * Special constant used to signify that the annotated method is a catch-all dynamic match.
     */
    String DYNAMIC = "\u0000\ufefforg.kohsuke.stapler.annotations.StaplerPath#DYNAMIC\ufeff\u0000";
    /**
     * Special constant used to signify that the path segment should be inferred from the method name:
     * <ul>
     * <li>Method names starting with {@code get} will have the {@code get} removed and the next character turned to
     * lowercase</li>
     * <li>Method names starting with {@code do} will have the {@code do} removed and the next character turned to
     * lowercase</li>
     * <li>Method names starting with {@code js} will have the {@code js} removed and the next character turned to
     * lowercase</li>
     * <li>All other methods will be ignored (annotation processor should flag such methods as incorrectly annotated)
     * </li>
     * <li>Field names will be used verbatim</li>
     * </ul>
     */
    String INFER_FROM_NAME = "\u0000\ufefforg.kohsuke.stapler.annotations.StaplerPath#INFER\ufeff\u0000";
    /**
     * Special constant used to signify that the path segment should be treated as the "index" page
     * (which also matches an empty segment)
     */
    String INDEX = "";

    String value() default INFER_FROM_NAME;

    /**
     * Meta-annotation to flag an annotation as implying {@link StaplerPath#INFER_FROM_NAME} without explicitly
     * requiring the method / field to have a {@link StaplerPath} annotation
     */
    @Target(ANNOTATION_TYPE)
    @Retention(RUNTIME)
    @Documented
    public @interface Implicit {
    }
}
