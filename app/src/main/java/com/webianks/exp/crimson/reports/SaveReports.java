package com.webianks.exp.crimson.reports;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.webianks.exp.crimson.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

/**
 * Created by R Ankit on 11-02-2017.
 */

public class SaveReports extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private StorageReference spaceRef;
    private Toolbar toolbar;
    private ImageView imageView;
    private RelativeLayout loginContainer;
    private Button gallery, camera;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    String selectedImagePath;
    private DatabaseReference reportsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_reports);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imageView = (ImageView) findViewById(R.id.image);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.fab).setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        loginContainer = (RelativeLayout) findViewById(R.id.loginContainer);
        gallery = (Button) findViewById(R.id.gallery);
        camera = (Button) findViewById(R.id.camera);

        gallery.setOnClickListener(this);
        camera.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reportsRef = database.getReference("reports");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

                updateUI(user);

            }
        };

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                updateUI(null);

            }
        }

        bitmap = null;
        selectedImagePath = null;

        imageView.setImageBitmap(null);

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getBaseContext(),

                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;

            }

            try {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);


                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                findViewById(R.id.fab).setVisibility(View.VISIBLE);

                //storeImageTosdCard(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                String filePath = GetFilePathFromDevice.getPath(this, data.getData());

                bitmap = BitmapFactory.decodeFile(filePath);

                bitmap = Bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), false);

                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);

                findViewById(R.id.fab).setVisibility(View.VISIBLE);


            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }



    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        showProgressDialog();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SaveReports.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    private void hideProgressDialog() {

        if (progressDialog != null)
            progressDialog.dismiss();

    }


    private void showProgressDialog() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing");
        progressDialog.setMessage("Please wait..");
        progressDialog.setIndeterminate(true);
        progressDialog.show();


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {

        mAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                    }
                });
    }


    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

            loginContainer.setVisibility(View.GONE);
            findViewById(R.id.uploadContainer).setVisibility(View.VISIBLE);

        } else {

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }
    }


    private void uploadReport() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://crimsonnew-33e2c.appspot.com");

        final String uuid = UUID.randomUUID().toString();

        String name= "reports/test"+uuid+".jpg";

        spaceRef = storageRef.child(name);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = spaceRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

                progressDialog.dismiss();
                Toast.makeText(SaveReports.this,"Failed to upload.",Toast.LENGTH_SHORT).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                reportsRef.child(uuid).setValue(downloadUrl.toString());
                imageView.refreshDrawableState();

                progressDialog.dismiss();
                Toast.makeText(SaveReports.this,"Uploaded successfully.",Toast.LENGTH_SHORT).show();
                imageView.refreshDrawableState();

            }
        });


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button)
            signIn();
        else if (view.getId() == R.id.fab) {

            uploadReport();
        } else if (view.getId() == R.id.gallery) {

            galleryPick();

        } else if (view.getId() == R.id.camera) {
            cameraCapture();
        }
    }

    private void cameraCapture() {

        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment
                .getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(f));

        startActivityForResult(intent,
                CAMERA_REQUEST);

    }

    private void galleryPick() {

        Intent pictureActionIntent = null;

        pictureActionIntent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(
                pictureActionIntent,
                GALLERY_PICTURE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sub,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.ic_view)
            startActivity(new Intent(this,ViewReports.class));

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

}
