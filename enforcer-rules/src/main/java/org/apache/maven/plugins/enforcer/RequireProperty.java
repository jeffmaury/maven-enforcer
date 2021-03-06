package org.apache.maven.plugins.enforcer;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

/**
 * This rule checks that certain properties are set.
 *
 * @author Paul Gier
 */
public class RequireProperty
    extends AbstractNonCacheableEnforcerRule
{

    /** Specify the required property. */
    public String property = null;

    /** Match the property value to a given regular expression. Defaults to <code>null</code> (any value is ok). */
    public String regex = null;

    /** Specify a warning message if the regular expression is not matched. */
    public String regexMessage = null;

    /**
     * Execute the rule.
     *
     * @param helper the helper
     * @throws EnforcerRuleException the enforcer rule exception
     */
    public void execute( EnforcerRuleHelper helper )
        throws EnforcerRuleException
    {
        Object propValue = null;
        try
        {
            propValue = helper.evaluate( "${" + property + "}" );
        }
        catch ( ExpressionEvaluationException eee )
        {
            throw new EnforcerRuleException( "Unable to evaluate property: " + property, eee );
        }

        // Check that the property is not null or empty string
        if ( propValue == null )
        {
            if ( message == null )
            {
                message = "Property \"" + property + "\" is required for this build.";
            }
            throw new EnforcerRuleException( message );
        }
        // If there is a regex, check that the property matches it
        if ( regex != null && !propValue.toString().matches( regex ) )
        {
            if ( regexMessage == null )
            {
                regexMessage =
                    "Property \"" + property + "\" evaluates to \"" + propValue + "\".  " +
                        "This does not match the regular expression \"" + regex + "\"";
            }
            throw new EnforcerRuleException( regexMessage );
        }
    }
}
