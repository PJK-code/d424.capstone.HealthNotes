package com.example.d424capstone.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.example.d424capstone.R;

import java.util.Objects;

// allows the user to select a backside body zone by tapping
public class BackBodyActivity extends AppCompatActivity {

    private TextView tvSelectedZone;
    private MaterialButton btnConfirmSelection;
    private String selectedZone = null;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_body);

        tvSelectedZone = findViewById(R.id.tvSelectedZone);
        ImageView imgBackBodyMap = findViewById(R.id.imgBackBodyMap);
        btnConfirmSelection = findViewById(R.id.btnConfirmSelection);

        imgBackBodyMap.setOnTouchListener((v, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                ImageView img = (ImageView) v;
                Drawable drawable = img.getDrawable();
                if (drawable == null) {
                    return true;
                }

                int intrinsicWidth = drawable.getIntrinsicWidth();
                int intrinsicHeight = drawable.getIntrinsicHeight();
                if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
                    return true;
                }

                float[] pts = new float[]{event.getX(), event.getY()};
                Matrix inverse = new Matrix();
                if (img.getImageMatrix() != null) {
                    img.getImageMatrix().invert(inverse);
                    inverse.mapPoints(pts);
                }

                float normX = pts[0] / (float) intrinsicWidth;
                float normY = pts[1] / (float) intrinsicHeight;

                float viewportX = normX * 1024f;
                float viewportY = normY * 1536f;

                String zone = BodyZoneMapper.getBackBodyZone(viewportX, viewportY);
                if (zone != null) {
                    if (Objects.equals(zone, selectedZone)) {
                        confirmAndFinish();
                        return true;
                    }
                    selectedZone = zone;
                    tvSelectedZone.setText(getString(R.string.selected_zone_format, zone));
                    btnConfirmSelection.setEnabled(true);
                    btnConfirmSelection.setText(getString(R.string.confirm_selection_format, zone));
                    Toast.makeText(this, getString(R.string.selected_zone_format, zone), Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        });

        btnConfirmSelection.setOnClickListener(v -> confirmAndFinish());
    }

    private void confirmAndFinish() {
        if (selectedZone != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedBodyLocation", selectedZone);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}
