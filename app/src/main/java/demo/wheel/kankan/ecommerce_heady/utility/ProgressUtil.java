package demo.wheel.kankan.ecommerce_heady.utility;

import android.content.Context;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by mac on 2/7/18.
 */

public class ProgressUtil {

    private static ProgressUtil mInstance = null;

    public static ProgressUtil getInstance() {
        if (mInstance == null) {
            mInstance = new ProgressUtil();
        }
        return mInstance;
    }

    public KProgressHUD initProgressBar(Context context) {
        return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setDimAmount(0.5f);
    }

    public void showDialog(KProgressHUD dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideDialog(KProgressHUD dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
