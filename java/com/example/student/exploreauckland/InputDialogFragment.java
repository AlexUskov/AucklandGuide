package com.example.student.exploreauckland;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

/**
 * Fragment for making note
 * Created by Aleksandr on 20.08.2015.
 */
public class InputDialogFragment extends DialogFragment {
    private EditText noteText;
    private Button saveButton, cancelButton;
    private String note;
    //flag to check which button is clicked
    private boolean buttonPressed = false;

    static String DialogboxTitle;

    //empty constructor
    public InputDialogFragment() {

    }
    //set the title of the dialog window
    public void setDialogTitle(String title) {
        DialogboxTitle = title;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.input_note_dialog, container);

        noteText = (EditText) view.findViewById(R.id.note_textView);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        note = getArguments().getString("existingNote");
        noteText.setText(note);

        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                buttonPressed = true;
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //---show the keyboard automatically
        noteText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        //---set the title for the dialog
        getDialog().setTitle(DialogboxTitle);

        return view;
    }

    @Override public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
        //if clicked saveButton, put new note to extras
        // and call onActivityResult method in parent fragment
        if(buttonPressed){
            Intent intent = new Intent();
            intent.putExtra("note", noteText.getText().toString());
            getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
        }
    }
}
