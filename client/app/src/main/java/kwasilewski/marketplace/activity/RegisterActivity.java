package kwasilewski.marketplace.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.util.List;

import kwasilewski.marketplace.R;
import kwasilewski.marketplace.dto.HintData;
import kwasilewski.marketplace.dto.user.UserData;
import kwasilewski.marketplace.retrofit.RetrofitService;
import kwasilewski.marketplace.retrofit.service.HintService;
import kwasilewski.marketplace.retrofit.service.UserService;
import kwasilewski.marketplace.util.MRKSpinner;
import kwasilewski.marketplace.util.MRKUtil;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private boolean registerInProgress = false;
    private UserService userService;
    private HintService hintService;
    private Long selectedProvince;

    private View progressBar;
    private View registerFormView;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText cityEditText;
    private MRKSpinner provinceSpinner;
    private EditText phoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userService = RetrofitService.getInstance().getUserService();
        hintService = RetrofitService.getInstance().getHintService();

        progressBar = findViewById(R.id.register_progress);
        registerFormView = findViewById(R.id.register_form);
        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        firstNameEditText = findViewById(R.id.register_first_name);
        lastNameEditText = findViewById(R.id.register_last_name);
        cityEditText = findViewById(R.id.register_city);
        phoneEditText = findViewById(R.id.register_phone);
        phoneEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        provinceSpinner = findViewById(R.id.register_province);
        populateProvinceSpinner();
        provinceSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                if (item instanceof HintData){
                    selectedProvince = ((HintData) item).getId();
                }
                provinceSpinner.setError(null);
            }
        });

        Button singUpButton = findViewById(R.id.sign_up_button);
        singUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateProvinceSpinner();
    }

    private void attemptRegister() {
        if (registerInProgress) {
            return;
        }

        registerInProgress = true;
        emailEditText.setError(null);
        passwordEditText.setError(null);
        firstNameEditText.setError(null);
        lastNameEditText.setError(null);
        cityEditText.setError(null);
        provinceSpinner.setError(null);
        phoneEditText.setError(null);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String city = cityEditText.getText().toString();
        String phone = phoneEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(phone) && !isPhoneValid(phone)) {
            phoneEditText.setError(getString(R.string.error_incorrect_phone));
            focusView = phoneEditText;
            cancel = true;
        }

        if (selectedProvince == null) {
            provinceSpinner.setError(getString(R.string.error_field_required));
            focusView = provinceSpinner;
            cancel = true;
        }

        if (TextUtils.isEmpty(city)) {
            cityEditText.setError(getString(R.string.error_field_required));
            focusView = cityEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstName)) {
            lastNameEditText.setError(getString(R.string.error_field_required));
            focusView = lastNameEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            focusView = passwordEditText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            focusView = passwordEditText;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            focusView = emailEditText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            focusView = emailEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            registerInProgress = false;
        } else {
            showProgress(true);
            register(new UserData(email, MRKUtil.encodePassword(email, password), firstName, lastName, city, selectedProvince, phone));
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 3;
    }

    private boolean isPhoneValid(String phone) {
        return PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }

    private void showProgress(final boolean show) {
        MRKUtil.showProgressBarHideView(this, registerFormView, progressBar, show);
    }

    private void register(UserData userData) {
        Call<ResponseBody> call = userService.register(userData);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                registerInProgress = false;
                if(response.isSuccessful()) {
                    registerSuccess();
                } else if (response.code() == HttpURLConnection.HTTP_NOT_ACCEPTABLE){
                    showProgress(false);
                    emailEditText.setError(getString(R.string.error_email_taken));
                    emailEditText.requestFocus();
                } else {
                    showProgress(false);
                    connectionProblem();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                registerInProgress = false;
                showProgress(false);
                connectionProblem();
            }
        });
    }

    private void registerSuccess() {
        setResult(AppCompatActivity.RESULT_OK);
        finish();
    }

    private void connectionProblem() {
        MRKUtil.connectionProblem(this);
    }

    private void populateProvinceSpinner() {
        Call<List<HintData>> call = hintService.getProvinces();
        call.enqueue(new Callback<List<HintData>>() {
            @Override
            public void onResponse(Call<List<HintData>> call, Response<List<HintData>> response) {
                if(response.isSuccessful()) {
                    setSpinnerAdapter(response.body());
                } else {
                    connectionProblem();
                }
            }

            @Override
            public void onFailure(Call<List<HintData>> call, Throwable t) {
                connectionProblem();
            }
        });
    }

    private void setSpinnerAdapter(List<HintData> hintData) {
        ArrayAdapter<HintData> adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, hintData);
        provinceSpinner.setAdapter(adapter);
    }

}
