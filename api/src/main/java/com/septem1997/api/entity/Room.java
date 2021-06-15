package com.septem1997.api.entity;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table
@DynamicUpdate
public class Room extends BaseEntity implements Serializable {

    private String roomName;
    private String roomNo;
    private String accessKey;
}
