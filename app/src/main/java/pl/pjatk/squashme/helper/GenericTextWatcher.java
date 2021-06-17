package pl.pjatk.squashme.helper;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.textfield.TextInputLayout;

/**
 * Generic text watcher for TextInputLayout
 */
public class GenericTextWatcher implements TextWatcher {

    private final TextInputLayout inputLayout;

    public GenericTextWatcher(TextInputLayout inputLayout) {
        this.inputLayout = inputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        inputLayout.setErrorEnabled(false);
        inputLayout.setErrorEnabled(true);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
