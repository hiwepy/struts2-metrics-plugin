/*
 * Copyright (c) 2018 (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.struts2.metrics.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.metrics.utils.AntPathMatcher;
import org.apache.struts2.metrics.utils.PathMatcher;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class MetricsHealthChecksInterceptor extends AbstractInterceptor {

	protected static final Logger LOG = LogManager.getLogger(MetricsHealthChecksInterceptor.class);
    protected final PathMatcher pathMatcher = new AntPathMatcher();
    protected String inputPattern;
    
    public String getInputPattern() {
		return inputPattern;
	}
    
	public void setInputPattern(String inputPattern) {
		this.inputPattern = inputPattern;
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		
		LOG.debug("Find the Request in context");
        HttpServletRequest oRequest = ServletActionContext.getRequest();
        
        //uri去掉web上下文
    	String resPath = oRequest.getRequestURI().substring(oRequest.getContextPath().length());
    	
		//匹配资源路径是否需要处理
    	if(pathMatcher.match(inputPattern, resPath)){
    		return "webjars";
    	}
    	
        return invocation.invoke();
    }
}
