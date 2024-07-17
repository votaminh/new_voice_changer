package com.flash.light.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.msc.demo_mvvm.base.activity.BaseActivity

abstract class BaseFragment<V : ViewBinding> : Fragment() {
    lateinit var viewBinding: V
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = provideViewBinding(container)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.root.isClickable = true
        initViews()
        initData()
        initObserver()
    }

    fun addFragment(id: Int, fragment: Fragment) {
        (activity as? BaseActivity<*>)?.addFragment(id, fragment)
    }

    fun replaceFragment(id: Int, fragment: Fragment) {
        (activity as? BaseActivity<*>)?.replaceFragment(id, fragment)
    }

    fun showLoading() {
        (activity as? BaseActivity<*>)?.showLoading()
    }

    fun hideLoading() {
        (activity as? BaseActivity<*>)?.hideLoading()
    }

    fun showToast(mes: String, duration: Int = Toast.LENGTH_SHORT) {
        (activity as? BaseActivity<*>)?.showToast(mes, duration)
    }

    abstract fun provideViewBinding(container: ViewGroup?): V
    open fun initViews() {}
    open fun initData() {}

    open fun initObserver() {}

}