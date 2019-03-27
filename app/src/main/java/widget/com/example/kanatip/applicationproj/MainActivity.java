package widget.com.example.kanatip.applicationproj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static widget.com.example.kanatip.applicationproj.R.id.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SYSTEM_ALERT_WINDOW_PERMISSION = 7;
    Button button ;
    private TextView textHelp;
    private RelativeLayout rlOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = findViewById(buttonShow);

        textHelp = findViewById(R.id.textHelp);
        textHelp.setOnClickListener(this);
        rlOverlay = findViewById(R.id.rlOverlay);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            RuntimePermissionForUser();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                    startService(new Intent(MainActivity.this, FloatingWidgetShowService.class));

                    finish();

                } else if (Settings.canDrawOverlays(MainActivity.this)) {

                    startService(new Intent(MainActivity.this, FloatingWidgetShowService.class));

                    finish();

                } else {
                    RuntimePermissionForUser();

                    Toast.makeText(MainActivity.this, "System Alert Window Permission Is Required For Floating Widget.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void RuntimePermissionForUser() {

        Intent PermissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));

        startActivityForResult(PermissionIntent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.textHelp:
                if (textHelp.getText().toString().equals("Next")) {
                    textHelp.setText("Got It");
                } else {
                    rlOverlay.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

    }
}
