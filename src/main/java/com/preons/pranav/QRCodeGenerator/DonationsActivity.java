/*
 * Copyright (C) 2011-2015 Dominik Schürmann <dominik@dominikschuermann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.preons.pranav.QRCodeGenerator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.sufficientlysecure.donations.DonationsFragment;

public class DonationsActivity extends FragmentActivity {

    /**
     * Google
     */
    private static final String GOOGLE_PUBKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhMFIBtUF61BHd8wT/9urAPJ8zehdH3nv18WMSQBHz93Bp0+Gq80ZyZxLttY/RAfr2p4lLveB/B/Hbcpkj1JGj3RjgqEK2HV0PIzKRNQ/xUtrP4akVZMtST82ScyhG1aGM8aGEsTLlLD3MU7h5mEWTXFxCV/rq9fLU+PRNZIcEZuyFnli1/admgjgYoOR6bPBkhxde8q7PlEMtkAY4mpx6Ra/1Vrzi05lJ5zm5JlJsCC5xm1nHtNEtX+LPXSD1fdfULnWRu3XlEXVLbjz7L8j9m5Y8W63bMkD/mRCZo7BD/Wh1eheSfyG5cYcYqtZd9CMvAWNCD+aV70SwcU1lZ+TwwIDAQAB";
    private static final String[] GOOGLE_CATALOG = new String[]{"ntpsync.donation.1",
            "ntpsync.donation.2", "ntpsync.donation.3", "ntpsync.donation.5", "ntpsync.donation.8",
            "ntpsync.donation.13"};

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.donations_activity);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DonationsFragment donationsFragment;

        donationsFragment = DonationsFragment.newInstance(BuildConfig.DEBUG, true, GOOGLE_PUBKEY, GOOGLE_CATALOG,
                getResources().getStringArray(R.array.donation_google_catalog_values), false, null, null,
                null, false, null, null, false, null);

        ft.replace(R.id.donations_activity_container, donationsFragment, "donationsFragment");
        ft.commit();
    }

    /**
     * Needed for Google Play In-app Billing. It uses startIntentSenderForResult(). The result is not propagated to
     * the Fragment like in startActivityForResult(). Thus we need to propagate manually to our Fragment.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("donationsFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}