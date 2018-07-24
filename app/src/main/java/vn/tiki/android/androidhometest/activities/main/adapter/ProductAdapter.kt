package vn.tiki.android.androidhometest.activities.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import vn.tiki.android.androidhometest.R
import vn.tiki.android.androidhometest.activities.main.MainActivity
import vn.tiki.android.androidhometest.activities.main.MainActivity.Companion.mMap
import vn.tiki.android.androidhometest.data.api.response.Deal
import vn.tiki.android.androidhometest.util.formatDateTime
import vn.tiki.android.androidhometest.util.getBitmapFromAsset
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max

class ProductAdapter constructor(context: Context, var mListProduct: MutableList<Deal>) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var mContext = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return mListProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deal = mListProduct[position]
        holder.txtProductName.text = deal.productName
        holder.txtProductPrice.text = String.format(Locale.US, "Price: %.2f", deal.productPrice)
        holder.ivProductThumbnail.setImageBitmap(getBitmapFromAsset(mContext, deal.productThumbnail))
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("ss")
        holder.txtStartDeals.text = String.format(Locale.US, "Starts in: %s", sdf.format(deal.startedDate))
        holder.position = deal.productId

        holder.startUpdateCountDownTask()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductThumbnail: ImageView = itemView.findViewById(R.id.ivProductThumbnail)
        val txtProductPrice: TextView = itemView.findViewById(R.id.txtProductPrice)
        val txtProductName: TextView = itemView.findViewById(R.id.txtProductName)
        val txtEndDeals: TextView = itemView.findViewById(R.id.txtEndDeals)
        val txtStartDeals: TextView = itemView.findViewById(R.id.txtStartDeals)
        var mTimer: Timer? = null
        var position: Int? = -1
        var updateCountDownTask: TimerTask? = null


        fun startUpdateCountDownTask() {
            updateCountDownTask = object : TimerTask() {
                override fun run() {
                    itemView.post {
                        txtEndDeals.text = "Ends in: ${formatDateTime(mMap[position]!!)}"
                    }
                }
            }
            mTimer?.cancel()
            mTimer = Timer()
            mTimer!!.schedule(updateCountDownTask, 0, 1000)
        }
    }

}