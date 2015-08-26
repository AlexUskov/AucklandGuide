package com.example.student.exploreauckland;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Dialog Fragment with info About app
 * Created by Aleksandr on 21.08.2015.
 */
public class AboutDialog extends DialogFragment {

    private ImageView icon;
    private TextView link;

    //Creation of dialog view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_alert_dialog, container, false);
        icon = (ImageView)rootView.findViewById(R.id.icon_about);
        icon.setImageResource(R.drawable.icon);
        link = (TextView)rootView.findViewById(R.id.textView_link);
        //set test on TextView in html format with link tag
        link.setText(Html.fromHtml("<a href=\"http://www.exploreauckland.co.nf\">Explore Auckland</a> "));
        //set the link
        link.setMovementMethod(LinkMovementMethod.getInstance());
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
