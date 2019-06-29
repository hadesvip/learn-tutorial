package com.kevin.autoconfig.elastic.job;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * elastic-job配置
 *
 * @author wangyong
 */
@ConfigurationProperties(prefix = "elastic.job.zookeeper")
@Setter
@Getter
public class ZookeeperProperties {

  /**
   * 服务地址
   */
  private String serverList;

  /**
   * 命名空间
   */
  private String nameSpace;


}
