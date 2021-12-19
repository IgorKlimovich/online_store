package org.academy.OnlineStoreDemo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserDemo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Lob
    @Basic (fetch = FetchType.LAZY)
    private String image;

    @Transient
    public String getPhotosImagePath() {
        if (image == null || id == null) return null;

        return "/user-photos/" + id + "/" + image;
    }
}
