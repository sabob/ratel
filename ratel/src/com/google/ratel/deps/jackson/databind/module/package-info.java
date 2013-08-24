/**
 * Package that contains classes and interfaces to help implement
 * custom extension {@link com.google.ratel.deps.jackson.databind.Module}s
 * (which are registered using
 * {@link com.google.ratel.deps.jackson.databind.ObjectMapper#registerModule}.
 *<p>
 * Note that classes in the package only support registering
 * handlers for non-generic types (types without type
 * parameterization) -- hence "simple" -- which works for
 * many cases, but not all. So if you will need to register
 * handlers for generic types, you will usually need to either
 * sub-class handlers, or implement/extend base types directly.
 */
package com.google.ratel.deps.jackson.databind.module;
