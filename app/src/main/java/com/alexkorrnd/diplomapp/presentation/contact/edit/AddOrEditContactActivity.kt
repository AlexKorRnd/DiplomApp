package com.alexkorrnd.diplomapp.presentation.contact.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.alexkorrnd.base.DelegationAdapter
import com.alexkorrnd.base.extensions.textWithSelection
import com.alexkorrnd.diplomapp.R
import com.alexkorrnd.diplomapp.domain.Contact
import com.alexkorrnd.diplomapp.domain.DetailType
import com.alexkorrnd.diplomapp.domain.Group
import com.alexkorrnd.diplomapp.domain.Region
import com.alexkorrnd.diplomapp.presentation.base.BaseActivity
import com.alexkorrnd.diplomapp.presentation.internal.di.DbModule

@Suppress("UNCHECKED_CAST")
class AddOrEditContactActivity : BaseActivity(), AddOrEditContactPresenter.View {

    companion object {
        private val EXTRA_CONTACT = "EXTRA_CONTACT"
        private val EXTRA_REGION = "EXTRA_REGION"

        fun unpackContact(data: Intent): Contact =
                data.getParcelableExtra(EXTRA_CONTACT)

        fun editContanct(context: Context, contact: Contact) =
                Intent(context, AddOrEditContactActivity::class.java).apply {
                    putExtra(EXTRA_CONTACT, contact)
                }

        fun addContact(context: Context, region: Region) =
                Intent(context, AddOrEditContactActivity::class.java).apply {
                    putExtra(EXTRA_REGION, region)
                }
    }

    private val contact: Contact?
        get() = intent.getParcelableExtra(EXTRA_CONTACT)

    private val region: Region
        get() =
        if (contact == null) {
            intent.getParcelableExtra(EXTRA_REGION)
        } else {
            contact!!.region
        }

    private val etName: EditText by lazy { findViewById(R.id.etName) as EditText }
    private val etAddress: EditText by lazy { findViewById(R.id.etAddress) as EditText }
    private val etComment: EditText by lazy { findViewById(R.id.etComment) as EditText }
    private val spinnerGroup: Spinner by lazy { findViewById(R.id.spinnerGroup) as Spinner }

    private val rvItems: RecyclerView by lazy { findViewById(R.id.rvItems) as RecyclerView }

    private lateinit var adaprer: DelegationAdapter

    private lateinit var presenter: AddOrEditContactPresenter

    private lateinit var adapterGroups: ArrayAdapter<Group>

    private var selectedGroupPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        if (contact == null) {
            title = "Добавление контакта"
        } else {
            title = "Редактирование контакта"
        }

        adaprer = DelegationAdapter()
        adaprer.delegatesManager
                .addDelegate(DetailTypeDelegate(adaprer, this))

        val sqLiteOpenHelper = DbModule.provideSQLiteOpenHelper(this)
        presenter = AddOrEditContactPresenter(this,
                sqLiteOpenHelper,
                DbModule.provideStorIOSQLite(sqLiteOpenHelper)
        )

        adapterGroups = ArrayAdapter<Group>(this, android.R.layout.simple_spinner_dropdown_item)
        spinnerGroup.adapter = adapterGroups
        spinnerGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedGroupPosition = position
            }
        }

        if (contact != null) {
            etName.textWithSelection = contact!!.fullName
            etComment.textWithSelection = contact!!.comment
            etAddress.textWithSelection = contact!!.address
        }

        initRecyclerView()

        presenter.loadGroups()
        presenter.loadDetailTypes()
    }

    private fun initRecyclerView() {
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adaprer
        rvItems.isNestedScrollingEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_contact_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val detailes = ArrayList<DetailType>()
                adaprer.items.forEach { if (it is DetailType) detailes.add(it) }
                presenter.saveContact(contact?.id,
                        etName.text.toString(),
                        etAddress.text.toString(),
                        etComment.text.toString(),
                        adapterGroups.getItem(selectedGroupPosition).gid,
                        region.gid,
                        detailes
                        )
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGroupsLoaded(groups: List<Group>) {
        adapterGroups.addAll(groups)
        adapterGroups.notifyDataSetChanged()

        if (contact != null) {
            groups.forEachIndexed { index, group ->
                if (contact!!.group == group) {
                    selectedGroupPosition = index
                    spinnerGroup.setSelection(index)
                }
            }
        }


    }

    override fun onDetailTypesLoaded(detailTypes: List<DetailType>) {
        adaprer.addAll(detailTypes)
        if (contact != null) {
            contact?.detailTypes?.forEach {
                val index = detailTypes.indexOf(it)
                Log.d("aaaaaaaaaaaa", " it $it  index = $index")
                adaprer.replace(it, index)
            }
        }
    }

    override fun onContactSaved(contact: Contact) {
        val text =
        if (this.contact == null) {
            "Контакт успешно добавлен!"
        } else {
            "Контакт успешно изменен!"
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(EXTRA_CONTACT, contact)
        })
        finish()
    }
}
