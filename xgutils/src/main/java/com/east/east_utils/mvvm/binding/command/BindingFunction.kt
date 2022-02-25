package com.east.east_utils.mvvm.binding.command

/**
 * Represents a function with zero arguments.
 *
 * @param <T> the result type
</T> */
interface BindingFunction<T> {
    fun call(): T
}
