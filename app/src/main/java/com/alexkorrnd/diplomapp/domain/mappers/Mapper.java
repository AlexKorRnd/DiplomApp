package com.alexkorrnd.diplomapp.domain.mappers;


import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactDetailsEntity;
import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactEntity;
import com.alexkorrnd.diplomapp.data.db.contact.entity.DetailTypeEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.GroupEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionEntity;
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionsWithParentEntity;
import com.alexkorrnd.diplomapp.domain.Contact;
import com.alexkorrnd.diplomapp.domain.DetailType;
import com.alexkorrnd.diplomapp.domain.Group;
import com.alexkorrnd.diplomapp.domain.Region;

public class Mapper {

    public static Group mapTo(GroupEntity entity) {
        return new Group(entity.getGid(), entity.getTitle(), entity.getShortTitle());
    }

    public static Region mapTo(RegionsWithParentEntity entity) {
        final RegionEntity regionEntity = entity.getRegionEntity();
        return new Region(regionEntity.getGid(),
                regionEntity.getTitle(),
                regionEntity.getShortTitle(),
                mapTo(entity.getGroupEntity())
        );
    }

    public static Contact mapTo(ContactEntity entity) {
        return new Contact(entity.getKey(), null, entity.getFullName(), entity.getComment(), entity.getAddress());
    }


    public static DetailType mapTo(DetailTypeEntity detailTypeEntity,
                                   ContactDetailsEntity contactDetailsEntity) {
        return new DetailType(detailTypeEntity.getKey(),
                detailTypeEntity.getTitle(),
                detailTypeEntity.getShortTitle(),
                contactDetailsEntity.getValue()
                );
    }
}
