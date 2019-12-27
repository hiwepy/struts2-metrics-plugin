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
package org.apache.struts2.metrics.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;

public class StrutsMetricsPrepareAndExecuteFilter extends StrutsPrepareAndExecuteFilter {

	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
	protected MetricRegistry registry = null;

    protected long initialDelay = 0; 
	
	protected long period = 1;
	
	protected TimeUnit unit = TimeUnit.SECONDS;
	
	public String getInitParameter(FilterConfig filterConfig,String key, String defaultStr) {
		String value = filterConfig.getInitParameter(key);
		return StringUtils.isEmpty(value) ? defaultStr : value;
	}
	
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	super.init(filterConfig);
    	
    	String name = this.getInitParameter(filterConfig , "registry-name", "struts-http-registry");
    	this.setRegistry(SharedMetricRegistries.getOrCreate(name));
    	
    	String initialDelay = this.getInitParameter(filterConfig ,"registry-initialDelay", "0");
    	this.setInitialDelay(Integer.parseInt(initialDelay));
    	
    	String period = this.getInitParameter(filterConfig ,"registry-period", "1");
    	this.setPeriod(Long.parseLong(period));
    	
    	String unit = this.getInitParameter(filterConfig ,"registry-unit", "SECONDS");
    	this.setUnit(TimeUnit.valueOf(unit));
    	
    	String debug = this.getInitParameter(filterConfig ,"registry-debug", "true");
    	if(BooleanUtils.toBoolean(debug)){
    		/**
    		 * 在控制台上打印输出
    		 */
    		ConsoleReporter.forRegistry(registry).build().start(getInitialDelay(), getPeriod(), getUnit());
    	}
    	
    }
    
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		super.doFilter(req, res, chain);
		
		HttpServletRequest oRequest = (HttpServletRequest) req;
		// uri去掉web上下文
		String uri = oRequest.getRequestURI().substring(oRequest.getContextPath().length());
		String prefix = MetricRegistry.name(this.getClass(), uri );
		
		registry.counter(prefix).inc();
		
	}
	
	public MetricRegistry getRegistry() {
		return registry;
	}


	public void setRegistry(MetricRegistry registry) {
		this.registry = registry;
	}

	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}
	
}
