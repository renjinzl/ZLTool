package com.renjinzl.zltool.view

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

/**
 * ViewPager
 * @author   renjinzl
 * date     2022-06-18 10:56
 *
 * version  1.0.0   update 2022-06-18 10:56   起航
 * @since 描述
 */
interface ZLViewPagerItemEntity {
    val title: String
}

inline fun <T : ZLViewPagerItemEntity, reified R : View> ViewPager.setData(data: MutableList<T>, noinline block: (T) -> R) {
    if (adapter == null) {
        adapter = ViewPagerPagerAdapter(data) {
            block(data[it])
        }
    } else {
//        (adapter as ViewPagerPagerAdapter<*,*>).apply {
////            mData = (data)
//            adapter?.notifyDataSetChanged()
//        }
        adapter?.notifyDataSetChanged()
    }
}

/**
 * setFragment
 */
fun <T : ZLFragment> ViewPager.setFragment(dataList: MutableList<T>, fragmentManager: FragmentManager) {
    adapter = ZLFragmentPagerAdapter(dataList, fragmentManager)
}

class ViewPagerPagerAdapter<T : ZLViewPagerItemEntity,R : View>(var mData: MutableList<T>, val block: (Int) -> R) : PagerAdapter() {

    private val dataView: MutableList<R> = arrayListOf()

    override fun getCount(): Int {
        return mData.size
    }

    override fun isViewFromObject(view: View, temp: Any): Boolean {
        return view == temp
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        if (position >= dataView.size) {
            dataView.add(block(position))
        }
        container.addView(dataView[position])
        return dataView[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, tempView: Any) {
        container.removeView(tempView as View)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if (position < mData.size) {
            return mData[position].title
        }
        return super.getPageTitle(position)
    }

}

open class ZLFragment : Fragment() {

    open var title: String = ""

    override fun onStart() {
        super.onStart()
        zlComeBack()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            zlComeBack()
        }else{
            zlExit()
        }
    }

    /**
     * 肉眼可见
     */
    open fun zlComeBack() {}

    open fun zlExit() {

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

class ZLFragmentPagerAdapter<T : ZLFragment>(private val dataList: MutableList<T>, fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

//    override fun getCount(): Int = dataList.size

    override fun getCount(): Int {
        return dataList.size
    }

//    override fun isViewFromObject(view: View, temp: Any): Boolean {
//        return view === temp
//    }

    override fun getItem(position: Int): Fragment {
        return dataList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return dataList[position].title
    }

//    override fun getPageTitle(position: Int): CharSequence? {
//        return dataList.get(position)
//    }

//    override fun destroyItem(container: ViewGroup, position: Int, temp: Any) {
//        super.destroyItem(container, position, temp)
//    }


}