package com.pyn.photogallery.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * Description：为 https://github.com/CymChad/BaseRecyclerViewAdapterHelper 这个开源库的 Recyclerview adapter 写的 base with ViewBinding
 * @author Created by pengyanni
 * @e-mail pengyanni@viabtc.com
 * @time   2021/11/19 2:49 下午
 */

// BaseRecyclerViewAdapterHelper
abstract class BaseBindingQuickAdapter<T, VB : ViewBinding>(layoutResId: Int = -1) :
    BaseQuickAdapter<T, BaseBindingQuickAdapter.BaseBindingHolder>(layoutResId) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        BaseBindingHolder(inflateBindingWithGeneric<VB>(parent))

    class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
        constructor(itemView: View) : this(ViewBinding { itemView })

        @Suppress("UNCHECKED_CAST")
        fun <VB : ViewBinding> getViewBinding() = binding as VB
    }
}

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(parent: ViewGroup): VB =
    inflateBindingWithGeneric(LayoutInflater.from(parent.context), parent, false)

@JvmName("inflateWithGeneric")
fun <VB : ViewBinding> Any.inflateBindingWithGeneric(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
): VB =
    withGenericBindingClass<VB>(this) { clazz ->
        clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
            .invoke(null, layoutInflater, parent, attachToParent) as VB
    }.also { binding ->
        if (this is Fragment && binding is ViewDataBinding) {
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

private fun <VB : ViewBinding> withGenericBindingClass(genericOwner: Any, block: (Class<VB>) -> VB): VB {
    var genericSuperclass = genericOwner.javaClass.genericSuperclass
    var superclass = genericOwner.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType) {
            genericSuperclass.actualTypeArguments.forEach {
                try {
                    return block.invoke(it as Class<VB>)
                } catch (e: NoSuchMethodException) {
                } catch (e: ClassCastException) {
                } catch (e: InvocationTargetException) {
                    var tagException: Throwable? = e
                    while (tagException is InvocationTargetException) {
                        tagException = e.cause
                    }
                    throw tagException ?: IllegalArgumentException("ViewBinding generic was found, but creation failed.")
                }
            }
        }
        genericSuperclass = superclass.genericSuperclass
        superclass = superclass.superclass
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}

