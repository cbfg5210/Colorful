package com.ue.colorful.feature.pickpalette;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ue.colorful.R;
import com.ue.colorful.widget.PaletteColorPickerView;

import java.util.Locale;

public class PaletteColorPickerActivity extends AppCompatActivity implements PaletteColorPickerView.OnColorChangedListener {

    private PaletteColorPickerView colorPickerView;
    private ImageView newColorPanelView;
    private TextView tvColorHex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);

        setContentView(R.layout.activity_palette_color_picker);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int initialColor = prefs.getInt("color_3", 0xFF000000);

        colorPickerView = findViewById(R.id.cpv_color_picker_view);
        newColorPanelView = findViewById(R.id.cpv_color_panel_new);
        tvColorHex = findViewById(R.id.tvColorHex);

        colorPickerView.setOnColorChangedListener(this);
        colorPickerView.setColor(initialColor, true);

        onColorChanged(initialColor);
    }

    @Override
    public void onColorChanged(int newColor) {
        newColorPanelView.setBackgroundColor(newColor);
        tvColorHex.setText("#" + (Color.alpha(newColor) != 255 ? Integer.toHexString(newColor) : String.format("%06X", 0xFFFFFF & newColor)).toUpperCase(Locale.ENGLISH));
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putInt("color_3", colorPickerView.getColor())
                .apply();
        super.onDestroy();
    }
}
