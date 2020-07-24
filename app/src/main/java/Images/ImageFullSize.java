package Images;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import SharedPreferences.SharedPreferences;
import com.jsibbold.zoomage.ZoomageView;

public class ImageFullSize extends AppCompatActivity {

    ImageButton imageButton;
    LottieAnimationView lottieAnimationView;
    ZoomageView imageView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = new SharedPreferences();
        setContentView(R.layout.activity_image_full_size);
        Intent intent = getIntent();
        final String uri = intent.getStringExtra("uri");
        imageView = (ZoomageView) findViewById(R.id.imageFullSize);
        Glide
                .with(this)
                .load(uri)
                //.transition(GenericTransitionOptions.<Drawable>with(R.anim.up_from_bottom))
                .into(imageView)
        ;

        imageButton = findViewById(R.id.sharingText);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("image/*");
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
                startActivity(Intent.createChooser(sendIntent, "send to..."));
            }
        });

        lottieAnimationView = findViewById(R.id.favoriteButton);
        if (sharedPreferences.isFavorite(uri, this)) {
            lottieAnimationView.setProgress(1);
        }
        lottieAnimationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharedPreferences.isFavorite(uri,getBaseContext())) {
                    lottieAnimationView.resumeAnimation();
                    sharedPreferences.addFavorite(uri, getBaseContext());
                } else {
                    lottieAnimationView.reverseAnimationSpeed();
                    lottieAnimationView.playAnimation();
                    sharedPreferences.removeFavorite(uri, getBaseContext());
                }
            }
        });
    }



}
