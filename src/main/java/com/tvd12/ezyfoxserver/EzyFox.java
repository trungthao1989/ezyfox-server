package com.tvd12.ezyfoxserver;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyFoxApp;
import com.tvd12.ezyfoxserver.config.EzyFoxConfig;
import com.tvd12.ezyfoxserver.config.EzyFoxPlugin;
import com.tvd12.ezyfoxserver.config.EzyFoxSettings;
import com.tvd12.ezyfoxserver.loader.EzyFoxAppEntryLoader;
import com.tvd12.ezyfoxserver.loader.EzyFoxPluginEntryLoader;
import com.tvd12.ezyfoxserver.service.EzyFoxJsonMapping;
import com.tvd12.ezyfoxserver.service.EzyFoxXmlReading;

import lombok.Getter;
import lombok.Setter;

/**
 * Hello world!
 *
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EzyFox {

	private EzyFoxConfig config;
	private EzyFoxSettings settings;
	
	@JsonIgnore
	private ClassLoader classLoader;
	@JsonIgnore
	private EzyFoxXmlReading xmlReading;
	@JsonIgnore
	private EzyFoxJsonMapping jsonMapping;
	@JsonIgnore
	private Map<String, EzyAppClassLoader> appClassLoaders;
	
	@JsonIgnore
	public Set<String> getAppNames() {
		return settings.getAppNames();
	}
	
	@JsonIgnore
	public Set<Integer> getAppIds() {
		return settings.getAppIds();
	}
	
	public EzyFoxApp getAppByName(final String name) {
		return settings.getAppByName(name);
	}
	
	public EzyFoxApp getAppById(final Integer id) {
		return settings.getAppById(id);
	}
	
	public Class<EzyFoxAppEntryLoader> 
			getAppEntryLoaderClass(final String appName) throws Exception {
		return getAppEntryLoaderClass(getAppByName(appName));
	}
	
	@SuppressWarnings("unchecked")
	public Class<EzyFoxAppEntryLoader> 
			getAppEntryLoaderClass(final EzyFoxApp app) throws Exception {
		return  (Class<EzyFoxAppEntryLoader>) 
			Class.forName(app.getEntryLoader(), true, getClassLoader(app.getName()));
	}
	
	public EzyFoxAppEntryLoader newAppEntryLoader(final String appName) throws Exception {
		return getAppEntryLoaderClass(appName).newInstance();
	}
	
	//==================== plugins ================//
	@JsonIgnore
	public Set<String> getPluginNames() {
		return settings.getPluginNames();
	}
	
	@JsonIgnore
	public Set<Integer> getPluginIds() {
		return settings.getPluginIds();
	}
	
	public EzyFoxPlugin getPluginByName(final String name) {
		return settings.getPluginByName(name);
	}
	
	public EzyFoxPlugin getPluginById(final Integer id) {
		return settings.getPluginById(id);
	}
	
	public Class<EzyFoxPluginEntryLoader> 
			getPluginEntryLoaderClass(final String pluginName) throws Exception {
		return getPluginEntryLoaderClass(getPluginByName(pluginName));
	}
	
	@SuppressWarnings("unchecked")
	public Class<EzyFoxPluginEntryLoader> 
			getPluginEntryLoaderClass(final EzyFoxPlugin plugin) throws Exception {
		return  (Class<EzyFoxPluginEntryLoader>) Class.forName(plugin.getEntryLoader());
	}
	
	public EzyFoxPluginEntryLoader newPluginEntryLoader(final String pluginName) throws Exception {
		return getPluginEntryLoaderClass(pluginName).newInstance();
	}
	//=============================================//
	
	public EzyAppClassLoader getClassLoader(final String appName) {
		if(appClassLoaders.containsKey(appName)) 
			return appClassLoaders.get(appName);
		throw new IllegalArgumentException("has class loader for app " + appName);
	}
	
	@Override
	public String toString() {
		return jsonMapping.writeAsString(this);
	}
    
}
