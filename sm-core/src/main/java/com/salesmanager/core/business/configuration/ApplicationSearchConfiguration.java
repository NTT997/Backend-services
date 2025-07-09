package com.salesmanager.core.business.configuration;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.salesmanager.core.business.services.search.SearchServiceImpl;

import modules.commons.search.configuration.Credentials;
import modules.commons.search.configuration.SearchHost;

/**
 * Reads search related properties required for search starter
 * @author carlsamson
 *
 */


@Configuration
@ConfigurationProperties(prefix = "search")
@PropertySource("classpath:shopizer-core.properties")
public class ApplicationSearchConfiguration {
	
    private String clusterName;
    private Credentials credentials;
    private List<SearchHost> host;
    private List<String> searchLanguages;
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationSearchConfiguration.class);
	@PostConstruct
	public void logSearchConfig() {
		System.out.println("[Search Config]");
		System.out.println("Cluster name: " + clusterName);
		System.out.println("Credentials: " + (credentials != null ? credentials.getUserName() : "null"));
		System.out.println("Port: " + host.get(0).getPort());
		System.out.println("Schema: " + host.get(0).getScheme());
		System.out.println("Hosts: " + host.get(0).getHost());
		System.out.println("Languages: " + (searchLanguages != null ? searchLanguages.toString() : "null"));
	}

	public String getClusterName() {
		System.out.println("Print line 28 at ApplicaitonSearchConfiguration");
		return clusterName;
	}
	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
	public Credentials getCredentials() {
		return credentials;
	}
	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}
	public List<SearchHost> getHost() {
		return host;
	}
	public void setHost(List<SearchHost> host) {
		this.host = host;
	}
	public List<String> getSearchLanguages() {
		return searchLanguages;
	}
	public void setSearchLanguages(List<String> searchLanguages) {
		this.searchLanguages = searchLanguages;
	}


}
