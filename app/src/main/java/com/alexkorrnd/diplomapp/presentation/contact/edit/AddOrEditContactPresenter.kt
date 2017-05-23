package com.alexkorrnd.diplomapp.presentation.contact.edit

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.alexkorrnd.diplomapp.MainApp
import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactDetailsEntity
import com.alexkorrnd.diplomapp.data.db.contact.entity.ContactEntity
import com.alexkorrnd.diplomapp.data.db.contact.entity.DetailTypeEntity
import com.alexkorrnd.diplomapp.data.db.contact.tables.DetailTypesTable
import com.alexkorrnd.diplomapp.data.db.groups.entity.GroupEntity
import com.alexkorrnd.diplomapp.data.db.groups.entity.RegionEntity
import com.alexkorrnd.diplomapp.data.db.groups.tables.GroupsTable
import com.alexkorrnd.diplomapp.data.db.groups.tables.RegionsTable
import com.alexkorrnd.diplomapp.domain.Contact
import com.alexkorrnd.diplomapp.domain.DetailType
import com.alexkorrnd.diplomapp.domain.Group
import com.alexkorrnd.diplomapp.domain.mappers.Mapper
import com.alexkorrnd.diplomapp.domain.mappers.Mapper.mapTo
import com.alexkorrnd.diplomapp.presentation.BasePresenter
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import com.pushtorefresh.storio.sqlite.operations.put.PutResult
import com.pushtorefresh.storio.sqlite.operations.put.PutResults
import com.pushtorefresh.storio.sqlite.queries.Query
import rx.Single
import rx.SingleSubscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class AddOrEditContactPresenter(private val view: View,
                                private val sqLiteOpenHelper: SQLiteOpenHelper,
                                private val storIOSQLite: StorIOSQLite
) : BasePresenter {

    interface View {
        fun onGroupsLoaded(groups: List<Group>)
        fun onContactSaved(contact: Contact)
        fun onDetailTypesLoaded(detailTypes: List<DetailType>)
    }

    fun loadGroups() {
        storIOSQLite
                .get()
                .listOfObjects(GroupEntity::class.java)
                .withQuery(Query.builder()
                        .table(GroupsTable.TABLE)
                        .build())
                .prepare()
                .asRxSingle()
                .subscribe({ groups ->
                    view.onGroupsLoaded(listMapTo(groups))
                })
    }

    private fun getDetailTypes(): Single<List<DetailTypeEntity>> {
        return storIOSQLite
                .get()
                .listOfObjects(DetailTypeEntity::class.java)
                .withQuery(Query.builder()
                        .table(DetailTypesTable.TABLE)
                        .build())
                .prepare()
                .asRxSingle()
    }

    fun loadDetailTypes() {
        getDetailTypes()
                .subscribeOn(Schedulers.io())
                //.map{ types -> types.map { Mapper.mapTo(it) }}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<List<DetailTypeEntity>?>() {
                    override fun onError(error: Throwable?) {

                    }

                    override fun onSuccess(t: List<DetailTypeEntity>?) {
                        val list = ArrayList<DetailType>()
                        t?.forEach { list.add(Mapper.mapTo(it)) }
                        view.onDetailTypesLoaded(list)
                    }
                })
    }

    fun saveContact(id: String?,
                    fullName: String?,
                    address: String?,
                    comment: String?,
                    groupId: String?,
                    regionId: String,
                    detailTypes: List<DetailType>
    ) {
        val contactEntity = ContactEntity()
        contactEntity.key = id
        contactEntity.fullName = fullName
        contactEntity.address = address
        contactEntity.comment = comment
        contactEntity.groupId = groupId
        contactEntity.regionId = regionId
        if (id == null) {
            contactEntity.key = createId()
        } else {
        }
        saveContact(contactEntity, detailTypes)
    }

    private fun saveContact(contactEntity: ContactEntity, detailTypes: List<DetailType>) {
        putContact(contactEntity)
                .subscribeOn(Schedulers.io())
                .subscribe(object : SingleSubscriber<PutResult?>() {
                    override fun onError(error: Throwable?) {
                        Log.d("aaa", error?.message, error)
                        Toast.makeText(MainApp.instance, "При сохранении контакта произошла ошибка", Toast.LENGTH_LONG).show()
                    }

                    override fun onSuccess(t: PutResult?) {
                        val contact = Mapper.mapTo(contactEntity)
                        contact.detailTypes = detailTypes
                        putDetailes(contact, contactEntity, detailTypes)
                    }
                })
    }

    private fun loadGroupById(contact: Contact, contactEntity: ContactEntity) {
        storIOSQLite
                .get()
                .`object`(GroupEntity::class.java)
                .withQuery(Query.builder()
                        .table(GroupsTable.TABLE)
                        .where(GroupsTable.COLUMN_ID + " =?")
                        .whereArgs(contactEntity.groupId)
                        .build())
                .prepare()
                .asRxSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ group ->
                    contact.group = Mapper.mapTo(group)
                    loadRegionById(contact, contactEntity)
                })
    }

    private fun loadRegionById(contact: Contact, contactEntity: ContactEntity) {
        storIOSQLite
                .get()
                .`object`(RegionEntity::class.java)
                .withQuery(Query.builder()
                        .table(RegionsTable.TABLE)
                        .where(RegionsTable.COLUMN_ID + " =?")
                        .whereArgs(contactEntity.regionId)
                        .build())
                .prepare()
                .asRxSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ region ->
                    contact.region = Mapper.mapTo(region)
                    view.onContactSaved(contact)
                })
    }

    private fun putContact(contactEntity: ContactEntity): Single<PutResult> {
        return storIOSQLite
                .put()
                .`object`(contactEntity)
                .prepare()
                .asRxSingle()
    }

    private fun putDetailes(contact: Contact,
                            contactEntity: ContactEntity,
                            detailTypes: List<DetailType>) {
        val entities = ArrayList<ContactDetailsEntity>()
        detailTypes.forEach {
            entities.add(mapTo(contactEntity.key,
                    it))
        }
        storIOSQLite
                .put()
                .objects(entities)
                .prepare()
                .asRxSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleSubscriber<PutResults<ContactDetailsEntity>?>() {
                    override fun onSuccess(t: PutResults<ContactDetailsEntity>?) {
                        loadGroupById(contact, contactEntity)
                    }

                    override fun onError(error: Throwable?) {

                    }
                })
    }

    private fun mapTo(contactId: String,
                      detailType: DetailType): ContactDetailsEntity {
        val entity = ContactDetailsEntity()
        entity.contactId = contactId
        entity.typeId = detailType.key
        entity.value = detailType.value
        entity.gid = detailType.id
        if (entity.gid == null) {
            entity.gid = createId()
        }
        return entity
    }

    private fun createId(): String {
        return UUID.randomUUID().toString()
    }

    private fun listMapTo(groupEntities: List<GroupEntity>): List<Group> {
        val groups = ArrayList<Group>()
        for (groupEntity in groupEntities) {
            groups.add(mapTo(groupEntity))
        }
        return groups
    }

    override fun start() {

    }

    override fun stop() {

    }
}
