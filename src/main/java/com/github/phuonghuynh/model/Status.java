package com.github.phuonghuynh.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by phuonghqh on 7/17/16.
 */
@Entity
@Table(name = "status")
public class Status
{
  private boolean valid;
  private UUID id;
  private UUID sourceId;
  private UUID dstId;
  private String desc;
  private DateTime timeStamp;
  private int statusType;
  private int payloadType;
  private byte[] payload;

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "id", unique = true, columnDefinition = "BINARY(16)")
  private UUID getId()
  {
    return id;
  }

  @Column(name = "source_id", columnDefinition = "BINARY(16)")
  private UUID getSourceId()
  {
    return sourceId;
  }

  private void setSourceId(UUID sourceId)
  {
    this.sourceId = sourceId;
  }

  @Column(name = "dst_id", columnDefinition = "BINARY(16)")
  private UUID getDstId()
  {
    return dstId;
  }

  private void setDstId(UUID dstId)
  {
    this.dstId = dstId;
  }

  @Column(name = "is_valid")
  @Type(type = "numeric_boolean")
  private boolean isValid()
  {
    return valid;
  }

  private void setValid(boolean valid)
  {
    this.valid = valid;
  }

  private void setId(UUID id)
  {
    this.id = id;
  }

  @Column(name = "desc")
  private String getDesc()
  {
    return desc;
  }

  private void setDesc(String desc)
  {
    this.desc = desc;
  }

  @Column(name = "create_time")
  @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  private DateTime getTimeStamp()
  {
    return timeStamp;
  }

  private void setTimeStamp(DateTime timeStamp)
  {
    this.timeStamp = timeStamp;
  }

  @Column(name = "status_type")
  private int getStatusType()
  {
    return statusType;
  }

  private void setStatusType(int statusType)
  {
    this.statusType = statusType;
  }

  @Column(name = "payload_type")
  private int getPayloadType()
  {
    return payloadType;
  }

  private void setPayloadType(int payloadType)
  {
    this.payloadType = payloadType;
  }

  @Column(name = "payload")
  private byte[] getPayload()
  {
    return payload;
  }

  private void setPayload(byte[] payload)
  {
    this.payload = payload;
  }
}