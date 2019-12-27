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
package org.apache.struts2.metrics;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;

public class MetricsFactory {

	static {
		SharedMetricRegistries.setDefault("default-metrics");
	}

	/**
	 * 实例化一个专用于仪表的registry
	 */
	public static MetricRegistry getGaugeMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Gauges");
	}

	/**
	 * 实例化一个专用于计数器的registry
	 */
	public static MetricRegistry getCounterMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Counters");
	}

	/**
	 * 实例化一个专用于直方图的registry
	 */
	public static MetricRegistry getHistogramMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Histograms");
	}

	/**
	 * 实例化一个专用于速率的registry
	 */
	public static MetricRegistry getMeterMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Meters");
	}

	/**
	 * 实例化一个专用于计时器的registry
	 */
	public static MetricRegistry getTimerMetricRegistry() {
		return SharedMetricRegistries.getOrCreate("Timers");
	}

	/**
	 * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
	 */
	public static MetricRegistry getMetricRegistry(String metrics) {
		return SharedMetricRegistries.getOrCreate(metrics);
	}

	/**
	 * 实例化一个counter,同样可以通过如下方式进行实例化再注册进去 Counter jobs = new Counter();
	 * metrics.register(MetricRegistry.name(TestCounter.class, "jobs"), jobs);
	 */
	public static <T> Counter counter(Class<T> clazz, String metrics, String name) {
		return getMetricRegistry(metrics).counter(MetricRegistry.name(clazz, name));
	}

	public static <T> Counter counter(Class<T> clazz, String name) {
		return SharedMetricRegistries.getDefault().counter(MetricRegistry.name(clazz, name));
	}

	/**
	 * 实例化一个Histograms
	 */
	public static <T> Histogram histogram(Class<T> clazz, String metrics, String name) {
		return getMetricRegistry(metrics).histogram(MetricRegistry.name(clazz, name));
	}

	public static <T> Histogram histogram(Class<T> clazz, String name) {
		return SharedMetricRegistries.getDefault().histogram(MetricRegistry.name(clazz, name));
	}

}
