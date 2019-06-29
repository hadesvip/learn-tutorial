package com.kevin.autoconfig.elastic.job;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Component;

/**
 * elastic-job注解
 *
 * @author wangyong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ElasticJob {

  String jobName() default "";

  String corn() default "";

  int shardingTotalCount() default 1;

  boolean overwrite() default false;


}
