package com.ue.colorful.feature.material;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ue.colorful.R;
import com.ue.colorful.model.PaletteColor;

import java.util.List;

/**
 * Created by gimbert on 14-11-10.
 */
public class ColorCardRecyclerAdapter extends RecyclerView.Adapter<ColorCardRecyclerAdapter.ViewHolder> {

    private final Context mContext;
    private final ColorCardRecyclerAdapterCallback mCallback;
    private final LayoutInflater mInflater;
    private List<PaletteColor> mCards;

    public ColorCardRecyclerAdapter(Context context, List<PaletteColor> mCards, ColorCardRecyclerAdapterCallback callback) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mCards = mCards;
        this.mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mInflater.inflate(R.layout.card_color, parent, false);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final PaletteColor paletteColor = mCards.get(position);
        viewHolder.coloredZone.setBackgroundColor(paletteColor.getHex());
        viewHolder.title.setText(paletteColor.getBaseName());
        viewHolder.content.setText(paletteColor.getHexString());
        if (isColorLight(paletteColor.getHex())) {
            viewHolder.copyColor.setImageResource(R.mipmap.ic_content_copy_black_24dp);
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.color_card_title_dark_color));
            viewHolder.content.setTextColor(mContext.getResources().getColor(R.color.color_card_content_dark_color));
        } else {
            viewHolder.copyColor.setImageResource(R.mipmap.ic_content_copy_white_24dp);
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.color_card_title_light_color));
            viewHolder.content.setTextColor(mContext.getResources().getColor(R.color.color_card_content_light_color));
        }
        viewHolder.copyColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    mCallback.onCopyColorClicked(paletteColor);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View coloredZone;
        public final TextView title;
        public final TextView content;
        public final ImageButton copyColor;

        public ViewHolder(View itemView) {
            super(itemView);
            coloredZone = itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.color_title);
            content = (TextView) itemView.findViewById(R.id.color_content);
            copyColor = (ImageButton) itemView.findViewById(R.id.color_copy);
        }
    }

    private float[] hsb;

    private boolean isColorLight(int hex) {
        hsb = new float[3];
        Color.colorToHSV(hex, hsb);

        return hsb[2] > 0.5;
    }

    public interface ColorCardRecyclerAdapterCallback {
        void onCopyColorClicked(PaletteColor paletteColor);
    }

}
