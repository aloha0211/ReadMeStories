package ominext.com.readmestories.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ominext.com.readmestories.R;

/**
 * Created by Vinh on 10/28/2016.
 */

public class DialogUtils {

    public static void showAlertDialog(final Context context, String title, String content, final View.OnClickListener onClickListener) {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.alert_dialog_layout);
        dialog.setCancelable(false);

        TextView tvTitle = (TextView) dialog.findViewById(R.id.title_text_view);
        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        TextView tvMessage = (TextView) dialog.findViewById(R.id.message_text_view);
        tvMessage.setText(content);

        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        final Button btnOk = (Button) dialog.findViewById(R.id.ok_button);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null)
                    onClickListener.onClick(btnOk);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void showAlertDialog(Context context, String content, final View.OnClickListener onClickListener) {
        showAlertDialog(context, null, content, onClickListener);
    }

    public static void showAlertDialog(Context context, String title, String content) {
        showAlertDialog(context, title, content, null);
    }

    public static void showAlertDialog(Context context, String content) {
        showAlertDialog(context, null, content);
    }
}
