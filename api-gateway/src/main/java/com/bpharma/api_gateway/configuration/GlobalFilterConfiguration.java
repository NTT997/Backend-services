package com.bpharma.api_gateway.configuration;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 
	Buoc xac thuc dau tien cho moi request (public - private)
	xac dinh cac Ip bi block, cac user agent ko dc phep, va content-type
	boi vi getOrder = -1 nen no se chay dau tien
 */
@Configuration
public class GlobalFilterConfiguration implements GlobalFilter, Ordered {

	private final List<String> blackListIps = List.of("192.168.1.100");

	private final List<String> listBlockedUserAgent = List.of(
			"sqlmap", "nmap", "nikto", "acunetix", "wpscan", "fimap",
			"dirbuster", "netsparker", "crawler", "curl", "wget", "python-requests", "libwww-perl",
			"scrapy", "httpclient", "bot"
	);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String ip = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
		String contentType = exchange.getRequest().getHeaders().getFirst("Content-Type");
		String userAgent = exchange.getRequest().getHeaders().getFirst("User-Agent").toLowerCase();
		
		if (ip != null && blackListIps.contains(ip)) {
			exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
			exchange.getResponse().getHeaders().add("X-Blocked-Reason", "block ip: " + ip);
			return exchange.getResponse().setComplete();
		}
		
		if (userAgent != null) {
			String userAgentLower = userAgent.toLowerCase();
			for (String blocked : listBlockedUserAgent) {
				if (userAgentLower.contains(blocked)) {
					exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
					exchange.getResponse().getHeaders().add("X-Blocked-Reason", "Blocked User-Agent: " + userAgent);
					return exchange.getResponse().setComplete();
				}
			}
		}

		if (contentType != null && !contentType.contains("application/json")) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			exchange.getResponse().getHeaders().add("X-Blocled-Reason", "invalid content-type: " + contentType);
		}
		System.out.println("xac thuc buoc 1 thanh cong!");
		return chain.filter(exchange); // cho phep di tiep
	}

	@Override
	public int getOrder() {
		return -1;
	}

}
