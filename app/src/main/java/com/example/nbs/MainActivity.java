package com.example.nbs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;

import net.openid.appauth.AppAuthConfiguration;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.browser.BrowserAllowList;
import net.openid.appauth.browser.VersionedBrowserMatcher;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private AuthState authState;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button logout = findViewById(R.id.logout);
        logout.setOnClickListener(v -> {
            AuthorizationServiceConfiguration.fetchFromIssuer(
                    Uri.parse("https://accounts.google.com/"),
                    (serviceConfiguration, ex) -> {
                        if (ex != null) {
                            Log.e(TAG, "Failed to fetch configuration");
                            return;
                        }
                        assert serviceConfiguration != null;
                        Log.i(TAG, "discovery: " + serviceConfiguration.toJsonString());

                        logout(authState, serviceConfiguration);
                    }
            );
        });
        logout.setEnabled(false);

        ImageButton signingGoogle = findViewById(R.id.signinGoogle);
        signingGoogle.setOnClickListener(v -> {
            doOIDConnect("https://accounts.google.com/");
        });

        // currently failing, facebook has f*ed up their SSL,
        //    could be solved by custom SslConnection fucktory.
        ImageButton signinFacebook = findViewById(R.id.signinFacebook);
        signinFacebook.setOnClickListener(v -> {
            doOIDConnect("https://www.facebok.com/");
        });

        Button signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(this, StepperActivity.class);
            startActivity(intent);
        });
        signUp.setEnabled(false);


        TextView welcome = findViewById(R.id.welcome);

        sp = getSharedPreferences("com.example.nbs", MODE_PRIVATE);
        String stateJson = sp.getString("authState", null);

        if (stateJson != null) {
            try {
                AuthState s = AuthState.jsonDeserialize(stateJson);
                authState = s;

                // Authentication is successful
                if (s.isAuthorized()) {
                    signingGoogle.setEnabled(false);
                    signingGoogle.setVisibility(View.INVISIBLE);

                    signinFacebook.setEnabled(false);
                    signinFacebook.setVisibility(View.INVISIBLE);

                    logout.setEnabled(true);
                    signUp.setEnabled(true);

                    Log.i(TAG, "Entering authorized state!");
                    String tkn = s.getIdToken();
                    assert tkn != null;
                    JWT jwt = new JWT(tkn);

                    Log.i(TAG, "Name: " + jwt.getClaim("name").asString());
                    Log.i(TAG, "Given Name: " + jwt.getClaim("given_name").asString());
                    Log.i(TAG, "Family Name: " + jwt.getClaim("family_name").asString());
                    Log.i(TAG, "e-mail: " + jwt.getClaim("email").asString());

                    welcome.setText("Sveicināti, " + jwt.getClaim("name").asString() + "!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void logout(AuthState state, AuthorizationServiceConfiguration authServConf) {
        // EndSessionRequest endSessionRequest =
        //         new EndSessionRequest.Builder(authServConf)
        //             .setIdTokenHint(state.getIdToken())
        //             .setPostLogoutRedirectUri(Uri.parse("com.example.nbs:/oauth2endsession"))
        //             .build();
        // AuthorizationService authorizationService = new AuthorizationService(this);
        // Intent endSessionIntent = authorizationService.getEndSessionRequestIntent(endSessionRequest);
        // startActivityForResult(endSessionIntent, 99);
        //
        // Google, for example, does not provide endSessionEndpoint, which seems to be
        //       optional by OpenID specs as well (sessions are separate sub-specification)
        //       Therefore, simply let's destroy authSession locally
        sp.edit().putString("authState", null).apply();
        authState = null;
        recreate();
    }

    private void doOIDConnect(String discoveryUrl) {
        AuthorizationServiceConfiguration.fetchFromIssuer(
                Uri.parse(discoveryUrl),
                (serviceConfiguration, ex) -> {
                    if (ex != null) {
                        Log.e(TAG, "Failed to fetch configuration");
                        return;
                    }
                    assert serviceConfiguration != null;

                    // seed initial auth state
                    AuthState state = new AuthState(serviceConfiguration);
                    sp.edit().putString("authState", state.jsonSerializeString()).apply();

                    AppAuthConfiguration appAuthConfig = new AppAuthConfiguration.Builder()
                            .setBrowserMatcher(new BrowserAllowList(VersionedBrowserMatcher.CHROME_CUSTOM_TAB)).build();

                    AuthorizationRequest.Builder authReqBuilder =
                            new AuthorizationRequest.Builder(
                                    serviceConfiguration,
                                    "872861715936-24am6pdjo1j28tt1ru6vm4luugkm7vq7.apps.googleusercontent.com",
                                    ResponseTypeValues.CODE,
                                    Uri.parse("com.example.nbs:/oauth2")
                            ).setScope("openid email profile");

                    AuthorizationRequest authReq = authReqBuilder.build();
                    AuthorizationService authService = new AuthorizationService(this, appAuthConfig);

                    authService.performAuthorizationRequest(
                            authReq,
                            PendingIntent.getActivity(this, 0, new Intent(this, AuthResultHandler.class), 0),
                            PendingIntent.getActivity(this, 0, new Intent(this, AuthResultHandler.class), 0)
                    );
                }
        );
    }
}