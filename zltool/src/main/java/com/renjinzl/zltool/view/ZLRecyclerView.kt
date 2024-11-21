package com.renjinzl.zltool.view

/**

 * Author   renjinzl
 * Date     2022-06-08 14:26
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-08 14:26   起航
 * Describe 描述
 */

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log
import android.view.*
import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.listener.OnItemLongClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.renjinzl.zltool.R

/**

 * Author   renjinzl
 * Date     2022-06-08 13:54
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-08 13:54   起航
 * Describe 描述
 */
class ZLRecyclerViewAdapter<VH : BaseViewHolder>(private val recyclerView : RecyclerView, data: MutableList<ZLViewModel>?) : BaseMultiItemQuickAdapter<ZLViewModel, VH>(data), OnItemClickListener, OnItemChildClickListener ,
    OnItemLongClickListener {

    var mOnRecyclerViewItemClickListeners: MutableList<OnItemClickListener> = arrayListOf()

    var mOnItemChildClickListener: OnItemChildClickListener? = null

    var mOnItemLongClickListener: OnItemLongClickListener? = null

    private val numberInScreen = 0

    private var mWidth = 0

    private var itemWidth = 0

    var maxCount = 0

    override fun getItemCount(): Int {
        return if (maxCount > 0 && maxCount < super.getItemCount()) {
            maxCount
        } else {
            super.getItemCount()
        }
    }

    private fun getItemWidth(): Int {
        if (itemWidth == 0 && numberInScreen > 0) {
            itemWidth = mWidth / numberInScreen
        }
        return itemWidth
    }

    override fun convert(helper: VH, item: ZLViewModel) {
        val viewItem: ZLRecyclerViewItemView<*>? = recyclerView.viewItems[item.itemType]
        if (viewItem != null) {
            viewItem.params(helper, getItemWidth())
            viewItem.initViews(helper, item)
        }
    }

    init {
        recyclerView.viewItems.let {
            for ((key, value) in it.entries) {
                addItemType(key, value.itemLayout)
                addChildClickViewIds(*value.itemChildClickViewIds)
            }
        }

        setOnItemLongClickListener(this)
        setOnItemClickListener(this)
        setOnItemChildClickListener(this)
    }

    fun addItemType(viewItem: ZLRecyclerViewItemView<*>) {
        addItemType(viewItem.itemType, viewItem.itemLayout)
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {

        if (adapter.data.size > 0) {
            val viewItem: ZLRecyclerViewItemView<*>? = recyclerView.viewItems[((adapter.data[position] as MultiItemEntity)).itemType]
            viewItem?.onItemClick(adapter, view, adapter.data[position] as MultiItemEntity, position)
//            mOnRecyclerViewItemClickListeners.let {
//                for (listener in it) {
//                    listener.onItemClick(adapter, view, position)
//                }
//            }
            mOnRecyclerViewItemClickListeners.forEach {
                it.onItemClick(adapter, view, position)
            }
        }
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
        mOnItemChildClickListener?.onItemChildClick(adapter, view, position)
        if (adapter.data.size > 0) {
            val viewItem: ZLRecyclerViewItemView<*>? = recyclerView.viewItems[((adapter.data[position] as MultiItemEntity)).itemType]
            adapter.data[position]?.let { viewItem?.onItemChildClick(adapter, view, it, position) }
        }
    }

    override fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean {
        "onItemLongClick".log()
        mOnItemLongClickListener?.onItemLongClick(adapter, view, position)
        if (adapter.data.size > 0) {
            val viewItem: ZLRecyclerViewItemView<*>? = recyclerView.viewItems[((adapter.data[position] as MultiItemEntity)).itemType]
            adapter.data[position]?.let { viewItem?.onItemLongClick(adapter, view, it, position) }
        }
        return true
    }

}

abstract class ZLRecyclerViewItemView<T : MultiItemEntity>(open val itemType: Int = 0, open val itemLayout: Int, vararg childIds: Int) {

    var itemChildClickViewIds: IntArray = childIds

    fun params(helper: BaseViewHolder, mWidth: Int) {
        if (mWidth > 0) {
            val view: View = helper.itemView
            val params = view.layoutParams as RecyclerView.LayoutParams
            params.width = mWidth
            view.layoutParams = params
        }
    }

    abstract fun initViews(helper: BaseViewHolder, item: Any)

    open fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View, item: Any, index: Int) {
    }

    open fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, item: Any, index: Int) {
    }

    fun onItemLongClick(adapter: BaseQuickAdapter<*, *>, view: View, any: Any, position: Int): Boolean {
        return false
    }

}

val RecyclerView.viewItems: MutableMap<Int, ZLRecyclerViewItemView<*>>
//= mutableMapOf()
    get() {
    if (getTag(R.id.content) == null){
        setTag(R.id.content,mutableMapOf<Int, ZLRecyclerViewItemView<*>>())
    }
        return getTag(R.id.content) as MutableMap<Int, ZLRecyclerViewItemView<*>>
    }
//    set(value) = setTag(R.id.content,value)

private fun RecyclerView.init(spanCount : Int,@RecyclerView.Orientation orientation: Int) {
    layoutManager = if (spanCount>0){
        GridLayoutManager(context,spanCount, orientation, false)
    }else{
        LinearLayoutManager(context, orientation, false)
    }
//    viewItems = mutableMapOf()
}

fun RecyclerView.initHorizontal() {
    init(0,RecyclerView.HORIZONTAL)
}

fun RecyclerView.initVertical() {
    init(0,RecyclerView.VERTICAL)
}
fun RecyclerView.initHorizontal(spanCount : Int) {
    init(spanCount,RecyclerView.HORIZONTAL)
}

fun RecyclerView.initVertical(spanCount : Int) {
    init(spanCount,RecyclerView.VERTICAL)
}

fun <T : MultiItemEntity> RecyclerView.addItemType(viewItem: ZLRecyclerViewItemView<T>) {
    viewItems[viewItem.itemType] = viewItem

//    tempAdapter.addItemType(viewItem)
//    tempAdapter.addChildClickViewIds(*viewItem.itemChildClickViewIds)

//    if (adapter == null) {
//        val tempAdapter = ZLRecyclerViewAdapter<BaseViewHolder>(this,arrayListOf())
//        tempAdapter.addItemType(viewItem)
//        adapter = tempAdapter
//    }
//    adapter?.apply {
//        val mAdapter: ZLRecyclerViewAdapter<*> = (this as ZLRecyclerViewAdapter<*>)
//        mAdapter.addItemType(viewItem)
//    }
}
val RecyclerView.tempAdapter : ZLRecyclerViewAdapter<*>
    get() {
        if (adapter == null){
            adapter = ZLRecyclerViewAdapter<ZLViewHolder>(this, arrayListOf())
        }
        return adapter as ZLRecyclerViewAdapter<*>
    }

fun RecyclerView.setData(data: MutableList<ZLViewModel>) {
    if (viewItems.isNotEmpty()){
        if (adapter == null) {
            adapter = tempAdapter
        }
//        adapter = ZLRecyclerViewAdapter<BaseViewHolder>(this, data)
//        else {
//            val mAdapter: ZLRecyclerViewAdapter<*> = (this.adapter as ZLRecyclerViewAdapter<*>)
//            adapter = mAdapter
//            mAdapter.setNewData(data)
//        }
        tempAdapter.setNewData(data)
        tempAdapter.notifyDataSetChanged()
    }
}
//fun RecyclerView.setNewData(data: MutableList<MultiItemEntity>) {
//    if (viewItems.isNotEmpty()){
//        adapter = ZLRecyclerViewAdapter<BaseViewHolder>(this, data).apply {
//            mOnRecyclerViewItemClickListeners.addAll(tempAdapter.mOnRecyclerViewItemClickListeners)
//        }
//    }
//}

fun RecyclerView.addData(data: MutableList<ZLViewModel>) {
    tempAdapter.addData(data)
}

fun RecyclerView.addData(data: ZLViewModel) {
    adapter?.apply {
        val mAdapter: ZLRecyclerViewAdapter<*> = (this as ZLRecyclerViewAdapter<*>)
        mAdapter.addData(data)
    }
}
fun RecyclerView.addData(@IntRange(from = 0) position: Int,data: ZLViewModel) {
    adapter?.apply {
        val mAdapter: ZLRecyclerViewAdapter<*> = (this as ZLRecyclerViewAdapter<*>)
        mAdapter.addData(position,data)
    }
}
fun RecyclerView.addData(@IntRange(from = 0) position: Int,data: MutableList<ZLViewModel>) {
    adapter?.apply {
        val mAdapter: ZLRecyclerViewAdapter<*> = (this as ZLRecyclerViewAdapter<*>)
        mAdapter.addData(position,data)
    }
}

fun RecyclerView.setOnItemClickListener(onItemClickListener: OnItemClickListener) {
    tempAdapter.mOnRecyclerViewItemClickListeners.add(onItemClickListener)
}

fun RecyclerView.setOnItemChildClickListener(onItemChildClickListener: OnItemChildClickListener) {
    tempAdapter.mOnItemChildClickListener = onItemChildClickListener
}
fun RecyclerView.setOnItemLongClickListener(onItemChildClickListener: OnItemLongClickListener) {
    tempAdapter.mOnItemLongClickListener = onItemChildClickListener
}

fun RecyclerView.notifyItemChanged(position : Int) {
    adapter?.notifyItemChanged(position)
}

fun RecyclerView.notifyDataSetChanged() {
    adapter?.notifyDataSetChanged()
}

fun RecyclerView.setMaxCount(count : Int) {
    tempAdapter.maxCount = count
}

val RecyclerView.maxCount : Int get() = tempAdapter.maxCount

/**

 * @Author   renjinzl
 * @Date     2020/5/26 11:42 AM
 * @Email    renjinzl@163.com
 * @Version  1.0.0   @Update 2020/5/26 11:42 AM   起航
 * @Describe 头尾渐隐渐出
 */
class ZLRecyclerViewFadeOnScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstPosition = layoutManager.findFirstVisibleItemPosition()
        val lastPosition = layoutManager.findLastVisibleItemPosition()

        for (index in firstPosition until lastPosition){
            layoutManager.findViewByPosition(firstPosition)?.alpha = 1f
        }

        val firstView = layoutManager.findViewByPosition(firstPosition)
        if (firstView != null) {
            val firstViewTop = firstView.top.toFloat()
            Log.d("epet", "-----epet-------")
            firstView.alpha = (firstView.height + firstViewTop) / firstView.height
        }

        val lastView = layoutManager.findViewByPosition(lastPosition)
        if (lastView != null) {
            val lastViewBottom = lastView.bottom.toFloat()
            Log.d("epet", "-----epet-------" + lastViewBottom + " " + lastView.height + " " + recyclerView.height)
            lastView.alpha = (recyclerView.height - lastView.height - lastViewBottom) / lastView.height
//            lastView.alpha = lastViewBottom / (lastView.height * 2)
        }
    }
}

//----------------test-------------

fun RecyclerView.testSetData(int : Int) {
    val data: MutableList<ZLViewModel> = arrayListOf()
    for (i in 0 until int) {
        data.add(TestEntity(0))
    }
    setData(data)
}

fun RecyclerView.testData(int : Int) {
//    initVertical()


    refresh(0x0000ff) {
        loadDataComplete()
    }.loadMore {
        loadDataComplete()
        Log.d("----------------", "12312312312313")
    }

}

fun RecyclerView.test(int : Int) {
//    initVertical()
    addItemType(TestItemView())

    testData(20)
}

fun RecyclerView.test() {
    test(20)
}

class TestEntity(override val itemType: Int) :  ZLViewModel() ,MultiItemEntity{
    var text :String = "它致"
    var isCheck = false
}

class TestItemView : ZLRecyclerViewItemView<TestEntity>(0, R.layout.test_item_layout) {
//    var mBinding : TestItemLayoutBinding? = null
//    val data : TestEntity = TestEntity(0)
    override fun initViews(helper: BaseViewHolder, item: Any) {
    val itemData = item as TestEntity
//        mBinding = helper.getBinding()
//        mBinding = TestItemLayoutBinding.inflate(LayoutInflater.from(helper.itemView.context),helper.itemView as ViewGroup,false)
//        mBinding?.data = item as TestEntity
//        item.text = "2123123"
//        mBinding?.data?.text = "1231231"

        helper.setText(R.id.content_text_view,itemData.text)
    }
}


open class ZLViewModel(override val itemType: Int = 0) : ViewModel() , MultiItemEntity{
    //悬停
    var itemIsHover = false
}


open class ZLViewHolder(view: View) : BaseViewHolder(view)

/**
 * 导航栏的高度(如果显示了)
 */
fun navBarHeight(context: Context): Int {
    var result = 0

    if (context is Activity) {
        val decorRect = Rect()
        val windowRect = Rect()

        context.window.decorView.getGlobalVisibleRect(decorRect)
        context.window.findViewById<View>(Window.ID_ANDROID_CONTENT).getGlobalVisibleRect(windowRect)

        result = if (decorRect.width() > decorRect.height()) {
            //横屏
            decorRect.width() - windowRect.width()
        } else {
            //竖屏
            decorRect.bottom - windowRect.bottom
        }
    }

    return result
}

public val <T> T.dp: Float by lazy {
    Resources.getSystem()?.displayMetrics?.density ?: 0f
}