package com.eosr14.masksearch.ui.main

import android.app.Activity
import android.view.View
import com.eosr14.masksearch.R
import com.eosr14.masksearch.common.extension.convertDisplayDate
import com.eosr14.masksearch.common.extension.convertMaskCount
import com.eosr14.masksearch.model.MaskStoreModel
import kotlinx.android.synthetic.main.custom_callout_balloon.view.*
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem

class CustomMarkerBalloonAdapter(
    private val activity: Activity
) : CalloutBalloonAdapter {

    private val container: View =
        activity.layoutInflater.inflate(R.layout.custom_callout_balloon, null)


    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View? {
        return null
    }

    override fun getCalloutBalloon(item: MapPOIItem?): View {
        item?.userObject?.apply {
            val model = this as MaskStoreModel.Stores

            container.title.text = model.name

            when {
                model.addr.isNullOrEmpty() -> container.address.visibility = View.GONE
                else -> {
                    container.address.visibility = View.VISIBLE
                    container.address.text = model.addr
                }
            }

            when {
                model.remainStat.isNullOrEmpty() -> container.mask_count.visibility = View.GONE
                else -> {
                    container.mask_count.visibility = View.VISIBLE
                    container.mask_count.text = model.remainStat.convertMaskCount()
                }
            }

            when {
                model.createdAt.isNullOrEmpty() -> container.created_at.visibility = View.GONE
                else -> {
                    container.created_at.visibility = View.VISIBLE
                    container.created_at.text = model.createdAt.convertDisplayDate()
                }
            }
        }

        return container
    }


}