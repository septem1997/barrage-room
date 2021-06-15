package com.septem1997.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
public class Barrage extends BaseEntity implements Serializable {
    private String content;

//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY,optional=false)
//    @JoinColumn(name="room_id")
//    private Room room;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY,optional=false)
    @JoinColumn(name="user_id")
    private User user;

    private String userNickname;
}
