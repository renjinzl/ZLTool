//package com.renjinzl.tool.view
//
//import android.content.Context
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.view.ViewParent
//import androidx.annotation.ColorInt
//import androidx.core.view.children
//import androidx.core.view.updateLayoutParams
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.StaggeredGridLayoutManager
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
//import com.renjinzl.tool.R
//import java.lang.RuntimeException
//
///**
//
// * Author   renjinzl
// * Date     2022-06-08 15:44
// * Email    renjinzl@163.com
// * Version  1.0.0   @Update 2022-06-08 15:44   起航
// * Describe 加载更多 刷新
// */
//
//fun <T : View> T.refresh(block: T.() -> Unit): T {
//    return refresh(0xFF2840) { block() }
//}
//
///**
// * 添加刷新
// * 使用loadDataComplete()结束刷新状态
// *
// * ！！注意，外面一定要用FrameLayout包一层，bug未解决！！
// */
//fun <T : View> T.refresh(@ColorInt vararg colors: Int, block: T.() -> Unit): T {
//    parent?.let {
//        val rootView: View = parent as View
//        if (rootView !is SwipeRefreshLayout){
//            val refreshLayout = SwipeRefreshLayout(context)
//            refreshLayout.setColorSchemeColors(*colors)
//            refreshLayout.layoutParams =  layoutParams
////复制约束
////        val paramsClass = layoutParams.javaClass
////        val newInstance = Class.forName(paramsClass.name).getConstructor(ViewGroup.LayoutParams::class.java).newInstance(layoutParams) as ViewGroup.LayoutParams
////        for (paramsField in paramsClass.declaredFields) {
////            if (TextUtils.equals(paramsField.name, "widget")) { // 该属性导致约束布局的后续修改，影响被复制的View属性
////                continue
////            }
////            paramsField.isAccessible = true
////            paramsField.set(newInstance, paramsField.get(layoutParams))
////            println("当前字段 ${paramsField.name}:${paramsField.get(layoutParams)}")
////        }
////        refreshLayout.layoutParams =  newInstance
//
//
//
//            refreshLayout.setOnRefreshListener {
//                block(this)
//            }
//            val viewIndex = (rootView as ViewGroup).children.indexOf(this)
//            rootView.removeView(this)
//            refreshLayout.addView(this, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
//            rootView.addView(refreshLayout, viewIndex, layoutParams)
//        }
//    }
//    return this
//}
//
///**
// * 开始刷新
// */
//fun <T : View> T.startRefresh(block: T.() -> Unit): T {
//    parent?.let {
//        val rootView: View = parent as View
//        if (rootView is SwipeRefreshLayout){
//            if (!rootView.isRefreshing){
//                rootView.isRefreshing = true
//                block.invoke(this)
//            }
//        } else {
//            block.invoke(this)
//        }
//    }?: run {
//        block.invoke(this)
//    }
//    return this
//}
//
///**
// * 加载更多
// */
//fun RecyclerView.loadMore(itemSize: Int = 20, block: LoadMoreEvent.() -> Unit): RecyclerView {
//    val loadMoreEvent = LoadMoreEvent(itemSize,block)
//    setTag(R.id.load_more_loading_view,loadMoreEvent)
//    addOnScrollListener(loadMoreEvent)
//    return this
//}
//
///**
// * 加载完成
// */
//fun RecyclerView.loadDataComplete(canPreLoadMore:Boolean = true): View {
//    getTag(R.id.load_more_loading_view)?.run {
//        if (this is LoadMoreEvent){
//            this.loadDataComplete(canPreLoadMore)
//        }
//    }
//    val rootView: ViewParent? = parent
//    rootView?.run {
//        if (rootView is SwipeRefreshLayout){
//            rootView.isRefreshing = false
//        }
//    }
//    return this
//}
//
///**
// * 跟新图
// */
//fun <T : View>RecyclerView.setDefaultView(defaultClass : Class<T>): T? {
//    if (tempAdapter.data.isEmpty()){
//        val defaultView = defaultClass.getDeclaredConstructor(Context::class.java).newInstance(context)
//        tempAdapter.setHeaderView(defaultView)
//        defaultView.updateLayoutParams {
//            height = this@setDefaultView.height
//            width = this@setDefaultView.width
//        }
//        return defaultView
//    } else {
//        tempAdapter.removeAllHeaderView()
//    }
//    return null
//}
//
//class LoadMoreEvent(var itemSize: Int = 20, val block: LoadMoreEvent.() -> Unit) : RecyclerView.OnScrollListener() {
//
//    /*** 提前几个ITEM预加载下一页数据  */
//    private var preLoadItemCount = 5
//
//    /*** 是否能加载更多数据  */
//    private var canPreLoadMore = true
//
//    /**
//     * 数据加载完成
//     */
//    fun loadDataComplete(canLoadMore:Boolean = true) {
//        canPreLoadMore = canLoadMore
//    }
//
//    /**
//     * 设置预加载数量，实际数量要
//     *
//     * @param preLoadItemCount 距离底部第N个ITEM时触发预加载
//     */
//    fun setPreLoadItemCount(preLoadItemCount: Int) {
//        this.preLoadItemCount = preLoadItemCount + 1
//    }
//
//    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//        super.onScrolled(recyclerView, dx, dy)
//        when (val layoutManager = recyclerView.layoutManager) {
//            is LinearLayoutManager -> {
//                handledLinearLayoutManager(recyclerView,layoutManager, canPreLoadMore)
//            }
//            is StaggeredGridLayoutManager -> {
//                handledStaggeredGridLayoutManager(layoutManager, canPreLoadMore)
//            }
//            is GridLayoutManager -> {
//                handledGridLayoutManager(layoutManager, canPreLoadMore)
//            }
//        }
//    }
//
//    /**
//     * 线性布局 包含 GridLayoutManager
//     *
//     * @param manager     LinearLayoutManager 或者 GridLayoutManager
//     * @param canLoadMore 是否可以加载更多
//     */
//    private fun handledLinearLayoutManager(recyclerView: RecyclerView,manager: LinearLayoutManager, canLoadMore: Boolean) {
//        if (!canLoadMore) {
//            return
//        }
//        /*** 最后一个可见项的位置  */
//        val lastVisibleItem = manager.findLastVisibleItemPosition()
//
//        /*** 适配器总条数  */
//        val totalItemCount = manager.itemCount
//
//        /*** 屏幕内可见项条数  */
//        val visibleItemCount = manager.childCount
//        /*** 可见项＞0 ，且 最后一项可见项 ≥ 预加载项  */
//        if (totalItemCount > preLoadItemCount && visibleItemCount > 0 && lastVisibleItem >= totalItemCount - preLoadItemCount) {
//            if (timeAllowCanLoadMore() && recyclerView.tempAdapter.data.size % itemSize == 0) {
//                canPreLoadMore = false
//                block()
//            }
//        }
//    }
//
//    /**
//     * 瀑布流布局的加载更多
//     *
//     * @param manager StaggeredGridLayoutManager
//     */
//    private fun handledStaggeredGridLayoutManager(manager: StaggeredGridLayoutManager, canLoadMore: Boolean) {
//        if (!canLoadMore) {
//            return
//        }
//        val mSpanCount = manager.spanCount
//        if (mSpanCount == 0) {
//            throw RuntimeException("StaggeredGridLayoutManager的SpanCount不能设置为0~")
//        }
//        val lastVisibleItems = manager.findLastVisibleItemPositions(IntArray(mSpanCount))
//
//        /*** 最后一项可见项位置  */
//        val lastVisibleItem: Int
//        when (manager.spanCount) {
//            1 -> {
//                lastVisibleItem = lastVisibleItems[0]
//            }
//            2 -> {
//                lastVisibleItem = lastVisibleItems[0].coerceAtLeast(lastVisibleItems[1])
//            }
//            3 -> {
//                lastVisibleItem = lastVisibleItems[0].coerceAtLeast(lastVisibleItems[1]).coerceAtLeast(lastVisibleItems[2])
//            }
//            else -> {
//                lastVisibleItem = 0
//                Log.d("1111", "暂不支持SpanCount≥3的情况~")
//            }
//        }
//        /*** 适配器总条数  */
//        val totalItemCount = manager.itemCount
//
//        /*** 屏幕内可见项条数  */
//        val visibleItemCount = manager.childCount
//        if (totalItemCount > preLoadItemCount && visibleItemCount > 0 && lastVisibleItem >= totalItemCount - preLoadItemCount) {
//            if (timeAllowCanLoadMore()) {
//                canPreLoadMore = false
//                block()
//            }
//        }
//    }
//
//
//    /**
//     * 网格布局的加载更多
//     * @param manager
//     * @param canLoadMore
//     */
//    private fun handledGridLayoutManager(manager: GridLayoutManager, canLoadMore: Boolean) {
//        if (!canLoadMore) {
//            return
//        }
//        /*** 最后一个可见项的位置  */
//        val lastVisibleItem = manager.findLastVisibleItemPosition()
//
//        /*** 适配器总条数  */
//        val totalItemCount = manager.itemCount
//
//        /*** 屏幕内可见项条数  */
//        val visibleItemCount = manager.childCount
//        /*** 可见项＞0 ，且 最后一项可见项 ≥ 预加载项  */
//        if (totalItemCount > preLoadItemCount && visibleItemCount > 0 && lastVisibleItem >= totalItemCount - preLoadItemCount) {
//            if (timeAllowCanLoadMore()) {
//                canPreLoadMore = false
//                block()
//            }
//        }
//    }
//
//
//    /*** ========= 限制发起加载更多事件的频率 800毫秒内不允许多次触发，500毫秒后数据已经回来了 =========  */
//    /*** 最后一次发起时间（时间戳 毫秒）  */
//    private var mLastLoadMoreTime: Long = 0
//
//    private fun timeAllowCanLoadMore(): Boolean {
//        val currentTimeMillis = System.currentTimeMillis()
//        val canLoadMore = currentTimeMillis - mLastLoadMoreTime > 1000
//        if (canLoadMore) {
//            // 发起失败的情况下：不允许重新赋值时间，这样会无限延长失败次数
//            mLastLoadMoreTime = currentTimeMillis
//        }
//        return canLoadMore
//    }
//}