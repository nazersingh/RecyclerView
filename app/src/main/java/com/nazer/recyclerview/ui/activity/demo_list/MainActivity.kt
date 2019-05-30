package com.nazer.recyclerview.ui.activity.demo_list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.nazer.recyclerview.R
import com.nazer.recyclerview.pojo.DataPojo
import com.nazer.recyclerview.ui.adapter.ListAdapter
import com.nazer.recyclerview.ui.base.BaseActivity
import com.nazer.recyclerview.ui.base.BaseObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity(), BaseObserver<ArrayList<DataPojo>> {

    var viewModel= ViewModel(this)


    val adapter: ListAdapter = ListAdapter(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpObservers()
        setupLayout()
    }

    private fun setupLayout() {


        recycler.layoutManager = LinearLayoutManager(this)
//       recycler.layoutManager=GridLayoutManager(this,2)

        recycler.addOnScrollListener(onScrollListener)

        recycler.adapter = adapter

    }

    internal var onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {

    }



    override fun setUpBinding() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setUpObservers() {
        viewModel.addObserver(this)
    }
    override fun addDisposal(disposable: Disposable) {
        addViewDisposal(disposable)
    }

    override fun update(o: Observable?, arg: Any?) {
    }
    override fun getApiData(t: ArrayList<DataPojo>) {
        adapter.addItems(t)
    }

}
