package com.east.east_utils.ui.recycleview.base_adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 为了减少每次创建RecycleView的Adapter都要写很多重复代码进行的封装
 *  viewType ：1->代表头，2->代表尾，0->代表普通
 * @author East
 * @date：2017/12/8 16:38
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    //item类型
    public static final int ITEM_TYPE_HEADER = 1;
    public static final int ITEM_TYPE_CONTENT = 0;
    public static final int ITEM_TYPE_FOOTER = 2;

    protected List<T> mDatas = new ArrayList<>();
    //    protected OnItemClickListener<T> mOnItemClickListener;
//    protected OnItemLongClickListener<T> mOnItemLongClickListener;
    protected Context mContext;
    protected boolean hasHeader = false;//是否有头部
    protected boolean hasFooter = false;//是否有尾部


    public BaseRecycleAdapter(Context context) {
        this(null, context, false, false);
    }

    public BaseRecycleAdapter(Context context, boolean hasHeader, boolean hasFooter) {
        this(null, context, hasHeader, hasFooter);
    }

    public BaseRecycleAdapter(List<T> datas, Context context) {
        this(datas, context, false, false);
    }

    public BaseRecycleAdapter(List<T> datas, Context context, boolean hasHeader, boolean hasFooter) {
        if (datas != null) {
            this.mDatas = datas;
        }
//        mOnItemClickListener = onItemClickListener;
        this.mContext = context;
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
    }

    public BaseRecycleAdapter() {
        this((List<T>) null, false, false);
    }

    public BaseRecycleAdapter(boolean hasHeader, boolean hasFooter) {
        this((List<T>) null, hasHeader, hasFooter);
    }

    public BaseRecycleAdapter(List<T> datas) {
        this(datas, false, false);
    }

    public BaseRecycleAdapter(List<T> datas, boolean hasHeader, boolean hasFooter) {
        if (datas != null) {
            this.mDatas = datas;
        }
//        mOnItemClickListener = onItemClickListener;
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
    }

    /**
     * 获取子item
     *
     * @return
     */
    public abstract int getLayoutId(int viewType);

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (hasHeader) {
            if (position == 0)
                bindData(holder, position, (T)null);
            else {
                if (position <= mDatas.size()) {
                    T t = mDatas.get(position - 1);
                    bindData(holder, position, t);
                } else
                    bindData(holder, position, (T)null);
            }
        } else {
            if (position < mDatas.size()) {
                T t = mDatas.get(position);
                bindData(holder, position, t);
            } else
                bindData(holder, position, (T)null);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            //文艺青年中的文青
            Bundle payload = (Bundle) payloads.get(0);
            bindData(holder,position,payload);
        }
    }

    /**
     * 刷新数据
     *
     * @param datas
     */
    public void refresh(List<T> datas) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clear() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     *
     * @param datas
     */
    public void addAllData(List<T> datas) {
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     *
     * @param data
     */
    public void addAData(T data) {
        this.mDatas.add(data);
        notifyDataSetChanged();
    }

    /**
     * 绑定数据
     * @param holder   具体的viewHolder
     * @param position 对应的索引
     */
    protected abstract void bindData(BaseViewHolder holder, int position, T t);

    /**
     *  绑定数据
     * @param holder 具体的viewHolder
     * @param position 对应的索引
     * @param bundle DiffUtils中getChangePayload中返回的
     */
    protected void bindData(BaseViewHolder holder, int position, Bundle bundle){}


    @Override
    public int getItemCount() {
        if (hasFooter && hasHeader) {
            return mDatas == null ? 2 : mDatas.size() + 2;
        } else if (hasHeader || hasFooter) {
            return mDatas == null ? 1 : mDatas.size() + 1;
        } else {
            return mDatas == null ? 0 : mDatas.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader) {
            if (position == 0) {
                return ITEM_TYPE_HEADER;
            }
            if (hasFooter) {
                if (position == mDatas.size() + 1) {
                    return ITEM_TYPE_FOOTER;
                } else {
                    return ITEM_TYPE_CONTENT;
                }
            } else {
                return ITEM_TYPE_CONTENT;
            }
        } else if (hasFooter) {
            if (position == mDatas.size()) {
                return ITEM_TYPE_FOOTER;
            } else {
                return ITEM_TYPE_CONTENT;
            }
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }


    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    //view的单机事件
    public interface OnItemClickListener<T> {
        void onItemClickListener(T t, View view, int position);
    }

    //view的长按事件
    public interface OnItemLongClickListener<T> {
        void onItemLongClickListener(T t, View view, int position);
    }
}


