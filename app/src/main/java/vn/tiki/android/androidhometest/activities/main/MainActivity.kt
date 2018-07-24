package vn.tiki.android.androidhometest.activities.main

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import vn.tiki.android.androidhometest.R
import vn.tiki.android.androidhometest.activities.main.adapter.ProductAdapter
import vn.tiki.android.androidhometest.data.api.ApiServices
import vn.tiki.android.androidhometest.data.api.response.Deal
import vn.tiki.android.androidhometest.di.initDependencies
import vn.tiki.android.androidhometest.di.inject
import vn.tiki.android.androidhometest.di.releaseDependencies
import java.util.*

class MainActivity : AppCompatActivity() {

    val apiServices by inject<ApiServices>()

    companion object {
        val mMap: HashMap<Int, Int> = hashMapOf()
    }

    private lateinit var mProductAdapter: ProductAdapter
    private lateinit var mTimer: Timer
    private lateinit var setCountDownTask: TimerTask
    private var mListProduct: MutableList<Deal> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDependencies(this)
        setContentView(R.layout.activity_main)
        mTimer = Timer()
        mProductAdapter = ProductAdapter(this, mListProduct)
        listProduct.layoutManager = GridLayoutManager(this, 2)
        listProduct.adapter = mProductAdapter
        val loadProductList = LoadProductList()
        loadProductList.execute()
        setCountDownTask = object : TimerTask() {
            override fun run() {
                for ((key, value) in mMap) {
                    if (value > 0) {
                        mMap[key] = value - 1
                        if (value - 1 <= 0) {
                            for (i in 0 until mListProduct.size) {
                                if (key == mListProduct[i].productId) {
                                    mListProduct.removeAt(i)
                                    runOnUiThread {
                                        mProductAdapter.notifyItemRemoved(i)
                                    }
                                    break
                                }
                            }
                        }
                    }
                }
                if (mListProduct.size == 0) mTimer.cancel()
            }
        }
    }



    @SuppressLint("StaticFieldLeak")
    inner class LoadProductList : AsyncTask<Void, Void, List<Deal>>() {

        override fun doInBackground(vararg params: Void?): List<Deal> {
            return apiServices.getDeals()
        }

        override fun onPostExecute(result: List<Deal>?) {
            super.onPostExecute(result)
            if (result != null) {
                mListProduct.clear()
                mListProduct.addAll(result)
                mProductAdapter.notifyDataSetChanged()
                for (i in 0 until result.size) {
                    mMap[result[i].productId] = (result[i].endDate.time - result[i].startedDate.time).toInt() / 1000
                }
                startCountDownTimer()
            }
        }
    }


    private fun startCountDownTimer() {
        mTimer.schedule(setCountDownTask, 0, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer.cancel()
        releaseDependencies()
    }
}
