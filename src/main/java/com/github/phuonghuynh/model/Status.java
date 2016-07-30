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
 * This is the main 'message' object type.  Instances of this class will be
 * sent back and forth from clients and servers.  Either via Chronicle Queue
 * or JMS/ActiveMQ Artemis.
 *
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

  /**
   * The actual message's ID
   *
   * @return
     */
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @Column(name = "id", unique = true, columnDefinition = "BINARY(16)")
  public UUID getId()
  {
    return id;
  }

  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * ID of sending service/client
   * @return
     */
  @Column(name = "source_id", columnDefinition = "BINARY(16)")
  public UUID getSourceId()
  {
    return sourceId;
  }

  public void setSourceId(UUID sourceId)
  {
    this.sourceId = sourceId;
  }

  /**
   * ID of receiver client/service.
   *
   * @return
     */
  @Column(name = "dst_id", columnDefinition = "BINARY(16)")
  public UUID getDstId()
  {
    return dstId;
  }

  public void setDstId(UUID dstId)
  {
    this.dstId = dstId;
  }

  /**
   * Flags this status message as valid or not
   * @return
     */
  @Column(name = "is_valid")
  @Type(type = "numeric_boolean")
  public boolean isValid()
  {
    return valid;
  }

  public void setValid(boolean valid)
  {
    this.valid = valid;
  }

  @Column(name = "desc")
  public String getDesc()
  {
    return desc;
  }

  public void setDesc(String desc)
  {
    this.desc = desc;
  }

  /**
   * Create time-stamp of status message
   * @return
     */
  @Column(name = "create_time")
  @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
  public DateTime getTimeStamp()
  {
    return timeStamp;
  }

  public void setTimeStamp(DateTime timeStamp)
  {
    this.timeStamp = timeStamp;
  }

  /**
   * Status message type.
   *
   * @return
     */
  @Column(name = "status_type")
  public int getStatusType()
  {
    return statusType;
  }

  public void setStatusType(int statusType)
  {
    this.statusType = statusType;
  }

  /**
   * Message Payload's type
   * @return
     */
  @Column(name = "payload_type")
  public int getPayloadType()
  {
    return payloadType;
  }

  public void setPayloadType(int payloadType)
  {
    this.payloadType = payloadType;
  }

  /**
   * Bytes carried along with this status message
   * @return
     */
  @Column(name = "payload")
  public byte[] getPayload()
  {
    return payload;
  }

  public void setPayload(byte[] payload)
  {
    this.payload = payload;
  }
}