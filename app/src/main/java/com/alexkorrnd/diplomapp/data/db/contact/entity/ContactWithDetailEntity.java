package com.alexkorrnd.diplomapp.data.db.contact.entity;


import java.util.List;

public class ContactWithDetailEntity {

    ContactEntity contactEntity;
    List<DetailTypeEntity> detailTypeEntities;

    public ContactWithDetailEntity(ContactEntity contactEntity,
                                   List<DetailTypeEntity> detailTypeEntities) {
        this.contactEntity = contactEntity;
        this.detailTypeEntities = detailTypeEntities;
    }

    public ContactEntity getContactEntity() {
        return contactEntity;
    }

    public List<DetailTypeEntity> getDetailTypeEntities() {
        return detailTypeEntities;
    }
}
