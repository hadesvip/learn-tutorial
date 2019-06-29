package com.kevin;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.base.Joiner;
import com.kevin.job.MyDataFlow;
import com.kevin.job.MySimpleJob;
import java.util.Arrays;

/**
 * 程序运行入口
 *
 * @author wangyong
 */
public class Console {


  public static void main(String[] args) {
    System.out.println("开始启动程序....");
    new JobScheduler(zkCenter(),getScriptConfig()).init();


  }

  public static LiteJobConfiguration getScriptConfig() {
    JobCoreConfiguration coreConfiguration = JobCoreConfiguration
        .newBuilder("myScriptJob", "0/10 * * * * ?", 2)
        .build();

    JobTypeConfiguration jtc = new ScriptJobConfiguration(coreConfiguration,"sh /Users/yundan/code/learn-tutorial/elastic-job/demo.sh");

    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(jtc).overwrite(true)
        .build();
    return liteJobConfiguration;
  }


  public static LiteJobConfiguration getSimpleConfig() {
    JobCoreConfiguration coreConfiguration = JobCoreConfiguration
        .newBuilder("mySimpleJob", "0/10 * * * * ?", 2)
        .build();

    JobTypeConfiguration jtc = new SimpleJobConfiguration(coreConfiguration,
        MySimpleJob.class.getCanonicalName());

    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(jtc).overwrite(true)
        .build();
    return liteJobConfiguration;
  }


  public static LiteJobConfiguration getDataFlowConfig() {
    JobCoreConfiguration coreConfiguration = JobCoreConfiguration
        .newBuilder("myDataFlowJob", "0/10 * * * * ?", 2)
        .build();

    //DataFlowJob
    JobTypeConfiguration jtc = new DataflowJobConfiguration(coreConfiguration,
        MyDataFlow.class.getCanonicalName(), true);

    LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(jtc).overwrite(true)
        .build();
    return liteJobConfiguration;
  }





  public static CoordinatorRegistryCenter zkCenter() {
    String zkList = Joiner.on(",")
        .join(Arrays.asList("118.24.16.109:2181", "118.24.16.109:2182", "118.24.16.109:2183"));
    ZookeeperConfiguration zc = new ZookeeperConfiguration(zkList,
        "simple-reg-center");
    CoordinatorRegistryCenter crc = new ZookeeperRegistryCenter(zc);
    crc.init();
    return crc;
  }

}
