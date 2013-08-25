/*
 * The MIT License
 *
 * Copyright 2013 Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.synopsys.arc.jenkinsci.plugins.jobrestrictions.restrictions.job;

import com.synopsys.arc.jenkinsci.plugins.jobrestrictions.Messages;
import com.synopsys.arc.jenkinsci.plugins.jobrestrictions.restrictions.JobRestriction;
import com.synopsys.arc.jenkinsci.plugins.jobrestrictions.restrictions.JobRestrictionDescriptor;
import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Queue;
import hudson.util.FormValidation;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

/**
 *
 * @author Oleg Nenashev <nenashev@synopsys.com>, Synopsys Inc.
 */
public class RegexNameRestriction extends JobRestriction {
    String regexExpression;

    @DataBoundConstructor
    public RegexNameRestriction(String regexExpression) {
        this.regexExpression = regexExpression;
    }

    public String getRegexExpression() {
        return regexExpression;
    }
 
    @Override
    public boolean canTake(Queue.BuildableItem item) {
        try {
            return item.task.getName().matches(regexExpression);
        } catch (PatternSyntaxException ex) {
            return true; // Ignore invalid pattern
        }
    }
    
    @Extension
    public static class DescriptorImpl extends JobRestrictionDescriptor {
        @Override
        public String getDisplayName() {
            return Messages.restrictions_Job_RegexName();
        }
        
        public FormValidation doCheckRegexExpression(@QueryParameter String regexExpression) {
            try {
                Pattern.compile(regexExpression);
            } catch (PatternSyntaxException exception) {
                return FormValidation.error(exception.getDescription());
            }
            return FormValidation.ok(Messages.restrictions_Job_RegexName_OkMessage());
        }
    }
}