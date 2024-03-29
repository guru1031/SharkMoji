package com.makemoji.mojilib;

import android.support.v7.widget.RecyclerView.*;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makemoji.mojilib.gif.GifImageView;
import com.makemoji.mojilib.model.MojiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott Baar on 1/5/2016.
 */
public class HorizRVAdapter extends Adapter<HorizRVAdapter.RVHolder>{

    IMojiSelected mil;
    List<MojiModel> list = new ArrayList<>();
    boolean showNames = false;
    boolean useSpanSizes;
    public boolean enablePulse = true;
    public String curWord = "";

    public HorizRVAdapter(IMojiSelected mojiInputLayout){
        mil = mojiInputLayout;
        useSpanSizes = OneGridPage.useSpanSizes;
    }
    @Override public int getItemViewType(int position){
//        if (list.get(position).gif==1)return MojiGridAdapter.ITEM_GIF;
        return 0;
    }
    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType==MojiGridAdapter.ITEM_GIF){
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mm_horiz_gif_item,parent,false);
        }
        else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.mm_horiz_moji_item, parent, false);
        }
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RVHolder rvHolder = ((RVHolder)v.getTag());
                if (list.size() > 0 && rvHolder.pos > 1) {
                    MojiModel model = list.get(rvHolder.pos-2);
                    if (mil instanceof MojiInputLayout && model.fromSearch)((MojiInputLayout) mil).removeSuggestion();
                    mil.mojiSelected(model, null);
                }
            }
        });


        int width = v.getResources().getDisplayMetrics().widthPixels / 3;
        v.setMinimumWidth(width);

        return new RVHolder(v,parent);
    }

    @Override
    public void onBindViewHolder(RVHolder holder, int position) {
        if (position == 0) {
            holder.name.setText(curWord);
            holder.image.setVisibility(View.GONE);
        } else if (position == 1){
            MojiModel m = list.get(position-1);
            Mojilytics.trackView(m.id);
            holder.name.setText(m.name);
            holder.name.setVisibility(showNames?View.VISIBLE:View.GONE);
            holder.name.setVisibility(View.VISIBLE);
//            holder.v.setPadding((int)Moji.density*(position==0?6:2),0,(int)Moji.density*2,0);

            holder.image.setVisibility(View.GONE);

            if (holder.image!=null){
                holder.image.forceDimen(holder.dimen);
                holder.image.setPulseEnabled(enablePulse);
                holder.image.setModel(m);
            }
            else{
                holder.gifImageView.getFromUrl(m.image_url);
            }
            holder.pos = holder.getAdapterPosition();

        } else {
            MojiModel m = list.get(position-2);
            Mojilytics.trackView(m.id);
            holder.name.setText(m.name);
            holder.name.setVisibility(showNames?View.VISIBLE:View.GONE);
            holder.name.setVisibility(View.GONE);
//            holder.v.setPadding((int)Moji.density*(position==0?6:2),0,(int)Moji.density*2,0);

            if (holder.image!=null){
                holder.image.forceDimen(holder.dimen);
                holder.image.setPulseEnabled(enablePulse);
                holder.image.setModel(m);
            }
            else{
                holder.gifImageView.getFromUrl(m.image_url);
            }
            holder.pos = holder.getAdapterPosition();
        }
    }


    public void setMojiModels(List<MojiModel> newList){
//        if (!list.equals(newList)) {
            list = newList;
            notifyDataSetChanged();
//        }
    }
    @Override
    public int getItemCount() {
        if (list.size() == 0) {
            return 1;
        } else {
            return list.size() + 2;
        }
    }

    public void showNames(boolean enable){
        if (enable!=showNames){
            showNames= enable;
            notifyDataSetChanged();
        }
    }

    public class RVHolder extends ViewHolder
    {
        LinearLayout itemView;
        TextView name;
        MojiImageView image;
        GifImageView gifImageView;
        View v;
        int dimen;
        int pos;
        public RVHolder(View v,ViewGroup parent){
            super(v);
            this.v = v;
            v.setTag(this);
            itemView = v.findViewById(R.id.itemView);

            name = v.findViewById(R.id._mm_horiz_item_name);
            if (mil instanceof MojiInputLayout)name.setTextColor(((MojiInputLayout) mil).getHeaderTextColor());
            View view = v.findViewById(R.id._mm_horiz_item_image);
            if (view instanceof GifImageView){
                gifImageView = (GifImageView) view;
            } else {
                image = v.findViewById(R.id._mm_horiz_item_image);
                image.sizeImagesToSpanSize(false);
                int h = (int) (45.0 * Moji.density * .85);
                image.sizeImagesToSpanSize(useSpanSizes);
                dimen =h;
            }

        }
    }
}
