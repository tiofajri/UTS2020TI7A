package ac.id.atmaluhur.uts_amub_ti7a_1711500136_tio;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity {
    ImageView pic_photo_register_user;
    Button btn_continue,btn_add_photo;
    EditText hotel, alamat;



    DatabaseReference reference;
    String USERNAME_KEY = " usernamekey";
    String username_key = "";

    String username_key_new = "";
    Uri photo_location;
    Integer photo_max = 1;

    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();
        btn_add_photo = findViewById(R.id.btn_add_photo);
        hotel = findViewById(R.id.hotel);
        alamat = findViewById(R.id.alamat);

        pic_photo_register_user = findViewById(R.id.pic_photo_register_user);
        btn_continue = findViewById(R.id.btn_continue);

        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                                reference = FirebaseDatabase.getInstance().getReference()
                                                        .child("Users/Ray");
                                                storage = FirebaseStorage.getInstance().getReference().child("Photousers/Ray Dian Cahya");

                                                if(photo_location != null) {
                                                    StorageReference storageReference1;
                                                    storageReference1 = storage.child(System.currentTimeMillis() + "."+
                                                            getFileExtension(photo_location));
                                                    storageReference1.putFile(photo_location)
                                                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                @Override
                                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                    //String uri_photo = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                                                    String uri_photo = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                                                    reference.getRef().child("hotel").setValue(hotel.getText().toString());
                                                                    reference.getRef().child("alamat").setValue(alamat.getText().toString());
                                                                }
                                                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                            Intent gotoregister2register = new Intent(RegisterTwoActivity.this, MainActivity.class);
                                                            startActivity(gotoregister2register);
                                                        }
                                                    });
                                                }




                                            }
                                        }

        );

    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == photo_max && resultCode == RESULT_OK && data!= null && data.getData() != null) {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register_user);
        }
    }
}
