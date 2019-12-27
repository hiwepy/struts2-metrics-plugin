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

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.metrics.utils.ReflectionUtils;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

@SuppressWarnings("serial")
public class MetricsCountersInterceptor extends AbstractInterceptor {

	protected static final Logger LOG = LogManager.getLogger(MetricsCountersInterceptor.class);

	public String intercept(ActionInvocation invocation) throws Exception {
		
		LOG.debug("Find the Request in context");
        HttpServletRequest oRequest = ServletActionContext.getRequest();
        
        
        //处理请求的Action对象类型
        Object actionObject = invocation.getProxy().getAction();
      	//action 将要执行的方法名称
    	String methodName = invocation.getProxy().getMethod();
    	//action 将要执行的方法对象
    	Method method = ReflectionUtils.getMethod(actionObject.getClass(), methodName);
    	
        //uri去掉web上下文
    	String resPath = oRequest.getRequestURI().substring(oRequest.getContextPath().length());
    	
    	
        return invocation.invoke();
    }
}
