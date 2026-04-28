package com.gov.gatewayserver.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * Sentinel Gateway配置类
 * 配置Gateway的流控规则和异常处理
 *
 * @author chengbin
 * @since 2026-04-28
 */
@Configuration
public class SentinelGatewayConfig {

    /**
     * 初始化网关流控规则（Sentinel会自动注册GatewayFilter和ExceptionHandler）
     */
    @PostConstruct
    public void doInit() {
        initGatewayRules();
    }

    /**
     * 配置网关流控规则
     */
    private void initGatewayRules() {
        Set<GatewayFlowRule> rules = new HashSet<>();
        
        // 针对todo-service的流控规则：每秒最多100个请求
        rules.add(new GatewayFlowRule("todo-service")
                .setCount(100)
                .setIntervalSec(1));
        
        // 针对system-service的流控规则：每秒最多200个请求
        rules.add(new GatewayFlowRule("system-service")
                .setCount(200)
                .setIntervalSec(1));
        
        GatewayRuleManager.loadRules(rules);
    }
}
