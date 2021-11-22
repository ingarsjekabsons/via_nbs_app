package com.example.nbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;

import org.json.JSONException;

public class AuthResultHandler extends AppCompatActivity {
    final private static String TAG = "AuthResultHandler";
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_result_handler);

        sp = getSharedPreferences("com.example.nbs", MODE_PRIVATE);
        String stringState = sp.getString("authState", null);
        AuthState authState = null;
        if (stringState != null) {
            try {
                authState = AuthState.jsonDeserialize(stringState);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i(TAG, "Processing authorization response");

        Intent intent = getIntent();
        AuthorizationResponse resp = AuthorizationResponse.fromIntent(intent);
        AuthorizationException ex = AuthorizationException.fromIntent(intent);

        if (resp != null) {
            Log.i(TAG, "Authorization completed!");
            assert authState != null;
            authState.update(resp, ex);

            sp.edit().putString("authState", authState.jsonSerializeString()).apply();

            AuthorizationService authService = new AuthorizationService(this);

            AuthState finalAuthState = authState;
            authService.performTokenRequest(
                    resp.createTokenExchangeRequest(),
                    (tokenResp, tokenEx) -> {
                        if (tokenResp != null) {
                            finalAuthState.update(tokenResp, tokenEx);
                            sp.edit().putString("authState", finalAuthState.jsonSerializeString()).apply();

                            // Go back to the main screen
                            Intent main = new Intent(this, MainActivity.class);
                            startActivity(main);
                        } else {
                            Log.i(TAG, "Token exchange failed");
                            if (tokenEx != null) {
                                Log.i(TAG, "exc info: " + tokenEx.getMessage());
                            }
                        }
                    }
            );
        } else {
            if (ex != null) {
                Log.i(TAG, "Authorization failed!" + ex.getMessage());
            }
        }
    }
}