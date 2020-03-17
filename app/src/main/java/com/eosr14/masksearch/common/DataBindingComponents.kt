package com.eosr14.masksearch.common

import android.graphics.drawable.Animatable
import android.net.Uri
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eosr14.masksearch.R
import com.eosr14.masksearch.model.KaKaoKeyWord
import com.eosr14.masksearch.model.MaskStoreModel
import com.eosr14.masksearch.ui.main.AutoCompleteAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.RotationOptions
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequestBuilder
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

object DataBindingComponents {

    @JvmStatic
    @BindingAdapter("storesToMarker")
    fun setStoresToMarker(mapView: MapView, data: List<MaskStoreModel.Stores>) {
        data.forEach {
            mapView.addPOIItem(MapPOIItem().apply {
                itemName = it.name
                tag = it.code?.toIntOrNull() ?: 0
                mapPoint = MapPoint.mapPointWithGeoCoord(it.lat ?: 0.0, it.lng ?: 0.0)
                markerType = MapPOIItem.MarkerType.CustomImage
                customImageResourceId = R.drawable.pharmacy
                isCustomImageAutoscale = false
                setCustomImageAnchor(0.5f, 1.0f)
                userObject = it
            })
        }
    }

    @JvmStatic
    @BindingAdapter("autoCompleteList")
    fun setAutoCompleteList(recyclerView: RecyclerView, items: MutableList<KaKaoKeyWord.Document>) {
        recyclerView.adapter?.let { adapter ->
            if (adapter is AutoCompleteAdapter) {
                adapter.setItems(items)
                recyclerView.scheduleLayoutAnimation()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("urlToImage")
    fun setUrlToImage(view: SimpleDraweeView, url: String) {
        val imageRequest = ImageRequestBuilder
            .newBuilderWithSource(Uri.parse(url))
            .setRotationOptions(RotationOptions.autoRotate())
            .setProgressiveRenderingEnabled(true)
            .build()

        view.controller = Fresco.newDraweeControllerBuilder().run {
            this.oldController = view.controller
            this.imageRequest = imageRequest
            this.autoPlayAnimations = true
            this.controllerListener = object : BaseControllerListener<ImageInfo>() {
                override fun onFinalImageSet(
                    id: String?,
                    imageInfo: ImageInfo?,
                    animatable: Animatable?
                ) {
                    super.onFinalImageSet(id, imageInfo, animatable)
                    imageInfo?.let { info ->
                        view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        view.aspectRatio = info.width.toFloat() / info.height
                    }
                }
            }
            this.build()
        }
    }

}