package com.east.east_utils.ui.recycleview.base_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：减少创建Adapter的重复代码进行的封装
 * Created by East at 2018/1/4 14:41
 */

public abstract class BaseListAdapter<T,H> extends BaseAdapter {

    public List<T> datas = new ArrayList<T>();
    protected Context mContext;
    protected LayoutInflater mInflater;

    public BaseListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public BaseListAdapter(List<T> datas, Context context) {
        this.datas = datas;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H h = null;
        T t = datas.get(position);
        if(convertView == null){
            convertView = bindConvertView(mInflater,parent,position);
            h = buildHolderAndInitView(convertView,t,position);
            convertView.setTag(h);
        }else{
            h = (H) convertView.getTag();
        }

        bindViewDatas(h,t,position);

        return convertView;
    }

    /**
     * 建立convertView
     * @param inflater
     * @return
     */
    protected abstract View bindConvertView(LayoutInflater inflater, ViewGroup parent, int position);

    /**
     * 建立视图Holder
     * @param convertView
     * @return
     */
    public abstract H buildHolderAndInitView(View convertView, T t, int position);

    /**
     * 绑定数据
     * @param holder
     * @param t
     * @param position
     */
    public abstract void bindViewDatas(H holder,T t,int position);

    /**
     * 刷新数据
     * @param datas
     */
    public void refresh(List<T> datas){
        this.datas.clear();
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clear(){
        this.datas.clear();
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     * @param datas
     */
    public void addAllData(List<T> datas){
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     * @param data
     */
    public void addAData(T data){
        this.datas.add(data);
        notifyDataSetChanged();
    }
    
}
