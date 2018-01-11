package com.kyleproject.businesscardscanner;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class StoreActivity extends AppCompatActivity {

    TextView textView;
    StringBuilder stringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        textView = (TextView) findViewById(R.id.text_view_store2);
        textView.setText( MainActivity.getText() );
    }

    //=============================================================================
    //   onClickNewContactButton
    // When the user clicks on the New Contact button, then this function will be
    // activatd to save whatever information retrieved to the phone's contacts.
    //=============================================================================
    public void onClickNewContactButton(View v)
    {
        // This create  s intent Contact-add
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        // To retrieve input data
        EditText mName = (EditText)findViewById(R.id.name_id_input2);
        EditText mOrganisation = (EditText)findViewById(R.id.org_id_input2);
        EditText mEmail = (EditText)findViewById(R.id.email_id_input2);
        EditText mPhoneNo = (EditText)findViewById(R.id.phone_id_input2);

        // Inserts an email address
        intent
                .putExtra(ContactsContract.Intents.Insert.EMAIL, mEmail.getText())
                .putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .putExtra(ContactsContract.Intents.Insert.PHONE, mPhoneNo.getText())
                .putExtra(ContactsContract.Intents.Insert.NAME, mName.getText())
                .putExtra(ContactsContract.Intents.Insert.COMPANY, mOrganisation.getText());

        // To start form
        startActivity(intent);
    }
}
