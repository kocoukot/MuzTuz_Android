package com.artline.muztus.billing_feature

import android.app.Activity
import android.content.Context
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

private const val REWARD_AD_UNIT_ID = "R-M-15929932-2"

class AdsService(
    private val context: Context,
    private val addLoaded: (Boolean) -> Unit,
    private val onRewardSuccess: (Int) -> Unit
) {
    private var rewardedAd: RewardedAd? = null
    private var rewardedAdLoader: RewardedAdLoader? = null

    init {
        rewardedAdLoader = RewardedAdLoader(context).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewarded: RewardedAd) {
                    rewardedAd = rewarded
                    addLoaded.invoke(true)
                }

                override fun onAdFailedToLoad(error: AdRequestError) {
                    addLoaded.invoke(false)
                }
            })
        }
        loadRewardedAd()
    }

    private fun loadRewardedAd() {
        val adRequestConfiguration = AdRequestConfiguration.Builder(REWARD_AD_UNIT_ID).build()
        rewardedAdLoader?.loadAd(adRequestConfiguration)
    }


    fun showAd(activity: Activity) {
        rewardedAd?.apply {
            setAdEventListener(object : RewardedAdEventListener {

                override fun onAdShown() {
//                    launch { _effects.emit(AdRewardEffects.IsSuccessShown) }
                }

                override fun onRewarded(reward: Reward) {
                    onRewardSuccess.invoke(reward.amount)
                }

                override fun onAdFailedToShow(adError: AdError) {
                    // Called when an RewardedAd failed to show

                    // Clean resources after Ad failed to show
                    rewardedAd?.setAdEventListener(null)
                    rewardedAd = null

                    // Now you can preload the next rewarded ad.
                    loadRewardedAd()
                }

                override fun onAdDismissed() {
                    // Called when ad is dismissed.
                    // Clean resources after Ad dismissed
                    rewardedAd?.setAdEventListener(null)
                    rewardedAd = null

                    // Now you can preload the next rewarded ad.
                    loadRewardedAd()
                }

                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onAdImpression(impressionData: ImpressionData?) {
                    // Called when an impression is recorded for an ad.
                }
            })
            show(activity)
        }
    }

    fun recycleAll() {
        rewardedAdLoader?.setAdLoadListener(null)
        rewardedAdLoader = null
        destroyRewardedAd()
    }

    private fun destroyRewardedAd() {
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
    }


    private fun adInit() {
//        RewardedAd.load(
//            context,
//            BuildConfig.GOOGLE_WATCH_KEY,
//            adRequest,
//            object : RewardedAdLoadCallback() {
//                override fun onAdFailedToLoad(adError: LoadAdError) {
////                Log.d(TAG, adError?.toString())
//                    rewardedAd = null
//                }
//
//                override fun onAdLoaded(ad: RewardedAd) {
//                    Timber.d("ads client", "Ad was loaded.")
//                    rewardedAd = ad
//                }
//            })
    }

//    fun showAd(activity: Activity) {
//        if (rewardedAd != null) {
//            rewardedAd?.show(activity) {
//                rewardedAd?.fullScreenContentCallback = adCallBack
//                onRewardSuccess.invoke(it.amount)
//            }
//        }
//    }

//    private val adCallBack = object : FullScreenContentCallback() {
//        override fun onAdClicked() {
//            // Called when a click is recorded for an ad.
//            Timber.d("ads client", "ad was clicked.")
//        }
//
//        override fun onAdDismissedFullScreenContent() {
//            // Called when ad is dismissed.
//            // Set the ad reference to null so you don't show the ad a second time.
//            Timber.d("ads client", "ad was dismissed fullscreen content..")
//            adInit()
//            rewardedAd = null
//        }
//
//        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//            // Called when ad fails to show.
//            Timber.d("ads client", "ad was failed to show fullscreen content.")
//            rewardedAd = null
//        }
//
//        override fun onAdImpression() {
//            // Called when an impression is recorded for an ad.
//            Timber.d("ads client", "ad was recorded an impression.")
//        }
//
//        override fun onAdShowedFullScreenContent() {
//            // Called when ad is shown.
//            Timber.d("ads client", "ad was showed fullscreen content.")
//        }
//    }
}