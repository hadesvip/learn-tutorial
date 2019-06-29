package com.kevin.autoconfig.elastic.job;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author wangyong
 */
@Configuration
@ConditionalOnBean(ZookeeperAutoConfig.class)
//@AutoConfigureAfter(ZookeeperAutoConfig.class)
public class ElasticJobAutoConfig {

  @Resource
  private CoordinatorRegistryCenter coordinatorRegistryCenter;

  @Resource
  private ApplicationContext applicationContext;


  @PostConstruct
  public void init() {
    Map<String, Object> beansWithAnnotation = applicationContext
        .getBeansWithAnnotation(ElasticJob.class);
    beansWithAnnotation.forEach((k, v) -> {
      Class<?>[] interfaces = v.getClass().getInterfaces();
      for (Class<?> superInterface : interfaces) {
        if (superInterface == SimpleJob.class) {
          ElasticJob elasticJob = v.getClass().getAnnotation(ElasticJob.class);
          String corn = elasticJob.corn();
          String jobName = elasticJob.jobName();
          boolean overWrite = elasticJob.overwrite();
          int shardingTotalCount = elasticJob.shardingTotalCount();
          JobCoreConfiguration coreConfiguration = JobCoreConfiguration
              .newBuilder(jobName, corn, shardingTotalCount)
              .build();
          JobTypeConfiguration jtc = new SimpleJobConfiguration(coreConfiguration,
              v.getClass().getCanonicalName());
          LiteJobConfiguration liteJobConfiguration =
              LiteJobConfiguration.newBuilder(jtc).overwrite(overWrite)
              .build();
          new JobScheduler(coordinatorRegistryCenter, liteJobConfiguration).init();
        }
      }
    });
  }
}
