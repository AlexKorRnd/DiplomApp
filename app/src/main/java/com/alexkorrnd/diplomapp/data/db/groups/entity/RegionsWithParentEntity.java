package com.alexkorrnd.diplomapp.data.db.groups.entity;


import java.util.List;

public class RegionsWithParentEntity {
    RegionEntity regionEntity;
    List<RegionEntity> parents;

    public RegionsWithParentEntity(RegionEntity regionEntity, GroupEntity groupEntity) {
        this.regionEntity = regionEntity;
        this.groupEntity = groupEntity;
    }

    public RegionEntity getRegionEntity() {
        return regionEntity;
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }
}
