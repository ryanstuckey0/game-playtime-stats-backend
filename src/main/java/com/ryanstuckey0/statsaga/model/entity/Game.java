package com.ryanstuckey0.statsaga.model.entity;

import com.github.dozermapper.core.Mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Game extends BaseRecordClass {

    @Mapping("appid")
    private Long steamAppId;

    @Column(nullable = false)
    private String name;

    @Mapping("imgIconUrl")
    private String steamGameIconUrl;

    @Mapping("imgLogoUrl")
    private String steamGameLogoUrl;
}
