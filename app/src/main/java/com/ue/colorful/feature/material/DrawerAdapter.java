package com.ue.colorful.feature.material;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ue.colorful.R;
import com.ue.colorful.model.PaletteColorSection;

import java.util.List;

public class DrawerAdapter extends BaseAdapter {
    private final Context mContext;
    private List<PaletteColorSection> mColorList;

    public DrawerAdapter(Context context, List<PaletteColorSection> colorList) {
        mContext = context;
        mColorList = colorList;
    }

    @Override
    public int getCount() {
        return mColorList == null ? 0 : mColorList.size();
    }

    @Override
    public Object getItem(int position) {
        return mColorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.nav_item, parent, false);
            holder = new ViewHolder((TextView) convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PaletteColorSection paletteColorSection = mColorList.get(position);
        final String colorName = paletteColorSection.getColorSectionName();
        holder.textView.setText(colorName);

        final StateListDrawable sld = new StateListDrawable();
        final Drawable d = new ColorDrawable(paletteColorSection.getColorSectionValue());
        sld.addState(new int[]{android.R.attr.state_pressed}, d);
        sld.addState(new int[]{android.R.attr.state_checked}, d);
        holder.textView.setBackgroundDrawable(sld);
        return convertView;
    }

    private static final class ViewHolder {
        private final TextView textView;

        private ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}
