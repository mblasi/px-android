package com.mercadopago.paymentresult.props;

import android.support.annotation.NonNull;

import com.mercadopago.model.Instruction;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentResult;
import com.mercadopago.paymentresult.formatter.HeaderTitleFormatter;
import com.mercadopago.preferences.PaymentResultScreenPreference;

/**
 * Created by vaserber on 10/20/17.
 */

public class PaymentResultProps {

    public final PaymentResult paymentResult;
    public final PaymentResultScreenPreference preferences;
    public final Instruction instruction;
    public final String headerMode;
    public final HeaderTitleFormatter amountFormat;
    public final boolean loading;
    public final String processingMode;

    public PaymentResultProps(@NonNull final Builder builder) {
        this.paymentResult = builder.paymentResult;
        this.headerMode = builder.headerMode;
        this.preferences = builder.preferences;
        this.instruction = builder.instruction;
        this.amountFormat = builder.amountFormat;
        this.loading = builder.loading;
        this.processingMode = builder.processingMode;
    }

    public Builder toBuilder() {
        return new Builder()
                .setPaymentResult(this.paymentResult)
                .setPaymentResultScreenPreference(this.preferences)
                .setHeaderMode(this.headerMode)
                .setInstruction(this.instruction)
                .setAmountFormat(this.amountFormat)
                .setLoading(this.loading)
                .setProcessingMode(this.processingMode);
    }

    public boolean hasCustomizedTitle() {
        if (preferences != null) {
            if (isApprovedTitleValidState()) {
                return preferences.getApprovedTitle() != null && !preferences.getApprovedTitle().isEmpty();
            } else if (isPendingTitleValidState()) {
                return preferences.getPendingTitle() != null && !preferences.getPendingTitle().isEmpty();
            } else if (isRejectedTitleValidState()) {
                return preferences.getRejectedTitle() != null && !preferences.getRejectedTitle().isEmpty();
            }
        }
        return false;
    }

    public String getPreferenceTitle() {
        if (preferences != null) {
            if (isApprovedTitleValidState()) {
                return preferences.getApprovedTitle();
            } else if (isPendingTitleValidState()) {
                return preferences.getPendingTitle();
            } else if (isRejectedTitleValidState()) {
                return preferences.getRejectedTitle();
            }
        }
        return "";
    }

    private boolean isApprovedTitleValidState() {
        return isStatusApproved();
    }

    private boolean isStatusApproved() {
        return paymentResult != null && paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_APPROVED);
    }

    private boolean isStatusRejected() {
        return paymentResult != null && paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_REJECTED);
    }

    private boolean isPendingTitleValidState() {
        return paymentResult != null && ((paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_PENDING)
                && !paymentResult.getPaymentStatusDetail().equals(Payment.StatusCodes.STATUS_DETAIL_PENDING_WAITING_PAYMENT)) ||
                paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_IN_PROCESS));
    }

    private boolean isRejectedTitleValidState() {
        return isStatusRejected();
    }

    public boolean hasCustomizedLabel() {
        if (preferences != null) {
            if (isApprovedLabelValidState()) {
                return preferences.getApprovedLabelText() != null && !preferences.getApprovedLabelText().isEmpty();
            } else if (isRejectedLabelValidState()) {
                return !preferences.isRejectedLabelTextEnabled();
            }
        }
        return false;
    }

    public String getPreferenceLabel() {
        if (preferences != null) {
            if (isApprovedLabelValidState()) {
                return preferences.getApprovedLabelText();
            } else if (isRejectedLabelValidState() && !preferences.isRejectedLabelTextEnabled()) {
                return "";
            }
        }
        return "";
    }

    private boolean isApprovedLabelValidState() {
        return isStatusApproved();
    }

    private boolean isRejectedLabelValidState() {
        return isStatusRejected();
    }

    public boolean hasCustomizedBadge() {
        if (preferences != null && isApprovedBadgeValidState()) {
            return preferences.getApprovedBadge() != null && !preferences.getApprovedBadge().isEmpty();
        }
        return false;
    }

    public String getPreferenceBadge() {
        if (preferences != null && isApprovedBadgeValidState()) {
            return preferences.getApprovedBadge();
        }
        return "";
    }

    private boolean isApprovedBadgeValidState() {
        return isStatusApproved();
    }

    public boolean hasCustomizedIcon() {
        if (preferences != null) {
            if (isApprovedIconValidState()) {
                return preferences.getApprovedIcon() != null;
            } else if (isPendingIconValidState()) {
                return preferences.getPendingIcon() != null;
            } else if (isRejectedIconValidState()) {
                return preferences.getRejectedIcon() != null;
            }
        }
        return false;
    }

    public int getPreferenceIcon() {
        if (preferences != null) {
            if (isApprovedIconValidState()) {
                return preferences.getApprovedIcon();
            } else if (isPendingIconValidState()) {
                return preferences.getPendingIcon();
            } else if (isRejectedIconValidState()) {
                return preferences.getRejectedIcon();
            }
        }
        return 0;
    }

    private boolean isApprovedIconValidState() {
        return isStatusApproved();
    }

    private boolean isPendingIconValidState() {
        return paymentResult != null && (paymentResult.getPaymentStatus().equals(Payment.StatusCodes.STATUS_PENDING)
                && paymentResult.getPaymentStatusDetail().equals(Payment.StatusCodes.STATUS_DETAIL_PENDING_WAITING_PAYMENT));
    }

    private boolean isRejectedIconValidState() {
        return isStatusRejected();
    }

    public boolean hasInstructions() {
        return instruction != null;
    }

    public String getInstructionsTitle() {
        if (hasInstructions()) {
            return instruction.getTitle();
        } else {
            return "";
        }
    }

    public static class Builder {

        public PaymentResult paymentResult;

        public PaymentResultScreenPreference preferences =
                new PaymentResultScreenPreference.Builder().build();

        public Instruction instruction;
        public String headerMode = HeaderProps.HEADER_MODE_WRAP;
        public HeaderTitleFormatter amountFormat;
        public boolean loading = true;
        public String processingMode;

        public Builder setPaymentResult(@NonNull final PaymentResult paymentResult) {
            this.paymentResult = paymentResult;
            return this;
        }

        public Builder setHeaderMode(@NonNull final String headerMode) {
            this.headerMode = headerMode;
            return this;
        }

        public Builder setPaymentResultScreenPreference(@NonNull final PaymentResultScreenPreference paymentResultScreenPreference) {
            this.preferences = paymentResultScreenPreference;
            return this;
        }

        public Builder setInstruction(@NonNull final Instruction instruction) {
            this.instruction = instruction;
            return this;
        }

        public Builder setAmountFormat(@NonNull final HeaderTitleFormatter amountFormat) {
            this.amountFormat = amountFormat;
            return this;
        }

        public Builder setLoading(final boolean loading) {
            this.loading = loading;
            return this;
        }

        public Builder setProcessingMode(String processingMode) {
            this.processingMode = processingMode;
            return this;
        }

        public PaymentResultProps build() {
            return new PaymentResultProps(this);
        }
    }
}
