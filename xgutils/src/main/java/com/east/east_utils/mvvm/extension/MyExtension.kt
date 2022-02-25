package com.east.east_utils.mvvm.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * |---------------------------------------------------------------------------------------------------------------|
 *  @description:
 *  @author: East
 *  @date: 2019-09-16
 * |---------------------------------------------------------------------------------------------------------------|
 */

fun <E : ViewModel> FragmentActivity.viewModel(a: Class<E>): E {
    return ViewModelProvider(this).get(a)
}

fun <E : ViewModel> Fragment.viewModel(a: Class<E>): E {
    return ViewModelProvider(this).get(a)
}