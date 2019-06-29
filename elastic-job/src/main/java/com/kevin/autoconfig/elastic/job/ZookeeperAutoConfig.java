package com.kevin.autoconfig.elastic.job;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * elastic-job自动配置
 * @author wangyong
 */
@Configuration
@ConditionalOnProperty("elastic.job.zookeeper.server-list")
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperAutoConfig {

  @Resource
  private ZookeeperProperties zookeeperProperties;


  @Bean(initMethod = "init")
  public CoordinatorRegistryCenter getZKCenter() {
    String nameSpace = zookeeperProperties.getNameSpace();
    String serverList = zookeeperProperties.getServerList();
    ZookeeperConfiguration zc = new ZookeeperConfiguration(serverList, nameSpace);
    return new ZookeeperRegistryCenter(zc);
  }

}
