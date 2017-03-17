package com.mercadopago;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.mercadopago.adapters.IdentificationTypesAdapter;
import com.mercadopago.callbacks.card.CardIdentificationNumberEditTextCallback;
import com.mercadopago.controllers.CheckoutTimer;
import com.mercadopago.controllers.CustomServicesHandler;
import com.mercadopago.core.MercadoPagoComponents;
import com.mercadopago.customviews.MPEditText;
import com.mercadopago.customviews.MPTextView;
import com.mercadopago.listeners.card.CardIdentificationNumberTextWatcher;
import com.mercadopago.model.ApiException;
import com.mercadopago.model.CardInfo;
import com.mercadopago.model.Identification;
import com.mercadopago.model.IdentificationType;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.model.PaymentType;
import com.mercadopago.mptracker.MPTracker;
import com.mercadopago.observers.TimerObserver;
import com.mercadopago.preferences.DecorationPreference;
import com.mercadopago.presenters.GuessingCardPresenter;
import com.mercadopago.presenters.IdentificationPresenter;
import com.mercadopago.uicontrollers.card.IdentificationCardView;
import com.mercadopago.util.ApiUtil;
import com.mercadopago.util.ColorsUtil;
import com.mercadopago.util.ErrorUtil;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.ScaleUtil;
import com.mercadopago.views.IdentificationActivityView;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by marlanti on 3/16/17.
 */

public class IdentificationActivity extends MercadoPagoBaseActivity implements IdentificationActivityView, TimerObserver {



    public static final String ERROR_STATE = "textview_error";
    public static final String NORMAL_STATE = "textview_normal";

    public static final String IDENTIFICATION_BUNDLE = "mIdentification";
    public static final String IDENTIFICATION_NUMBER_BUNDLE = "mIdentificationNumber";
    public static final String IDENTIFICATION_TYPE_BUNDLE = "mIdentificationType";
    public static final String IDENTIFICATION_TYPES_LIST_BUNDLE = "mIdTypesList";
    public static final String LOW_RES_BUNDLE = "mLowRes";

    //ViewMode
    protected boolean mLowResActive;
    protected IdentificationPresenter mPresenter;
    private Activity mActivity;

    //View controls
    private DecorationPreference mDecorationPreference;
    private ScrollView mScrollView;

    //View Low Res
    private Toolbar mLowResToolbar;
    private MPTextView mLowResTitleToolbar;

    //View Normal
    private Toolbar mNormalToolbar;
    private FrameLayout mCardBackground;
    private FrameLayout mIdentificationCardContainer;
    private IdentificationCardView mIdentificationCardView;
    private MPTextView mTimerTextView;

    //Input Views
    private FrameLayout mNextButton;
    private TextView mNextButtonText;
    private ProgressBar mProgressBar;
    private LinearLayout mInputContainer;
    private Spinner mIdentificationTypeSpinner;
    private LinearLayout mIdentificationTypeContainer;
    private LinearLayout mButtonContainer;
    private MPEditText mIdentificationNumberEditText;
    private LinearLayout mCardIdentificationInput;
    private FrameLayout mErrorContainer;
    private MPTextView mErrorTextView;
    private String mErrorState;


    protected String mDefaultBaseURL;
    protected String mMerchantDiscountBaseURL;
    protected String mMerchantGetDiscountURI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = new IdentificationPresenter(getBaseContext());
        }
        mPresenter.setView(this);
        mActivity = this;
        getActivityParameters();
        if (isCustomColorSet()) {
            setTheme(R.style.Theme_MercadoPagoTheme_NoActionBar);
        }
        analizeLowRes();
        setMerchantInfo();
        setContentView();
        mPresenter.validateActivityParameters();
    }

    private boolean isCustomColorSet() {
        return mDecorationPreference != null && mDecorationPreference.hasColors();
    }

    private void setMerchantInfo() {
        if (CustomServicesHandler.getInstance().getServicePreference() != null) {
            mDefaultBaseURL = CustomServicesHandler.getInstance().getServicePreference().getDefaultBaseURL();
            mMerchantDiscountBaseURL = CustomServicesHandler.getInstance().getServicePreference().getGetMerchantDiscountBaseURL();
            mMerchantGetDiscountURI = CustomServicesHandler.getInstance().getServicePreference().getGetMerchantDiscountURI();
        }

    }

    private void getActivityParameters() {

        String publicKey = getIntent().getStringExtra("merchantPublicKey");
        mDecorationPreference = JsonUtil.getInstance().fromJson(getIntent().getStringExtra("decorationPreference"), DecorationPreference.class);


        Identification identification = new Identification();

        mPresenter.loadIdentificationTypes();
        mPresenter.setPublicKey(publicKey);
        mPresenter.setIdentification(identification);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(IDENTIFICATION_BUNDLE, JsonUtil.getInstance().toJson(mPresenter.getIdentification()));
        outState.putString(IDENTIFICATION_NUMBER_BUNDLE, mPresenter.getIdentificationNumber());
        outState.putString(IDENTIFICATION_TYPE_BUNDLE, JsonUtil.getInstance().toJson(mPresenter.getIdentificationType()));
        outState.putString(IDENTIFICATION_TYPES_LIST_BUNDLE, JsonUtil.getInstance().toJson(mPresenter.getIdentificationTypes()));
        outState.putBoolean(LOW_RES_BUNDLE, mLowResActive);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            List<IdentificationType> identificationTypesList;
            try {
                Type listType = new TypeToken<List<IdentificationType>>() {
                }.getType();
                identificationTypesList = JsonUtil.getInstance().getGson().fromJson(
                        savedInstanceState.getString(IDENTIFICATION_TYPES_LIST_BUNDLE), listType);
            } catch (Exception ex) {
                identificationTypesList = null;
            }

            mPresenter.setIdentificationTypesList(identificationTypesList);
            String idNumber = savedInstanceState.getString(IDENTIFICATION_NUMBER_BUNDLE);
            mPresenter.setIdentificationNumber(idNumber);
            Identification identification = JsonUtil.getInstance().fromJson(savedInstanceState.getString(IDENTIFICATION_BUNDLE), Identification.class);
            identification.setNumber(idNumber);
            mPresenter.setIdentification(identification);

            IdentificationType identificationType = JsonUtil.getInstance().fromJson(savedInstanceState.getString(IDENTIFICATION_TYPE_BUNDLE), IdentificationType.class);

            mLowResActive = savedInstanceState.getBoolean(LOW_RES_BUNDLE);


            mIdentificationCardView.setIdentificationNumber(idNumber);
            mIdentificationCardView.setIdentificationType(identificationType);
            mIdentificationCardView.draw();

        }
    }


    private void analizeLowRes() {
        this.mLowResActive = ScaleUtil.isLowRes(this);
    }

    private void setContentView() {
        if (mLowResActive) {
            setContentViewLowRes();
        } else {
            setContentViewNormal();
        }
    }

    private void setContentViewLowRes() {
        setContentView(R.layout.mpsdk_activity_form_card_lowres);
    }

    private void setContentViewNormal() {
        setContentView(R.layout.mpsdk_activity_form_card_normal);
    }

    @Override
    public void setNextButtonListeners() {
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCurrentEditText();
            }
        });
    }


    @Override
    public void onInvalidStart(String message) {
        finish();
    }

    @Override
    public void onValidStart() {
        mPresenter.initializeMercadoPago();
        initializeViews();
        loadViews();
        decorate();

        if (CheckoutTimer.getInstance().isTimerEnabled()) {
            CheckoutTimer.getInstance().addObserver(this);
            mTimerTextView.setVisibility(View.VISIBLE);
            mTimerTextView.setText(CheckoutTimer.getInstance().getCurrentTime());
        } else {
            //TODO
        }

        mErrorState = NORMAL_STATE;
        mPresenter.initialize();
    }

    private void initializeViews() {
        mTimerTextView = (MPTextView) findViewById(R.id.mpsdkTimerTextView);

        if (mLowResActive) {
            mLowResToolbar = (Toolbar) findViewById(R.id.mpsdkLowResToolbar);
            mLowResTitleToolbar = (MPTextView) findViewById(R.id.mpsdkTitle);
            mLowResToolbar.setVisibility(View.VISIBLE);
        } else {
            mNormalToolbar = (Toolbar) findViewById(R.id.mpsdkTransparentToolbar);
            mCardBackground = (FrameLayout) findViewById(R.id.mpsdkCardBackground);
            mIdentificationCardContainer = (FrameLayout) findViewById(R.id.mpsdkIdentificationCardContainer);
        }
        mIdentificationTypeContainer = (LinearLayout) findViewById(R.id.mpsdkCardIdentificationTypeContainer);
        mIdentificationTypeSpinner = (Spinner) findViewById(R.id.mpsdkCardIdentificationType);
        mIdentificationNumberEditText = (MPEditText) findViewById(R.id.mpsdkCardIdentificationNumber);
        mInputContainer = (LinearLayout) findViewById(R.id.mpsdkInputContainer);
        mProgressBar = (ProgressBar) findViewById(R.id.mpsdkProgressBar);
        mNextButton = (FrameLayout) findViewById(R.id.mpsdkNextButton);
        mNextButtonText = (TextView) findViewById(R.id.mpsdkNextButtonText);
        mButtonContainer = (LinearLayout) findViewById(R.id.mpsdkButtonContainer);
        mCardIdentificationInput = (LinearLayout) findViewById(R.id.mpsdkCardIdentificationInput);
        mErrorContainer = (FrameLayout) findViewById(R.id.mpsdkErrorContainer);
        mErrorTextView = (MPTextView) findViewById(R.id.mpsdkErrorTextView);
        mScrollView = (ScrollView) findViewById(R.id.mpsdkScrollViewContainer);
        mInputContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);


        fullScrollDown();
    }

    @Override
    public void showInputContainer() {
        mIdentificationTypeContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mInputContainer.setVisibility(View.VISIBLE);
    }

    private void loadViews() {
        if (mLowResActive) {
            loadLowResViews();
        } else {
            loadNormalViews();
        }
    }

    private boolean cardViewsActive() {
        return !mLowResActive;
    }

    private void loadLowResViews() {
        loadToolbarArrow(mLowResToolbar);
    }

    private void loadNormalViews() {
        loadToolbarArrow(mNormalToolbar);
        mIdentificationCardView = new IdentificationCardView(mActivity);
        mIdentificationCardView.inflateInParent(mIdentificationCardContainer, true);
        mIdentificationCardView.initializeControls();
        mIdentificationCardView.draw();
        mIdentificationCardView.show();
        requestIdentificationFocus();
        showIdentificationInput();

    }

    private void loadToolbarArrow(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    setResult(RESULT_CANCELED, returnIntent);
                    finish();
                }
            });
        }
    }

    private void decorate() {
        if (isDecorationEnabled()) {
            if (mLowResActive) {
                decorateLowRes();
            } else {
                decorateNormal();
            }
        }
    }

    private boolean isDecorationEnabled() {
        return mDecorationPreference != null && mDecorationPreference.hasColors();
    }

    private void decorateLowRes() {
        ColorsUtil.decorateLowResToolbar(mLowResToolbar, mLowResTitleToolbar, mDecorationPreference,
                getSupportActionBar(), this);
        if (mTimerTextView != null) {
            ColorsUtil.decorateTextView(mDecorationPreference, mTimerTextView, this);
        }
        mNextButtonText.setTextColor(mDecorationPreference.getDarkFontColor(this));
    }

    private void decorateNormal() {

        if (mTimerTextView != null) {
            ColorsUtil.decorateTextView(mDecorationPreference, mTimerTextView, this);
        }
        mIdentificationCardView.decorateCardBorder(mDecorationPreference.getLighterColor());
        mCardBackground.setBackgroundColor(mDecorationPreference.getLighterColor());
        mNextButtonText.setTextColor(mDecorationPreference.getDarkFontColor(this));
    }


    @Override
    public void initializeTitle() {
        if (mLowResActive) {
            String paymentTypeText = getString(R.string.mpsdk_form_card_title);

            mLowResTitleToolbar.setText(paymentTypeText);
        }
    }

    @Override
    public void setIdentificationNumber(String identificationNumber) {
        mIdentificationNumberEditText.setText(identificationNumber);
        if (cardViewsActive()) {
            mIdentificationCardView.setIdentificationNumber(identificationNumber);
        }
    }

    @Override
    public void setIdentificationTypeListeners() {
        mIdentificationTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mPresenter.saveIdentificationType((IdentificationType) mIdentificationTypeSpinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mIdentificationTypeSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                openKeyboard(mIdentificationNumberEditText);
                return false;
            }
        });
    }

    @Override
    public void setIdentificationNumberListeners() {
        mIdentificationNumberEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return onNextKey(actionId, event);
            }
        });
        mIdentificationNumberEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onTouchEditText(mIdentificationNumberEditText, event);
                return true;
            }
        });
        mIdentificationNumberEditText.addTextChangedListener(new CardIdentificationNumberTextWatcher(new CardIdentificationNumberEditTextCallback() {
            @Override
            public void checkOpenKeyboard() {
                openKeyboard(mIdentificationNumberEditText);
            }

            @Override
            public void saveIdentificationNumber(CharSequence string) {
                mPresenter.saveIdentificationNumber(string.toString());
                if (mPresenter.getIdentificationNumberMaxLength() == string.length()) {
                    mPresenter.setIdentificationNumber(string.toString());
                    mPresenter.validateIdentificationNumber();
                }
                if (cardViewsActive()) {
                    mIdentificationCardView.setIdentificationNumber(string.toString());
                    mIdentificationCardView.draw();
                }
            }

            @Override
            public void changeErrorView() {
                checkChangeErrorView();
            }

            @Override
            public void toggleLineColorOnError(boolean toggle) {
                mIdentificationNumberEditText.toggleLineColorOnError(toggle);
            }
        }));
    }


    @Override
    public void setIdentificationNumberRestrictions(String type) {
        setInputMaxLength(mIdentificationNumberEditText, mPresenter.getIdentificationNumberMaxLength());
        if ("number".equals(type)) {
            mIdentificationNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            mIdentificationNumberEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        if (!mIdentificationNumberEditText.getText().toString().isEmpty()) {
            mPresenter.validateIdentificationNumber();
        }
    }

    @Override
    public void initializeIdentificationTypes(List<IdentificationType> identificationTypes) {
        mIdentificationTypeSpinner.setAdapter(new IdentificationTypesAdapter(this, identificationTypes));
        mIdentificationTypeContainer.setVisibility(View.VISIBLE);
        if (cardViewsActive()) {
            mIdentificationCardView.setIdentificationType(identificationTypes.get(0));
        }
    }


    private void onTouchEditText(MPEditText editText, MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_DOWN) {
            openKeyboard(editText);
        }
    }

    private boolean onNextKey(int actionId, KeyEvent event) {
        if (isNextKey(actionId, event)) {
            validateCurrentEditText();
            return true;
        }
        return false;
    }

    private boolean isNextKey(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_ACTION_NEXT ||
                (event != null && event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
    }


    @Override
    public void showApiExceptionError(ApiException exception) {
        ApiUtil.showApiExceptionError(mActivity, exception);
    }

    @Override
    public void startErrorView(String message, String errorDetail) {
        ErrorUtil.startErrorActivity(mActivity, message, errorDetail, false);
    }


    private void setInputMaxLength(MPEditText text, int maxLength) {
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        text.setFilters(fArray);
    }


    private void openKeyboard(MPEditText ediText) {
        ediText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ediText, InputMethodManager.SHOW_IMPLICIT);
        fullScrollDown();
    }

    private void fullScrollDown() {
        Runnable r = new Runnable() {
            public void run() {
                mScrollView.fullScroll(View.FOCUS_DOWN);
            }
        };
        mScrollView.post(r);
        r.run();
    }


    private void requestIdentificationFocus() {

        MPTracker.getInstance().trackScreen("IDENTIFICATION_NUMBER", "2", mPresenter.getPublicKey(),
                BuildConfig.VERSION_NAME, this);
        openKeyboard(mIdentificationNumberEditText);
        if (mLowResActive) {
            mLowResTitleToolbar.setText(getResources().getString(R.string.mpsdk_form_identification_title));
        }
    }


    @Override
    public void hideIdentificationInput() {
        mCardIdentificationInput.setVisibility(View.GONE);
    }


    @Override
    public void showIdentificationInput() {
        mCardIdentificationInput.setVisibility(View.VISIBLE);
    }


    @Override
    public void setErrorView(String message) {
        mButtonContainer.setVisibility(View.GONE);
        mErrorContainer.setVisibility(View.VISIBLE);
        mErrorTextView.setText(message);
        setErrorState(ERROR_STATE);
    }

    @Override
    public void clearErrorView() {
        mButtonContainer.setVisibility(View.VISIBLE);
        mErrorContainer.setVisibility(View.GONE);
        mErrorTextView.setText("");
        setErrorState(NORMAL_STATE);
    }


    @Override
    public void setErrorIdentificationNumber() {
        mIdentificationNumberEditText.toggleLineColorOnError(true);
        mIdentificationNumberEditText.requestFocus();
    }

    @Override
    public void clearErrorIdentificationNumber() {
        mIdentificationNumberEditText.toggleLineColorOnError(false);
    }

    private void setErrorState(String mErrorState) {
        this.mErrorState = mErrorState;
    }

    private void checkChangeErrorView() {
        if (mErrorState != null && mErrorState.equals(ERROR_STATE)) {
            clearErrorView();
        }
    }

    private boolean validateCurrentEditText() {

        if (mPresenter.validateIdentificationNumber()) {
            finishWithResult(mPresenter.getIdentificationType(), mPresenter.getIdentification());
            return true;
        }
        return false;
    }


    @Override
    public void finishWithResult(IdentificationType identificationType, Identification identification) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("identification", JsonUtil.getInstance().toJson(identification));
        returnIntent.putExtra("identificationType", JsonUtil.getInstance().toJson(identificationType));
        setResult(RESULT_OK, returnIntent);
        finish();
        overridePendingTransition(R.anim.mpsdk_slide_right_to_left_in, R.anim.mpsdk_slide_right_to_left_out);
    }


    @Override
    public void onBackPressed() {
        MPTracker.getInstance().trackEvent("IDENTIFICATION_ACTIVITY", "BACK_PRESSED", "2", mPresenter.getPublicKey(),
                BuildConfig.VERSION_NAME, this);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("backButtonPressed", true);
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onTimeChanged(String timeToShow) {
        mTimerTextView.setText(timeToShow);
    }

    @Override
    public void onFinish() {
        this.finish();
    }


    @Override
    public void setSoftInputMode() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public IdentificationPresenter getPresenter() {
        return mPresenter;
    }
}

