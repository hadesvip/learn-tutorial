package com.kevin.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.kevin.autoconfig.elastic.job.ElasticJob;
import lombok.extern.slf4j.Slf4j;

/**
 * job实现方式1：
 *
 * @author wangyong
 */
@Slf4j
@ElasticJob(jobName = "mySimpleJob",
    corn = "0/10 * * * * ?",
    shardingTotalCount = 2,
    overwrite = true)
public class MySimpleJob implements SimpleJob {

  @Override
  public void execute(ShardingContext shardingContext) {
    log.info("我的总分片项：" + shardingContext.getShardingTotalCount());
    log.info("我的分片项：" + shardingContext.getShardingItem());

  }
}
