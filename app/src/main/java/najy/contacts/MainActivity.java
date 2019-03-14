package najy.contacts;


import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private int READ_CONTACTS_PERMISSION_CODE = 123;

    Cursor phones;
    ListView lv;
    List<String> contacts = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestReadContactPermission();

        } else {
            lv = (ListView) findViewById(R.id.lv);
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            phones = getContentResolver().query(uri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            getContacts();
        }



    }

    public void getContacts(){
        while (phones.moveToNext()) {
            int nameIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String strphone = phones.getString(phoneIndex);
            String strname = phones.getString(nameIndex);
            contacts.add(strname + "\n " + strphone);
        }

        ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
        lv.setAdapter(data);
    }



    public void requestReadContactPermission() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == READ_CONTACTS_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                lv = (ListView) findViewById(R.id.lv);
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                phones = getContentResolver().query(uri, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

                getContacts();

            } else {
                Toast.makeText(this, "Read Contacts Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}




