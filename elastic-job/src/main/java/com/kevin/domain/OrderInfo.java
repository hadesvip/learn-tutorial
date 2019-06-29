package com.kevin.domain;

import java.util.Date;
import lombok.Data;

/**
 * 订单信息
 *
 * @author wangyong
 */
@Data
public class OrderInfo {

  /**
   * 订单编号
   */
  private int orderId;

  /**
   * 订单金额
   */
  private String amount;

  /**
   * 订单状态
   */
  private String status;

  /**
   * 创建时间
   */
  private Date createDate;


  public OrderInfo(int orderId, String amount, String status, Date createDate) {
    this.orderId = orderId;
    this.amount = amount;
    this.status = status;
    this.createDate = createDate;
  }
}
